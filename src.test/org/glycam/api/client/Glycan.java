package org.glycam.api.client;

import java.io.IOException;

import org.glycam.api.client.util.GlycamClient;

/**
 *
 * @author rene
 *
 */
public class Glycan
{
    // https://github.com/GLYCAM-Web/website/issues/25
    public static void main(String[] args) throws IOException
    {
        // create the client
        GlycamClient t_client = new GlycamClient("https://dev.glycam.org/json/");

        // DGlpNAcb1-OH
        String t_sequence = "DGlpNAcb1-3DGalpNAca1-3[LFucpa1-2]DGalpb1-3[DGlcpNAcb1-3DGlcpNAcb1-6]DGalpNAca1-OH";
        String t_response = t_client.submitGlycan(t_sequence);

        if (t_response == null)
        {
            System.out.println("Unable to extract job ID.");
        }
        else
        {
            System.out.println(t_response);
            // String t_pdb = t_client.downloadPDB(t_jobId);
            // System.out.println(t_pdb);
        }
        // close the client connection and cleanup
        t_client.close();

    }

}
