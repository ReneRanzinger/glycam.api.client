package org.glycam.api.client;

import java.io.IOException;

import org.glycam.api.client.util.Client;

// https://github.com/GLYCAM-Web/website/tree/master/Examples/JsonApi
public class Glycan
{

    public static void main(String[] args) throws IOException, InterruptedException
    {
        // create the client
        Client t_client = new Client("https://dev.glycam.org/json/");

        String t_sequence = "DGlcpNAcb1-OH";
        String t_jobId = t_client.submitGlycan(t_sequence);
        if (t_jobId == null)
        {
            System.out.println("Unable to extract job ID.");
        }
        else
        {
            System.out.println(t_jobId);
            // Thread.sleep(30000);
            String t_pdb = t_client.downloadPDB(t_jobId);
            System.out.println(t_pdb);
        }
        // close the client connection and cleanup
        t_client.close();

    }

}
