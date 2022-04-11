package org.glycam.api.client.json.glycan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sequence
{
    private String m_payload = null;

    @JsonProperty("payload")
    public String getPayload()
    {
        return this.m_payload;
    }

    public void setPayload(String a_payload)
    {
        this.m_payload = a_payload;
    }
}
