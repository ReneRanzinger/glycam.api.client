package org.glycam.api.client.http;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.glycam.api.client.json.RequestBuilder;
import org.glycam.api.client.om.GlycamJob;
import org.glycam.api.client.om.WebResponse;

public class GlycamClient
{
    private String m_baseUrl = null;
    private BasicCookieStore m_cookieStore = null;
    private CloseableHttpClient m_httpclient = null;
    private String m_csrfToken = null;

    public GlycamClient(String a_baseURL) throws ClientProtocolException, IOException
    {
        this.m_baseUrl = a_baseURL;
        // we need to get the cookie to be used in the subsequent calls
        this.connect();
    }

    private void connect() throws ClientProtocolException, IOException
    {
        int timeout = 90;
        RequestConfig t_config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD)
                .setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        // configuration for self certificate
        SSLConnectionSocketFactory sslsf = null;
        try
        {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, (chain, authType) -> true);
            sslsf = new SSLConnectionSocketFactory(builder.build());
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
        // create cookie store and HTTP client
        this.m_cookieStore = new BasicCookieStore();
        this.m_httpclient = HttpClients.custom().setDefaultCookieStore(this.m_cookieStore)
                .setSSLSocketFactory(sslsf).setDefaultRequestConfig(t_config).build();
        // get request the token to get initial cookies
        HttpGet t_httpGet = new HttpGet(this.m_baseUrl + "getToken/");
        CloseableHttpResponse t_response = this.m_httpclient.execute(t_httpGet);
        HttpEntity t_entity = t_response.getEntity();
        if (t_response.getStatusLine().getStatusCode() >= 400)
        {
            throw new IOException("Requesting cookie result in HTTP code: "
                    + Integer.toString(t_response.getStatusLine().getStatusCode()));
        }
        // now we read the response to get the token
        this.m_csrfToken = this.entityToString(t_entity);
        // consume and close the response
        EntityUtils.consume(t_entity);
        t_response.close();
    }

    public void close() throws IOException
    {
        this.m_httpclient.close();
    }

    public String entityToString(HttpEntity a_entity)
            throws UnsupportedOperationException, IOException
    {
        StringWriter t_writer = new StringWriter();
        IOUtils.copy(a_entity.getContent(), t_writer, StandardCharsets.UTF_8);
        return t_writer.toString();
    }

    public void submitGlycanForBuild(GlycamJob a_job) throws IOException
    {
        String t_json = RequestBuilder.buildGlycanBuildRequest(a_job.getGlycam());
        a_job.setRequest(t_json);
        try
        {
            // build post request
            HttpPost t_httpPost = new HttpPost(this.m_baseUrl);
            // set the json as payload
            StringEntity t_entityJson = new StringEntity(t_json);
            t_httpPost.setEntity(t_entityJson);
            // add content type and token
            t_httpPost.setHeader("Accept", "application/json");
            t_httpPost.setHeader("Content-type", "application/json");
            t_httpPost.setHeader("X-CSRFToken", this.m_csrfToken);
            // execute request
            CloseableHttpResponse t_response = this.m_httpclient.execute(t_httpPost);
            a_job.setHttpCode(t_response.getStatusLine().getStatusCode());
            HttpEntity t_entity = t_response.getEntity();
            // extract response
            String t_responseContent = this.entityToString(t_entity);
            a_job.setResponse(t_responseContent);
            a_job.setStatus(GlycamJob.STATUS_SUBMITTED);
            // close response
            EntityUtils.consume(t_entity);
            t_response.close();
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void submitGlycanForEvaluate(GlycamJob a_job) throws IOException
    {
        String t_json = RequestBuilder.buildGlycanEvaluateRequest(a_job.getGlycam());
        a_job.setRequest(t_json);
        try
        {
            // build post request
            HttpPost t_httpPost = new HttpPost(this.m_baseUrl);
            // set the json as payload
            StringEntity t_entityJson = new StringEntity(t_json);
            t_httpPost.setEntity(t_entityJson);
            // add content type and token
            t_httpPost.setHeader("Accept", "application/json");
            t_httpPost.setHeader("Content-type", "application/json");
            t_httpPost.setHeader("X-CSRFToken", this.m_csrfToken);
            // execute request
            CloseableHttpResponse t_response = this.m_httpclient.execute(t_httpPost);
            a_job.setHttpCode(t_response.getStatusLine().getStatusCode());
            HttpEntity t_entity = t_response.getEntity();
            // extract response
            String t_responseContent = this.entityToString(t_entity);
            a_job.setResponse(t_responseContent);
            a_job.setStatus(GlycamJob.STATUS_SUBMITTED);
            // close response
            EntityUtils.consume(t_entity);
            t_response.close();
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    public WebResponse getStatus(GlycamJob a_job) throws IOException
    {
        try
        {
            // build post request
            HttpGet t_httpGet = new HttpGet(
                    this.m_baseUrl + "project_status/sequence/" + a_job.getJobId() + "/");
            // add content type and token
            t_httpGet.setHeader("Accept", "application/json");
            t_httpGet.setHeader("Content-type", "application/json");
            t_httpGet.setHeader("X-CSRFToken", this.m_csrfToken);
            // execute request
            CloseableHttpResponse t_response = this.m_httpclient.execute(t_httpGet);
            HttpEntity t_entity = t_response.getEntity();
            // extract response
            String t_responseContent = this.entityToString(t_entity);
            WebResponse t_result = new WebResponse(t_responseContent,
                    t_response.getStatusLine().getStatusCode());
            // close response
            EntityUtils.consume(t_entity);
            t_response.close();
            return t_result;
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }

    }

    public void downloadPDB(String a_downloadURL, String a_fileNamePath)
            throws ClientProtocolException, IOException
    {
        HttpGet t_httpGet = new HttpGet(a_downloadURL);
        CloseableHttpResponse t_response = this.m_httpclient.execute(t_httpGet);
        HttpEntity t_entity = t_response.getEntity();
        if (t_response.getStatusLine().getStatusCode() >= 400)
        {
            throw new IOException("Requesting PDB results in HTTP code: "
                    + Integer.toString(t_response.getStatusLine().getStatusCode()));
        }
        FileWriter t_writer = new FileWriter(new File(a_fileNamePath));
        IOUtils.copy(t_entity.getContent(), t_writer, StandardCharsets.UTF_8);
        // consume and close the response
        EntityUtils.consume(t_entity);
        t_response.close();
        t_writer.flush();
        t_writer.close();
    }

}
