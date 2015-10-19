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

public final class EPProp
{

    private String endPointID;
    private String properties;
    private String propertiesValue;


    public EPProp(){
        this(AltoClientConst.AltoClientConstString.Invalid,AltoClientConst.AltoClientConstString.Invalid,AltoClientConst.AltoClientConstString.Invalid);
    }
    public EPProp(String endPointID,    String properties,String propertiesValue)
    {
        this.endPointID=endPointID;
        this.properties=properties;
        this.propertiesValue=propertiesValue;

    }

    public void setEndPointID(String endPointID)
    {
        this.endPointID=endPointID;
    }
    public String getEndPointID()
    {
        return endPointID;
    }

    public void setProperties(String properties)
    {
        this.properties=properties;
    }
    public String getProperties()
    {
        return properties;
    }

    public void setPropertiesValue(String propertiesValue)
    {
        this.propertiesValue=propertiesValue;
    }
    public String getPropertiesValue()
    {
        return propertiesValue;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EPProp ret=(EPProp)super.clone();
        return ret;
    }

    @Override
    public String toString()
    {
        JSONObject json=new JSONObject();

        try
        {
            json.put("endPointID", this.getEndPointID());
            json.put("properties", this.getProperties());
            json.put("propertiesValue", this.getPropertiesValue());
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
