package org.glycam.api.client.json;

import java.io.IOException;
import java.util.List;

import org.glycam.api.client.json.polling.PollingStatus;
import org.glycam.api.client.json.response.Entity;
import org.glycam.api.client.json.response.FrontEndNotice;
import org.glycam.api.client.json.response.Notice;
import org.glycam.api.client.json.response.Project;
import org.glycam.api.client.json.response.Responses;
import org.glycam.api.client.json.response.SubmitInformation;
import org.glycam.api.client.json.response.TopLevelNotice;
import org.glycam.api.client.om.GlycamJob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil
{
    public void processGlycanResponse(GlycamJob a_job)
            throws JsonMappingException, JsonProcessingException
    {
        ObjectMapper t_mapper = new ObjectMapper();
        SubmitInformation t_responseInfo = t_mapper.readValue(a_job.getResponse(),
                SubmitInformation.class);
        if (!this.checkEntity(a_job, t_responseInfo))
        {
            return;
        }
        if (!this.checkProject(a_job, t_responseInfo))
        {
            return;
        }
        if (!this.checkNotice(a_job, t_responseInfo))
        {
            return;
        }
    }

    private boolean checkProject(GlycamJob a_job, SubmitInformation a_responseInfo)
    {
        Project t_project = a_responseInfo.getProject();
        if (t_project == null)
        {
            a_job.setErrorType("Response JSON error");
            a_job.setErrorMessage("Missing project property");
            return false;
        }
        String t_id = t_project.getId();
        if (t_id == null)
        {
            a_job.setErrorType("Response JSON error");
            a_job.setErrorMessage("Missing pUUID property in project");
            return false;
        }
        a_job.setJobId(t_id);
        String t_timestamp = t_project.getTimestamp();
        if (t_timestamp == null)
        {
            a_job.setErrorType("Response JSON error");
            a_job.setErrorMessage("Missing timestamp property in project");
            return false;
        }
        a_job.setTimestamp(t_timestamp);
        String t_downloadPath = t_project.getDownloadUrl();
        if (t_downloadPath == null)
        {
            a_job.setErrorType("Response JSON error");
            a_job.setErrorMessage("Missing download_url_path property in project");
            return false;
        }
        a_job.setDownloadURL(t_downloadPath);
        return true;
    }

    private boolean checkNotice(GlycamJob a_job, SubmitInformation a_responseInfo)
    {
        List<TopLevelNotice> t_notices = a_responseInfo.getNotices();
        if (t_notices == null)
        {
            return true;
        }
        if (t_notices.size() == 0)
        {
            return true;
        }
        for (TopLevelNotice t_topLevelNotice : t_notices)
        {
            List<String> t_types = t_topLevelNotice.getTypes();
            if (t_types == null || t_types.size() == 0)
            {
                a_job.setErrorType("Response JSON error");
                a_job.setErrorMessage("Missing noticeType property in notices");
                return false;
            }
            List<String> t_codes = t_topLevelNotice.getCodes();
            if (t_codes == null || t_codes.size() == 0)
            {
                a_job.setErrorType("Response JSON error");
                a_job.setErrorMessage("Missing noticeCode property in notices");
                return false;
            }
            String t_brief = t_topLevelNotice.getBrief();
            if (t_brief == null)
            {
                a_job.setErrorType("Response JSON error");
                a_job.setErrorMessage("Missing noticeBrief property in notices");
                return false;
            }
            a_job.setErrorType(this.listToString(t_types) + " (Codes " + this.listToString(t_codes)
                    + "): " + t_brief);
            a_job.setErrorMessage(t_topLevelNotice.getMessage());
            return false;
        }
        return true;
    }

    private String listToString(List<String> a_list)
    {
        StringBuffer t_result = new StringBuffer();
        boolean t_first = true;
        for (String t_string : a_list)
        {
            if (t_first)
            {
                t_result.append(t_string);
            }
            else
            {
                t_result.append(", " + t_string);
            }
        }
        return t_result.toString();
    }

    private boolean checkEntity(GlycamJob a_job, SubmitInformation a_responseInfo)
    {
        Entity t_entity = a_responseInfo.getEntity();
        if (t_entity == null)
        {
            a_job.setErrorType("Response JSON error");
            a_job.setErrorMessage("Missing entity property");
            return false;
        }
        List<Responses> t_responses = t_entity.getResponses();
        if (t_responses == null)
        {
            return true;
        }
        if (t_responses.size() == 0)
        {
            return true;
        }
        for (Responses t_response : t_responses)
        {
            FrontEndNotice t_noticeFrontEnd = t_response.getFrontendNotice();
            if (t_noticeFrontEnd == null)
            {
                a_job.setErrorType("Response JSON error");
                a_job.setErrorMessage(
                        "Missing FrontEndNotice property in responses array (entity)");
                return false;
            }
            Notice t_notice = t_noticeFrontEnd.getNotice();
            if (t_notice == null)
            {
                a_job.setErrorType("Response JSON error");
                a_job.setErrorMessage(
                        "Missing FrontEndNotice property in responses array (entity)");
                return false;
            }
            a_job.setErrorType("Request JSON error");
            a_job.setErrorMessage("Code " + t_notice.getCode() + ": " + t_notice.getMessage());
            return false;
        }
        return true;
    }

    public String processPollingResponse(String a_statusJson) throws IOException
    {
        ObjectMapper t_mapper = new ObjectMapper();
        PollingStatus t_responseInfo = t_mapper.readValue(a_statusJson, PollingStatus.class);
        if (t_responseInfo.getStatus() == null)
        {
            throw new IOException("Missing status property in polling response.");
        }
        return t_responseInfo.getStatus();
    }
}
