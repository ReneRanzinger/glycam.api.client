package org.glycam.api.client.util;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

public class ResponseUtil
{

    public static String entityToString(HttpEntity a_entity) throws UnsupportedOperationException, IOException
    {
        StringWriter t_writer = new StringWriter();
        IOUtils.copy(a_entity.getContent(), t_writer, StandardCharsets.UTF_8);
        return t_writer.toString();
    }

    /**
     * Gets the JobID from a provided content string: output:
     * "{\"entity\": {\"type\": \"Sequence\"}, \"responses\": [{\"Build3DStructure\": {\"payload\": \"5c7f2265-0eb8-48ce-9737-1b913bd56645\"}}]}"
     *
     * @param a_responseContent
     *            Content provided by the webservices
     * @return Extracted JobID or null if extraction fails
     */
    public static String extractJobId(String a_responseContent)
    {
        try
        {
            Integer t_pos = a_responseContent.indexOf("payload");
            String t_jobId = a_responseContent.substring(t_pos);
            String[] t_parts = t_jobId.split("\"");
            t_jobId = t_parts[2];
            t_jobId = t_jobId.replaceAll("\\\\", "");
            return t_jobId;
        }
        catch (Exception t_e)
        {
            return null;
        }

    }

    public static String glycanSequenceToJSON(String a_sequence)
    {
        String t_json = "{ \"entity\" : { \"type\": \"Sequence\", \"services\" : [ { \"Build\" :  { \"type\" : \"Build3DStructure\" } } ], \"inputs\" : [ { \"Sequence\" : { \"payload\" : \"XxXxX\" } } ] }}";
        t_json = t_json.replace("XxXxX", a_sequence);
        return t_json;
    }

}
