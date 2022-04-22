package org.glycam.api.client.util;

import java.util.ArrayList;
import java.util.List;

import org.glycam.api.client.http.GlycamClient;
import org.glycam.api.client.om.GlycamJob;

public class GlycamUtil
{
    private static final Integer MAX_QUEUE_LENGTH = 5;
    private String m_outputFolder = null;

    public GlycamUtil(String a_outputFolder)
    {
        this.m_outputFolder = a_outputFolder;
    }

    public void process(List<GlycamJob> a_jobs)
    {
        // create the client
        GlycamClient t_client = new GlycamClient("https://glycam.org/json/");

        // queue
        List<GlycamJob> t_jobQueue = new ArrayList<>();

        for (GlycamJob t_glycamJob : a_jobs)
        {
            // submit the job and add to the queue
            t_glycamJob.setTimestampSubmission(System.currentTimeMillis());
            t_client.submitGlycan(t_glycamJob);
            t_jobQueue.add(t_glycamJob);
            // check if the queue is full
            if (t_jobQueue.size() >= GlycamUtil.MAX_QUEUE_LENGTH)
            {
                // queue is full, need to check and wait

            }
        }
        // all glycans are submitted, wait for the rest in the queue

    }

}
