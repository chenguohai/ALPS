package alps.alto.client.core;


/**
 *
 */

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */


import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("unused")
public final class AltoClientHttp
{
    private CloseableHttpClient httpclient;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private CloseableHttpResponse response;

    public AltoClientHttp(){
        httpclient = null;
        httpGet = null;
        httpPost = null;
        response = null;
    }
    public void setHttpGetRequest(HttpGet httpGet)
    {
        this.httpGet = httpGet;

    }
    public void setHttpPostRequest(HttpPost httpPost)
    {
        this.httpPost = httpPost;

    }

    public CloseableHttpResponse GetHttpGetResponse()throws Exception
    {
        CloseableHttpResponse retResponse;
        httpclient = HttpClients.createDefault();
        try{
            response = httpclient.execute(httpGet);
            String strResult=EntityUtils.toString(response.getEntity());
            retResponse=response;
        }
        finally{
            response.close();
            httpclient.close();
        }

        return retResponse;

    }
    public long GetHttpGetResponseString(List<String> httpResponse )throws Exception
    {
        long contentLength=-1;
        httpResponse.clear();
        httpclient = HttpClients.createDefault();
        try{
            response = httpclient.execute(httpGet);

            if((httpResponse!=null)&&(response!=null))
            {
                String httpStatusLine=response.getStatusLine().toString();
                HttpEntity entity=response.getEntity();
                contentLength=entity.getContentLength();
                String contentType=entity.getContentType().getValue();
                String strEntity=EntityUtils.toString(response.getEntity());
                httpResponse.add(httpStatusLine);
                httpResponse.add(contentType);
                httpResponse.add(strEntity);
            }
        }
        finally{
            if(response!=null)
                response.close();
            httpclient.close();
        }

        return contentLength;
    }

    public CloseableHttpResponse GetHttpPostResponse()throws Exception
    {
        CloseableHttpResponse retResponse;
        httpclient = HttpClients.createDefault();
        try{
            response = httpclient.execute(httpPost);
            retResponse=response;
        }
        finally{
            response.close();
            httpclient.close();
        }

        return retResponse;

    }
    public long GetHttpPostResponseString(List<String> httpResponse )throws Exception
    {
        long contentLength=-1;
        httpResponse.clear();
        httpclient = HttpClients.createDefault();
        try{
            response = httpclient.execute(httpPost);
            if((httpResponse!=null)&&(response!=null))
            {
                String httpStatusLine=response.getStatusLine().toString();
                HttpEntity entity=response.getEntity();
                contentLength=entity.getContentLength();
                String contentType=entity.getContentType().getValue();
                String strEntity=EntityUtils.toString(response.getEntity());
                httpResponse.add(httpStatusLine);
                httpResponse.add(contentType);
                httpResponse.add(strEntity);
            }
        }
        finally{
            if(response!=null)
                response.close();
            httpclient.close();
        }

        return contentLength;
    }

    public long GetHttpGetResponseStringInterOP(List<String> httpResponse )throws Exception
    {
        long contentLength=-1;
        httpResponse.clear();
        httpclient = HttpClients.createDefault();
        try{
            response = httpclient.execute(httpGet);

            if((httpResponse!=null)&&(response!=null))
            {

                String httpStatusLine=response.getStatusLine().toString();
                if(!httpStatusLine.contains("406"))
                {
                    HttpEntity entity=response.getEntity();
                    contentLength=entity.getContentLength();
                    String contentType=entity.getContentType().getValue();
                    String strEntity=EntityUtils.toString(response.getEntity());
                    httpResponse.add(response.getStatusLine().getReasonPhrase());
                    httpResponse.add(Integer.toString(response.getStatusLine().getStatusCode()));
                }
                else
                {

                    httpResponse.add(response.getStatusLine().getReasonPhrase());
                    httpResponse.add(Integer.toString(response.getStatusLine().getStatusCode()));

                }
            }
        }
        finally{
            if(response!=null)
                response.close();
            httpclient.close();
        }

        return contentLength;
    }
    public long GetHttpPostResponseStringInterOP(List<String> httpResponse )throws Exception
    {
        long contentLength=-1;
        httpResponse.clear();
        httpclient = HttpClients.createDefault();
        try{
            response = httpclient.execute(httpPost);
            if((httpResponse!=null)&&(response!=null))
            {
                String httpStatusLine=response.getStatusLine().toString();
                HttpEntity entity=response.getEntity();
                contentLength=entity.getContentLength();
                String contentType=entity.getContentType().getValue();
                String strEntity=EntityUtils.toString(response.getEntity());
                httpResponse.add(httpStatusLine);
                //httpResponse.add(Integer.toString(response.getStatusLine().getStatusCode()));
                //httpResponse.add(contentType);
                httpResponse.add(strEntity);

            }
        }
        finally{
            if(response!=null)
                response.close();
            httpclient.close();
        }

        return contentLength;
    }
}
