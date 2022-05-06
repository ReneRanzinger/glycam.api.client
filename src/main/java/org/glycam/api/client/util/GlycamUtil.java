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
import org.glycam.api.client.om.Warning;
import org.glycam.api.client.om.WebResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

public class GlycamUtil
{
    private static final Integer MAX_PROCESSING_COUNT = Integer.MAX_VALUE;

    private String m_pdbFolder = null;
    private Long m_maxWaitingTimeInMillis = null;
    private Long m_pollingSleepTimeInMillis = null;
    private Integer m_maxQueueLength = null;
    private GlycamClient m_client = null;
    private ResponseUtil m_utilResponse = null;
    private boolean m_verbose = false;

    public GlycamUtil(String a_pdbFolder, Long a_maxWaitingTime, Long a_pollingSleepTime,
            Integer a_queueLength, boolean a_verbose) throws ClientProtocolException, IOException
    {
        super();
        this.m_client = new GlycamClient("https://glycam.org/json/");
        this.m_utilResponse = new ResponseUtil();
        this.m_maxWaitingTimeInMillis = a_maxWaitingTime;
        this.m_pollingSleepTimeInMillis = a_pollingSleepTime;
        this.m_maxQueueLength = a_queueLength;
        this.m_verbose = a_verbose;
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
                // wait 1 second between the individual submits
                Thread.sleep(1000);
                // submit the job and add to the queue
                if (this.isSubmit(t_glycamJob))
                {
                    this.printMessage("Submitting Job " + t_counter.toString() + ": "
                            + t_glycamJob.getGlyTouCanId());
                    t_glycamJob.setTimestampSubmission(System.currentTimeMillis());
                    try
                    {
                        if (this.trySubmission(t_glycamJob))
                        {
                            this.m_utilResponse.processGlycanResponse(t_glycamJob);
                        }
                        else
                        {
                            t_glycamJob.setStatus(GlycamJob.STATUS_ERROR);
                            t_glycamJob.setErrorType("Submission error");
                            t_glycamJob.setErrorMessage("Submission failed due to HTTP Code: "
                                    + t_glycamJob.getHttpCode().toString());
                        }
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
                while (t_jobQueue.size() >= this.m_maxQueueLength)
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

    private boolean trySubmission(GlycamJob a_glycamJob) throws InterruptedException, IOException
    {
        this.m_client.submitGlycan(a_glycamJob);
        if (a_glycamJob.getHttpCode() < 400)
        {
            return true;
        }
        this.addWarning(a_glycamJob, a_glycamJob.getHttpCode(), "Submission error",
                "Failed submission with HTTP Code: " + a_glycamJob.getHttpCode().toString());
        this.waitBecauseOfFailedRequest();
        int t_chances = a_glycamJob.getSecondChances();
        for (int i = t_chances; i > 0; i--)
        {
            this.m_client.submitGlycan(a_glycamJob);
            if (a_glycamJob.getHttpCode() < 400)
            {
                a_glycamJob.setSecondChances(i);
                return true;
            }
            this.addWarning(a_glycamJob, a_glycamJob.getHttpCode(), "Submission error",
                    "Failed submission with HTTP Code: " + a_glycamJob.getHttpCode().toString());
            this.waitBecauseOfFailedRequest();
        }
        return false;
    }

    private String tryStatus(GlycamJob a_glycamJob) throws InterruptedException, IOException
    {
        WebResponse t_response = this.m_client.getStatus(a_glycamJob);
        if (t_response.getHttpCode() < 400)
        {
            return t_response.getContent();
        }
        this.addWarning(a_glycamJob, t_response.getHttpCode(), "Polling error",
                "Failed polling with HTTP Code: " + t_response.getHttpCode().toString());
        this.waitBecauseOfFailedRequest();
        int t_chances = a_glycamJob.getSecondChances();
        for (int i = t_chances; i > 0; i--)
        {
            t_response = this.m_client.getStatus(a_glycamJob);
            if (t_response.getHttpCode() < 400)
            {
                a_glycamJob.setSecondChances(i);
                return t_response.getContent();
            }
            this.addWarning(a_glycamJob, t_response.getHttpCode(), "Submission error",
                    "Failed polling with HTTP Code: " + t_response.getHttpCode().toString());
            this.waitBecauseOfFailedRequest();
        }
        return null;
    }

    private void addWarning(GlycamJob a_glycamJob, Integer a_httpCode, String a_type,
            String a_message)
    {
        Warning t_warning = new Warning();
        t_warning.setHttpCode(a_httpCode);
        t_warning.setMessage(a_message);
        t_warning.setType(a_type);
        t_warning.setTimestamp(System.currentTimeMillis());
        a_glycamJob.getWarnings().add(t_warning);
    }

    private void waitBecauseOfFailedRequest() throws InterruptedException
    {
        this.printMessage("Waiting due to failed request");
        Thread.sleep(5000);
    }

    private void printMessage(String a_message)
    {
        if (this.m_verbose)
        {
            System.out.println(a_message);
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
        this.printMessage("Waiting on queue");
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
            Thread.sleep(this.m_pollingSleepTimeInMillis);
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
                String t_statusResponse = this.tryStatus(t_glycamJob);
                if (t_statusResponse == null)
                {
                    t_glycamJob.setStatus(GlycamJob.STATUS_ERROR);
                    t_glycamJob.setErrorType("Polling error");
                    t_glycamJob.setErrorMessage(
                            "Unable to retrieve polling response due to HTTP code >400");
                    t_finishedJobs.add(t_glycamJob);
                }
                else
                {
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
                        if ((System.currentTimeMillis() - t_glycamJob
                                .getTimestampSubmission()) > this.m_maxWaitingTimeInMillis)
                        {
                            t_glycamJob.setTimestampLastCheck(System.currentTimeMillis());
                            t_glycamJob.setStatus(GlycamJob.STATUS_TIMEOUT);
                            t_finishedJobs.add(t_glycamJob);
                        }
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

    public void reTestTimeout(List<GlycamJob> a_jobs) throws InterruptedException
    {
        Integer t_counter = 0;
        List<GlycamJob> t_jobQueue = new ArrayList<>();
        for (GlycamJob t_glycamJob : a_jobs)
        {
            if (t_glycamJob.getStatus().equals(GlycamJob.STATUS_TIMEOUT))
            {
                t_counter++;
                this.printMessage("Re-test timeouts " + t_counter.toString() + ": "
                        + t_glycamJob.getGlyTouCanId());
                t_jobQueue.add(t_glycamJob);
                // check if the queue is full
                while (t_jobQueue.size() >= this.m_maxQueueLength)
                {
                    // queue is full, need to check and wait till time is up or
                    // at least 1 job is finished
                    this.waitOnQueue(t_jobQueue);
                }
            }
        }
    }

}
