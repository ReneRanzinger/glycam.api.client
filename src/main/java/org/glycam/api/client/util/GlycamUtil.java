package org.glycam.api.client.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.glycam.api.client.http.GlycamClient;
import org.glycam.api.client.json.ResponseUtil;
import org.glycam.api.client.om.GlycamJob;

import com.fasterxml.jackson.core.JsonProcessingException;

public class GlycamUtil
{
    public static final Long DEFAULT_MAX_WAITING_TIME = 600000L;
    private static final Long POLLING_SLEEP_MILLIS = 3000L;
    private static final Integer MAX_QUEUE_LENGTH = 5;
    private static final Integer MAX_PROCESSING_COUNT = Integer.MAX_VALUE;

    private String m_pdbFolder = null;
    private Long m_maxWaitingTimeInMillis = DEFAULT_MAX_WAITING_TIME;
    private GlycamClient m_client = null;
    private ResponseUtil m_utilResponse = null;

    public GlycamUtil(String a_pdbFolder, Long a_maxWaitingTime)
            throws ClientProtocolException, IOException
    {
        super();
        this.m_client = new GlycamClient("https://glycam.org/json/");
        this.m_utilResponse = new ResponseUtil();
        this.m_maxWaitingTimeInMillis = a_maxWaitingTime;
        File t_file = new File(a_pdbFolder);
        t_file.mkdirs();
        this.m_pdbFolder = a_pdbFolder;
    }

    public void process(List<GlycamJob> a_jobs) throws InterruptedException
    {
        Integer t_counter = 0;
        // queue
        List<GlycamJob> t_jobQueue = new ArrayList<>();

        for (GlycamJob t_glycamJob : a_jobs)
        {
            t_counter++;
            if (t_counter < MAX_PROCESSING_COUNT)
            {
                System.out.println("Submitting Job " + t_counter.toString() + ": "
                        + t_glycamJob.getGlyTouCanId());
                // submit the job and add to the queue
                if (this.isSubmit(t_glycamJob))
                {
                    t_glycamJob.setTimestampSubmission(System.currentTimeMillis());
                    try
                    {
                        this.m_client.submitGlycan(t_glycamJob);
                        this.m_utilResponse.processGlycanResponse(t_glycamJob);
                    }
                    catch (JsonProcessingException e)
                    {
                        t_glycamJob.setStatus(GlycamJob.STATUS_ERROR);
                        t_glycamJob.setErrorType("Response JSON error");
                        t_glycamJob.setErrorMessage(e.getMessage());
                    }
                    catch (IOException e)
                    {
                        t_glycamJob.setStatus(GlycamJob.STATUS_ERROR);
                        t_glycamJob.setErrorType("Request submission error");
                        t_glycamJob.setErrorMessage(e.getMessage());
                    }
                }
                if (this.isQueue(t_glycamJob))
                {
                    t_jobQueue.add(t_glycamJob);
                }
                // check if the queue is full
                while (t_jobQueue.size() >= GlycamUtil.MAX_QUEUE_LENGTH)
                {
                    // queue is full, need to check and wait till time is up or
                    // at least 1 job is finished
                    this.waitOnQueue(t_jobQueue);
                }
            }
        }
        // all glycans are submitted, wait for the rest in the queue
        while (t_jobQueue.size() > 0)
        {
            this.waitOnQueue(t_jobQueue);
        }
    }

    private boolean isQueue(GlycamJob a_glycamJob)
    {
        if (a_glycamJob.getStatus().equals(GlycamJob.STATUS_SUBMITTED))
        {
            return true;
        }
        if (a_glycamJob.getStatus().equals(GlycamJob.STATUS_TIMEOUT))
        {
            return true;
        }
        return false;
    }

    private boolean isSubmit(GlycamJob a_glycamJob)
    {
        if (a_glycamJob.getStatus().equals(GlycamJob.STATUS_INIT))
        {
            return true;
        }
        return false;
    }

