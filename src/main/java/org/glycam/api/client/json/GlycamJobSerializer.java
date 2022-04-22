package org.glycam.api.client.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.glycam.api.client.om.GlycamJob;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GlycamJobSerializer
{
    public static void serialize(List<GlycamJob> a_jobs, String a_fileNamePath)
            throws StreamWriteException, DatabindException, IOException
    {
        ObjectMapper t_mapper = new ObjectMapper();
        t_mapper.writeValue(new FileWriter(new File(a_fileNamePath)), a_jobs);
    }

    public static List<GlycamJob> deserialize(String a_fileNamePath)
            throws StreamReadException, DatabindException, FileNotFoundException, IOException
    {
        ObjectMapper t_mapper = new ObjectMapper();
        GlycamJob[] t_responseList = t_mapper.readValue(new FileReader(new File(a_fileNamePath)),
                GlycamJob[].class);
        List<GlycamJob> t_result = Arrays.asList(t_responseList);
        return t_result;

    }
}
