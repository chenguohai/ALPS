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

public final class AltoClientCostMapSwapInfo
{

    private List<VTag> vTagList;
    private CostType costType;
    private List<CostMap> costMapList;
    private String uri;
    private String operationStatus;

    public AltoClientCostMapSwapInfo()
    {
        vTagList=new ArrayList<VTag>();//new VTag(AltoClientRightString.InvalidTAG,"");
        costType=new CostType();
        costMapList=new ArrayList<CostMap>();
        uri=AltoClientConst.AltoClientRightString.InvalidURI;
        operationStatus=AltoClientConst.AltoClientRightString.OperationFailure;

    }
    public AltoClientCostMapSwapInfo(List<VTag> vTagList,CostType costType,List<CostMap> costMapList)
    {
        this.vTagList=vTagList;
        this.costType=costType;
        this.costMapList=costMapList;
        uri=AltoClientConst.AltoClientRightString.InvalidURI;
        operationStatus=AltoClientConst.AltoClientRightString.OperationFailure;

    }

    public void setVTagList(List<VTag> vTagList)
    {
        this.vTagList=vTagList;
    }
    public List<VTag> getVTagList()
    {
        return vTagList;
    }

    public void setCostType(CostType costType)
    {
        this.costType=costType;
    }
    public CostType getCostType()
    {
        return costType;
    }

    public void setCostMapList(List<CostMap> costMapList)
    {
        this.costMapList=costMapList;
    }
    public List<CostMap> getCostMapList()
    {
        return costMapList;
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
    public String toString()
    {
        JSONObject json=new JSONObject();
        JSONArray vTagListJsonMembers = new JSONArray();
        for(VTag loop:vTagList)
        {
            vTagListJsonMembers.put(loop);
        }
        JSONArray costMapListJsonMembers = new JSONArray();
        for(CostMap loop:costMapList)
        {
            costMapListJsonMembers.put(loop);
        }
        try
        {
            json.put("operationStatus", this.getOperationStatus());
            json.put("vTagList", vTagListJsonMembers);
            json.put("costType", this.getCostType());
            json.put("costMapList", costMapListJsonMembers);
            json.put("uri", this.getURI());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String ret=json.toString();
        return ret;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AltoClientCostMapSwapInfo ret=(AltoClientCostMapSwapInfo)super.clone();

        List<VTag> vTagListOrigin=this.getVTagList();
        List<VTag> vTagListtCopy=new ArrayList<VTag>();
        for(VTag loop:vTagListOrigin)
        {
            VTag vTag=(VTag) loop.clone();
            vTagListtCopy.add(vTag);
        }

        CostType costType=(CostType)(this.getCostType().clone());

        List<CostMap> costMapListOrigin=this.getCostMapList();
        List<CostMap> costMapListCopy=new ArrayList<CostMap>();
        for(CostMap loop:costMapListOrigin)
        {
            CostMap costMap= (CostMap)(loop.clone());
            costMapListCopy.add(costMap);
        }

        ret.setVTagList(vTagListtCopy);
        ret.setCostType(costType);
        ret.setCostMapList(costMapListCopy);

        return ret;
    }
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // just used for test
    }

}
