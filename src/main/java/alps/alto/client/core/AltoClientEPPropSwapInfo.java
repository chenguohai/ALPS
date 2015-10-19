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

public final class AltoClientEPPropSwapInfo
{

    private List<VTag> vTagList;
    private List<EPProp> endPointPropList;
    private String uri;
    private String operationStatus;

    public AltoClientEPPropSwapInfo()
    {
        vTagList=new ArrayList<VTag>();
        endPointPropList=new ArrayList<EPProp>();
        uri=AltoClientConst.AltoClientRightString.InvalidURI;
        operationStatus=AltoClientConst.AltoClientRightString.OperationFailure;
    }
    public AltoClientEPPropSwapInfo( List<VTag> vTagList, List<EPProp> endPointPropList, String operationStatus)
    {
        this.vTagList=vTagList;
        this.endPointPropList=endPointPropList;
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

    public void setEndPointPropList(List<EPProp> endPointPropList)
    {
        this.endPointPropList=endPointPropList;
    }
    public List<EPProp>  getEndPointPropList()
    {
        return endPointPropList;
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
        JSONArray vTAGListJsonMembers = new JSONArray();
        for(VTag loop:vTagList)
        {
            vTAGListJsonMembers.put(loop);
        }
        JSONArray ePPropListJsonMembers = new JSONArray();
        for(EPProp loop:endPointPropList)
        {
            ePPropListJsonMembers.put(loop);
        }
        try
        {
            json.put("operationStatus", this.getOperationStatus());
            json.put("vTagList", vTAGListJsonMembers);
            json.put("endPointPropList", ePPropListJsonMembers);
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
        AltoClientEPPropSwapInfo ret=(AltoClientEPPropSwapInfo)super.clone();

        List<VTag> vTagListOrigin=this.getVTagList();
        List<VTag> vTagListCopy=new ArrayList<VTag>();

        List<EPProp> endPointPropListOrigin=this.getEndPointPropList();
        List<EPProp> endPointPropListCopy=new ArrayList<EPProp>();

        int size;
        size=vTagListOrigin.size();
        for(int i=0;i<size;i++)
        {
            VTag tmp= (VTag)(vTagListOrigin.get(i).clone());
            vTagListCopy.add(tmp);
        }

        size=endPointPropListOrigin.size();
        for(int i=0;i<size;i++)
        {
            EPProp tmp= (EPProp)(endPointPropListOrigin.get(i).clone());
            endPointPropListCopy.add(tmp);
        }

        ret.setVTagList(vTagListCopy);
        ret.setEndPointPropList(endPointPropListCopy);

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
