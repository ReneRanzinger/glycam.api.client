package org.glycam.api.client.om;

public class WebResponse
{
    private String m_content = null;
    private Integer m_httpCode = null;

    public WebResponse(String a_content, Integer a_httpCode)
    {
        super();
        this.m_content = a_content;
        this.m_httpCode = a_httpCode;
    }

    public String getContent()
    {
        return this.m_content;
    }

    public void setContent(String a_content)
    {
        this.m_content = a_content;
    }

    public Integer getHttpCode()
    {
        return this.m_httpCode;
    }

    public void setHttpCode(Integer a_httpCode)
    {
        this.m_httpCode = a_httpCode;
    }
}
