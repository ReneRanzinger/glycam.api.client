package org.glycam.api.client.json.glycan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Services
{
    private Build m_build = null;

    @JsonProperty("build")
    public Build getBuild()
    {
        return this.m_build;
    }

    public void setBuild(Build a_build)
    {
        this.m_build = a_build;
    }
}
