package org.glycam.api.client.json.submit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FrontEndNotice
{
    private String m_type = null;
    private Notice m_notice = null;

    @JsonProperty("type")
    public String getType()
    {
        return this.m_type;
    }

    public void setType(String a_type)
    {
        this.m_type = a_type;
    }

    @JsonProperty("notice")
    public Notice getNotice()
    {
        return this.m_notice;
    }

    public void setNotice(Notice a_t_notice)
    {
        this.m_notice = a_t_notice;
    }
}
