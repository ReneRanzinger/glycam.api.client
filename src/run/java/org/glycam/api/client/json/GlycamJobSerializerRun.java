package org.glycam.api.client.json;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.glycam.api.client.om.GlycamJob;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

public class GlycamJobSerializerRun
{

    public static void main(String[] args)
            throws StreamReadException, DatabindException, FileNotFoundException, IOException
    {

        List<GlycamJob> t_jobs = GlycamJobSerializer.deserialize("./data/output/jobs.json");

        for (GlycamJob t_glycamJob : t_jobs)
        {
            if (t_glycamJob.getErrorMessage() != null
                    && t_glycamJob.getErrorMessage().startsWith("Missing"))
            {
                System.out.println(t_glycamJob.getResponse());
                System.out.println();
                System.out.println();
            }
        }
    }

}
