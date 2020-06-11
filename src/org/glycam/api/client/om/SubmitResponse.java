package org.glycam.api.client.om;

public class SubmitResponse
{
    private String m_request = null;
    private String m_response = null;
    private String m_jobId = null;
    private String m_downloadURL = null;
    public String getRequest()
    {
        return this.m_request;
    }
    public void setRequest(String a_request)
    {
        this.m_request = a_request;
    }
    public String getResponse()
    {
        return this.m_response;
    }
    public void setResponse(String a_response)
    {
        this.m_response = a_response;
    }
    public String getJobId()
    {
        return this.m_jobId;
    }
    public void setJobId(String a_jobId)
    {
        this.m_jobId = a_jobId;
    }
    public String getDownloadURL()
    {
        return this.m_downloadURL;
    }
    public void setDownloadURL(String a_downloadURL)
    {
        this.m_downloadURL = a_downloadURL;
    }

}
