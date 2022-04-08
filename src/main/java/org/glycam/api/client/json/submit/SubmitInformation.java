package org.glycam.api.client.json.submit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitInformation
{
    private Entity m_entiry = null;

    @JsonProperty("entity")
    public Entity getEntiry()
    {
        return this.m_entiry;
    }

    public void setEntiry(Entity a_entiry)
    {
        this.m_entiry = a_entiry;
    }
}
