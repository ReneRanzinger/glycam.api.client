package org.glycam.api.client.json.glycan;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entity
{
    private Inputs m_inputs = null;
    private List<String> m_resources = new ArrayList<>();
    private Services m_serivces = null;
    private String m_type = null;

    @JsonProperty("inputs")
    public Inputs getInputs()
    {
        return this.m_inputs;
    }

    public void setInputs(Inputs a_inputs)
    {
        this.m_inputs = a_inputs;
    }

    @JsonProperty("resources")
    public List<String> getResources()
    {
        return this.m_resources;
    }

    public void setResources(List<String> a_resources)
    {
        this.m_resources = a_resources;
    }

    @JsonProperty("services")
    public Services getSerivces()
    {
        return this.m_serivces;
    }

    public void setSerivces(Services a_serivces)
    {
        this.m_serivces = a_serivces;
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
}
