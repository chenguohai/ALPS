package alps.alto.client.core;

/**
 *
 */


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */
@SuppressWarnings("unused")
public final class AltoClientEndPointProp
{
    private String hostName;
    private String requestLine;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private List<String> httpResponse;
    String uri;

    public AltoClientEndPointProp(String hostName,String requestLine,List<String> properties,List<String> endpoints)
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
            obj.put("properties", properties);
            obj.put("endpoints", endpoints);
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
        httpPost.setHeader("Accept", AltoClientConst.AltoClientAcceptConst.EndPointPropAccept);


        AltoClientHttp altoClientHttpIns= new AltoClientHttp();
        altoClientHttpIns.setHttpPostRequest(httpPost);

        try
        {
            long length=altoClientHttpIns.GetHttpPostResponseString(httpResponse);
            if(length ==-1)
                throw(new Exception("http error!"));

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
        if(httpResponse.get(1).toString().intern()!=AltoClientConst.AltoClientResponseType.EndPointPropResponseType.intern())
            return false;
        return true;
    }

    public void getInfo(AltoClientEPPropSwapInfo EPPInfo)
    {
        if(checkResponseHeader())//begin to parse some data from response for IRD
        {
            try{
                JSONArray jsonMembers = null;
                int size,loop;
                Iterator<String> iters;

                List<VTag> vTagList=new ArrayList<VTag>();
                List<EPProp> ePPropList=new ArrayList<EPProp>();

                //System.out.println(httpResponse.get(2));
                JSONObject jsonobj = new JSONObject(httpResponse.get(2));
                JSONObject metaObj=(JSONObject) (jsonobj.get("meta"));
                JSONObject ePPObj=(JSONObject) (jsonobj.get("endpoint-properties"));

                //v tag parse
                if(metaObj.has("dependent-vtags")){
                    JSONArray vTagObjArray=metaObj.getJSONArray("dependent-vtags");
                    size=vTagObjArray.length();
                    for(loop=0;loop<size;loop++)
                    {
                        JSONObject obj = vTagObjArray.getJSONObject(loop);
                        VTag vTag=new VTag(AltoClientConst.AltoClientRightString.InvalidSourceID,AltoClientConst.AltoClientRightString.InvalidTAG);

                        if(obj.has("resource-id"))
                        {
                            vTag.setResourceID(obj.getString("resource-id"));
                        }

                        if(obj.has("tag"))
                        {
                            vTag.setTag(obj.getString("tag"));
                        }
                        vTagList.add(vTag);
                    }
                }

                //end point properties parse
                iters = ePPObj.keys();
                while(iters.hasNext())
                {
                    String key= iters.next();
                    JSONObject endPointPropetizesObj=ePPObj.getJSONObject(key);
                    String endPointID=key;

                    Iterator<String> itersOneProp =endPointPropetizesObj.keys() ;
                    while(itersOneProp.hasNext())
                    {
                        String properties=itersOneProp.next();
                        String propertiesValue=endPointPropetizesObj.getString(properties);
                        ePPropList.add(new EPProp(endPointID,properties,propertiesValue));
                    }
                }

                EPPInfo.setVTagList(vTagList);
                EPPInfo.setEndPointPropList(ePPropList);
                EPPInfo.setURI(uri);
                EPPInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationSuccess);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                EPPInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
            }
        }
        else{
            EPPInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
        }
    }

    /**
     * @param args
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static void mainb(String[] args)
    {
        List<VTag> vTagList;
        List<EPProp> endPointPropList;
        String operationStatus;

        AltoClientEPPropSwapInfo EPPInfo=new AltoClientEPPropSwapInfo();
        AltoClientEndPointProp altoClientEndPointProp;

        List<String> properties=new ArrayList<String>();
        List<String> endpoints=new ArrayList<String>();
        properties.add(args[2]);
        properties.add(args[3]);
        properties.add(args[4]);
        int size=args.length -5;
        while(size>0)
        {
            endpoints.add(args[size+4]);
            size=size-1;
        }
        //http://alto.alcatel-lucent.com:8000/end-props
        altoClientEndPointProp=new AltoClientEndPointProp("alto.alcatel-lucent.com:8000","end-props",properties,endpoints);
        //altoClientEndPointProp=new AltoClientEndPointProp(args[0],args[1],properties,endpoints);
        altoClientEndPointProp.getInfo(EPPInfo);


        vTagList=EPPInfo.getVTagList();
        endPointPropList=EPPInfo.getEndPointPropList();
        operationStatus=EPPInfo.getOperationStatus();
        System.out.println("operationStatus:"+operationStatus.toString());

        for(VTag loop:vTagList)
        {
            System.out.println("\nvTAG---------------");
            System.out.println(loop.getResourceID().toString());
            System.out.println(loop.getTag().toString());
        }
        for(EPProp loop:endPointPropList)
        {
            System.out.println(loop);
        }
        System.out.println("done");
    }

    /**
     * @param args
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args)
    {
        List<VTag> vTagList;
        List<EPProp> endPointPropList;
        String operationStatus;

        AltoClientEPPropSwapInfo EPPInfo=new AltoClientEPPropSwapInfo();
        AltoClientEndPointProp altoClientEndPointProp;

        List<String> properties=new ArrayList<String>();
        List<String> endpoints=new ArrayList<String>();
        properties.add("alt-network.pid");
        properties.add("def-network.pid");
        endpoints.add("ipv4:100.0.1.25");
        //endpoints.add("ipv4:100.0.0.0/8");
        endpoints.add("ipv4:128.0.1.25");
        endpoints.add("ipv4:132.0.1.25");
        endpoints.add("ipv4:135.0.1.25");

        altoClientEndPointProp=new AltoClientEndPointProp("alto.alcatel-lucent.com:8000","end-props",properties,endpoints);
        altoClientEndPointProp.getInfo(EPPInfo);


        vTagList=EPPInfo.getVTagList();
        endPointPropList=EPPInfo.getEndPointPropList();
        operationStatus=EPPInfo.getOperationStatus();
        System.out.println("done");
        System.out.println("operationStatus:"+operationStatus.toString());

        for(VTag loop:vTagList)
        {
            System.out.println("\nvTAG---------------");
            System.out.println(loop.getResourceID().toString());
            System.out.println(loop.getTag().toString());
        }
        System.out.println(endPointPropList.size());
        for(EPProp loop:endPointPropList)
        {
            System.out.println(loop);
        }
        System.out.println("done");
    }

}
