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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public final class NetworkMap implements Cloneable
{
    String pidID;
    List<String> iPV4List;
    List<String> iPV6List;

    public NetworkMap()
    {
        this(AltoClientConst.AltoClientConstString.InvalidPID,null,null);
    }
    public NetworkMap(String pidID,List<String> iPV4List,List<String> iPV6List)
    {
        this.pidID=pidID;
        this.iPV4List=iPV4List;
        this.iPV6List=iPV6List;
    }

    public void setPidID(String pidID )
    {
        this.pidID=pidID;
    }
    public String getPidID()
    {
        return pidID;
    }

    public void setIPV4List(List<String> iPV4List )
    {
        this.iPV4List=iPV4List;
    }
    public List<String> getIPV4List()
    {
        return iPV4List;
    }

    public void setIPV6List(List<String> iPV6List )
    {
        this.iPV6List=iPV6List;
    }
    public List<String> getIPV6List()
    {
        return iPV6List;
    }



    @Override
    public Object clone() throws CloneNotSupportedException {
        NetworkMap ret=(NetworkMap) super.clone();

        List<String> iPV4ListOrign=this.getIPV4List();
        List<String> iPV4ListCopy=new ArrayList<String>();
        for(String loop:iPV4ListOrign)
        {
            String tmp=loop;
            iPV4ListCopy.add(tmp);
        }

        List<String> iPV6ListOrigin=this.getIPV6List();
        List<String> iPV6ListCopy=new ArrayList<String>();
        for(String loop:iPV6ListOrigin)
        {
            String tmp=loop;
            iPV6ListCopy.add(tmp);
        }

        ret.setIPV4List(iPV4ListCopy);
        ret.setIPV6List(iPV6ListCopy);

        return ret;
    }
    @Override
    public String toString()
    {
        JSONObject json=new JSONObject();
        List<String> iPV4List=this.getIPV4List();
        List<String> iPV6List=this.getIPV6List();

        JSONArray iPV4ListJsonMembers = new JSONArray();
        JSONArray iPV6ListJsonMembers = new JSONArray();
        for(String loop:iPV4List)
        {
            iPV4ListJsonMembers.put(loop);
        }
        for(String loop:iPV6List)
        {
            iPV6ListJsonMembers.put(loop);
        }
        try
        {
            json.put("pidID", this.getPidID());
            json.put("iPV4List", iPV4ListJsonMembers);
            json.put("iPV6List", iPV6ListJsonMembers);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String ret=json.toString();
        return ret;
    }

    public String toStringLinked()
    {
        JSONObject json=new JSONObject();
        List<String> iPV4List=this.getIPV4List();
        List<String> iPV6List=this.getIPV6List();

        JSONArray iPV4ListJsonMembers = new JSONArray();
        JSONArray iPV6ListJsonMembers = new JSONArray();
        for(String loop:iPV4List)
        {
            iPV4ListJsonMembers.put(loop);
        }
        for(String loop:iPV6List)
        {
            iPV6ListJsonMembers.put(loop);
        }
        AltoClientConstMethod altoClientConstMethod=new AltoClientConstMethod();
        Map<String,String> maps = new LinkedHashMap<String,String>();
        maps.put(altoClientConstMethod.add("pidID"), altoClientConstMethod.add(this.getPidID()));
        if(iPV4ListJsonMembers.length()>0){
            maps.put(altoClientConstMethod.add("iPV4List"), iPV4ListJsonMembers.toString());
        }
        if(iPV6ListJsonMembers.length()>0){
            maps.put(altoClientConstMethod.add("iPV6List"), iPV6ListJsonMembers.toString());
        }
        String ret=maps.toString();
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
