package com.template.framework.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPService
{

    public static String getRequest(String url, Map<String, String> headers) throws Exception
    {

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        if (headers != null && headers.size() > 0)
        {
            for (String key : headers.keySet())
            {
                httpGet.setHeader(key, headers.get(key));
            }
        }

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseString = null;

        if (entity != null)
        {
            InputStream inputStream = entity.getContent();
            try
            {
                responseString = IOUtils.toString(inputStream, "UTF-8");
            }
            finally
            {
                inputStream.close();
            }
        }
        return responseString;
    }


    public static String postRequest(String url, Map<String, String> headers, Map<String, String> data, String body) throws Exception
    {

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        if (data != null && data.size() > 0)
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>(data.size());
            for (String key : data.keySet())
            {
                params.add(new BasicNameValuePair(key, data.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        }

        if (headers != null && headers.size() > 0)
        {
            for (String key : headers.keySet())
            {
                httpPost.setHeader(key, headers.get(key));
            }
        }

        if (body != null)
        {
            httpPost.setEntity(new StringEntity(body));
        }

        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseString = null;

        if (entity != null)
        {
            InputStream inputStream = entity.getContent();
            try
            {
                responseString = IOUtils.toString(inputStream, "UTF-8");
            }
            finally
            {
                inputStream.close();
            }
        }
        return responseString;
    }

}

