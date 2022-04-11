package org.glycam.api.client;

import java.io.IOException;

import org.glycam.api.client.http.GlycamClient;
import org.glycam.api.client.om.GlycamJob;
import org.glycam.api.client.util.ResponseUtil;

/**
 *
 * @author Rene Ranzinger
 *
 */
public class Glycan
{
    // https://github.com/GLYCAM-Web/website/issues/25
    public static void main(String[] args) throws IOException
    {
        // create the client
        GlycamClient t_client = new GlycamClient("https://glycam.org/json/");

        // DGlpNAcb1-OH
        String t_sequence = "DGlcpNAcb1-3DGalpNAca1-3[LFucpa1-2]DGalpb1-3[DGlcpNAcb1-3DGlcpNAcb1-6]DGalpNAca1-OH";
        t_sequence = "DGlcpNAcb1-OH";
        GlycamJob t_response = t_client.submitGlycan(t_sequence);

        System.out.println(t_response.getHttpCode());
        System.out.println(t_response.getResponse());

        ResponseUtil t_util = new ResponseUtil();
        t_util.processGlycanResponse(t_response);

        // close the client connection and cleanup
        t_client.close();
    }

}
