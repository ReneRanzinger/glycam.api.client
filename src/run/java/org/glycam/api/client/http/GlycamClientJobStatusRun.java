package org.glycam.api.client.http;

import java.io.IOException;

import org.glycam.api.client.json.ResponseUtil;
import org.glycam.api.client.om.GlycamJob;
import org.glycam.api.client.om.WebResponse;

/**
 *
 * @author Rene Ranzinger
 *
 */
public class GlycamClientJobStatusRun
{
    // https://github.com/GLYCAM-Web/website/issues/25
    public static void main(String[] args) throws IOException, InterruptedException
    {

        String[] t_jobIDs = new String[] { "10a24c39-ffb9-4016-9b3a-010b1ce55d52",
                "54ffd6a0-6c80-4aeb-ac9c-681d41ff2685", "5d953472-8e67-48d2-b391-dba59bb7250e",
                "ad705118-8c72-4218-a7cc-cbe819cf228d", "09e16fde-cfcb-436b-bc1b-f454343ba022",
                "4ae4ab66-960e-4c5e-ae6b-192e7b9544e6", "f938607a-ff25-4110-a299-e9ab59ea74d0",
                "6ef660b6-9c5a-4caf-b763-f60a0a30a1ee", "dd13e959-23aa-4819-af45-161c6ccb5a75",
                "2d37e7ff-7ced-4654-89ff-908a636e39b4", "b832a49f-53c6-488e-9fbe-d977ebeaca92",
                "1431ad58-816f-4c24-a60e-b11e162cc17b", "77bd2e66-a862-4f97-81b1-189e95236d31",
                "c600377c-aaa8-4613-a079-9cae627dd7f3", "71b353e4-12e3-478d-a96e-02a1d3d85bd8",
                "a27e4d21-c9e0-410a-a69d-b9d7a802437e", "471ebf1a-fed5-41c8-ad9b-5b14620a4931",
                "40c5270e-517a-44c5-bedf-261de29ba552", "beef0b20-0999-4bce-ad8b-43cb6dc0d884",
                "5f0d25b4-87f8-4f23-91f5-b32a083c4524", "adb68869-0dc5-4bbf-bb97-f6441377ac34",
                "085406bf-8aec-4f7f-8723-c72e31abb4e8", "a061572c-fb3f-4295-8394-bfca8fa8ec3d",
                "dd796482-e715-4f2b-9cbb-8e1f65550f3f", "b8c5f17f-07e4-45d8-bbd4-dab7b49d349c",
                "397f1ecf-beb2-44be-9f7b-77b208c6fe0b", "03e9d77f-caf6-47c2-8b6c-61ab488e4f22" };

        // create the client
        GlycamClient t_client = new GlycamClient("https://glycam.org/json/");
        ResponseUtil t_util = new ResponseUtil();

        for (String t_jobId : t_jobIDs)
        {
            GlycamJob t_job = new GlycamJob();
            t_job.setJobId(t_jobId);
            WebResponse t_statusJson = t_client.getStatus(t_job);
            String t_status = t_util.processPollingResponse(t_statusJson.getContent());

            System.out.println(t_jobId + " - " + t_status);
            Thread.sleep(1000);
        }
        // close the client connection and cleanup
        t_client.close();

    }

}
