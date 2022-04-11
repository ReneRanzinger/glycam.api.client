package org.glycam.api.client.json.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity
{
    private List<Responses> m_responses = new ArrayList<>();

    @JsonProperty("responses")
    public List<Responses> getResponses()
    {
        return this.m_responses;
    }

    public void setResponses(List<Responses> a_responses)
    {
        this.m_responses = a_responses;
    }
}
