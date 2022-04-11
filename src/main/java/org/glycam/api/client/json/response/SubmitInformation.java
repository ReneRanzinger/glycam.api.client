package org.glycam.api.client.json.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitInformation
{
    private Entity m_entity = null;
    private Project m_project = null;
    private List<TopLevelNotice> m_notices = new ArrayList<>();

    @JsonProperty("entity")
    public Entity getEntity()
    {
        return this.m_entity;
    }

    public void setEntity(Entity a_entiry)
    {
        this.m_entity = a_entiry;
    }

    @JsonProperty("project")
    public Project getProject()
    {
        return this.m_project;
    }

    public void setProject(Project a_project)
    {
        this.m_project = a_project;
    }

    @JsonProperty("notices")
    public List<TopLevelNotice> getNotices()
    {
        return this.m_notices;
    }

    public void setNotices(List<TopLevelNotice> a_notices)
    {
        this.m_notices = a_notices;
    }
}
