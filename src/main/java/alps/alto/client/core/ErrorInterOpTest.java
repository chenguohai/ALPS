package alps.alto.client.core;

/**
 *
 */

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings("unused")
public final class ErrorInterOpTest {

    private String hostName;
    private String requestLine;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private List<String> httpResponse;
    private String uri;

    public ErrorInterOpTest(String hostName,String requestLine,String acceptInput)
    {

        if(hostName==null)
            hostName=AltoClientConst.AltoClientRequestHost.NetworkMapRequestHost;
        if(requestLine==null)
            requestLine=AltoClientConst.AltoClientRequestLine.NetworkMapRequestLine;
        this.hostName = hostName;
        this.requestLine = requestLine;
        this.httpResponse=new ArrayList<String>();

        String httpGetPara=this.hostName;
        httpGetPara= "http://"+httpGetPara +"/"+this.requestLine;
        uri=httpGetPara;
        httpGet=new HttpGet(httpGetPara);

        httpGet.setHeader("Accept", acceptInput);
        //httpGet.setHeader("Accept", AltoClientAcceptConst.NetworkMapAccept);

        AltoClientHttp altoClientHttpIns= new AltoClientHttp();
        altoClientHttpIns.setHttpGetRequest(httpGet);

        try
        {
            long length=altoClientHttpIns.GetHttpGetResponseStringInterOP(httpResponse);
        } catch (Exception e)
        {
            e.printStackTrace();
            httpResponse.clear();
        }
    }
    public ErrorInterOpTest(String operationError,String hostName,String requestLine,List<String> properties,List<String> endpoints)
    {
        if(hostName==null)
            hostName=AltoClientConst.AltoClientRequestHost.EndPointPropRequestHost;
        if(requestLine==null)
            requestLine=AltoClientConst.AltoClientRequestLine.EndPointPropRequestLine;
        this.hostName = hostName;
        this.requestLine = requestLine;
        //this.requestLine = requestLine+"/lookup";
        this.httpResponse=new ArrayList<String>();

        String httpPostPara=this.hostName;
        httpPostPara= "http://"+httpPostPara +"/"+this.requestLine;
        uri=httpPostPara;
        httpPost=new HttpPost(httpPostPara);

        JSONObject obj = new JSONObject();
        try {

            if(operationError.contains("52MPF")){

            }
            else if(operationError.contains("58JSE"))
            {
                obj.put("properties", "");
            }
            else
            {
                obj.put("properties", properties);
            }

            if(operationError.contains("51IFT")){
                obj.put("endpoints", endpoints.get(0));
            }
            else
            {
                obj.put("endpoints", endpoints);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        StringEntity sEntity=null;
        try {
            sEntity = new StringEntity(obj.toString());
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        //sEntity.setContentEncoding("UTF-8");
        sEntity.setContentType(AltoClientConst.AltoClientFilteredContentTypeString.EndPointProperty);
        httpPost.setEntity(sEntity);
        if(operationError.contains("510IAH"))
        {
            httpPost.setHeader("Accept", "text/html");
        }
        else if(operationError.contains("511ICT"))
        {
            httpPost.setHeader("Accept", "text/text");
        }
        else
        {
            httpPost.setHeader("Accept", AltoClientConst.AltoClientAcceptConst.EndPointPropAccept);
        }


        AltoClientHttp altoClientHttpIns= new AltoClientHttp();
        altoClientHttpIns.setHttpPostRequest(httpPost);

        try
        {
            long length=altoClientHttpIns.GetHttpPostResponseStringInterOP(httpResponse);
            /*if(length ==-1)
                throw(new Exception("http error!"));*/

        } catch (Exception e)
        {
            e.printStackTrace();
            httpResponse.clear();
        }
    }
    public ErrorInterOpTest(String operationError,String hostName,String requestLine,CostType costType,List<String> srcPids,List<String> dstPids,List<String> constraints)
    {
        if(hostName==null)
            hostName=AltoClientConst.AltoClientRequestHost.CostMapRequestHost;
        if(requestLine==null)
            requestLine=AltoClientConst.AltoClientRequestLine.CostMapRequestLine;
        this.hostName = hostName;
        this.requestLine = requestLine;
        //this.requestLine = requestLine+"/filtered";
        this.httpResponse=new ArrayList<String>();

        String httpPostPara=this.hostName;
        httpPostPara= "http://"+httpPostPara +"/"+this.requestLine;
        uri=httpPostPara;
        httpPost=new HttpPost(httpPostPara);

        JSONObject costTypeObj = new JSONObject();
        try
        {
            costTypeObj.put("cost-mode", costType.getMode());
            costTypeObj.put("cost-metric", costType.getMetric());

        } catch (JSONException e1)
        {
            e1.printStackTrace();
        }
        JSONObject pidsObj = new JSONObject();
        try
        {
            pidsObj.put("srcs", srcPids);
            pidsObj.put("dsts", dstPids);
        } catch (JSONException e1)
        {
            e1.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        try
        {
            obj.put("cost-type", costTypeObj);
            obj.put("pids", pidsObj);
            if(constraints!=null)
            {
                obj.put("constraints", constraints);
            }
        } catch (JSONException e1)
        {
            e1.printStackTrace();
        }

        StringEntity sEntity=null;
        try
        {
            sEntity = new StringEntity(obj.toString());
        } catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        //sEntity.setContentEncoding("UTF-8");
        sEntity.setContentType(AltoClientConst.AltoClientFilteredContentTypeString.CostMap);
        httpPost.setEntity(sEntity);
        httpPost.setHeader("Accept", AltoClientConst.AltoClientAcceptConst.CostMapAccept);

        AltoClientHttp altoClientHttpIns= new AltoClientHttp();
        altoClientHttpIns.setHttpPostRequest(httpPost);

        try
        {
            long length=altoClientHttpIns.GetHttpPostResponseStringInterOP(httpResponse);

        } catch (Exception e)
        {
            e.printStackTrace();
            httpResponse.clear();
        }
    }
    public int getInfo(List<String> getInfoList)
    {
        if((this.httpResponse!=null) && (getInfoList!=null))
        {
            getInfoList.clear();
            for(String loop:this.httpResponse)
            {
                getInfoList.add(loop);
            }
            return 0;
        }
        else
             return -1;
    }
    /**
     * @param args
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws JSONException, UnsupportedEncodingException
    {
        System.out.println("begin to start test");
        for(String loop:args)
        {
            System.out.println(loop);
        }

        String testOperation=args[0];

        ErrorInterOpTest errorInterOpTest=null;
        if(testOperation.contains("59IAH"))
        {
            errorInterOpTest=new ErrorInterOpTest(args[1],args[2],args[3]);
            List<String> getInfoList=new ArrayList<String>();
            errorInterOpTest.getInfo(getInfoList);
            System.out.println(getInfoList);
        }
        else if(testOperation.contains("51IFT") || testOperation.contains("52MPF") || testOperation.contains("53IPN") || testOperation.contains("54IEA") || testOperation.contains("58JSE") || testOperation.contains("510IAH") || testOperation.contains("511ICT"))
        {
            int propertiesCnt=Integer.parseInt(args[3]);
            int EPCnt=Integer.parseInt(args[propertiesCnt+4]);
            int EPPos=propertiesCnt+4;

            List<String> EPs=new ArrayList<String>();
            List<String> properties=new ArrayList<String>();
            while(propertiesCnt > 0)
            {
                properties.add(args[3+propertiesCnt]);
                propertiesCnt=propertiesCnt-1;
            }
            while(EPCnt > 0)
            {
                EPs.add(args[EPCnt+EPPos]);
                EPCnt=EPCnt-1;
            }

            System.out.println("EPs "+EPs.toString());
            System.out.println("properties "+properties.toString());
            errorInterOpTest=new ErrorInterOpTest(args[0],args[1],args[2],properties,EPs);
            List<String> getInfoList=new ArrayList<String>();
            errorInterOpTest.getInfo(getInfoList);
            System.out.println("http status: "+getInfoList.get(0));
            if(!(testOperation.contains("510IAH") || testOperation.contains("511ICT") )){
                JSONObject jsonobj = new JSONObject(getInfoList.get(1));
                JSONObject metaObj=(JSONObject) (jsonobj.get("meta"));
                System.out.println("code: "+metaObj.getString("code"));
                System.out.println("field: "+metaObj.getString("field"));
                if(testOperation.contains("53IPN") || testOperation.contains("54IEA") )
                {
                    System.out.println("value: "+metaObj.getString("value"));

                }
            }
        }
        else
        {
            CostType postCostType=new CostType();
            postCostType.setMetric(args[3]);
            postCostType.setMode(args[4]);
            List<String> srcPids=new ArrayList<String>();
            List<String> dstPids=new ArrayList<String>();
            List<String> constraints=new ArrayList<String>();
            constraints.add(args[5]);

            System.out.println("CostType"+postCostType.toString());
            System.out.println("constraints "+constraints.toString());

            if(args[0].contains("57ICC")){
                errorInterOpTest=new ErrorInterOpTest(args[0],args[1],args[2],postCostType,srcPids,dstPids,constraints);
            }
            else
            {
                errorInterOpTest=new ErrorInterOpTest(args[0],args[1],args[2],postCostType,srcPids,dstPids,null);
            }
            List<String> getInfoList=new ArrayList<String>();
            errorInterOpTest.getInfo(getInfoList);
            System.out.println("http status: "+getInfoList.get(0));
            JSONObject jsonobj = new JSONObject(getInfoList.get(1));
            JSONObject metaObj=(JSONObject) (jsonobj.get("meta"));
            System.out.println("code: "+metaObj.getString("code"));
            System.out.println("field: "+metaObj.getString("field"));
            System.out.println("value: "+metaObj.getString("value"));
        }
        System.out.println("done");

    }
}
