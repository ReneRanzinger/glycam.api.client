package org.glycam.api.client.json.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopLevelNotice
{
    private List<String> m_types = new ArrayList<>();
    private List<String> m_codes = new ArrayList<>();
    private String m_brief = null;
    private String m_message = null;

    @JsonProperty("noticeType")
    public List<String> getTypes()
    {
        return this.m_types;
    }

    public void setTypes(List<String> a_types)
    {
        this.m_types = a_types;
    }

    @JsonProperty("noticeCode")
    public List<String> getCodes()
    {
        return this.m_codes;
    }

    public void setCodes(List<String> a_codes)
    {
        this.m_codes = a_codes;
    }

    @JsonProperty("noticeBrief")
    public String getBrief()
    {
        return this.m_brief;
    }

    public void setBrief(String a_brief)
    {
        this.m_brief = a_brief;
    }

    @JsonProperty("noticeMessage")
    public String getMessage()
    {
        return this.m_message;
    }

    public void setMessage(String a_message)
    {
        this.m_message = a_message;
    }
}
