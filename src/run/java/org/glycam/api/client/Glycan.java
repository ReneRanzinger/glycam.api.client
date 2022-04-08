package org.glycam.api.client;

import java.io.IOException;

import org.glycam.api.client.http.ClientResponse;
import org.glycam.api.client.http.GlycamClient;
import org.glycam.api.client.json.submit.SubmitInformation;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        ClientResponse t_response = t_client.submitGlycan(t_sequence);

        System.out.println(t_response.getStatusCode() + " " + t_response.getStatusPhrase());
        System.out.println(t_response.getResponseBody());

        ObjectMapper t_mapper = new ObjectMapper();
        SubmitInformation t_responseInfo = t_mapper.readValue(t_response.getResponseBody(),
                SubmitInformation.class);

        // close the client connection and cleanup
        t_client.close();
    }

}