    /**
     * Check on the status of the jobs in queue
     *
     * Checks the status of the jobs in the queue. There is also a maximum
     * waiting time for a job. If its not finished by than it will be treated as
     * failed and removed from the queue.
     *
     * @param a_jobQueue
     *            Queue with the list of submitted jobs
     * @throws InterruptedException
     */
    private void waitOnQueue(List<GlycamJob> a_jobQueue) throws InterruptedException
    {
        System.out.println("Waiting on queue");
        List<GlycamJob> t_finished = this.checkQueue(a_jobQueue);
        if (t_finished.size() > 0)
        {
            // remove finished jobs
            for (GlycamJob t_finishedJob : t_finished)
            {
                a_jobQueue.remove(t_finishedJob);
            }
        }
        else
        {
            Thread.sleep(POLLING_SLEEP_MILLIS);
        }
    }

    private List<GlycamJob> checkQueue(List<GlycamJob> a_jobQueue)
    {
        List<GlycamJob> t_finishedJobs = new ArrayList<>();
        for (GlycamJob t_glycamJob : a_jobQueue)
        {
            // check the status from the web
            try
            {
                String t_statusResponse = this.m_client.getStatus(t_glycamJob);
                String t_status = this.m_utilResponse.processPollingResponse(t_statusResponse);
                if (t_status.equals("All complete"))
                {
                    t_glycamJob.setTimestampLastCheck(System.currentTimeMillis());
                    String t_fileNamePath = this.m_pdbFolder + File.separator
                            + t_glycamJob.getGlyTouCanId() + ".pdb";
                    try
                    {
                        this.m_client.downloadPDB(t_glycamJob.getDownloadURL(), t_fileNamePath);
                        t_glycamJob.setStatus(GlycamJob.STATUS_SUCCESS);
                        try
                        {
                            this.testPDB(t_fileNamePath);
                        }
                        catch (Exception e)
                        {
                            t_glycamJob.setStatus(GlycamJob.STATUS_PDB_ERROR);
                            t_glycamJob.setErrorType("PDB failed test");
                            t_glycamJob.setErrorMessage(e.getMessage());
                        }
                    }
                    catch (Exception e)
                    {
                        t_glycamJob.setStatus(GlycamJob.STATUS_PDB_ERROR);
                        t_glycamJob.setErrorType("Error downloading PDB");
                        t_glycamJob.setErrorMessage(e.getMessage());
                    }
                    t_finishedJobs.add(t_glycamJob);
                }
                else
                {
                    // is the time up
                    if ((System.currentTimeMillis()
                            - t_glycamJob.getTimestampSubmission()) > this.m_maxWaitingTimeInMillis)
                    {
                        t_glycamJob.setTimestampLastCheck(System.currentTimeMillis());
                        t_glycamJob.setStatus(GlycamJob.STATUS_TIMEOUT);
                        t_finishedJobs.add(t_glycamJob);
                    }
                }
            }
            catch (Exception e)
            {
                t_glycamJob.setStatus(GlycamJob.STATUS_ERROR);
                t_glycamJob.setErrorType("Error polling");
                t_glycamJob.setErrorMessage(e.getMessage());
                t_finishedJobs.add(t_glycamJob);
            }
        }
        return t_finishedJobs;
    }

    private void testPDB(String a_fileNamePath) throws IOException
    {
        int t_counterAtom = 0;
        BufferedReader t_reader = new BufferedReader(new FileReader(a_fileNamePath));
        String t_line = t_reader.readLine();
        while (t_line != null)
        {
            String[] t_parts = t_line.split(" ");
            if (t_parts.length > 1)
            {
                if (t_parts[0].equals("ATOM"))
                {
                    t_counterAtom++;
                }
                else if (t_parts[0].equals("TER"))
                {
                }
                else if (t_parts[0].equals("END"))
                {
                }
                else
                {
                    t_reader.close();
                    throw new IOException("Found unknown keyword in PDB file: " + t_parts[0]);
                }
            }
            t_line = t_reader.readLine();
        }
        t_reader.close();
        if (t_counterAtom < 10)
        {
            throw new IOException("Less than 10 atoms found.");
        }
    }

}
