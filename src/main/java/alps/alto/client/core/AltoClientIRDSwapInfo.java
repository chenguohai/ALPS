package alps.alto.client.core;


/**
 *
 */

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class AltoClientIRDSwapInfo implements Cloneable
{
    private List<CostType> costTypeList;
    private String defaultAltoNetworkMap;
    private List<Resource> resourceList;
    private String uri;
    private String operationStatus;

    public AltoClientIRDSwapInfo()
    {
        costTypeList=null;
        defaultAltoNetworkMap="";
        resourceList=null;
        uri=AltoClientConst.AltoClientRightString.InvalidURI;
        operationStatus=AltoClientConst.AltoClientRightString.OperationFailure;;
    }
    public AltoClientIRDSwapInfo(List<CostType> costTypeList,String defaultAltoNetworkMap,List<Resource> resourceList,String operationStatus)
    {
        this.costTypeList=costTypeList;
        this.defaultAltoNetworkMap=defaultAltoNetworkMap;
        this.resourceList=resourceList;
        uri=AltoClientConst.AltoClientRightString.InvalidURI;
        this.operationStatus=operationStatus;
    }

    public void setCostTypeList(List<CostType> costTypeList)
    {
        this.costTypeList=costTypeList;
    }
    public List<CostType>  getCostTypeList()
    {
        return costTypeList;
    }

    public void setDefaultAltoNetworkMap(String defaultAltoNetworkMap)
    {
        this.defaultAltoNetworkMap=defaultAltoNetworkMap;
    }
    public String  getDefaultAltoNetworkMap()
    {
        return defaultAltoNetworkMap;
    }

    public void setResourceList(List<Resource> resourceList)
    {
        this.resourceList=resourceList;
    }
    public List<Resource> getResourceList()
    {
        return resourceList;
    }

    public void addCostTypeNode(CostType node)
    {
        costTypeList.add(node);
    }

    public void addCostResourceNode(Resource node)
    {
        resourceList.add(node);
    }

    public void setURI(String uri)
    {
        this.uri=uri;
    }

    public String getURI()
    {
        return uri;
    }

    public void setOperationStatus(String operationStatus)
    {
        this.operationStatus=operationStatus;
    }

    public String getOperationStatus()
    {
        return operationStatus;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        AltoClientIRDSwapInfo ret=(AltoClientIRDSwapInfo) super.clone();

        List<CostType> costTypeListOrign=this.getCostTypeList();
        List<CostType> costTypeListCopy=new ArrayList<CostType>();
        for(CostType loop:costTypeListOrign)
        {
            CostType tmp=loop;
            costTypeListCopy.add(tmp);
        }

        List<Resource> resourceListOrigin=this.getResourceList();
        List<Resource> resourceListCopy=new ArrayList<Resource>();
        for(Resource loop:resourceListOrigin)
        {
            Resource tmp=loop;
            resourceListCopy.add(tmp);
        }

        ret.setCostTypeList(costTypeListCopy);
        ret.setResourceList(resourceListCopy);

        return ret;
    }

    @Override
    public String toString()
    {
        JSONObject json=new JSONObject();
        JSONArray costTypeListJsonMembers = new JSONArray();
        for(CostType loop:costTypeList)
        {
            costTypeListJsonMembers.put(loop);
        }
        JSONArray resoucesListJsonMembers = new JSONArray();
        for(Resource loop:resourceList)
        {
            resoucesListJsonMembers.put(loop);
        }
        try
        {
            json.put("operationStatus", this.getOperationStatus());
            json.put("uri", this.getURI());
            json.put("defaultAltoNetworkMap", this.getDefaultAltoNetworkMap());
            json.put("costTypeList", costTypeListJsonMembers);
            json.put("resourceList", resoucesListJsonMembers);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String ret=json.toString();
        return ret;
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        //just used to test

    }
}
