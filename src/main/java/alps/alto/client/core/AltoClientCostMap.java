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
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings("unused")
public final class AltoClientCostMap
{

    private String hostName;
    private String requestLine;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private List<String> httpResponse;
    private String uri;

    public AltoClientCostMap()
    {
        this(AltoClientConst.AltoClientRequestHost.NetworkMapRequestHost,AltoClientConst.AltoClientRequestLine.NetworkMapRequestLine);
    }
    public AltoClientCostMap(String hostName,String requestLine)
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
        httpGet.setHeader("Accept", AltoClientConst.AltoClientAcceptConst.CostMapAccept);

        AltoClientHttp altoClientHttpIns= new AltoClientHttp();
        altoClientHttpIns.setHttpGetRequest(httpGet);

        try
        {
            long length=altoClientHttpIns.GetHttpGetResponseString(httpResponse);

        } catch (Exception e)
        {
            e.printStackTrace();
            httpResponse.clear();
        }
    }

    public AltoClientCostMap(String hostName,String requestLine,CostType costType,List<String> srcPids,List<String> dstPids,List<String> constraints)
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
        if(httpResponse.get(1).toString().intern()!=AltoClientConst.AltoClientResponseType.CostMapResponseType.intern())
            return false;
        return true;
    }

    public void getInfo(AltoClientCostMapSwapInfo costMapInfo)
    {
        if(checkResponseHeader())//begin to parse some data from response for IRD
        {
            try{
                JSONArray jsonMembers = null;
                int size,loop;
                Iterator<String> iters;

                List<VTag> vTagList=new ArrayList<VTag>();
                CostType costType=new CostType();
                List<CostMap> costMapList=new ArrayList<CostMap>();

                //System.out.println(httpResponse.get(2));
                JSONObject jsonobj = new JSONObject(httpResponse.get(2));
                JSONObject metaObj=(JSONObject) (jsonobj.get("meta"));
                JSONObject costMapListObj=(JSONObject) (jsonobj.get("cost-map"));

                //v tag array parse
                JSONArray vTagObjArray=metaObj.getJSONArray("dependent-vtags");
                size=vTagObjArray.length();
                for(loop=0;loop<size;loop++)
                {
                    JSONObject vTagObj=vTagObjArray.getJSONObject(loop);
                    VTag vTag=new VTag(AltoClientConst.AltoClientConstString.InvalidResourceID,AltoClientConst.AltoClientConstString.InvalidTAG);
                    if(vTagObj.has("resource-id"))
                    {
                        vTag.setResourceID(vTagObj.getString("resource-id"));
                    }
                    if(vTagObj.has("tag"))
                    {
                        vTag.setTag(vTagObj.getString("tag"));
                    }
                    vTagList.add(vTag);
                }


                JSONObject costTypeObj=(JSONObject) (metaObj.get("cost-type"));
                costType.setName("cost-type");
                if(costTypeObj.has("cost-mode"))
                {
                    costType.setMode(costTypeObj.getString("cost-mode"));
                }
                if(costTypeObj.has("cost-metric"))
                {
                    costType.setMetric(costTypeObj.getString("cost-metric"));
                }


                //cost map parse
                iters = costMapListObj.keys();
                while(iters.hasNext())
                {
                    String srcPID= iters.next();
                    JSONObject sameSrcPIDCostList=costMapListObj.getJSONObject(srcPID);
                    Iterator<String> itersDstPID=sameSrcPIDCostList.keys();
                    //System.out.println(sameSrcPIDCostList.toString());
                    while(itersDstPID.hasNext())
                    {
                        String dstPID= itersDstPID.next();
                        CostMap costMap=new CostMap();
                        costMap.setSrcPID(srcPID);
                        costMap.setDstPID(dstPID);
                        costMap.setCost(sameSrcPIDCostList.getDouble(dstPID));

                        costMapList.add(costMap);
                    }
                }

                costMapInfo.setVTagList(vTagList);
                costMapInfo.setCostType(costType);
                costMapInfo.setCostMapList(costMapList);
                costMapInfo.setURI(uri);
                costMapInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationSuccess);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                costMapInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
            }
        }
        else{
            costMapInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
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

        List<VTag> vTagList;
        CostType costType;
        List<CostMap> costMapList;
        String operationStatus;
        AltoClientCostMapSwapInfo costMapInfo=new AltoClientCostMapSwapInfo();
        AltoClientCostMap altoClientCostMap;
        CostType postCostType;

        String strTmp="false";

        if(args[0].intern()==strTmp.intern())
        {
            altoClientCostMap=new AltoClientCostMap(args[1],args[2]);
            altoClientCostMap.getInfo(costMapInfo);
        }
        else
        {
            postCostType=new CostType();
            postCostType.setName(args[4]);
            postCostType.setMode(args[5]);
            postCostType.setMetric(args[6]);

            List<String> srcPids;
            List<String> dstPids;
            List<String> constraints;
            srcPids=new ArrayList<String>();
            dstPids=new ArrayList<String>();
            constraints=null;

            if(args[3].contains("E2")){

            }
            else if(args[3].contains("ES"))
            {
                dstPids.add(args[10]);dstPids.add(args[11]);
            }
            else if(args[3].contains("ED"))
            {

                srcPids.add(args[7]);srcPids.add(args[8]);
            }
            else if(args[3].contains("NON2"))
            {
                srcPids.add(args[9]);
                dstPids.add(args[12]);
            }
            else if(args[3].contains("NON1S"))
            {

                srcPids.add(args[7]);srcPids.add(args[8]);srcPids.add(args[9]);
                dstPids.add(args[10]);dstPids.add(args[11]);;
            }
            else if(args[3].contains("NON1D"))
            {
                srcPids.add(args[7]);srcPids.add(args[8]);
                dstPids.add(args[10]);dstPids.add(args[11]);dstPids.add(args[12]);
            }
            else if(args[3].contains("NONSD"))
            {
                srcPids.add(args[7]);srcPids.add(args[8]);srcPids.add(args[9]);
                dstPids.add(args[10]);dstPids.add(args[11]);dstPids.add(args[12]);
            }
            else if(args[3].contains("CONSTRAINTS"))
            {

                constraints=new ArrayList<String>();
                constraints.add("ge 20");
                constraints.add("le 30");
            }

            altoClientCostMap=new AltoClientCostMap(args[1],args[2],postCostType,srcPids,dstPids,constraints);
            altoClientCostMap.getInfo(costMapInfo);
        }

        System.out.println(costMapInfo.toString());
        vTagList=costMapInfo.getVTagList();
        costType=costMapInfo.getCostType();
        costMapList=costMapInfo.getCostMapList();
        operationStatus=costMapInfo.getOperationStatus();
        for(VTag loop:vTagList)
        {
            System.out.println(loop.toString());
        }

        System.out.println("\ncostType---------------");
        System.out.println(costType.toString());

        System.out.println("\nCostMapList---------------");
        for(CostMap loop:costMapList)
        {
            System.out.println(loop.toStringLinked());
        }
        System.out.println("\ndone");
    }

    /**
     * @param args
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static void mainbank(String[] args) throws JSONException, UnsupportedEncodingException
    {
        boolean bFiltered=false;
        List<VTag> vTagList;
        CostType costType;
        List<CostMap> costMapList;
        String operationStatus;
        AltoClientCostMapSwapInfo costMapInfo=new AltoClientCostMapSwapInfo();
        AltoClientCostMap altoClientCostMap;
        CostType postCostType;
        if(bFiltered == false)
        {
            System.out.println("begin");
            altoClientCostMap=new AltoClientCostMap("alto.alcatel-lucent.com:8000","def-network/costs/full/rtg/num");
            altoClientCostMap.getInfo(costMapInfo);
            System.out.println("done");
        }
        else
        {
            System.out.println("begin");
            postCostType=new CostType();

            List<String> srcPids;
            List<String> dstPids;
            srcPids=new ArrayList<String>();
            dstPids=new ArrayList<String>();
            srcPids.add("mypid1");
            srcPids.add("mypid2");
            dstPids.add("peeringpid2");
            dstPids.add("mypid2");

            postCostType.setName("rtg-num ");
            postCostType.setMode("numerical");
            postCostType.setMetric("routingcost");

            altoClientCostMap=new AltoClientCostMap("alto.alcatel-lucent.com:8000","costs/filtered",postCostType,srcPids,dstPids,null);
            altoClientCostMap.getInfo(costMapInfo);
            System.out.println("done");
        }
        System.out.println(costMapInfo.toString());
        vTagList=costMapInfo.getVTagList();
        costType=costMapInfo.getCostType();
        costMapList=costMapInfo.getCostMapList();
        operationStatus=costMapInfo.getOperationStatus();
        for(VTag loop:vTagList)
        {
            System.out.println(loop.toString());
        }

        System.out.println("\ncostType---------------");
        System.out.println(costType.toString());

        System.out.println("\nCostMapList---------------");
        for(CostMap loop:costMapList)
        {
            System.out.println(loop.toStringLinked());
        }
        System.out.println("\ndone");
    }

}
