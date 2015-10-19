package alps.alto.client.core;


/**
 *
 */

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class CostType implements Cloneable
{
    String name;
    String mode;
    String metric;
    String description;
    public CostType()
    {
        this("num-routing","numerical","routingcost","");
    }
    public CostType(String name,String mode,String metric,String description)
    {
        this.name=name;
        this.mode=mode;
        this.metric=metric;
        this.description=description;
    }
    public void setName(String name )
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }

    public void setMode(String mode )
    {
        this.mode=mode;
    }
    public String getMode()
    {
        return mode;
    }

    public void setMetric(String metric )
    {
        this.metric=metric;
    }
    public String getMetric()
    {
        return metric;
    }

    public void setDescription(String description )
    {
        this.description=description;
    }
    public String getDescription()
    {
        return description;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString()
    {
        JSONObject json=new JSONObject();
        try
        {
            json.put("name", this.getName());
            json.put("mode", this.getMode());
            json.put("metric", this.getMetric());
            json.put("description", this.getDescription());
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
