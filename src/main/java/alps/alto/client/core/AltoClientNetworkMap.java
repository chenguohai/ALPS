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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings("unused")
public final class AltoClientNetworkMap
{

    private String hostName;
    private String requestLine;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private List<String> httpResponse;
    private String uri;

    public AltoClientNetworkMap()
    {
        this(AltoClientConst.AltoClientRequestHost.NetworkMapRequestHost,AltoClientConst.AltoClientRequestLine.NetworkMapRequestLine);
    }
    public AltoClientNetworkMap(String hostName,String requestLine)
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
        httpGet.setHeader("Accept", AltoClientConst.AltoClientAcceptConst.NetworkMapAccept);

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
    public AltoClientNetworkMap(String hostName,String requestLine,List<String> pids,List<String> address_types)
    {
        if(hostName==null)
            hostName=AltoClientConst.AltoClientRequestHost.NetworkMapRequestHost;
        if(requestLine==null)
            requestLine=AltoClientConst.AltoClientRequestLine.NetworkMapRequestLine;
        this.hostName = hostName;
        this.requestLine = requestLine;
        //this.requestLine = requestLine+"/filtered";
        this.httpResponse=new ArrayList<String>();

        String httpPostPara=this.hostName;
        httpPostPara= "http://"+httpPostPara +"/"+this.requestLine;
        uri=httpPostPara;
        httpPost=new HttpPost(httpPostPara);


        JSONObject obj = new JSONObject();
        try
        {
            obj.put("pids", pids);
            if(address_types!=null){
                obj.put("address-types", address_types);
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
        sEntity.setContentType(AltoClientConst.AltoClientFilteredContentTypeString.NetworkMap);
        httpPost.setEntity(sEntity);
        httpPost.setHeader("Accept", AltoClientConst.AltoClientAcceptConst.NetworkMapAccept);/**/

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
        if(httpResponse.get(1).toString().intern()!=AltoClientConst.AltoClientResponseType.NetworkMapResponseType.intern())
            return false;
        return true;
    }

    public void getInfo(AltoClientNetworkMapSwapInfo networkMapInfo)
    {
        if(checkResponseHeader())//begin to parse some data from response for IRD
        {
            try{
                JSONArray jsonMembers = null;
                int size,loop;
                Iterator<String> iters;

                VTag vTag=new VTag(AltoClientConst.AltoClientConstString.InvalidResourceID,AltoClientConst.AltoClientConstString.InvalidTAG);
                List<NetworkMap> networkMapList=new ArrayList<NetworkMap>();
                //System.out.println(httpResponse.get(2));
                JSONObject jsonobj = new JSONObject(httpResponse.get(2));
                JSONObject metaObj=(JSONObject) (jsonobj.get("meta"));
                JSONObject networkMapObj=(JSONObject) (jsonobj.get("network-map"));

                //v tag parse
                JSONObject vTagObj=(JSONObject) (metaObj.get("vtag"));
                if(vTagObj.has("resource-id"))
                {
                    vTag.setResourceID(vTagObj.getString("resource-id"));
                }
                if(vTagObj.has("tag"))
                {
                    vTag.setTag(vTagObj.getString("tag"));
                }

                //network map parse
                iters = networkMapObj.keys();
                while(iters.hasNext())
                {
                    String pidID= iters.next();
                    List<String> iPV4List=new ArrayList<String>();
                    List<String> iPV6List=new ArrayList<String>();

                    JSONObject obj= networkMapObj.getJSONObject(pidID);

                    if(obj.has("ipv4")){
                        jsonMembers=obj.getJSONArray("ipv4");
                        size=jsonMembers.length();
                        for(loop=0;loop<size;loop++)
                        {
                            String tmp=jsonMembers.getString(loop);
                            iPV4List.add(tmp);
                        }
                    }

                    if(obj.has("ipv6")){
                        jsonMembers=obj.getJSONArray("ipv6");
                        size=jsonMembers.length();
                        for(loop=0;loop<size;loop++)
                        {
                            String tmp=jsonMembers.getString(loop);
                            iPV6List.add(tmp);
                        }
                    }

                    networkMapList.add(new NetworkMap(pidID,iPV4List,iPV6List));
                }

                networkMapInfo.setVTag(vTag);
                networkMapInfo.setNetworkMapList(networkMapList);
                networkMapInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationSuccess);
                networkMapInfo.setURI(uri);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                networkMapInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
            }
        }
        else{
            networkMapInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
        }
    }

    /**
     * @param args
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static void mainbank(String[] args) throws JSONException, UnsupportedEncodingException
    {
        System.out.println("begin to start test");
        for(String loop:args)
        {
            System.out.println(loop);
        }
        VTag vTag;
        List<NetworkMap> networkMapList;
        String operationStatus;
        AltoClientNetworkMapSwapInfo networkMapInfo=new AltoClientNetworkMapSwapInfo();
        AltoClientNetworkMap altoClientNetworkMap=null;

        String strTmp="false";

        if(args[0].intern()==strTmp.intern())
        {
            altoClientNetworkMap=new AltoClientNetworkMap(args[1],args[2]);
            altoClientNetworkMap.getInfo(networkMapInfo);
        }
        else
        {
            List<String> pids=new ArrayList<String>();
            List<String> address_types=null;
            int size=args.length-4;
            while(size>0)
            {
                pids.add(args[size+4-1]);
                size=size-1;
            }
            String address_type_input=args[3];
            if(address_type_input.contains("O"))
            {
                address_types=null;
            }
            else if(address_type_input.contains("E"))
            {
                address_types=new ArrayList<String>();
            }
            else if(address_type_input.contains("6"))
            {
                address_types=new ArrayList<String>();
                address_types.add("ipv6");
            }
            altoClientNetworkMap=new AltoClientNetworkMap(args[1],args[2],pids,address_types);
            altoClientNetworkMap.getInfo(networkMapInfo);

        }
        System.out.println(networkMapInfo.toString());

        vTag=networkMapInfo.getVTag();
        networkMapList=networkMapInfo.getNetworkMapList();
        operationStatus=networkMapInfo.getOperationStatus();
        System.out.println("\nvTAG---------------");
        System.out.println(vTag.toString());
        System.out.println("\nnetworkMapList---------------");
        for(NetworkMap loop:networkMapList)
        {
            System.out.println(loop.toStringLinked());
        }
        System.out.println("done");
    }

    /**
     * @param args
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws JSONException, UnsupportedEncodingException
    {
        boolean bFiltered=true;
        VTag vTag;
        List<NetworkMap> networkMapList;
        String operationStatus;
        AltoClientNetworkMapSwapInfo networkMapInfo=new AltoClientNetworkMapSwapInfo();
        AltoClientNetworkMap altoClientNetworkMap=null;
        if(bFiltered == false)
        {
            System.out.println("begin");
            altoClientNetworkMap=new AltoClientNetworkMap("alto.benocs.berlin:8000","default/networkmap");
            altoClientNetworkMap.getInfo(networkMapInfo);
            System.out.println("done");
        }
        else
        {
            System.out.println("begin");
            List<String> pids=new ArrayList<String>();
            pids.add("peer1");
            //pids.add("peeringpid2");
           // pids.add("mypid2");
            System.out.println(pids);
            List<String> address_type=new ArrayList<String>();address_type.add("ipv4");
            altoClientNetworkMap=new AltoClientNetworkMap("alto.benocs.berlin:8000","default/networkmap/filtered",pids,null);
            //altoClientNetworkMap=new AltoClientNetworkMap("alto.benocs.berlin:8000","default/networkmap/filtered",pids,address_type);
            altoClientNetworkMap.getInfo(networkMapInfo);
            System.out.println("done");
        }
        System.out.println(networkMapInfo.toString());

        vTag=networkMapInfo.getVTag();
        networkMapList=networkMapInfo.getNetworkMapList();
        operationStatus=networkMapInfo.getOperationStatus();
        System.out.println("\nvTAG---------------");
        System.out.println(vTag.toString());
        System.out.println("\nnetworkMapList---------------");
        for(NetworkMap loop:networkMapList)
        {
            System.out.println(loop.toStringLinked());
        }
        System.out.println("\ndone");
    }
}
