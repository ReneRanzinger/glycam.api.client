package org.glycam.api.client.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.glycam.api.client.om.GlycamJob;

import com.opencsv.CSVWriter;

public class CSVError
{
    private CSVWriter m_csvError = null;

    public CSVError(String a_fileNamePath) throws IOException
    {
        // first create file object for file
        File t_file = new File(a_fileNamePath + File.separator + "error-log.csv");

        // create FileWriter object with file as parameter
        FileWriter t_fileWriter = new FileWriter(t_file);

        // create CSVWriter with tab as separator
        this.m_csvError = new CSVWriter(t_fileWriter, CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        String[] t_header = new String[] { "GlyTouCan ID", "Glycam", "Job", "status", "error type",
                "error message" };
        // write the header
        this.m_csvError.writeNext(t_header);
    }

    public void closeFile() throws IOException
    {
        this.m_csvError.flush();
        this.m_csvError.close();
    }

    public void writeError(GlycamJob a_job)
    {
        String[] t_line = new String[6];
        t_line[0] = a_job.getGlyTouCanId();
        t_line[1] = a_job.getGlycam();
        t_line[2] = this.writeString(a_job.getJobId());
        t_line[3] = this.writeString(a_job.getStatus());
        t_line[4] = this.writeString(a_job.getErrorType());
        t_line[5] = this.writeString(a_job.getErrorMessage());
        this.m_csvError.writeNext(t_line);
    }

    private String writeString(String a_string)
    {
        if (a_string == null)
        {
            return "";
        }
        return a_string;
    }

}
