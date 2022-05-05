package org.glycam.api.client.json.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseError 
{
	private String m_respondingService = null;
	private ResponseNotice m_notice = null;
	
	@JsonProperty("respondingService")
	public String getRespondingService() 
	{
		return m_respondingService;
	}
	public void setRespondingService(String m_respondingService) 
	{
		this.m_respondingService = m_respondingService;
	}
	@JsonProperty("notice")
	public ResponseNotice getNotice() 
	{
		return m_notice;
	}
	
	public void setNotice(ResponseNotice a_notice) 
	{
		this.m_notice = a_notice;
	}
	
}
