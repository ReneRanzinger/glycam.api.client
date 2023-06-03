package org.glycam.api.client.csv;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.opencsv.CSVWriter;

public abstract class CSVFileWriter
{
    protected CSVWriter m_csvFile = null;

    public void closeFile() throws IOException
    {
        this.m_csvFile.flush();
        this.m_csvFile.close();
    }

    protected String writeString(String a_string)
    {
        if (a_string == null)
        {
            return "";
        }
        return a_string;
    }

    protected String addDate(Long a_date)
    {
        if (a_date == null)
        {
            return "";
        }
        Date t_date = new Date(a_date);
        SimpleDateFormat t_formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return t_formatter.format(t_date);
    }

}
