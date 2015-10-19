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
public final class AltoClientEndPointCost {

    private String hostName;
    private String requestLine;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private List<String> httpResponse;
    private String uri;

    public AltoClientEndPointCost(String hostName,String requestLine,CostType costType,List<String> srcEPs,List<String> dstEPs)
    {
        if(hostName==null)
            hostName=AltoClientConst.AltoClientRequestHost.EndPointCostRequestHost;
        if(requestLine==null)
            requestLine=AltoClientConst.AltoClientRequestLine.EndPointCostRequestLine;
        this.hostName = hostName;
        this.requestLine = requestLine;
        //this.requestLine = requestLine+"/lookup";
        this.httpResponse=new ArrayList<String>();

        String httpPostPara=this.hostName;
        httpPostPara= "http://"+httpPostPara +"/"+this.requestLine;
        uri=httpPostPara;
        httpPost=new HttpPost(httpPostPara);

        JSONObject objCostType = new JSONObject();
        try {
            objCostType.put("cost-mode", costType.getMode());
            objCostType.put("cost-metric", costType.getMetric());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JSONObject objPids = new JSONObject();
        try {
            objPids.put("srcs", srcEPs);
            objPids.put("dsts", dstEPs);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }


        JSONObject obj = new JSONObject();
        try {
            obj.put("cost-type", objCostType);
            obj.put("endpoints", objPids);
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
        sEntity.setContentType(AltoClientConst.AltoClientFilteredContentTypeString.EndPointCost);
        httpPost.setEntity(sEntity);
        httpPost.setHeader("Accept", AltoClientConst.AltoClientAcceptConst.EndPointCostAccept);

        AltoClientHttp altoClientHttpIns= new AltoClientHttp();
        altoClientHttpIns.setHttpPostRequest(httpPost);

        try
        {
            long length=altoClientHttpIns.GetHttpPostResponseString(httpResponse);

        } catch (Exception e)
        {
            e.printStackTrace();
            httpResponse.clear();
        }
    }

    private boolean checkResponseHeader()
    {
        int size=httpResponse.size();
        //System.out.println(size);
        if(size<3)
            return false;
        if(httpResponse.get(0).toString().intern()!=AltoClientConst.AltoClientRightString.HttpGetRightStatus.intern())
            return false;
        if(httpResponse.get(1).toString().intern()!=AltoClientConst.AltoClientResponseType.EndPointCostResponseType.intern())
            return false;
        return true;
    }

    public void getInfo(AltoClientEndPointCostSwapInfo endPointCostInfo)
    {
        if(checkResponseHeader())//begin to parse some data from response for IRD
        {
            try{
                JSONArray jsonMembers = null;
                int size,loop;
                Iterator<String> iters;

                CostType costType = new CostType(AltoClientConst.AltoClientConstString.Invalid,"","","");
                List<EndPointCost> endPointCostList=new ArrayList<EndPointCost>();

                JSONObject jsonobj = new JSONObject(httpResponse.get(2));
                JSONObject metaObj=(JSONObject) (jsonobj.get("meta"));
                JSONObject endPointCostObj=(JSONObject) (jsonobj.get("endpoint-cost-map"));

                //Cost Type
                if(metaObj.has("cost-type"))
                {
                    costType.setName("cost-type");
                    JSONObject costTypeObj=(JSONObject) (metaObj.get("cost-type"));
                    if(costTypeObj.has("cost-mode"))
                    {
                        costType.setMode(costTypeObj.getString("cost-mode"));
                    }
                    if(costTypeObj.has("cost-metric"))
                    {
                        costType.setMetric(costTypeObj.getString("cost-metric"));
                    }
                }

                //end point cost map
                iters = endPointCostObj.keys();
                while(iters.hasNext())
                {
                    String srcIterKey= iters.next();
                    JSONObject oneEndPointCostObj=(JSONObject) (endPointCostObj.get(srcIterKey));
                    Iterator<String> itersEndPointCost=oneEndPointCostObj.keys();
                    while(itersEndPointCost.hasNext())
                    {
                        String dstKey= itersEndPointCost.next();
                        EndPointCost endPointCostNode=new EndPointCost();
                        endPointCostNode.setSrcEP(srcIterKey);
                        endPointCostNode.setDstEP(dstKey);
                        endPointCostNode.setCost(oneEndPointCostObj.getDouble(dstKey));
                        endPointCostList.add(endPointCostNode);
                    }
                }
                endPointCostInfo.setCostType(costType);
                endPointCostInfo.setEndPointCostList(endPointCostList);
                endPointCostInfo.setURI(uri);
                endPointCostInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationSuccess);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                endPointCostInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
            }
        }
        else{
            endPointCostInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
        }
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
        int sourceCnt=Integer.parseInt(args[5]);
        int dstCnt=args.length-sourceCnt - 6;

        List<String> srcEPs=new ArrayList<String>();
        List<String> dstEPs=new ArrayList<String>();
        while(dstCnt > 0)
        {
            dstEPs.add(args[5+sourceCnt+dstCnt]);
            dstCnt=dstCnt-1;
        }
        while(sourceCnt > 0)
        {
            srcEPs.add(args[5+sourceCnt]);
            sourceCnt=sourceCnt-1;
        }

        System.out.println("srcEPs "+srcEPs.toString());
        System.out.println("dstEPs "+dstEPs.toString());
        CostType postCostType=new CostType();
        postCostType.setName(args[2]);
        postCostType.setMode(args[3]);
        postCostType.setMetric(args[4]);

        AltoClientEndPointCostSwapInfo endPointCostInfo=new AltoClientEndPointCostSwapInfo();
        AltoClientEndPointCost altoClientEndPointCost=null;
        altoClientEndPointCost=new AltoClientEndPointCost(args[0],args[1],postCostType,srcEPs,dstEPs);
        altoClientEndPointCost.getInfo(endPointCostInfo);

        String operationStatus;
        List<EndPointCost> endPointCostList;
        CostType costType=endPointCostInfo.getCostType();
        endPointCostList=endPointCostInfo.getEndPointCostList();
        operationStatus=endPointCostInfo.getOperationStatus();
        for(EndPointCost loop:endPointCostList)
        {
            System.out.println("{\"src\":"+"\""+loop.getSrcEP()+"\"" +",\"dst:\""+loop.getDstEP()+" \",\"cost\":"+loop.getCost()+"}");
        }

        System.out.println("done");

    }
    /**
     * @param args
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static void mainbank(String[] args) throws JSONException, UnsupportedEncodingException
    {
        CostType postCostType=new CostType();
        List<String> srcEPs;
        List<String> dstEPs;


        CostType costType;
        List<EndPointCost> endPointCostList;
        String operationStatus;

        System.out.println("begin");

        AltoClientEndPointCostSwapInfo endPointCostInfo=new AltoClientEndPointCostSwapInfo();
        AltoClientEndPointCost altoClientEndPointCost=null;

        postCostType.setName("rtg-num ");
        postCostType.setMode("numerical");
        postCostType.setMetric("routingcost");

        srcEPs=new ArrayList<String>();
        dstEPs=new ArrayList<String>();

        srcEPs.add("ipv4:15.0.64.0");
        srcEPs.add("ipv4:169.254.64.0");
        srcEPs.add("ipv4:127.0.64.0");
        srcEPs.add("ipv4:100.128.0.0");
        srcEPs.add("ipv4:100.32.0.0");
        srcEPs.add("ipv4:100.0.64.0");
        srcEPs.add("ipv4:100.64.64.0");
        srcEPs.add("ipv4:100.128.64.0");
        srcEPs.add("ipv4:128.0.64.0");
        srcEPs.add("ipv4:129.0.64.0");
        srcEPs.add("ipv4:192.168.64.0");
        srcEPs.add("ipv4:132.0.64.0");
        srcEPs.add("ipv4:135.0.64.0");

        dstEPs.add("ipv4:15.0.64.0");
        dstEPs.add("ipv4:169.254.64.0");
        dstEPs.add("ipv4:127.0.64.0");
        dstEPs.add("ipv4:100.128.0.0");
        dstEPs.add("ipv4:100.32.0.0");
        dstEPs.add("ipv4:100.0.64.0");
        dstEPs.add("ipv4:100.64.64.0");
        dstEPs.add("ipv4:100.128.64.0");
        dstEPs.add("ipv4:128.0.64.0");
        dstEPs.add("ipv4:129.0.64.0");
        dstEPs.add("ipv4:192.168.64.0");
        dstEPs.add("ipv4:132.0.64.0");
        dstEPs.add("ipv4:135.0.64.0");



        altoClientEndPointCost=new AltoClientEndPointCost("alto.alcatel-lucent.com:8000","costs/end",postCostType,srcEPs,dstEPs);
        altoClientEndPointCost.getInfo(endPointCostInfo);
        System.out.println("done");

        costType=endPointCostInfo.getCostType();
        endPointCostList=endPointCostInfo.getEndPointCostList();
        operationStatus=endPointCostInfo.getOperationStatus();
        System.out.println("\t costType---------------");
        System.out.println(costType.getMode().toString());
        System.out.println(costType.getMetric().toString());
        System.out.println("\n endpoint cost map list---------------");
        for(EndPointCost loop:endPointCostList)
        {
            System.out.println(loop.toStringLinked());
        }
        System.out.println("done");
    }

}
