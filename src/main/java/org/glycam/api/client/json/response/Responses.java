package org.glycam.api.client.json.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Responses
{
    private FrontEndNotice m_frontendNotice = null;

    @JsonProperty("FrontEndNotice")
    public FrontEndNotice getFrontendNotice()
    {
        return this.m_frontendNotice;
    }

    public void setFrontendNotice(FrontEndNotice a_frontendNotice)
    {
        this.m_frontendNotice = a_frontendNotice;
    }
}
