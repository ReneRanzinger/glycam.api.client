package org.glycam.api.client.csv;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.glycam.api.client.om.GlycamJob;

import com.opencsv.CSVReader;

public class SequenceFileParser
{
    public List<GlycamJob> loadFile(String a_fileNamePath) throws IOException
    {
        List<GlycamJob> t_result = new ArrayList<>();
        Integer t_rowCounter = 1;
        try
        {
            // Create an object of filereader
            FileReader t_fileReader = new FileReader(a_fileNamePath);

            // create csvReader object passing
            CSVReader t_csvReader = new CSVReader(t_fileReader);
            // that should be the table heading
            String[] t_nextRecord = t_csvReader.readNext();

            // read data line by line
            while ((t_nextRecord = t_csvReader.readNext()) != null)
            {
                t_rowCounter++;
                GlycamJob t_fileConfig = this.parseRow(t_nextRecord, t_rowCounter);
                if (t_fileConfig != null)
                {
                    t_result.add(t_fileConfig);
                }
            }
            t_csvReader.close();
        }
        catch (Exception e)
        {
            throw new IOException("Error parsing sequence CSV file (row " + t_rowCounter.toString()
                    + "): " + e.getMessage());
        }
        return t_result;
    }

    private GlycamJob parseRow(String[] a_row, Integer a_rowCounter) throws IOException
    {
        GlycamJob t_glycamJob = new GlycamJob();
        // GlyTouCan ID
        String t_cellValue = this.getCell(a_row, 0);
        if (t_cellValue == null)
        {
            throw new IOException(
                    "Missing GlyTouCan ID in column one of row " + a_rowCounter.toString());
        }
        t_glycamJob.setGlyTouCanId(t_cellValue);
        // sequence
        t_cellValue = this.getCell(a_row, 1);
        if (t_cellValue == null)
        {
            throw new IOException("Missing sequence in row " + a_rowCounter.toString());
        }
        t_glycamJob.setGlycam(t_cellValue);
        return t_glycamJob;
    }

    private String getCell(String[] a_row, int a_position)
    {
        if (a_position < a_row.length)
        {
            String t_value = a_row[a_position].trim();
            if (t_value.length() == 0)
            {
                return null;
            }
            return t_value;
        }
        return null;
    }

}
