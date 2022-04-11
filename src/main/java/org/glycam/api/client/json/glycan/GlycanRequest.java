package org.glycam.api.client.json.glycan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlycanRequest
{
    private Entity m_entity = null;

    @JsonProperty("entity")
    public Entity getEntity()
    {
        return this.m_entity;
    }

    public void setEntity(Entity a_entity)
    {
        this.m_entity = a_entity;
    }
}
