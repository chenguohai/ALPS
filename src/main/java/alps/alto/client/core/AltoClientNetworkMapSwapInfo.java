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

public final class AltoClientNetworkMapSwapInfo implements Cloneable
{
    private VTag vTag;
    private List<NetworkMap> networkMapList;
    private String uri;
    private String operationStatus;
    public AltoClientNetworkMapSwapInfo()
    {
        vTag=new VTag(AltoClientConst.AltoClientRightString.InvalidTAG,"");
        networkMapList=new ArrayList<NetworkMap>();
        operationStatus=AltoClientConst.AltoClientRightString.OperationFailure;
        uri=AltoClientConst.AltoClientRightString.InvalidURI;

    }
    public AltoClientNetworkMapSwapInfo(VTag vTag,List<NetworkMap> networkMapList)
    {
        this.vTag=vTag;
        this.networkMapList=networkMapList;
        operationStatus=AltoClientConst.AltoClientRightString.OperationFailure;
        uri=AltoClientConst.AltoClientRightString.InvalidURI;
    }

    public void setVTag(VTag vTag)
    {
        this.vTag=vTag;
    }
    public VTag getVTag()
    {
        return vTag;
    }

    public void setNetworkMapList(List<NetworkMap> networkMapList)
    {
        this.networkMapList=networkMapList;
    }
    public List<NetworkMap> getNetworkMapList()
    {
        return networkMapList;
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
    public Object clone() throws CloneNotSupportedException
    {
        AltoClientNetworkMapSwapInfo ret=(AltoClientNetworkMapSwapInfo)super.clone();

        VTag vTagTmp=(VTag)(this.getVTag().clone());

        List<NetworkMap> networkMapListOrigin=this.getNetworkMapList();
        int size=networkMapListOrigin.size();
        List<NetworkMap> networkMapListCopy=new ArrayList<NetworkMap>();
        for(int i=0;i<size;i++)
        {
            NetworkMap tmp= (NetworkMap)(networkMapListOrigin.get(i).clone());
            networkMapListCopy.add(tmp);
        }

        ret.setVTag(vTagTmp);
        ret.setNetworkMapList(networkMapListCopy);

        return ret;
    }

    @Override
    public String toString()
    {
        JSONObject json=new JSONObject();

        JSONArray networkMapListJsonMembers = new JSONArray();
        for(NetworkMap loop:this.getNetworkMapList())
        {
            networkMapListJsonMembers.put(loop);
        }
        try
        {
            json.put("operationStatus", this.getOperationStatus());
            json.put("vTag", this.getVTag().toString());
            json.put("networkMapList", networkMapListJsonMembers);
            json.put("uri", this.getURI());
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
        // just used for test
    }

}
