package org.glycam.api.client.om;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GlycamJob
{
    public final static String STATUS_INIT = "initialized";
    public final static String STATUS_SUBMITTED = "submitted";
    public final static String STATUS_ERROR = "error";
    public final static String STATUS_TIMEOUT = "timeout";
    public final static String STATUS_PDB_ERROR = "pdb problem";
    public final static String STATUS_SUCCESS = "success";

    private String m_glycam = null;
    private String m_glyTouCanId = null;
    private String m_request = null;
    private String m_response = null;
    private String m_jobId = null;
    private String m_downloadURL = null;
    private Integer m_httpCode = null;
    private String m_errorType = null;
    private String m_errorMessage = null;
    private String m_status = GlycamJob.STATUS_INIT;
    private String m_timestampGlycam = null;
    private Long m_timestampSubmission = null;
    private Long m_timestampLastCheck = null;

    @JsonProperty("request")
    public String getRequest()
    {
        return this.m_request;
    }

    public void setRequest(String a_request)
    {
        this.m_request = a_request;
    }

    @JsonProperty("response")
    public String getResponse()
    {
        return this.m_response;
    }

    public void setResponse(String a_response)
    {
        this.m_response = a_response;
    }

    @JsonProperty("job_id")
    public String getJobId()
    {
        return this.m_jobId;
    }

    public void setJobId(String a_jobId)
    {
        this.m_jobId = a_jobId;
    }

    @JsonProperty("url")
    public String getDownloadURL()
    {
        return this.m_downloadURL;
    }

    public void setDownloadURL(String a_downloadURL)
    {
        this.m_downloadURL = a_downloadURL;
    }

    @JsonProperty("http_code")
    public Integer getHttpCode()
    {
        return this.m_httpCode;
    }

    public void setHttpCode(Integer a_httpCode)
    {
        this.m_httpCode = a_httpCode;
    }

    @JsonProperty("error_message")
    public String getErrorMessage()
    {
        return this.m_errorMessage;
    }

    public void setErrorMessage(String a_errorMessage)
    {
        this.m_errorMessage = a_errorMessage;
    }

    @JsonProperty("error_type")
    public String getErrorType()
    {
        return this.m_errorType;
    }

    public void setErrorType(String a_error)
    {
        this.m_errorType = a_error;
    }

    @JsonProperty("status")
    public String getStatus()
    {
        return this.m_status;
    }

    public void setStatus(String a_status)
    {
        this.m_status = a_status;
    }

    @JsonProperty("glycam")
    public String getGlycam()
    {
        return this.m_glycam;
    }

    public void setGlycam(String a_glycam)
    {
        this.m_glycam = a_glycam;
    }

    @JsonProperty("glytoucan")
    public String getGlyTouCanId()
    {
        return this.m_glyTouCanId;
    }

    public void setGlyTouCanId(String a_glyTouCanId)
    {
        this.m_glyTouCanId = a_glyTouCanId;
    }

    @JsonProperty("time_glycam")
    public String getTimestampGlycam()
    {
        return this.m_timestampGlycam;
    }

    public void setTimestampGlycam(String a_timestampGlycam)
    {
        this.m_timestampGlycam = a_timestampGlycam;
    }

    @JsonProperty("time_submission")
    public Long getTimestampSubmission()
    {
        return this.m_timestampSubmission;
    }

    public void setTimestampSubmission(Long a_timestampSubmission)
    {
        this.m_timestampSubmission = a_timestampSubmission;
    }

    @JsonProperty("time_check")
    public Long getTimestampLastCheck()
    {
        return this.m_timestampLastCheck;
    }

    public void setTimestampLastCheck(Long a_timestampLastCheck)
    {
        this.m_timestampLastCheck = a_timestampLastCheck;
    }

    @JsonIgnore
    public boolean hasError()
    {
        if (this.m_status.equals(STATUS_ERROR))
        {
            return true;
        }
        return false;
    }
}
