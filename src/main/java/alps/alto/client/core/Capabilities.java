package alps.alto.client.core;

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Capabilities implements Cloneable
{
    private Boolean costConstraints;
    private List<String> costTypeNamesList;
    private List<String> propTypesList;

    public Capabilities()
    {
        this(new Boolean("false"),null,null);
    }
    public Capabilities(Boolean costConstraints,    List<String> costTypeNamesList,  List<String> propTypesList)
    {
        this.costConstraints=costConstraints;
        this.costTypeNamesList=costTypeNamesList;
        this.propTypesList=propTypesList;
    }

    public void setCostConstraints(Boolean costConstraints )
    {
        this.costConstraints=costConstraints;
    }
    public Boolean getCostConstraints()
    {
        return costConstraints;
    }

    public void setCostTypeNamesList(List<String> costTypeNamesList )
    {
        this.costTypeNamesList=costTypeNamesList;
    }
    public List<String> getCostTypeNamesList()
    {
        return costTypeNamesList;
    }

    public void setPropTypesList(List<String> propTypesList )
    {
        this.propTypesList=propTypesList;
    }
    public List<String> getPropTypesList()
    {
        return propTypesList;
    }

    @Override
    public String toString()
    {

        JSONObject json=new JSONObject();
        JSONArray costTypeNamesListJsonMembers = new JSONArray();
        for(String loop:costTypeNamesList)
        {
            costTypeNamesListJsonMembers.put(loop);
        }
        JSONArray propTypesListJsonMembers = new JSONArray();
        for(String loop:propTypesList)
        {
            propTypesListJsonMembers.put(loop);
        }
        try
        {
            json.put("costConstraints", this.getCostConstraints());
            json.put("costTypeNamesList",costTypeNamesListJsonMembers);
            json.put("propTypesList", propTypesListJsonMembers);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String ret=json.toString();
        return ret;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Capabilities ret=(Capabilities) super.clone();
        //ret.setCostConstraints(getCostConstraints().)
        List<String> costTypeNamesListOrigin=this.getCostTypeNamesList();
        List<String> costTypeNamesListCopy=new ArrayList<String>();
        for(String strLoop:costTypeNamesListOrigin)
        {
            String tmp=strLoop;
            costTypeNamesListCopy.add(tmp);
        }
        List<String> propTypesListOrigin=this.getPropTypesList();
        List<String> propTypesListCopy=new ArrayList<String>();
        for(String strLoop:propTypesListOrigin)
        {
            String tmp=strLoop;
            propTypesListCopy.add(tmp);
        }

        ret.setCostTypeNamesList(costTypeNamesListCopy);
        ret.setPropTypesList(propTypesListCopy);

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
