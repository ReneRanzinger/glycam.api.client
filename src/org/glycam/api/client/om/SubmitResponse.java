package org.glycam.api.client.om;

public class SubmitResponse
{
    private String m_request = null;
    private String m_response = null;
    private String m_jobId = null;
    private String m_downloadURL = null;
    private Integer m_httpCode = null;
    private boolean m_successful = false;
    private String m_errorMessage = null;

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

    public Integer getHttpCode()
    {
        return this.m_httpCode;
    }

    public void setHttpCode(Integer a_httpCode)
    {
        this.m_httpCode = a_httpCode;
    }

    public boolean isSuccessful()
    {
        return this.m_successful;
    }

    public void setSuccessful(boolean a_successful)
    {
        this.m_successful = a_successful;
    }

    public String getErrorMessage()
    {
        return this.m_errorMessage;
    }

    public void setErrorMessage(String a_errorMessage)
    {
        this.m_errorMessage = a_errorMessage;
    }

}
