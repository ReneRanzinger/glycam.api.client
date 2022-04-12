package org.glycam.api.client.json.polling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PollingStatus
{
    private String m_status = null;

    @JsonProperty("status")
    public String getStatus()
    {
        return this.m_status;
    }

    public void setStatus(String a_status)
    {
        this.m_status = a_status;
    }
}
