package alps.alto.client.core;


/**
 *
 */

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public final class AltoClientIRD
{

    private String hostName;
    private String requestLine;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private List<String> httpResponse;
    private String uri;


    public AltoClientIRD()
    {
        this(AltoClientConst.AltoClientRequestHost.IRDRequestHost,AltoClientConst.AltoClientRequestLine.IRDRequestLine);
    }
    public AltoClientIRD(String hostName,String requestLine)
    {

        if(hostName==null)
            hostName=AltoClientConst.AltoClientRequestHost.IRDRequestHost;
        if(requestLine==null)
            requestLine=AltoClientConst.AltoClientRequestLine.IRDRequestLine;
        this.hostName = hostName;
        this.requestLine = requestLine;
        this.httpResponse=new ArrayList<String>();

        String httpGetPara=this.hostName;
        httpGetPara= "http://"+httpGetPara +'/' +this.requestLine;
        uri=httpGetPara;
        httpGet=new HttpGet(httpGetPara);
        httpGet.setHeader("Accept", AltoClientConst.AltoClientAcceptConst.IRDAccept);

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

    private boolean checkResponseHeader()
    {
        int size=httpResponse.size();
        if(size<3)
            return false;
        if(httpResponse.get(0).toString().intern()!=AltoClientConst.AltoClientRightString.HttpGetRightStatus.intern())
            return false;
        if(httpResponse.get(1).toString().intern()!=AltoClientConst.AltoClientResponseType.IRDResponseType.intern())
            return false;
        return true;
    }

    public void getInfo(AltoClientIRDSwapInfo IRDInfo)
    {
        if(checkResponseHeader())//begin to parse some data from response for IRD
        {
            try
            {
                JSONArray jsonMembers = null;
                int loop,size;
                Iterator<String> iters;

                List<CostType> costTypeList=new ArrayList<CostType>();
                String defaultAltoNetworkMap="";
                List<Resource> resourceList=new ArrayList<Resource>();

                JSONObject jsonobj = new JSONObject(httpResponse.get(2));
                JSONObject metaObj=(JSONObject) (jsonobj.get("meta"));
                JSONObject costTypeObj=(JSONObject) (metaObj.get("cost-types"));
                JSONObject resourceObj=(JSONObject) (jsonobj.get("resources"));

                //cost type parse
                iters = costTypeObj.keys();
                costTypeList.clear();
                while(iters.hasNext())
                {
                    String key= iters.next();
                    JSONObject obj= costTypeObj.getJSONObject(key);

                    CostType costTypeTmp= new CostType(key,AltoClientConst.AltoClientConstString.Invalid,AltoClientConst.AltoClientConstString.Invalid,"");
                    if(obj.has("cost-mode"))
                    {
                        costTypeTmp.setMode(obj.getString("cost-mode"));
                    }
                    if(obj.has("cost-metric"))
                    {
                        costTypeTmp.setMetric(obj.getString("cost-metric"));
                    }
                    if(obj.has("description"))
                    {
                        costTypeTmp.setDescription(obj.getString("description"));
                    }
                    costTypeList.add(costTypeTmp);
                }

                defaultAltoNetworkMap=metaObj.getString("default-alto-network-map");

                //resource parse
                iters = resourceObj.keys();
                resourceList.clear();
                while(iters.hasNext())
                {

                    List<String> usesList=new ArrayList<String>();

                    String key= iters.next();
                    JSONObject oneResourceObj= resourceObj.getJSONObject(key);

                    Resource resource= new Resource();

                    resource.setResourceName(key);
                    if(oneResourceObj.has("uri"))
                    {
                        resource.setUri(oneResourceObj.getString("uri"));
                    }
                    if(oneResourceObj.has("media-type"))
                    {
                        resource.setMediaType(oneResourceObj.getString("media-type"));
                    }

                    Capabilities capabilities=new Capabilities();
                    if(oneResourceObj.has("capabilities"))
                    {
                        JSONObject capabilitiesObj= oneResourceObj.getJSONObject("capabilities");


                        Boolean costConstraints=new Boolean("false");
                        List<String> costTypeNamesList=new ArrayList<String>();
                        List<String> propTypesList=new ArrayList<String>();

                        if(capabilitiesObj.has("cost-type-names"))
                        {
                            jsonMembers=capabilitiesObj.getJSONArray("cost-type-names");
                            size=jsonMembers.length();
                            for(loop=0;loop<size;loop++)
                            {
                                costTypeNamesList.add(jsonMembers.getString(loop));
                            }
                        }

                        if(capabilitiesObj.has("prop-types"))
                        {
                            jsonMembers=capabilitiesObj.getJSONArray("prop-types");
                            size=jsonMembers.length();
                            for(loop=0;loop<size;loop++)
                            {
                                propTypesList.add(jsonMembers.getString(loop));
                            }
                        }


                        if(capabilitiesObj.has("cost-constraints"))
                        {
                            boolean tmp=capabilitiesObj.getBoolean("cost-constraints");
                            costConstraints=new Boolean(tmp);
                        }

                        capabilities.setCostConstraints(costConstraints);
                        capabilities.setCostTypeNamesList(costTypeNamesList);
                        capabilities.setPropTypesList(propTypesList);

                        resource.setCapabilities(capabilities);
                    }
                    else
                    {
                        resource.setCapabilities(null);
                    }

                    if(oneResourceObj.has("accepts"))
                    {
                        resource.setAccepts(oneResourceObj.getString("accepts"));
                    }

                    if(oneResourceObj.has("uses"))
                    {
                        jsonMembers=oneResourceObj.getJSONArray("uses");
                        size=jsonMembers.length();
                        for(loop=0;loop<size;loop++)
                        {
                            usesList.add(jsonMembers.getString(loop));
                        }

                    }
                    resource.setUsesList(usesList);
                    resourceList.add(resource);
                }

                IRDInfo.setCostTypeList(costTypeList);
                IRDInfo.setDefaultAltoNetworkMap(defaultAltoNetworkMap);
                IRDInfo.setResourceList(resourceList);
                IRDInfo.setURI(uri);
                IRDInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationSuccess);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                IRDInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
            }
        }
        else{
            IRDInfo.setOperationStatus(AltoClientConst.AltoClientRightString.OperationFailure);
        }
    }




    /**
     * @param args
     * @throws JSONException
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args)
    {
        //http://alto.alcatel-lucent.com:8000/directory
        System.out.println("begin");
        //AltoClientIRD altoClientIRD=new AltoClientIRD("baidu.com","");
        //AltoClientIRD altoClientIRD=new AltoClientIRD("baidu.com","directory");
        AltoClientIRD altoClientIRD=new AltoClientIRD("alto.alcatel-lucent.com:8000","directory");
        AltoClientIRDSwapInfo IRDInfo=new AltoClientIRDSwapInfo();
        altoClientIRD.getInfo(IRDInfo);
        System.out.println("done");

        //print data
        List<CostType> costTypeList=IRDInfo.getCostTypeList();
        String defaultAltoNetworkMap=IRDInfo.getDefaultAltoNetworkMap();
        List<Resource> resourceList=IRDInfo.getResourceList();
        String operationStatus=IRDInfo.getOperationStatus(); //success failure,String operationStatus

        System.out.println(costTypeList.toString());
        System.out.println(resourceList.toString());
        System.out.println(IRDInfo.toString());


        System.out.println("operationStatus---------------");
        System.out.println(operationStatus.toString());
        System.out.println("cost type---------------");
        for(CostType loop:costTypeList)
        {
            System.out.println(loop.toString());
        }
        System.out.println("=====================");
        System.out.println("defaultAltoNetworkMap---------------");
        System.out.println(defaultAltoNetworkMap.toString());

        System.out.println("resourceList---------------");
        for(Resource loop:resourceList)
        {
            System.out.println(loop.toStringLinked());
        }
        System.out.println("IRDInfo---------------");

    }
}
