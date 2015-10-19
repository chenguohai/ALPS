package alps.alto.client.core;

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */

import java.util.ArrayList;
import java.util.List;

public class AltoClientConst {


    public AltoClientConst()
    {

    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World a!" );
    }
    static public class AltoClientAcceptConst{
        public final static String IRDAccept="application/alto-directory+json,application/alto-error+json";
        public final static String NetworkMapAccept="application/alto-networkmap+json,application/alto-error+json";
        public final static String CostMapAccept="application/alto-costmap+json,application/alto-error+json";
        public final static String EndPointPropAccept="application/alto-endpointprop+json,application/alto-error+json";
        public final static String EndPointCostAccept="application/alto-endpointcost+json,application/alto-error+json";
    }
    static public  class AltoClientRequestLine{
        public final static String IRDRequestLine="directory";
        public final static String NetworkMapRequestLine="networkmap";
        public final static String CostMapRequestLine="costmap";
        public final static String EndPointPropRequestLine="endpointprop";
        public final static String EndPointCostRequestLine="endpointcost";
    }
    static public class AltoClientRequestHost{
        public final static String IRDRequestHost="alto.example.com";
        public final static String NetworkMapRequestHost="alto.example.com";
        public final static String CostMapRequestHost="alto.example.com";
        public final static String EndPointPropRequestHost="alto.example.com";
        public final static String EndPointCostRequestHost="alto.example.com";
    }
    static public class AltoClientResponseType{
        public final static String IRDResponseType="application/alto-directory+json";
        public final static String NetworkMapResponseType="application/alto-networkmap+json";
        public final static String CostMapResponseType="application/alto-costmap+json";
        public final static String EndPointPropResponseType="application/alto-endpointprop+json";
        public final static String EndPointCostResponseType="application/alto-endpointcost+json";
    }

    static public class AltoClientRightString{
        public final static String HttpGetRightStatus="HTTP/1.1 200 OK";
        public final static String OperationSuccess="SUCCESS";
        public final static String OperationFailure="FAILURE";
        public final static String InvalidSourceID="InvalidSourceID";
        public final static String InvalidPID="InvalidPID";
        public final static String InvalidTAG="InvalidTAG";
        public final static String InvalidURI="InvalidURI";
    }

    static public class AltoClientFilteredContentTypeString{
        public final static String NetworkMap="application/alto-networkmapfilter+json";
        public final static String CostMap="application/alto-costmapfilter+json";
        public final static String EndPointProperty="application/alto-endpointpropparams+json";
        public final static String EndPointCost="application/alto-endpointcostparams+json";
        public final static String InvalidSourceID="InvalidSourceID";
        public final static String InvalidPID="InvalidPID";
        public final static String InvalidTAG="InvalidTAG";
    }

    static public class AltoClientConstString{
        public final static String InvalidSourceID="InvalidSourceID";
        public final static String InvalidResourceID="InvalidResourceID";
        public final static String InvalidPID="InvalidPID";
        public final static String InvalidTAG="InvalidTAG";
        public final static String InvalidEP="InvalidEP";
        public final static String Invalid="Invalid";
        public final static String Valid="Valid";
    }
    static public void methodName(String uri,List<String> hostNameRequestLine){
        if(hostNameRequestLine==null)
            hostNameRequestLine=new ArrayList<String>();
        if(uri==null)
            hostNameRequestLine.clear();
        else
        {

            String hostName = null;
            String requestLine = null;
            int firstPosition=uri.indexOf("//");
            uri=uri.substring(firstPosition+2);
            //System.out.println("AAA "+uri);
            int secondPosition=uri.indexOf("/");
            hostName=uri.substring(0,secondPosition);
            requestLine=uri.substring(secondPosition+1);
            hostNameRequestLine.add(hostName);
            hostNameRequestLine.add(requestLine);
        }
    }
}


