package org.glycam.api.client.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.glycam.api.client.om.GlycamJob;

import com.opencsv.CSVWriter;

public class CSVFileWriterStatus extends CSVFileWriter
{

    public CSVFileWriterStatus(String a_fileNamePath) throws IOException
    {
        // first create file object for file
        File t_file = new File(a_fileNamePath);

        // create FileWriter object with file as parameter
        FileWriter t_fileWriter = new FileWriter(t_file);

        // create CSVWriter with tab as separator
        this.m_csvFile = new CSVWriter(t_fileWriter, CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        String[] t_header = new String[] { "GlyTouCan ID", "Glycam", "Submission time", "Job",
                "status" };
        // write the header
        this.m_csvFile.writeNext(t_header);
    }

    public void writeStatus(GlycamJob a_job)
    {
        String[] t_line = new String[5];
        t_line[0] = a_job.getGlyTouCanId();
        t_line[1] = a_job.getGlycam();
        t_line[2] = this.addDate(a_job.getTimestampSubmission());
        t_line[3] = this.writeString(a_job.getJobId());
        t_line[4] = this.writeString(a_job.getStatus());
        this.m_csvFile.writeNext(t_line);
    }

}
