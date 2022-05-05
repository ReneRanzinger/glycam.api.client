package org.glycam.api.client.json.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Responses
{
    private FrontEndNotice m_frontendNotice = null;
    private ResponseError m_error = null;

    @JsonProperty("FrontEndNotice")
    public FrontEndNotice getFrontendNotice()
    {
        return this.m_frontendNotice;
    }

    public void setFrontendNotice(FrontEndNotice a_frontendNotice)
    {
        this.m_frontendNotice = a_frontendNotice;
    }

    @JsonProperty("Error")
	public ResponseError getError() {
		return m_error;
	}

	public void setError(ResponseError a_error) {
		this.m_error = a_error;
	}
}
