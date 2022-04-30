package org.glycam.api.client.om;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Warning
{
    private Integer m_httpCode = null;
    private String m_type = null;
    private String m_message = null;
    private Long m_timestamp = null;

    @JsonProperty("http_code")
    public Integer getHttpCode()
    {
        return this.m_httpCode;
    }

    public void setHttpCode(Integer a_httpCode)
    {
        this.m_httpCode = a_httpCode;
    }

    @JsonProperty("type")
    public String getType()
    {
        return this.m_type;
    }

    public void setType(String a_type)
    {
        this.m_type = a_type;
    }

    @JsonProperty("message")
    public String getMessage()
    {
        return this.m_message;
    }

    public void setMessage(String a_message)
    {
        this.m_message = a_message;
    }

    @JsonProperty("time")
    public Long getTimestamp()
    {
        return this.m_timestamp;
    }

    public void setTimestamp(Long a_timestamp)
    {
        this.m_timestamp = a_timestamp;
    }

}
