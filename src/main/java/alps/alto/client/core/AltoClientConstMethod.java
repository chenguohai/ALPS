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

public final class AltoClientConstMethod
{

    public AltoClientConstMethod()
    {

    }
    public void getHostNameAndRequestLine(String uri,List<String>HostNameRequestLine){
        if(HostNameRequestLine==null)
            HostNameRequestLine=new ArrayList<String>();
        if(uri==null)
            HostNameRequestLine.clear();
        else
        {
            String hostName = null;
            String requestLine = null;
            int firstPosition=uri.indexOf("//");
            uri=uri.substring(firstPosition+2);
            int secondPosition=uri.indexOf("/");
            hostName=uri.substring(0,secondPosition);
            requestLine=uri.substring(secondPosition+1);
            HostNameRequestLine.add(hostName);
            HostNameRequestLine.add(requestLine);
        }
    }
    public Resource locateECSResource(List<Resource> resourceList)
    {
        //judge if there exists end and costs
        Resource retResource=null;
        //System.out.println("----------");
        for(Resource loop:resourceList)
        {
            String uri=loop.getResourceName().toString();
            int costsPosition=uri.indexOf("costs");
            int endPosition=uri.indexOf("end");
            System.out.println(uri);
            if((endPosition>=0) &&(costsPosition>=0)){
                try
                {
                    retResource=(Resource)loop.clone();
                } catch (CloneNotSupportedException e)
                {
                    e.printStackTrace();
                    retResource=null;
                }
                break;
            }
        }
        return retResource;
    }

    public CostType getWantedCostType(List<String> costTypeNameList,String metric,String mode)
    {
        //"["num-routing", "num-hop","ord-routing", "ord-hop"]"
        // ordinal,numerical,routingcost,hopcount

        CostType retCostType=null;
        if(costTypeNameList == null)
            return retCostType;

        String supportMetric=null;
        String supportMode=null;
        for(String loop:costTypeNameList)
        {
            if(loop.intern() == "num-routing")
            {
                supportMetric="routingcost";
                supportMode="numerical";
            }
            else if(loop.intern() == "num-hop")
            {
                supportMetric="hopcount";
                supportMode="numerical";
            }
            else if(loop.intern() == "ord-routing")
            {
                supportMetric="ordinal";
                supportMode="numerical";
            }
            else if(loop.intern() == "ord-hop")
            {
                supportMetric="ordinal";
                supportMode="numerical";
            }

            if((supportMetric.intern()==metric.intern())&&(supportMode.intern()==mode.intern()))
            {
                retCostType=new CostType(AltoClientConst.AltoClientConstString.Valid,metric,mode,"");
                break;
            }
        }
        return retCostType;
    }
    public String add(String string)
    {
        return "\""+ string +"\"";
    }
    /**
     * @param args
     */
    public static void main(String[] args)
    {

    }

}
