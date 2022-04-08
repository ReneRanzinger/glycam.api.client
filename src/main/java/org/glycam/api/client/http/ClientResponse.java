package org.glycam.api.client.http;

public class ClientResponse
{
    private String m_payLoad = null;
    private String m_responseBody = null;
    private Integer m_statusCode = null;
    private String m_statusPhrase = null;

    public String getResponseBody()
    {
        return this.m_responseBody;
    }

    public void setResponseBody(String a_responseBody)
    {
        this.m_responseBody = a_responseBody;
    }

    public Integer getStatusCode()
    {
        return this.m_statusCode;
    }

    public void setStatusCode(Integer a_statusCode)
    {
        this.m_statusCode = a_statusCode;
    }

    public String getStatusPhrase()
    {
        return this.m_statusPhrase;
    }

    public void setStatusPhrase(String a_statusPhrase)
    {
        this.m_statusPhrase = a_statusPhrase;
    }

    public String getPayLoad()
    {
        return m_payLoad;
    }

    public void setPayLoad(String a_payLoad)
    {
        this.m_payLoad = a_payLoad;
    }
}
