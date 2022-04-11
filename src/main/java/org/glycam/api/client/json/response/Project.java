package org.glycam.api.client.json.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Project
{
    private String m_id = null;
    private String m_downloadUrl = null;
    private String m_timestamp = null;

    @JsonProperty("pUUID")
    public String getId()
    {
        return this.m_id;
    }

    public void setId(String a_id)
    {
        this.m_id = a_id;
    }

    @JsonProperty("download_url_path")
    public String getDownloadUrl()
    {
        return this.m_downloadUrl;
    }

    public void setDownloadUrl(String a_downloadUrl)
    {
        this.m_downloadUrl = a_downloadUrl;
    }

    @JsonProperty("timestamp")
    public String getTimestamp()
    {
        return this.m_timestamp;
    }

    public void setTimestamp(String a_timestamp)
    {
        this.m_timestamp = a_timestamp;
    }
}
