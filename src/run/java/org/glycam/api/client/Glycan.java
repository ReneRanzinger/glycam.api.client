package org.glycam.api.client;

import java.io.IOException;

import org.glycam.api.client.http.GlycamClient;
import org.glycam.api.client.json.ResponseUtil;
import org.glycam.api.client.om.GlycamJob;

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
        t_sequence = "DGalpa1-3DGalpb1-OH";
        GlycamJob t_job = new GlycamJob();
        t_job.setGlycam(t_sequence);
        t_client.submitGlycan(t_job);

        System.out.println(t_job.getHttpCode());
        System.out.println(t_job.getResponse());

        ResponseUtil t_util = new ResponseUtil();
        t_util.processGlycanResponse(t_job);

        String t_statusJson = t_client.getStatus(t_job);
        String t_status = t_util.processPollingResponse(t_statusJson);

        System.out.println(t_status);
        System.out.println(t_job.getDownloadURL());
        // t_client.downloadPDB(t_job.getDownloadURL(), "./data/test.pdb");

        // close the client connection and cleanup
        t_client.close();

        // PDBFileReader t_reader = new PDBFileReader();
        // Structure struc = t_reader.getStructure("./data/test.pdb");
        // System.out.println(struc);
    }

}
