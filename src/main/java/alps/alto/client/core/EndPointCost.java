package alps.alto.client.core;


/**
 *
 */

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class EndPointCost  implements Cloneable
{

    String srcEP;
    String dstEP;
    double  cost;
    public EndPointCost()
    {
        this(AltoClientConst.AltoClientConstString.InvalidPID,AltoClientConst.AltoClientConstString.InvalidEP,-1);
    }
    public EndPointCost(String srcEP,String dstEP,double  cost)
    {
        this.srcEP=srcEP;
        this.dstEP=dstEP;
        this.cost=cost;
    }
    public void setSrcEP(String srcEP )
    {
        this.srcEP=srcEP;
    }
    public String getSrcEP()
    {
        return srcEP;
    }

    public void setDstEP(String dstEP )
    {
        this.dstEP=dstEP;
    }
    public String getDstEP()
    {
        return dstEP;
    }

    public void setCost(double  cost )
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
            json.put("srcEP", this.getSrcEP());
            json.put("dstEP", this.getDstEP());
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
        maps.put(altoClientConstMethod.add("srcEP"), altoClientConstMethod.add(this.getSrcEP()));
        maps.put(altoClientConstMethod.add("dstEP"), altoClientConstMethod.add(this.getDstEP()));
        maps.put(altoClientConstMethod.add("cost"), altoClientConstMethod.add(Double.toString(this.getCost())));

        String ret=maps.toString();
        return ret;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EndPointCost ret=(EndPointCost)super.clone();

        //ret.setSrcEP(this.getSrcEP().toString());
        //ret.setSrcEP(this.getDstEP().toString());
        //ret.setCost(this.getCost());

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
