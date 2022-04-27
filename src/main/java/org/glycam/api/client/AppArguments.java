package org.glycam.api.client;

public class AppArguments
{
    private String m_glycanFileNamePath = null;
    private String m_glycanDumpFileNamePath = null;
    private String m_outputPath = null;
    private Long m_maxWaitingTime = 600000L;
    private Integer m_maxQueueLength = 5;
    private Long m_pollingSleepTime = 3000L;

    public String getGlycanFileNamePath()
    {
        return this.m_glycanFileNamePath;
    }

    public void setGlycanFileNamePath(String a_glycanFileNamePath)
    {
        this.m_glycanFileNamePath = a_glycanFileNamePath;
    }

    public String getGlycanDumpFileNamePath()
    {
        return this.m_glycanDumpFileNamePath;
    }

    public void setGlycanDumpFileNamePath(String a_glycanDumpFileNamePath)
    {
        this.m_glycanDumpFileNamePath = a_glycanDumpFileNamePath;
    }

    public String getOutputPath()
    {
        return this.m_outputPath;
    }

    public void setOutputPath(String a_outputPath)
    {
        this.m_outputPath = a_outputPath;
    }

    public Long getMaxWaitingTime()
    {
        return this.m_maxWaitingTime;
    }

    public void setMaxWaitingTime(Long a_maxWaitingTime)
    {
        this.m_maxWaitingTime = a_maxWaitingTime;
    }

    public Integer getMaxQueueLength()
    {
        return this.m_maxQueueLength;
    }

    public void setMaxQueueLength(Integer a_maxQueueLength)
    {
        this.m_maxQueueLength = a_maxQueueLength;
    }

    public Long getPollingSleepTime()
    {
        return this.m_pollingSleepTime;
    }

    public void setPollingSleepTime(Long a_pollingSleepTime)
    {
        this.m_pollingSleepTime = a_pollingSleepTime;
    }
}
