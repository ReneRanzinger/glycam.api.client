package org.glycam.api.client;

import java.io.IOException;
import java.util.List;

import org.glycam.api.client.csv.CSVError;
import org.glycam.api.client.csv.SequenceFileParser;
import org.glycam.api.client.json.GlycamJobSerializer;
import org.glycam.api.client.om.GlycamJob;
import org.glycam.api.client.util.GlycamUtil;

public class GlycanBatch
{

    public static void main(String[] args) throws IOException, InterruptedException
    {
        // load the glycam sequence
        SequenceFileParser t_csvParser = new SequenceFileParser();
        List<GlycamJob> t_jobs = t_csvParser.loadFile("./data/2022.04.10.glycan.csv");

        GlycamUtil t_util = new GlycamUtil("./data/output/pdb/",
                GlycamUtil.DEFAULT_MAX_WAITING_TIME);
        t_util.process(t_jobs);

        GlycamJobSerializer.serialize(t_jobs, "./data/output/jobs.json");

        CSVError t_errorLog = new CSVError("./data/output/");

        for (GlycamJob t_glycamJob : t_jobs)
        {
            String t_status = t_glycamJob.getStatus();
            if (!t_status.equals(GlycamJob.STATUS_SUCCESS)
                    && !t_status.equals(GlycamJob.STATUS_INIT))
            {
                t_errorLog.writeError(t_glycamJob);
            }
        }
        t_errorLog.closeFile();
    }

}

timer

command line
