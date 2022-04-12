package org.glycam.api.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.glycam.api.client.http.GlycamClient;
import org.glycam.api.client.om.GlycamJob;

public class GlycanBatch
{

    public static void main(String[] args) throws IOException
    {
        // load the glycam sequence
        GlyGenGlycamUtil t_util = new GlyGenGlycamUtil();
        List<GlycamJob> t_jobs = t_util.readJobs("./data/2022.04.10.glycan.csv");

        Integer t_countError = 0;
        Integer t_countSuccess = 0;
        PrintWriter t_writerError = new PrintWriter(new File("./input/error.txt"));
        PrintWriter t_writerSuccess = new PrintWriter(new File("./input/success.txt"));
        // create the client
        GlycamClient t_client = new GlycamClient("https://glygen.ccrc.uga.edu/glycam/json/");

        BufferedReader t_reader = new BufferedReader(
                new FileReader("./input/GlyTouCan.glygen.input"));
        for (String t_line; (t_line = t_reader.readLine()) != null;)
        {
            try
            {
                String[] t_parts = t_line.split(":");
                long t_start = System.currentTimeMillis();
                String t_jobId = t_client.submitGlycan(t_parts[1]);
                long t_end = System.currentTimeMillis();
                if (t_jobId == null)
                {
                    t_writerError.println("Missing Job ID:" + t_line + "\n");
                    t_writerError.flush();
                    t_countError++;
                }
                else
                {
                    try
                    {
                        long t_startPDB = System.currentTimeMillis();
                        t_client.downloadPDB(t_jobId);
                        long t_endPDB = System.currentTimeMillis();
                        t_countSuccess++;
                        String t_message = Integer.toString(t_countError + t_countSuccess) + " - "
                                + t_parts[0] + "\t" + Long.toString(t_end - t_start) + "\t"
                                + Long.toString(t_endPDB - t_startPDB);
                        System.out.println(t_message);
                        t_writerSuccess.println(t_message);
                        t_writerSuccess.flush();
                    }
                    catch (Exception e)
                    {
                        System.out.println(Integer.toString(t_countError + t_countSuccess));
                        t_writerError.println(t_jobId + ":" + t_line + "\n");
                        t_writerError.flush();
                        t_countError++;
                    }
                }
            }
            catch (Exception e)
            {
                t_reader.close();
                t_writerError.flush();
                t_writerError.close();
                t_writerSuccess.flush();
                t_writerSuccess.close();
                throw e;
            }
        }
        t_reader.close();
        t_writerError.flush();
        t_writerError.close();
        t_writerSuccess.flush();
        t_writerSuccess.close();
        // close the client connection and cleanup
        t_client.close();
        System.out.println("Error count: " + t_countError.toString());
        System.out.println("Success count: " + t_countSuccess.toString());
    }

}
