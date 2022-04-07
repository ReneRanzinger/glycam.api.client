package org.glycam.api.client.util;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.glycam.api.client.om.SubmitResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ResponseUtil
{
	private SubmitResponse m_result = null;

	private ResponseUtil(SubmitResponse a_result)
	{
		this.m_result = a_result;
	}

	public static String entityToString(HttpEntity a_entity) throws UnsupportedOperationException, IOException
	{
		StringWriter t_writer = new StringWriter();
		IOUtils.copy(a_entity.getContent(), t_writer, StandardCharsets.UTF_8);
		return t_writer.toString();
	}

	public static void processGlycanResponse(String a_json, SubmitResponse a_result)
	{
		ResponseUtil t_util = new ResponseUtil(a_result);
		// parse the JSON string
		JSONParser t_parser = new JSONParser();
		JSONObject t_jsonObject = (JSONObject)t_parser.parse(a_json);
		t_util.parseJSON(t_jsonObject);
	}

	private void parseJSON(JSONObject a_json)
	{
		JSONObject t_entity = ResponseUtil.getObject(a_json, "entity");
		if ( t_entity == null)
		{
			this.m_result.setSuccessful(false);
			this.m_result.setErrorMessage("Unable to find entity property in JSON response.");
		}
		else
		{
			if (this.errorInResponse(t_entity))
			{
				this.m_result.setSuccessful(false);
				return;
			}
			if (this.invalidSequence(t_entity))
			{
				this.m_result.setSuccessful(false);
				this.m_result.setErrorMessage("Invalid Glycam sequence.");
				return;
			}
			this.fillProcessInformation();			
		}
	}

	private boolean errorInResponse(JSONObject a_entity) 
	{
		JSONArray t_responses = ResponseUtil.getArray(a_entity, "responses");
		if ( t_responses == null )
		{
			return false;
		}
		for (Object t_object : t_responses) 
		{
			JSONObject t_response = (JSONObject) t_object; 
			// check for error
			JSONObject t_errorObject = ResponseUtil.getObject(t_response, "Error");
			if ( t_errorObject != null )
			{
				this.handleError(t_errorObject);
				return true;
			}
			// check for FrontEndNotice
			t_errorObject = ResponseUtil.getObject(t_response, "FrontEndNotice");
			if ( t_errorObject != null )
			{
				this.handleFrontEndNotice(t_errorObject);
				return true;
			}
		}
		return false;
	}

	private void handleError(JSONObject a_errorObject) 
	{
		
	}

	private static JSONObject getObject(JSONObject a_jsonObject, String a_key)
	{
		return (JSONObject) a_jsonObject.get(a_key);
	}

	private static JSONArray getArray(JSONObject a_jsonObject, String a_key)
	{
		return (JSONArray) a_jsonObject.get(a_key);
	}

	private static String getString(JSONObject a_jsonObject, String a_key) throws IOException
	{
		Object t_stringObject = a_jsonObject.get(a_key);
		if (t_stringObject == null)
		{
			return null;
		}
		if (t_stringObject instanceof String)
		{
			return (String) t_stringObject;
		}
		else
		{
			throw new IOException("JSON format error: Value for " + a_key + " is not a string.");
		}
	}

}
