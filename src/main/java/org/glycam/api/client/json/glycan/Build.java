package org.glycam.api.client.json.glycan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Build
{
    private String m_type = null;

    @JsonProperty("type")
    public String getType()
    {
        return this.m_type;
    }

    public void setType(String a_type)
    {
        this.m_type = a_type;
    }
}
