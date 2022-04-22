package org.glycam.api.client;

import java.io.IOException;
import java.util.List;

import org.glycam.api.client.csv.SequenceFileParser;
import org.glycam.api.client.json.GlycamJobSerializer;
import org.glycam.api.client.om.GlycamJob;
import org.glycam.api.client.util.GlycamUtil;

public class GlycanBatch
{

    public static void main(String[] args) throws IOException
    {
        // load the glycam sequence
        SequenceFileParser t_csvParser = new SequenceFileParser();
        List<GlycamJob> t_jobs = t_csvParser.loadFile("./data/2022.04.10.glycan.csv");

        GlycamUtil t_util = new GlycamUtil("./data/");
        t_util.process(t_jobs);

        GlycamJobSerializer.serialize(t_jobs, "./data/jobs.json");
    }

}
