package org.glycam.api.client.util;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ResponseUtil
{

    public static String entityToString(HttpEntity a_entity) throws UnsupportedOperationException, IOException
    {
        StringWriter t_writer = new StringWriter();
        IOUtils.copy(a_entity.getContent(), t_writer, StandardCharsets.UTF_8);
        return t_writer.toString();
    }

    /**
     * Gets the JobID from a provided content string:
     *
     * @param a_responseContent
     *            Content provided by the webservices
     * @return Extracted JobID or null if extraction fails
     */
    public static String extractJobId(String a_responseContent)
    {
        /*
         * { "entity":{ "type":"Sequence" }, "responses":[ {
         * "Build3DStructure":{ "payload":"7fde6e72-5864-43a9-ac71-92eb9e878fdb"
         * } } ] }
         */
        try
        {
            JSONParser t_parser = new JSONParser();
            JSONObject t_jsonObject = (JSONObject) t_parser.parse(a_responseContent);
            JSONArray t_array = ResponseUtil.getArray(t_jsonObject, "responses");
            for (Object t_object : t_array)
            {
                if (t_object instanceof JSONObject)
                {
                    t_jsonObject = ResponseUtil.getObject((JSONObject) t_object, "Build3DStructure");
                    if (t_jsonObject != null)
                    {
                        String t_jobId = ResponseUtil.getString(t_jsonObject, "payload");
                        return t_jobId;
                    }
                }
            }
        }
        catch (Exception t_e)
        {
            return null;
        }
        return null;
    }

    private static JSONObject getObject(JSONObject a_jsonObject, String a_key)
    {
        return (JSONObject) a_jsonObject.get(a_key);
    }

    private static JSONArray getArray(JSONObject a_jsonObject, String a_key)
    {
        return (JSONArray) a_jsonObject.get(a_key);
    }

    private static String getString(JSONObject a_jsonObject, String a_key) throws IOException
    {
        Object t_stringObject = a_jsonObject.get(a_key);
        if (t_stringObject == null)
        {
            return null;
        }
        if (t_stringObject instanceof String)
        {
            return (String) t_stringObject;
        }
        else
        {
            throw new IOException("JSON format error: Value for " + a_key + " is not a string.");
        }
    }

}
