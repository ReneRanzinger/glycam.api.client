package org.glycam.api.client.http;

import java.io.IOException;

import org.glycam.api.client.json.ResponseUtil;
import org.glycam.api.client.om.GlycamJob;
import org.glycam.api.client.om.WebResponse;

/**
 *
 * @author Rene Ranzinger
 *
 */
public class GlycamClientRun
{
    // https://github.com/GLYCAM-Web/website/issues/25
    public static void main(String[] args) throws IOException
    {
        // create the client
        GlycamClient t_client = new GlycamClient("https://glycam.org/json/");

        // DGlpNAcb1-OH
        String t_sequence = "DGlcpNAcb1-3DGalpNAca1-3[LFucpa1-2]DGalpb1-3[DGlcpNAcb1-3DGlcpNAcb1-6]DGalpNAca1-OH";
        t_sequence = "DGalpa1-3DGalpb1-OH";
        GlycamJob t_job = new GlycamJob();
        t_job.setGlycam(t_sequence);
        t_client.submitGlycan(t_job);

        System.out.println(t_job.getHttpCode());
        System.out.println(t_job.getResponse());

        ResponseUtil t_util = new ResponseUtil();
        t_util.processGlycanResponse(t_job);

        WebResponse t_statusJson = t_client.getStatus(t_job);
        String t_status = t_util.processPollingResponse(t_statusJson.getContent());

        System.out.println(t_status);
        System.out.println(t_job.getDownloadURL());

        // close the client connection and cleanup
        t_client.close();

    }

}
