package org.glycam.api.client.json.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notice
{
    private String m_code = null;
    private String m_message = null;

    @JsonProperty("code")
    public String getCode()
    {
        return this.m_code;
    }

    public void setCode(String a_code)
    {
        this.m_code = a_code;
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
}
