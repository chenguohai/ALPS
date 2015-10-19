package alps.alto.client.core;


/**
 *
 */

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */


import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public final class CostMap implements Cloneable
{
    String srcPID;
    String dstPID;
    double cost;
    public CostMap()
    {
        this(AltoClientConst.AltoClientRightString.InvalidPID,AltoClientConst.AltoClientRightString.InvalidPID,-1.0);
    }
    public CostMap( String srcPID,  String dstPID,  double cost)
    {
        this.srcPID=srcPID;
        this.dstPID=dstPID;
        this.cost=cost;
    }
    public void setSrcPID(String srcPID )
    {
        this.srcPID=srcPID;
    }
    public String getSrcPID()
    {
        return srcPID;
    }

    public void setDstPID(String dstPID )
    {
        this.dstPID=dstPID;
    }
    public String getDstPID()
    {
        return dstPID;
    }

    public void setCost(double cost )
    {
        this.cost=cost;
    }
    public double getCost()
    {
        return cost;
    }

    @Override
    public String toString()
    {
        JSONObject json=new JSONObject();

        try
        {
            json.put("srcPID", this.getSrcPID());
            json.put("dstPID", this.getDstPID());
            json.put("cost", this.getCost());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String ret=json.toString();
        return ret;
    }

    public String toStringLinked()
    {
        AltoClientConstMethod altoClientConstMethod=new AltoClientConstMethod();
        Map<String,String> maps = new LinkedHashMap<String,String>();
        maps.put(altoClientConstMethod.add("srcPID"), altoClientConstMethod.add(this.getSrcPID()));
        maps.put(altoClientConstMethod.add("dstPID"), altoClientConstMethod.add(this.getDstPID()));
        maps.put(altoClientConstMethod.add("cost"), altoClientConstMethod.add(Double.toString(this.getCost())));

        String ret=maps.toString();
        return ret;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // just used for test
    }

}
