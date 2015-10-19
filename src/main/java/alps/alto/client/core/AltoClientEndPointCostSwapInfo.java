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

public final class AltoClientEndPointCostSwapInfo {


    private CostType costType;
    private List<EndPointCost> endPointCostList;
    private String uri;
    private String operationStatus;

    public AltoClientEndPointCostSwapInfo()
    {
        costType=new CostType();
        endPointCostList=new ArrayList<EndPointCost>();
        uri=AltoClientConst.AltoClientRightString.InvalidURI;
        operationStatus=AltoClientConst.AltoClientRightString.OperationFailure;

    }
    public AltoClientEndPointCostSwapInfo(CostType costType,List<EndPointCost> endPointCostList,String operationStatus)
    {
        this.costType=costType;
        this.endPointCostList=endPointCostList;
        uri=AltoClientConst.AltoClientRightString.InvalidURI;
        this.operationStatus=operationStatus;
    }

    public void setCostType(CostType costType)
    {
        this.costType=costType;
    }
    public CostType getCostType()
    {
        return costType;
    }

    public void setEndPointCostList(List<EndPointCost> endPointCostList)
    {
        this.endPointCostList=endPointCostList;
    }
    public List<EndPointCost> getEndPointCostList()
    {
        return endPointCostList;
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
        JSONArray endPointCostListJsonMembers = new JSONArray();
        for(EndPointCost loop:endPointCostList)
        {
            endPointCostListJsonMembers.put(loop);
        }
        try
        {
            json.put("operationStatus", this.getOperationStatus());
            json.put("costType", this.getCostType());
            json.put("endPointCostList", endPointCostListJsonMembers);
            json.put("uri", this.getURI());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String ret=json.toString();
        return ret;
    }

    @SuppressWarnings("unused")
    @Override
    public Object clone() throws CloneNotSupportedException {
        AltoClientEndPointCostSwapInfo ret=(AltoClientEndPointCostSwapInfo)super.clone();

        CostType costType=(CostType)(this.getCostType().clone());
        List<EndPointCost> endPointCostListOrigin=this.getEndPointCostList();
        int size=endPointCostListOrigin.size();
        List<EndPointCost> endPointCostList=new ArrayList<EndPointCost>();
        if(endPointCostListOrigin!=null)
        {
            for(EndPointCost loop:endPointCostListOrigin)
            {
                EndPointCost tmp= (EndPointCost)loop.clone();
                endPointCostList.add(tmp);
            }
        }

        ret.setCostType(costType);
        ret.setEndPointCostList(endPointCostList);

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
