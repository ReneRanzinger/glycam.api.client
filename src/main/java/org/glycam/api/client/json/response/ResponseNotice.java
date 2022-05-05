package org.glycam.api.client.json.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseNotice 
{
	private String m_type = null;
	private String m_code = null;
	private String m_brief = null;
	private String m_message = null;

	@JsonProperty("type")	
	public String getType() {
		return m_type;
	}
	public void setType(String a_type) {
		m_type = a_type;
	}
	@JsonProperty("code")
	public String getCode() {
		return m_code;
	}
	public void setCode(String a_code) {
		m_code = a_code;
	}
	@JsonProperty("brief")
	public String getBrief() {
		return m_brief;
	}
	public void setBrief(String a_brief) {
		m_brief = a_brief;
	}

	@JsonProperty("message")
	public String getMessage() {
		return m_message;
	}
	public void setMessage(String a_message) {
		m_message = a_message;
	}	
}
