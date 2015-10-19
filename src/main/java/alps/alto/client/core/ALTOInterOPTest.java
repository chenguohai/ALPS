package alps.alto.client.core;

/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

















//import alps.alto.client.core;
import alps.alto.client.core.*;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;

import java.net.InetAddress;

@SuppressWarnings("unused")
public final class ALTOInterOPTest
{
    public static boolean generateCostMapList(List<CostMap> costMapList,List<CostMap> generatedCostMapList,String filterType,List<String> conditions)
    {
        generatedCostMapList.clear();

        if(filterType.intern()=="getSrc".intern())
        {
            for(CostMap loop :costMapList)
            {
                if(conditions.contains(loop.getSrcPID()))
                {
                    try
                    {
                        generatedCostMapList.add((CostMap)loop.clone());
                    } catch (CloneNotSupportedException e){ e.printStackTrace();}
                }
            }
        }else if(filterType.intern()=="getDst".intern())
        {
            for(CostMap loop :costMapList)
            {
                if(conditions.contains(loop.getDstPID()))
                {
                    try
                    {
                        generatedCostMapList.add((CostMap)loop.clone());
                    } catch (CloneNotSupportedException e){ e.printStackTrace();}
                }
            }
        }else if(filterType.intern()=="getConstraintCost".intern())
        {
            String constraint;
            boolean flagL=false;boolean flagLE=false;boolean flagG=false;boolean flagGE=false;boolean flagE=false;boolean flagNE=false;
            double valueL=0.0;double valueLE=0.0;double valueG=0.0; double valueGE=0.0; double valueE=0.0;double valueNE=0.0;
            for(String loop:conditions)
            {
                int index=0;
                index=loop.indexOf("le"); if(index!=-1){String tmp=loop.substring(index+2);valueLE=Double.parseDouble(tmp);flagLE=true;}
                index=loop.indexOf("ge"); if(index!=-1){String tmp=loop.substring(index+2);valueGE=Double.parseDouble(tmp);flagGE=true;}
                index=loop.indexOf("ne"); if(index!=-1){String tmp=loop.substring(index+2);valueNE=Double.parseDouble(tmp);flagNE=true;}
                index=loop.indexOf("l"); if((index!=-1)&&(flagLE==false)){String tmp=loop.substring(index);valueL=Double.parseDouble(tmp);flagL=true;}
                index=loop.indexOf("g"); if((index!=-1)&&(flagGE==false)){String tmp=loop.substring(index);valueG=Double.parseDouble(tmp);flagG=true;}
                index=loop.indexOf("e"); if((index!=-1)&&(flagLE==false)&&(flagGE==false)&&(flagNE==false)){String tmp=loop.substring(index);valueE=Double.parseDouble(tmp);flagE=true;}
            }
            for(CostMap loop :costMapList)
            {
                double value=loop.getCost();
                if(flagL){if(value<=valueL)continue;}
                if(flagLE){if(value>valueLE)continue;}
                if(flagG){if(value<=valueG)continue;}
                if(flagGE){if(value<valueGE)continue;}
                if(flagE){if(value!=valueE)continue;}
                if(flagNE){if(value==valueNE)continue;}

                try
                {
                    generatedCostMapList.add((CostMap)loop.clone());
                } catch (CloneNotSupportedException e){ e.printStackTrace();}
            }
        }
        return true;
    }

    public static boolean generateDefaultNetworkMap(List<NetworkMap> networkMapList)
    {
        networkMapList.clear();

        List<String> iPV4List1=new ArrayList<String>();iPV4List1.add("100.0.0.0/8");
        List<String> iPV6List1=new ArrayList<String>();
        networkMapList.add(new NetworkMap("mine",iPV4List1,iPV6List1));

        List<String> iPV4List2=new ArrayList<String>();iPV4List2.add("100.0.0.0/10");
        List<String> iPV6List2=new ArrayList<String>();
        networkMapList.add(new NetworkMap("mine1",iPV4List2,iPV6List2));

        List<String> iPV4List3=new ArrayList<String>();iPV4List3.add("100.0.1.0/24");iPV4List3.add("100.0.64.0/24");iPV4List3.add("100.0.192.0/24");
        List<String> iPV6List3=new ArrayList<String>();
        networkMapList.add(new NetworkMap("mine1a",iPV4List3,iPV6List3));

        List<String> iPV4List4=new ArrayList<String>();iPV4List4.add("100.64.0.0/10");
        List<String> iPV6List4=new ArrayList<String>();
        networkMapList.add(new NetworkMap("mine2",iPV4List4,iPV6List4));

        List<String> iPV4List5=new ArrayList<String>();iPV4List5.add("100.128.0.0/10");
        List<String> iPV6List5=new ArrayList<String>();
        networkMapList.add(new NetworkMap("mine3",iPV4List5,iPV6List5));

        List<String> iPV4List6=new ArrayList<String>();iPV4List6.add("128.0.0.0/16");iPV4List6.add("130.0.0.0/16");
        List<String> iPV6List6=new ArrayList<String>();iPV6List6.add(convertIPV6Network("2001:DB8:0000::/33"));
        networkMapList.add(new NetworkMap("peer1",iPV4List6,iPV6List6));

        List<String> iPV4List7=new ArrayList<String>();iPV4List7.add("129.0.0.0/16");iPV4List7.add("131.0.0.0/16");
        List<String> iPV6List7=new ArrayList<String>();iPV6List7.add(convertIPV6Network("2001:DB8:8000::/33"));
        networkMapList.add(new NetworkMap("peer2",iPV4List7,iPV6List7));


        List<String> iPV4List8=new ArrayList<String>();iPV4List8.add("132.0.0.0/16");
        List<String> iPV6List8=new ArrayList<String>();
        networkMapList.add(new NetworkMap("tran1",iPV4List8,iPV6List8));

        List<String> iPV4List9=new ArrayList<String>();iPV4List9.add("135.0.0.0/16");
        List<String> iPV6List9=new ArrayList<String>();
        networkMapList.add(new NetworkMap("tran2",iPV4List9,iPV6List9));

        List<String> iPV4ListA=new ArrayList<String>();iPV4ListA.add("0.0.0.0/0");
        List<String> iPV6ListA=new ArrayList<String>();iPV6ListA.add(convertIPV6Network("::0/0"));
        networkMapList.add(new NetworkMap("default",iPV4ListA,iPV6ListA));

        List<String> iPV4ListB=new ArrayList<String>();iPV4ListB.add("127.0.0.0/8");
        List<String> iPV6ListB=new ArrayList<String>();iPV6ListB.add(convertIPV6Network(("::1/128")));
        networkMapList.add(new NetworkMap("loopback",iPV4ListB,iPV6ListB));

        List<String> iPV4ListC=new ArrayList<String>();iPV4ListC.add("169.254.0.0/16");
        List<String> iPV6ListC=new ArrayList<String>();iPV6ListC.add(convertIPV6Network("FF80::/10"));
        networkMapList.add(new NetworkMap("linklocal",iPV4ListC,iPV6ListC));

        List<String> iPV4ListD=new ArrayList<String>();iPV4ListD.add("10.0.0.0/8");iPV4ListD.add("172.16.0.0/12");iPV4ListD.add("192.168.0.0/16");
        List<String> iPV6ListD=new ArrayList<String>();iPV6ListD.add(convertIPV6Network("FC00::/7"));
        networkMapList.add(new NetworkMap("private",iPV4ListD,iPV6ListD));

        return true;
    }

    public static boolean generateDefaultRoutingcostMap(List<CostMap> costMapList)
    {
        costMapList.clear();


        //costMapList.add(new CostMap("default","default",0.0));
        costMapList.add(new CostMap("default","mine",60.0));
        costMapList.add(new CostMap("default","mine1",63.0));
        costMapList.add(new CostMap("default","mine1a",65.0));
        costMapList.add(new CostMap("default","mine2",64.0));
        costMapList.add(new CostMap("default","mine3",65.0));
        //costMapList.add(new CostMap("default","private",75.0));

        costMapList.add(new CostMap("linklocal","linklocal",0.0));

        costMapList.add(new CostMap("loopback","loopback",0.0));

        costMapList.add(new CostMap("mine","default",60.0));
        costMapList.add(new CostMap("mine","mine",0.0));
        costMapList.add(new CostMap("mine","mine1",3.0));
        costMapList.add(new CostMap("mine","mine1a",5.0));
        costMapList.add(new CostMap("mine","mine2",4.0));
        costMapList.add(new CostMap("mine","mine3",5.0));
        costMapList.add(new CostMap("mine","peer1",20.0));
        costMapList.add(new CostMap("mine","peer2",25.0));
        costMapList.add(new CostMap("mine","tran1",35.0));
        costMapList.add(new CostMap("mine","tran2",45.0));

        costMapList.add(new CostMap("mine1","default",63.0));
        costMapList.add(new CostMap("mine1","mine",3.0));
        costMapList.add(new CostMap("mine1","mine1",0.0));
        costMapList.add(new CostMap("mine1","mine1a",2.0));
        costMapList.add(new CostMap("mine1","mine2",6.5));
        costMapList.add(new CostMap("mine1","mine3",8.0));
        costMapList.add(new CostMap("mine1","peer1",23.0));
        costMapList.add(new CostMap("mine1","peer2",28.0));
        costMapList.add(new CostMap("mine1","tran1",38.0));
        costMapList.add(new CostMap("mine1","tran2",48.0));

        costMapList.add(new CostMap("mine1a","default",65.0));
        costMapList.add(new CostMap("mine1a","mine",5.0));
        costMapList.add(new CostMap("mine1a","mine1",2.0));
        costMapList.add(new CostMap("mine1a","mine1a",0.0));
        costMapList.add(new CostMap("mine1a","mine2",4.5));
        costMapList.add(new CostMap("mine1a","mine3",10.0));
        costMapList.add(new CostMap("mine1a","peer1",25.0));
        costMapList.add(new CostMap("mine1a","peer2",30.0));
        costMapList.add(new CostMap("mine1a","tran1",40.0));
        costMapList.add(new CostMap("mine1a","tran2",50.0));

        costMapList.add(new CostMap("mine2","default",64.0));
        costMapList.add(new CostMap("mine2","mine",4.0));
        costMapList.add(new CostMap("mine2","mine1",7.0));
        costMapList.add(new CostMap("mine2","mine1a",9.0));
        costMapList.add(new CostMap("mine2","mine2",0.0));
        costMapList.add(new CostMap("mine2","mine3",9.0));
        costMapList.add(new CostMap("mine2","peer1",24.0));
        costMapList.add(new CostMap("mine2","peer2",29.0));
        costMapList.add(new CostMap("mine2","tran1",39.0));
        costMapList.add(new CostMap("mine2","tran2",49.0));

        costMapList.add(new CostMap("mine3","default",65.0));
        costMapList.add(new CostMap("mine3","mine",5.0));
        costMapList.add(new CostMap("mine3","mine1",8.0));
        costMapList.add(new CostMap("mine3","mine1a",10.0));
        costMapList.add(new CostMap("mine3","mine2",9.0));
        costMapList.add(new CostMap("mine3","mine3",0.0));
        costMapList.add(new CostMap("mine3","peer1",25.0));
        costMapList.add(new CostMap("mine3","peer2",30.0));
        costMapList.add(new CostMap("mine3","tran1",40.0));
        costMapList.add(new CostMap("mine3","tran2",50.0));

        costMapList.add(new CostMap("peer1","mine",20.0));
        costMapList.add(new CostMap("peer1","mine1",23.0));
        costMapList.add(new CostMap("peer1","mine1a",25.0));
        costMapList.add(new CostMap("peer1","mine2",24.0));
        costMapList.add(new CostMap("peer1","mine3",25.0));
        costMapList.add(new CostMap("peer1","peer1",0.0));

        costMapList.add(new CostMap("peer2","mine",25.0));
        costMapList.add(new CostMap("peer2","mine1",28.0));
        costMapList.add(new CostMap("peer2","mine1a",30.0));
        costMapList.add(new CostMap("peer2","mine2",29.0));
        costMapList.add(new CostMap("peer2","mine3",30.0));
        costMapList.add(new CostMap("peer2","peer2",0.0));

        //costMapList.add(new CostMap("private","default",75.0));
        costMapList.add(new CostMap("private","private",0.0));

        costMapList.add(new CostMap("tran1","mine",35.0));
        costMapList.add(new CostMap("tran1","mine1",38.0));
        costMapList.add(new CostMap("tran1","mine1a",40.0));
        costMapList.add(new CostMap("tran1","mine2",39.0));
        costMapList.add(new CostMap("tran1","mine3",40.0));
        costMapList.add(new CostMap("tran1","tran1",0.0));

        costMapList.add(new CostMap("tran2","mine",45.0));
        costMapList.add(new CostMap("tran2","mine1",48.0));
        costMapList.add(new CostMap("tran2","mine1a",50.0));
        costMapList.add(new CostMap("tran2","mine2",49.0));
        costMapList.add(new CostMap("tran2","mine3",50.0));
        costMapList.add(new CostMap("tran2","tran2",0.0));

        return true;
    }

    public static boolean generateDefaultHopcountMap(List<CostMap> costMapList)
    {
        costMapList.clear();


        //costMapList.add(new CostMap("default","default",0));
        costMapList.add(new CostMap("default","mine",3));
        costMapList.add(new CostMap("default","mine1",4));
        costMapList.add(new CostMap("default","mine1a",5));
        costMapList.add(new CostMap("default","mine2",4));
        costMapList.add(new CostMap("default","mine3",4));
        //costMapList.add(new CostMap("default","private",10.0));

        costMapList.add(new CostMap("linklocal","linklocal",0));

        costMapList.add(new CostMap("loopback","loopback",0));

        costMapList.add(new CostMap("mine","default",3.0));
        costMapList.add(new CostMap("mine","mine",0));
        costMapList.add(new CostMap("mine","mine1",2));
        costMapList.add(new CostMap("mine","mine1a",3));
        costMapList.add(new CostMap("mine","mine2",2));
        costMapList.add(new CostMap("mine","mine3",2));
        costMapList.add(new CostMap("mine","peer1",3));
        costMapList.add(new CostMap("mine","peer2",2));
        costMapList.add(new CostMap("mine","tran1",4));
        costMapList.add(new CostMap("mine","tran2",3));

        costMapList.add(new CostMap("mine1","default",4));
        costMapList.add(new CostMap("mine1","mine",2));
        costMapList.add(new CostMap("mine1","mine1",0));
        costMapList.add(new CostMap("mine1","mine1a",2));
        costMapList.add(new CostMap("mine1","mine2",3));
        costMapList.add(new CostMap("mine1","mine3",3));
        costMapList.add(new CostMap("mine1","peer1",4));
        costMapList.add(new CostMap("mine1","peer2",3));
        costMapList.add(new CostMap("mine1","tran1",5));
        costMapList.add(new CostMap("mine1","tran2",4));

        costMapList.add(new CostMap("mine1a","default",5));
        costMapList.add(new CostMap("mine1a","mine",3));
        costMapList.add(new CostMap("mine1a","mine1",2));
        costMapList.add(new CostMap("mine1a","mine1a",0));
        costMapList.add(new CostMap("mine1a","mine2",2));
        costMapList.add(new CostMap("mine1a","mine3",4));
        costMapList.add(new CostMap("mine1a","peer1",5));
        costMapList.add(new CostMap("mine1a","peer2",4));
        costMapList.add(new CostMap("mine1a","tran1",6));
        costMapList.add(new CostMap("mine1a","tran2",5));

        costMapList.add(new CostMap("mine2","default",4));
        costMapList.add(new CostMap("mine2","mine",2));
        costMapList.add(new CostMap("mine2","mine1",3));
        costMapList.add(new CostMap("mine2","mine1a",4));
        costMapList.add(new CostMap("mine2","mine2",0));
        costMapList.add(new CostMap("mine2","mine3",3));
        costMapList.add(new CostMap("mine2","peer1",4));
        costMapList.add(new CostMap("mine2","peer2",3));
        costMapList.add(new CostMap("mine2","tran1",5));
        costMapList.add(new CostMap("mine2","tran2",4));

        costMapList.add(new CostMap("mine3","default",4));
        costMapList.add(new CostMap("mine3","mine",2));
        costMapList.add(new CostMap("mine3","mine1",3));
        costMapList.add(new CostMap("mine3","mine1a",4));
        costMapList.add(new CostMap("mine3","mine2",3));
        costMapList.add(new CostMap("mine3","mine3",0));
        costMapList.add(new CostMap("mine3","peer1",4));
        costMapList.add(new CostMap("mine3","peer2",3));
        costMapList.add(new CostMap("mine3","tran1",5));
        costMapList.add(new CostMap("mine3","tran2",4));

        costMapList.add(new CostMap("peer1","mine",3));
        costMapList.add(new CostMap("peer1","mine1",4));
        costMapList.add(new CostMap("peer1","mine1a",5));
        costMapList.add(new CostMap("peer1","mine2",4));
        costMapList.add(new CostMap("peer1","mine3",4));
        costMapList.add(new CostMap("peer1","peer1",0));

        costMapList.add(new CostMap("peer2","mine",2));
        costMapList.add(new CostMap("peer2","mine1",3));
        costMapList.add(new CostMap("peer2","mine1a",4));
        costMapList.add(new CostMap("peer2","mine2",3));
        costMapList.add(new CostMap("peer2","mine3",3));
        costMapList.add(new CostMap("peer2","peer2",0));

        //costMapList.add(new CostMap("private","default",10.0));
        costMapList.add(new CostMap("private","private",0));

        costMapList.add(new CostMap("tran1","mine",4));
        costMapList.add(new CostMap("tran1","mine1",5));
        costMapList.add(new CostMap("tran1","mine1a",6));
        costMapList.add(new CostMap("tran1","mine2",5));
        costMapList.add(new CostMap("tran1","mine3",5));
        costMapList.add(new CostMap("tran1","tran1",0));

        costMapList.add(new CostMap("tran2","mine",3));
        costMapList.add(new CostMap("tran2","mine1",4));
        costMapList.add(new CostMap("tran2","mine1a",5));
        costMapList.add(new CostMap("tran2","mine2",4));
        costMapList.add(new CostMap("tran2","mine3",4));
        costMapList.add(new CostMap("tran2","tran2",0));

        return true;
    }


    public static boolean generateAlternateNetworkMap(List<NetworkMap> networkMapList)
    {
        networkMapList.clear();

        List<String> iPV4List1=new ArrayList<String>();iPV4List1.add("101.0.0.0/16");
        List<String> iPV6List1=new ArrayList<String>();
        networkMapList.add(new NetworkMap("dc1",iPV4List1,iPV6List1));

        List<String> iPV4List2=new ArrayList<String>();iPV4List2.add("102.0.0.0/16");
        List<String> iPV6List2=new ArrayList<String>();
        networkMapList.add(new NetworkMap("dc2",iPV4List2,iPV6List2));

        List<String> iPV4List3=new ArrayList<String>();iPV4List3.add("103.0.0.0/16");
        List<String> iPV6List3=new ArrayList<String>();
        networkMapList.add(new NetworkMap("dc3",iPV4List3,iPV6List3));

        List<String> iPV4List4=new ArrayList<String>();iPV4List4.add("104.0.0.0/16");
        List<String> iPV6List4=new ArrayList<String>();
        networkMapList.add(new NetworkMap("dc4",iPV4List4,iPV6List4));

        List<String> iPV4List5=new ArrayList<String>();iPV4List5.add("201.0.0.0/16");
        List<String> iPV6List5=new ArrayList<String>();
        networkMapList.add(new NetworkMap("user1",iPV4List5,iPV6List5));

        List<String> iPV4List6=new ArrayList<String>();iPV4List6.add("202.0.0.0/16");
        List<String> iPV6List6=new ArrayList<String>();
        networkMapList.add(new NetworkMap("user2",iPV4List6,iPV6List6));

        List<String> iPV4List7=new ArrayList<String>();iPV4List7.add("203.0.0.0/16");
        List<String> iPV6List7=new ArrayList<String>();
        networkMapList.add(new NetworkMap("user3",iPV4List7,iPV6List7));

        List<String> iPV4List8=new ArrayList<String>();iPV4List8.add("204.0.0.0/16");
        List<String> iPV6List8=new ArrayList<String>();
        networkMapList.add(new NetworkMap("user4",iPV4List8,iPV6List8));

        List<String> iPV4ListA=new ArrayList<String>();iPV4ListA.add("0.0.0.0/0");
        List<String> iPV6ListA=new ArrayList<String>();iPV6ListA.add(convertIPV6Network("::0/0"));
        networkMapList.add(new NetworkMap("default",iPV4ListA,iPV6ListA));

        List<String> iPV4ListB=new ArrayList<String>();iPV4ListB.add("127.0.0.0/8");
        List<String> iPV6ListB=new ArrayList<String>();iPV6ListB.add(convertIPV6Network("::1/128"));
        networkMapList.add(new NetworkMap("loopback",iPV4ListB,iPV6ListB));

        List<String> iPV4ListC=new ArrayList<String>();iPV4ListC.add("169.254.0.0/16");
        List<String> iPV6ListC=new ArrayList<String>();iPV6ListC.add(convertIPV6Network("FF80::/10"));
        networkMapList.add(new NetworkMap("linklocal",iPV4ListC,iPV6ListC));

        List<String> iPV4ListD=new ArrayList<String>();iPV4ListD.add("10.0.0.0/8");iPV4ListD.add("172.16.0.0/12");iPV4ListD.add("192.168.0.0/16");
        List<String> iPV6ListD=new ArrayList<String>();iPV6ListD.add(convertIPV6Network("FC00::/7"));
        networkMapList.add(new NetworkMap("private",iPV4ListD,iPV6ListD));

        return true;
    }

    public static boolean generateAlternateRoutingcostMap(List<CostMap> costMapList)
    {
        costMapList.clear();

        costMapList.add(new CostMap("dc1","dc1",0.0));
        costMapList.add(new CostMap("dc1","dc2",20.0));
        costMapList.add(new CostMap("dc1","dc3",20.0));
        costMapList.add(new CostMap("dc1","dc4",20.0));
        costMapList.add(new CostMap("dc1","default",50.0));
        costMapList.add(new CostMap("dc1","user1",10.0));
        costMapList.add(new CostMap("dc1","user2",15.0));
        costMapList.add(new CostMap("dc1","user3",20.0));
        costMapList.add(new CostMap("dc1","user4",25.0));

        costMapList.add(new CostMap("dc2","dc1",20.0));
        costMapList.add(new CostMap("dc2","dc2",0.0));
        costMapList.add(new CostMap("dc2","dc3",20.0));
        costMapList.add(new CostMap("dc2","dc4",20.0));
        costMapList.add(new CostMap("dc2","default",55.0));
        costMapList.add(new CostMap("dc2","user1",15.0));
        costMapList.add(new CostMap("dc2","user2",10.0));
        costMapList.add(new CostMap("dc2","user3",15.0));
        costMapList.add(new CostMap("dc2","user4",20.0));

        costMapList.add(new CostMap("dc3","dc1",20.0));
        costMapList.add(new CostMap("dc3","dc2",20.0));
        costMapList.add(new CostMap("dc3","dc3",0.0));
        costMapList.add(new CostMap("dc3","dc4",20.0));
        costMapList.add(new CostMap("dc3","default",55.0));
        costMapList.add(new CostMap("dc3","user1",20.0));
        costMapList.add(new CostMap("dc3","user2",15.0));
        costMapList.add(new CostMap("dc3","user3",10.0));
        costMapList.add(new CostMap("dc3","user4",15.0));

        costMapList.add(new CostMap("dc4","dc1",20.0));
        costMapList.add(new CostMap("dc4","dc2",20.0));
        costMapList.add(new CostMap("dc4","dc3",20.0));
        costMapList.add(new CostMap("dc4","dc4",0.0));
        costMapList.add(new CostMap("dc4","default",50.0));
        costMapList.add(new CostMap("dc4","user1",25.0));
        costMapList.add(new CostMap("dc4","user2",20.0));
        costMapList.add(new CostMap("dc4","user3",15.0));
        costMapList.add(new CostMap("dc4","user4",10.0));

        costMapList.add(new CostMap("default","dc1",50.0));
        costMapList.add(new CostMap("default","dc2",55.0));
        costMapList.add(new CostMap("default","dc3",55.0));
        costMapList.add(new CostMap("default","dc4",50.0));
        //costMapList.add(new CostMap("default","default",0.0));

        costMapList.add(new CostMap("user1","dc1",10.0));
        costMapList.add(new CostMap("user1","dc2",15.0));
        costMapList.add(new CostMap("user1","dc3",20.0));
        costMapList.add(new CostMap("user1","dc4",25.0));
        costMapList.add(new CostMap("user1","user1",0.0));

        costMapList.add(new CostMap("user2","dc1",15.0));
        costMapList.add(new CostMap("user2","dc2",10.0));
        costMapList.add(new CostMap("user2","dc3",15.0));
        costMapList.add(new CostMap("user2","dc4",20.0));
        costMapList.add(new CostMap("user2","user2",0.0));


        costMapList.add(new CostMap("user3","dc1",20.0));
        costMapList.add(new CostMap("user3","dc2",15.0));
        costMapList.add(new CostMap("user3","dc3",10.0));
        costMapList.add(new CostMap("user3","dc4",15.0));
        costMapList.add(new CostMap("user3","user3",0.0));

        costMapList.add(new CostMap("user4","dc1",25.0));
        costMapList.add(new CostMap("user4","dc2",20.0));
        costMapList.add(new CostMap("user4","dc3",15.0));
        costMapList.add(new CostMap("user4","dc4",10.0));
        costMapList.add(new CostMap("user4","user4",0.0));

        costMapList.add(new CostMap("private","private",0.0));
        costMapList.add(new CostMap("linklocal","linklocal",0.0));
        costMapList.add(new CostMap("loopback","loopback",0.0));

        return true;
    }

    public static boolean generateAlternateHopcountMap(List<CostMap> costMapList)
    {
        costMapList.clear();


        costMapList.add(new CostMap("dc1","dc1",0));
        costMapList.add(new CostMap("dc1","dc2",2));
        costMapList.add(new CostMap("dc1","dc3",2));
        costMapList.add(new CostMap("dc1","dc4",2));
        costMapList.add(new CostMap("dc1","default",3));
        costMapList.add(new CostMap("dc1","user1",2));
        costMapList.add(new CostMap("dc1","user2",3));
        costMapList.add(new CostMap("dc1","user3",4));
        costMapList.add(new CostMap("dc1","user4",5));

        costMapList.add(new CostMap("dc2","dc1",2));
        costMapList.add(new CostMap("dc2","dc2",0));
        costMapList.add(new CostMap("dc2","dc3",2));
        costMapList.add(new CostMap("dc2","dc4",2));
        costMapList.add(new CostMap("dc2","default",4));
        costMapList.add(new CostMap("dc2","user1",3));
        costMapList.add(new CostMap("dc2","user2",2));
        costMapList.add(new CostMap("dc2","user3",3));
        costMapList.add(new CostMap("dc2","user4",4));

        costMapList.add(new CostMap("dc3","dc1",2));
        costMapList.add(new CostMap("dc3","dc2",2));
        costMapList.add(new CostMap("dc3","dc3",0));
        costMapList.add(new CostMap("dc3","dc4",2));
        costMapList.add(new CostMap("dc3","default",4));
        costMapList.add(new CostMap("dc3","user1",4));
        costMapList.add(new CostMap("dc3","user2",3));
        costMapList.add(new CostMap("dc3","user3",2));
        costMapList.add(new CostMap("dc3","user4",3));


        costMapList.add(new CostMap("dc4","dc1",2));
        costMapList.add(new CostMap("dc4","dc2",2));
        costMapList.add(new CostMap("dc4","dc3",2));
        costMapList.add(new CostMap("dc4","dc4",0));
        costMapList.add(new CostMap("dc4","default",3));
        costMapList.add(new CostMap("dc4","user1",5));
        costMapList.add(new CostMap("dc4","user2",4));
        costMapList.add(new CostMap("dc4","user3",3));
        costMapList.add(new CostMap("dc4","user4",2));

        costMapList.add(new CostMap("default","dc1",3));
        costMapList.add(new CostMap("default","dc2",4));
        costMapList.add(new CostMap("default","dc3",4));
        costMapList.add(new CostMap("default","dc4",3));

        costMapList.add(new CostMap("user1","user1",0));
        costMapList.add(new CostMap("user1","dc1",2));
        costMapList.add(new CostMap("user1","dc2",3));
        costMapList.add(new CostMap("user1","dc3",4));
        costMapList.add(new CostMap("user1","dc4",5));

        costMapList.add(new CostMap("user2","user2",0));
        costMapList.add(new CostMap("user2","dc1",3));
        costMapList.add(new CostMap("user2","dc2",2));
        costMapList.add(new CostMap("user2","dc3",3));
        costMapList.add(new CostMap("user2","dc4",4));

        costMapList.add(new CostMap("user3","user3",0));
        costMapList.add(new CostMap("user3","dc1",4));
        costMapList.add(new CostMap("user3","dc2",3));
        costMapList.add(new CostMap("user3","dc3",2));
        costMapList.add(new CostMap("user3","dc4",3));

        costMapList.add(new CostMap("user4","user4",0));
        costMapList.add(new CostMap("user4","dc1",5));
        costMapList.add(new CostMap("user4","dc2",4));
        costMapList.add(new CostMap("user4","dc3",3));
        costMapList.add(new CostMap("user4","dc4",2));


        costMapList.add(new CostMap("loopback","loopback",0));
        costMapList.add(new CostMap("linklocal","linklocal",0));
        costMapList.add(new CostMap("private","private",0));

        return true;
    }



    public static boolean checkRoutingcostMap(List<String> argsList,List<String> reasonList,String networkMapID)
    {
        List<CostMap> DefaultcostMapList=new ArrayList<CostMap>();
        if(networkMapID.contains("default"))
            generateDefaultRoutingcostMap(DefaultcostMapList);
        else
            generateAlternateRoutingcostMap(DefaultcostMapList);


        List<VTag> vTagList;
        CostType costType;
        List<CostMap> ReponseCostMapList;
        String operationStatus;
        AltoClientCostMapSwapInfo costMapInfo=new AltoClientCostMapSwapInfo();
        AltoClientCostMap altoClientCostMap;

        altoClientCostMap=new AltoClientCostMap(argsList.get(0),argsList.get(1));
        altoClientCostMap.getInfo(costMapInfo);
        ReponseCostMapList=costMapInfo.getCostMapList();
        /*reasonList.add("=====================A   "+DefaultcostMapList.size() +"  "+ ReponseCostMapList.size());
        int i=0;
        List<CostMap> ReponseCostMapListFilter=new ArrayList<CostMap>();
        for(CostMap loop:ReponseCostMapList)
        {
            ReponseCostMapListFilter.add(loop);reasonList.add("=====================  "+loop);
            i++;

        }
        compareEqualCostMap(ReponseCostMapListFilter,DefaultcostMapList,reasonList);
        reasonList.add("=====================B");*/
        compareEqualCostMap(ReponseCostMapList,DefaultcostMapList,reasonList);

        return true;
    }

    public static boolean checkHopcountMap(List<String> argsList,List<String> reasonList,String networkMapID)
    {
        List<CostMap> CostMapList=new ArrayList<CostMap>();
        if(networkMapID.contains("default"))
            generateDefaultHopcountMap(CostMapList);
        else
            generateAlternateHopcountMap(CostMapList);

        List<VTag> vTagList;
        CostType costType;
        List<CostMap> costMapList;
        String operationStatus;
        AltoClientCostMapSwapInfo costMapInfo=new AltoClientCostMapSwapInfo();
        AltoClientCostMap altoClientCostMap;
        CostType postCostType;


        altoClientCostMap=new AltoClientCostMap(argsList.get(0),argsList.get(1));
        altoClientCostMap.getInfo(costMapInfo);

        costMapList=costMapInfo.getCostMapList();
        reasonList.clear();

        List<String> srcs=new ArrayList<String>();
        compareEqualCostMap(costMapList,CostMapList,reasonList);

        return true;
    }

    public static boolean checkNetworkMap(List<String> argsList,List<String> reasonList,String networkMapID)
    {
        List<NetworkMap> NetworkMapList=new ArrayList<NetworkMap>();
        if(networkMapID.contains("default"))
            generateDefaultNetworkMap(NetworkMapList);
        else
            generateAlternateNetworkMap(NetworkMapList);

        VTag vTag;
        List<NetworkMap> networkMapList;
        String operationStatus;
        AltoClientNetworkMapSwapInfo networkMapInfo=new AltoClientNetworkMapSwapInfo();
        AltoClientNetworkMap altoClientNetworkMap=null;


        altoClientNetworkMap=new AltoClientNetworkMap(argsList.get(0),argsList.get(1));
        altoClientNetworkMap.getInfo(networkMapInfo);

        vTag=networkMapInfo.getVTag();
        networkMapList=networkMapInfo.getNetworkMapList();
        operationStatus=networkMapInfo.getOperationStatus();

        reasonList.clear();
        compareEqualNetworkMap(networkMapList,NetworkMapList,reasonList);
        return true;
    }

    public static boolean checkEPPMap(List<String> argsList,List<String> reasonList)
    {

        class EPP{
            public  String ip;
            public  String val;
            EPP(String ip,String val)
            {
                this.ip=ip;
                this.val=val;
            }

        };
        List<VTag> vTagList;
        List<EPProp> endPointPropList;

        AltoClientEPPropSwapInfo EPPInfo=new AltoClientEPPropSwapInfo();
        AltoClientEndPointProp altoClientEndPointProp;

        List<String> properties=new ArrayList<String>();
        List<String> endpoints=new ArrayList<String>();
        properties.add("priv:ietf-type");
        //properties.add("alt-network.pid");
        String ip1="ipv4:100.0.1.25";
        String ip2="ipv4:128.0.1.25";
        String ip3="ipv6:2001:DB8:2::";
        String ip4="ipv4:132.0.1.25";
        String ip5="ipv4:135.0.1.25";
        String val1="mine";
        String val2="peer";
        String val3="peer";
        String val4="transit";
        String val5="transit";
        JSONObject jsonDefaultEPP=new JSONObject();
        endpoints.add(ip1);
        endpoints.add(ip2);
        endpoints.add(ip3);
        endpoints.add(ip4);
        endpoints.add(ip5);

        altoClientEndPointProp=new AltoClientEndPointProp(argsList.get(0),argsList.get(1),properties,endpoints);
        altoClientEndPointProp.getInfo(EPPInfo);
        vTagList=EPPInfo.getVTagList();
        endPointPropList=EPPInfo.getEndPointPropList();

        List<EPP> responseEPP=new ArrayList<EPP>();
        List<EPP> defaultEPP=new ArrayList<EPP>();
        defaultEPP.add(new EPP(ip1,val1));
        defaultEPP.add(new EPP(ip2,val2));
        defaultEPP.add(new EPP(ip3,val3));
        defaultEPP.add(new EPP(ip4,val4));
        defaultEPP.add(new EPP(ip5,val5));

        for(EPProp loop:endPointPropList)
        {
            responseEPP.add(new EPP(loop.getEndPointID(),loop.getPropertiesValue()));
        }
        reasonList.add("The Endpint properties value check is here.");
        boolean bAllFlag=false;

        List<String> responseID=new ArrayList<String>();
        List<String> defaultID=new ArrayList<String>();
        for(EPP loopResponse:responseEPP)
        {
            responseID.add(loopResponse.ip);
        }
        for(EPP loopResponse:defaultEPP)
        {
            defaultID.add(loopResponse.ip);
        }
        for(String loopResponse:responseID)
        {
            if(!(defaultID.contains(loopResponse)))
            {
                reasonList.add("Error:There is an additonal Endpoint Properties value for " + loopResponse);
                bAllFlag=true;
            }
        }
        for(String loopResponse:defaultID)
        {
            if(!(responseID.contains(loopResponse)))
            {
                reasonList.add("Error:There is an absent Endpoint Properties value for " + loopResponse);
                bAllFlag=true;
            }
        }
        for(EPP loopResponse:responseEPP)
        {
            for(EPP loopDefault:defaultEPP)
            {
                if(loopResponse.ip.intern()==loopDefault.ip.intern())
                {
                    if(loopResponse.val.intern()!=loopDefault.val.intern())
                    {
                        reasonList.add("Error:There is an Endpoint Properties value not consistent with Figure 7. The IP address is " + loopResponse.ip +" value is "+ loopResponse.val+". ");
                        bAllFlag=true;
                    }
                    break;
                }
            }
        }

        if(bAllFlag==false)
        {
            reasonList.add("The Tested server provides Values for \"priv:ietf-type\" consistent with Figure 7.");
        }

        return true;
    }

    public static boolean compareNetworkMapItem(NetworkMap aNetworkMap,NetworkMap bNetworkMap)
    {
        List<String> aPid4List=aNetworkMap.getIPV4List();
        List<String> aPid6List=aNetworkMap.getIPV6List();
        String aPidName=aNetworkMap.getPidID();
        List<String> bPid4List=bNetworkMap.getIPV4List();
        List<String> bPid6List=bNetworkMap.getIPV6List();

        List<String> aPid6ListC=new ArrayList<String>();
        List<String> bPid6ListC=new ArrayList<String>();
        String bPidName=bNetworkMap.getPidID();
        boolean fourEqual=true;
        boolean sixEqual=true;
        for(String loop:aPid4List)
        {
            if(!bPid4List.contains(loop))
            {
                fourEqual=false;
                break;
            }
        }

        for(String loop:bPid4List)
        {
            if(!aPid4List.contains(loop))
            {
                fourEqual=false;
                break;
            }
        }
        for(String loop:aPid6List)
        {
            aPid6ListC.add(convertIPV6Network(loop));
        }
        for(String loop:bPid6List)
        {
            bPid6ListC.add(convertIPV6Network(loop));
        }
        for(String loop:aPid6ListC)
        {
            if(!bPid6ListC.contains(loop))
            {
                sixEqual=false;
                break;
            }
        }

        for(String loop:bPid6ListC)
        {
            if(!aPid6ListC.contains(loop))
            {
                sixEqual=false;
                break;
            }
        }

        if((fourEqual==true) && (sixEqual==true) &&
           (aPidName.intern()==bPidName.intern()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public static boolean compareEqualNetworkMap(List<NetworkMap> aNetworkMapList,List<NetworkMap> bNetworkMapList,List<String> reasonList)
    {
        boolean bFlag=false;
        boolean bcompare=false;

        for(NetworkMap aLoop:aNetworkMapList)
        {
            bFlag=false;
            for(NetworkMap bLoop :bNetworkMapList){
                bcompare=compareNetworkMapItem(aLoop,bLoop);
                if(bcompare==true)
                {
                    bFlag=true;
                    break;
                }
            }
            if(bFlag==false)
            {
                reasonList.add("Error:There is an additional network map item.\t" + aLoop +".");
            }
        }


        for(NetworkMap bLoop:bNetworkMapList)
        {
            bFlag=false;
            for(NetworkMap aLoop :aNetworkMapList){
                bcompare=compareNetworkMapItem(aLoop,bLoop);
                if(bcompare==true)
                {
                    bFlag=true;
                    break;
                }
            }
            if(bFlag==false)
            {
                reasonList.add("Error:There is an absent network map item.\t" + bLoop +".");
            }
        }

        return true;
    }
    public static boolean getFilteredNetworkMap(List<NetworkMap> networkMapList,List<NetworkMap> filteredNetworkMapList,String filterType,List<String> conditionList)
    {
        if(filterType.intern()=="ipv6".intern())
        {
            filteredNetworkMapList.clear();
            for(NetworkMap loop:networkMapList)
            {
                if(loop.getIPV6List().size()>0)
                {
                    try {
                        NetworkMap tmpNetworkMap=(NetworkMap)loop.clone();
                        tmpNetworkMap.iPV4List.clear();
                        filteredNetworkMapList.add(tmpNetworkMap);
                        }
                    catch (CloneNotSupportedException e)    {       e.printStackTrace();}
                }
            }
        }
        else if(filterType.intern()=="getSubset")
        {
            filteredNetworkMapList.clear();
            for(NetworkMap loop:networkMapList)
            {
                if(conditionList.contains(loop.getPidID()))
                {
                    try {filteredNetworkMapList.add((NetworkMap)loop.clone());} catch (CloneNotSupportedException e)    {       e.printStackTrace();}
                }
            }
        }

        return true;
    }

    public static boolean filteredNetworkMapTest(List<String> argsList,List<String> reasonList,String mapID,List<String> invalidPidList,List<String> validPidList)
    {
        int stratID=4;
        int mapSelectID=0;
        reasonList.clear();
        boolean bFlag=false;

        List<NetworkMap> NetworkMapList=new ArrayList<NetworkMap>();
        List<NetworkMap> filteredNetworkMapList=new ArrayList<NetworkMap>();


        List<NetworkMap> networkMapList;
        AltoClientNetworkMapSwapInfo networkMapInfo=new AltoClientNetworkMapSwapInfo();
        AltoClientNetworkMap altoClientNetworkMap=null;
        List<String> pids=new ArrayList<String>();
        List<String> address_types=new ArrayList<String>();

        if(mapID.intern()=="default".intern())
        {
            mapSelectID=0;
        }
        else
        {
            mapSelectID=1;
        }

        if(mapID.intern()=="default".intern())
        {
            stratID=4;
        }
        else
        {
            stratID=8;
        }

        switch (mapSelectID){
        case 0:generateDefaultNetworkMap(NetworkMapList);break;
        case 1:generateAlternateNetworkMap(NetworkMapList);break;

        default:generateDefaultNetworkMap(NetworkMapList);break;
        }
        reasonList.add("");
        pids.clear();

        altoClientNetworkMap=new AltoClientNetworkMap(argsList.get(0),argsList.get(1),pids,null);
        altoClientNetworkMap.getInfo(networkMapInfo);
        networkMapList=networkMapInfo.getNetworkMapList();
        reasonList.add("result 1.1."+stratID+" A-----4.1.A Filtered Network Map Tests."+"Empty PIDs and Omitted \"address-types\" array.");
        compareEqualNetworkMap(networkMapList,NetworkMapList,reasonList);

        return true;
    }
    public static boolean filteredNetworkMapTestb(List<String> argsList,List<String> reasonList,String mapID,List<String> invalidPidList,List<String> validPidList)
    {
        int stratID=4;
        int mapSelectID=0;
        reasonList.clear();
        boolean bFlag=false;

        List<NetworkMap> NetworkMapList=new ArrayList<NetworkMap>();
        List<NetworkMap> filteredNetworkMapList=new ArrayList<NetworkMap>();


        List<NetworkMap> networkMapList;
        AltoClientNetworkMapSwapInfo networkMapInfo=new AltoClientNetworkMapSwapInfo();
        AltoClientNetworkMap altoClientNetworkMap=null;
        List<String> pids=new ArrayList<String>();
        List<String> address_types=new ArrayList<String>();

        if(mapID.intern()=="default".intern())
        {
            mapSelectID=0;
        }
        else
        {
            mapSelectID=1;
        }

        if(mapID.intern()=="default".intern())
        {
            stratID=4;
        }
        else
        {
            stratID=8;
        }

        switch (mapSelectID){
        case 0:generateDefaultNetworkMap(NetworkMapList);break;
        case 1:generateAlternateNetworkMap(NetworkMapList);break;

        default:generateDefaultNetworkMap(NetworkMapList);break;
        }
        reasonList.add("");
        pids.clear();

        altoClientNetworkMap=new AltoClientNetworkMap(argsList.get(0),argsList.get(1),pids,null);
        altoClientNetworkMap.getInfo(networkMapInfo);
        networkMapList=networkMapInfo.getNetworkMapList();
        reasonList.add("result 1.1."+stratID+" A-----4.1.A Filtered Network Map Tests."+"Empty PIDs and Omitted \"address-types\" array.");
        compareEqualNetworkMap(networkMapList,NetworkMapList,reasonList);


        pids.clear();
        address_types.clear();
        altoClientNetworkMap=new AltoClientNetworkMap(argsList.get(0),argsList.get(1),pids,address_types);
        altoClientNetworkMap.getInfo(networkMapInfo);
        networkMapList=networkMapInfo.getNetworkMapList();
        reasonList.add("result 1.1."+stratID+" A----- 4.1.B Filtered Network Map Tests."+"Empty PIDs and Empty \"address-types\" array.");
        compareEqualNetworkMap(networkMapList,NetworkMapList,reasonList);
        stratID++;


        getFilteredNetworkMap(NetworkMapList,filteredNetworkMapList,"ipv6",null);

        reasonList.add("");
        pids.clear();
        address_types.clear();
        address_types.add("ipv6");
        altoClientNetworkMap=new AltoClientNetworkMap(argsList.get(0),argsList.get(1),pids,address_types);
        altoClientNetworkMap.getInfo(networkMapInfo);
        networkMapList=networkMapInfo.getNetworkMapList();
        reasonList.add("result 1.1."+stratID+" ------ 4.1.C Filtered Network Map Tests."+"Empty PIDs and IPV6 address only.");
        bFlag=false;
        compareEqualNetworkMap(networkMapList,filteredNetworkMapList,reasonList);
        stratID++;

        reasonList.add("");
        pids.clear();
        for(String loop:invalidPidList)
        {
            pids.add(loop);
        }
        altoClientNetworkMap=new AltoClientNetworkMap(argsList.get(0),argsList.get(1),invalidPidList,null);
        altoClientNetworkMap.getInfo(networkMapInfo);
        networkMapList=networkMapInfo.getNetworkMapList();
        reasonList.add("result 1.1."+stratID+" ------4.1.D Filtered Network Map Tests."+"PIDs array with one or more non-existent PID names array.");
        if(networkMapList.size()>0)
        {
            for(NetworkMap loop:networkMapList)
                reasonList.add("\tError: return PIDs."+ loop.toStringLinked());
        }
        else
        {
            reasonList.add("\tresult 1.1."+stratID+" ------The test server does not return any PIDs.");
        }
        stratID++;

        getFilteredNetworkMap(NetworkMapList,filteredNetworkMapList,"getSubset",validPidList);
        reasonList.add("");
        pids.clear();
        for(String loop:invalidPidList)
        {
            pids.add(loop);
        }
        for(String loop:validPidList)
        {
            pids.add(loop);
        }
        altoClientNetworkMap=new AltoClientNetworkMap(argsList.get(0),argsList.get(1),pids,null);
        altoClientNetworkMap.getInfo(networkMapInfo);
        networkMapList=networkMapInfo.getNetworkMapList();
        NetworkMapList.clear();
        reasonList.add("result 1.1."+stratID+" ------4.1.E Filtered Network Map Tests."+"PIDs array with one or more non-existent PID names array.");
        compareEqualNetworkMap(networkMapList,filteredNetworkMapList,reasonList);
        return true;
    }

    public static boolean compareCostMapItem(CostMap aCostMap,CostMap bCostMap)
    {
        String aSrc=aCostMap.getSrcPID();
        String aDst=aCostMap.getDstPID();
        double aCost=aCostMap.getCost();

        String bSrc=bCostMap.getSrcPID();
        String bDst=bCostMap.getDstPID();
        double bCost=bCostMap.getCost();

        if((aSrc.intern()==bSrc.intern()) && (aDst.intern()==bDst.intern()) && (aCost == bCost))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean compareEqualCostMap(List<CostMap> aCostMapList,List<CostMap> bCostMapList,List<String> reasonList)
    {
        boolean bFlag=false;
        boolean bcompare=false;

        for(CostMap aLoop:aCostMapList)
        {
            bFlag=false;
            for(CostMap bLoop :bCostMapList)
            {
                bcompare=compareCostMapItem(aLoop,bLoop);
                if(bcompare==true)
                {
                    bFlag=true;
                    break;
                }
            }
            if(bFlag==false)
            {
                reasonList.add("Error:There is an additional cost map item.\t" + aLoop +".");
            }
        }

        for(CostMap bLoop:bCostMapList)
        {
            bFlag=false;
            for(CostMap aLoop :aCostMapList)
            {
                bcompare=compareCostMapItem(aLoop,bLoop);
                if(bcompare==true)
                {
                    bFlag=true;
                    break;
                }
            }
            if(bFlag==false)
            {
                reasonList.add("Error:There is an absent cost map item.\t" + bLoop +".");
            }
        }
        return true;
    }


    public static boolean filteredCostMapTest(List<String> argsList,List<String> reasonList,String mapID, String metric,List<String> constraintList)
    {
        int stratID=20;
        reasonList.clear();
        boolean bFlag=false;

        List<CostMap> CostMapList=new ArrayList<CostMap>();
        List<CostMap> CostMapListFiltered=new ArrayList<CostMap>();
        List<String> srcPids=new ArrayList<String>();
        List<String> dstPids=new ArrayList<String>();
        //List<String> constraints=new ArrayList<String>();
        List<CostMap> costMapList;
        List<String> conditions=new ArrayList<String>();


        CostType costType;
        AltoClientCostMapSwapInfo costMapInfo=new AltoClientCostMapSwapInfo();
        AltoClientCostMap altoClientCostMap;
        CostType postCostType=new CostType();

        postCostType.setName("invalid");
        postCostType.setMode("numerical");
        postCostType.setMetric(metric);
        int mapSelectID=0;

        if(mapID.intern()=="default".intern())
        {
            if(metric.intern()=="routingcost".intern())
            {
                mapSelectID=0;
            }
            else
            {
                mapSelectID=1;
            }
        }
        else
        {
            if(metric.intern()=="routingcost".intern())
            {
                mapSelectID=2;
            }
            else
            {
                mapSelectID=3;
            }
        }

        if(mapID.intern()=="default".intern())
        {
            if(metric.intern()=="routingcost".intern())
            {
                stratID=20;
            }
            else
            {
                stratID=26;
            }
        }
        else
        {
            if(metric.intern()=="routingcost".intern())
            {
                stratID=32;
            }
            else
            {
                stratID=38;
            }
        }

        reasonList.add("4.2. Filtered Cost Map Tests. "+mapID +" map, "+metric+".");
        CostMapList.clear();
        switch (mapSelectID){
        case 0:generateDefaultRoutingcostMap(CostMapList);break;
        case 1:generateDefaultHopcountMap(CostMapList);break;
        case 2:generateAlternateRoutingcostMap(CostMapList);break;
        case 3:generateAlternateHopcountMap(CostMapList);break;

        default:generateDefaultRoutingcostMap(CostMapList);break;
        }

        reasonList.add("result 1.1."+stratID+"----4.2.A Filtered Cost Map Tests.Empty \"srcs\" and \"dsts\" arrays");

        srcPids.clear();
        dstPids.clear();
        conditions.clear();
        altoClientCostMap=new AltoClientCostMap(argsList.get(0),argsList.get(1),postCostType,srcPids,dstPids,conditions);
        altoClientCostMap.getInfo(costMapInfo);
        costMapList=costMapInfo.getCostMapList();
        compareEqualCostMap(costMapList,CostMapList,reasonList);
        stratID++;

        CostMapList.clear();
        CostMapListFiltered.clear();
        conditions.clear();
        conditions.add("default");
        switch (mapSelectID){
        case 0:generateDefaultRoutingcostMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getDst", conditions);break;
        case 1:generateDefaultHopcountMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getDst", conditions);break;
        case 2:generateAlternateRoutingcostMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getDst", conditions);break;
        case 3:generateAlternateHopcountMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getDst", conditions);break;

        default:generateDefaultRoutingcostMap(CostMapList);conditions.add("default");generateCostMapList(CostMapList,CostMapListFiltered,"getDst", conditions);break;
        }

        reasonList.add("result 1.1."+stratID+"----4.2.B Filtered Cost Map Tests.Empty \"srcs\" \"dsts\" arrays with valid PIDs");
        srcPids.clear();
        dstPids.clear();
        dstPids.add("default");
        conditions.clear();
        altoClientCostMap=new AltoClientCostMap(argsList.get(0),argsList.get(1),postCostType,srcPids,dstPids,conditions);
        altoClientCostMap.getInfo(costMapInfo);
        costMapList=costMapInfo.getCostMapList();
        compareEqualCostMap(costMapList,CostMapListFiltered,reasonList);
        stratID++;

        CostMapList.clear();
        CostMapListFiltered.clear();
        conditions.clear();
        conditions.add("default");
        switch (mapSelectID){
        case 0:generateDefaultRoutingcostMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getSrc", conditions);break;
        case 1:generateDefaultHopcountMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getSrc", conditions);break;
        case 2:generateAlternateRoutingcostMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getSrc", conditions);break;
        case 3:generateAlternateHopcountMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getSrc", conditions);break;

        default:generateDefaultRoutingcostMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getSrc", conditions);break;
        }

        reasonList.add("result 1.1."+stratID+"----4.2.C Filtered Cost Map Tests.Empty \"dsts\" \"srcs\" arrays with valid PIDs");
        srcPids.clear();
        dstPids.clear();
        conditions.clear();
        srcPids.add("default");
        altoClientCostMap=new AltoClientCostMap(argsList.get(0),argsList.get(1),postCostType,srcPids,dstPids,conditions);
        altoClientCostMap.getInfo(costMapInfo);
        costMapList=costMapInfo.getCostMapList();
        compareEqualCostMap(costMapList,CostMapListFiltered,reasonList);
        stratID++;


        reasonList.add("result 1.1."+stratID+"----4.2.D Filtered Cost Map Tests.Both \"dsts\" \"srcs\" arrays with invalid PIDs");
        costMapList.clear();
        srcPids.clear();
        dstPids.clear();
        conditions.clear();
        srcPids.add("invaliddefault");
        dstPids.add("invaliddefault");
        altoClientCostMap=new AltoClientCostMap(argsList.get(0),argsList.get(1),postCostType,srcPids,dstPids,conditions);
        altoClientCostMap.getInfo(costMapInfo);
        costMapList=costMapInfo.getCostMapList();
        if(costMapList.size()==0)
        {
            reasonList.add("The tested server returns an empty cost map");
        }
        else
        {
            reasonList.add("Error:The tested server returns an non-empty cost map");
        }
        stratID++;

        reasonList.add("result 1.1."+stratID+"----4.2.E Filtered Cost Map Tests.Both \"dsts\" \"srcs\" arrays with invalid  and valid PIDs");
        costMapList.clear();
        srcPids.clear();
        dstPids.clear();
        conditions.clear();
        srcPids.add("invaliddefault");srcPids.add("linklocal");
        dstPids.add("linklocal");
        altoClientCostMap=new AltoClientCostMap(argsList.get(0),argsList.get(1),postCostType,srcPids,dstPids,conditions);
        altoClientCostMap.getInfo(costMapInfo);
        costMapList=costMapInfo.getCostMapList();
        if(costMapList.size()==1)
        {
            if((costMapList.get(0).getSrcPID().intern()=="linklocal") && ((costMapList.get(0).getDstPID().intern()=="linklocal")))
            {
                reasonList.add("The tested server returns cost values only for valid PIDs");
            }
            else
            {
                for(CostMap loop:costMapList)
                    reasonList.add("Error:The tested server returns a cost value. "+loop.toStringLinked());
            }

        }
        else
        {
            if(costMapList.size()==0)
            {
                reasonList.add("Error:The tested server returns no cost value. But there should be one. ");
            }
            else{
                for(CostMap loop:costMapList)
                    reasonList.add("Error:The tested server returns a cost value. "+loop.toStringLinked());
            }
        }
        stratID++;

        CostMapList.clear();
        conditions.clear();
        for(String loop:constraintList)
        {
            conditions.add(loop);
        }
        switch (mapSelectID){
        case 0:generateDefaultRoutingcostMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getConstraintCost", conditions);break;
        case 1:generateDefaultHopcountMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getConstraintCost", conditions);break;
        case 2:generateAlternateRoutingcostMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getConstraintCost", conditions);break;
        case 3:generateAlternateHopcountMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getConstraintCost", conditions);break;

        default:generateDefaultRoutingcostMap(CostMapList);generateCostMapList(CostMapList,CostMapListFiltered,"getConstraintCost", conditions);break;
        }

        reasonList.add("result 1.1."+stratID+"----4.2.F Filtered Cost Map Tests.Empty \"dsts\" \"srcs\" arrays and  constraints of "+ constraintList+ ".");
        srcPids.clear();
        dstPids.clear();
        altoClientCostMap=new AltoClientCostMap(argsList.get(0),argsList.get(1),postCostType,srcPids,dstPids,conditions);
        altoClientCostMap.getInfo(costMapInfo);
        costMapList=costMapInfo.getCostMapList();
        compareEqualCostMap(costMapList,CostMapListFiltered,reasonList);

        return true;
    }

    public static boolean generateDefaultEPPMap(List<EPProp> EPPMapList,String type)
    {
        EPPMapList.clear();
        if(type.contains("default")){

        EPPMapList.add(new EPProp("ipv4:0.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:10.1.2.3","default","private"));
        EPPMapList.add(new EPProp("ipv4:100.0.0.1","default","mine1"));
        EPPMapList.add(new EPProp("ipv4:100.0.1.1","default","mine1a"));
        EPPMapList.add(new EPProp("ipv4:100.0.192.1","default","mine1a"));
        EPPMapList.add(new EPProp("ipv4:100.0.64.1","default","mine1a"));
        EPPMapList.add(new EPProp("ipv4:100.130.0.1","default","mine3"));
        EPPMapList.add(new EPProp("ipv4:100.200.0.1","default","mine"));
        EPPMapList.add(new EPProp("ipv4:100.75.0.1","default","mine2"));
        EPPMapList.add(new EPProp("ipv4:101.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:101.1.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:102.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:103.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:104.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:127.0.0.1","default","loopback"));
        EPPMapList.add(new EPProp("ipv4:127.255.255.255","default","loopback"));
        EPPMapList.add(new EPProp("ipv4:128.0.0.1","default","peer1"));
        EPPMapList.add(new EPProp("ipv4:129.0.0.1","default","peer2"));
        EPPMapList.add(new EPProp("ipv4:130.0.0.1","default","peer1"));
        EPPMapList.add(new EPProp("ipv4:131.0.0.1","default","peer2"));
        EPPMapList.add(new EPProp("ipv4:132.0.0.1","default","tran1"));
        EPPMapList.add(new EPProp("ipv4:135.0.0.1","default","tran2"));
        EPPMapList.add(new EPProp("ipv4:169.254.1.2","default","linklocal"));
        EPPMapList.add(new EPProp("ipv4:201.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:201.1.2.3","default","default"));
        EPPMapList.add(new EPProp("ipv4:202.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:203.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:204.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv4:99.0.0.1","default","default"));
        EPPMapList.add(new EPProp("ipv6:::1","default","loopback"));
        EPPMapList.add(new EPProp("ipv6:::2","default","default"));
        EPPMapList.add(new EPProp("ipv6:2001:DB8::","default","peer1"));
        EPPMapList.add(new EPProp("ipv6:2001:DB8:8000::1","default","peer2"));
        EPPMapList.add(new EPProp("ipv6:FC00:1::","default","private"));
        EPPMapList.add(new EPProp("ipv6:FF80:1:2::","default","linklocal"));
        }
        if(type.contains("alternate")){
        EPPMapList.add(new EPProp("ipv4:0.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:10.1.2.3","alternate","private"));
        EPPMapList.add(new EPProp("ipv4:100.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:100.0.1.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:100.0.192.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:100.0.64.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:100.130.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:100.200.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:100.75.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:101.0.0.1","alternate","dc1"));
        EPPMapList.add(new EPProp("ipv4:101.1.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:102.0.0.1","alternate","dc2"));
        EPPMapList.add(new EPProp("ipv4:103.0.0.1","alternate","dc3"));
        EPPMapList.add(new EPProp("ipv4:104.0.0.1","alternate","dc4"));
        EPPMapList.add(new EPProp("ipv4:127.0.0.1","alternate","loopback"));
        EPPMapList.add(new EPProp("ipv4:127.255.255.255","alternate","loopback"));
        EPPMapList.add(new EPProp("ipv4:128.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:129.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:130.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:131.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:132.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:135.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:169.254.1.2","alternate","linklocal"));
        EPPMapList.add(new EPProp("ipv4:201.0.0.1","alternate","user1"));
        EPPMapList.add(new EPProp("ipv4:201.1.2.3","alternate","default"));
        EPPMapList.add(new EPProp("ipv4:202.0.0.1","alternate","user2"));
        EPPMapList.add(new EPProp("ipv4:203.0.0.1","alternate","user3"));
        EPPMapList.add(new EPProp("ipv4:204.0.0.1","alternate","user4"));
        EPPMapList.add(new EPProp("ipv4:99.0.0.1","alternate","default"));
        EPPMapList.add(new EPProp("ipv6:::1","alternate","loopback"));
        EPPMapList.add(new EPProp("ipv6:::2","alternate","default"));
        EPPMapList.add(new EPProp("ipv6:2001:DB8::","alternate","default"));
        EPPMapList.add(new EPProp("ipv6:2001:DB8:8000::1","alternate","default"));
        EPPMapList.add(new EPProp("ipv6:FC00:1::","alternate","private"));
        EPPMapList.add(new EPProp("ipv6:FF80:1:2::","alternate","linklocal"));
        }
        if(type.contains("ietf")){

        //EPPMapList.add(new EPProp("ipv4:0.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:10.1.2.3","priv:ietf-type","invalid"));
        EPPMapList.add(new EPProp("ipv4:100.0.0.1","priv:ietf-type","mine"));
        EPPMapList.add(new EPProp("ipv4:100.0.1.1","priv:ietf-type","mine"));
        EPPMapList.add(new EPProp("ipv4:100.0.192.1","priv:ietf-type","mine"));
        EPPMapList.add(new EPProp("ipv4:100.0.64.1","priv:ietf-type","mine"));
        EPPMapList.add(new EPProp("ipv4:100.130.0.1","priv:ietf-type","mine"));
        EPPMapList.add(new EPProp("ipv4:100.200.0.1","priv:ietf-type","mine"));
        EPPMapList.add(new EPProp("ipv4:100.75.0.1","priv:ietf-type","mine"));
        //EPPMapList.add(new EPProp("ipv4:101.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:101.1.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:102.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:103.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:104.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:127.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:127.255.255.255","priv:ietf-type","invalid"));
        EPPMapList.add(new EPProp("ipv4:128.0.0.1","priv:ietf-type","peer"));
        EPPMapList.add(new EPProp("ipv4:129.0.0.1","priv:ietf-type","peer"));
        EPPMapList.add(new EPProp("ipv4:130.0.0.1","priv:ietf-type","peer"));
        EPPMapList.add(new EPProp("ipv4:131.0.0.1","priv:ietf-type","peer"));
        EPPMapList.add(new EPProp("ipv4:132.0.0.1","priv:ietf-type","transit"));
        EPPMapList.add(new EPProp("ipv4:135.0.0.1","priv:ietf-type","transit"));
        //EPPMapList.add(new EPProp("ipv4:169.254.1.2","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:201.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:201.1.2.3","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:202.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:203.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:204.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv4:99.0.0.1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv6:::1","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv6:::2","priv:ietf-type","invalid"));
        EPPMapList.add(new EPProp("ipv6:2001:DB8::","priv:ietf-type","peer"));
        EPPMapList.add(new EPProp("ipv6:2001:DB8:8000::1","priv:ietf-type","peer"));
        //EPPMapList.add(new EPProp("ipv6:fc00:1::","priv:ietf-type","invalid"));
        //EPPMapList.add(new EPProp("ipv6:ff80:1:2::","priv:ietf-type","invalid"));
        }
        return true;
    }

    public static boolean generateDefaulEPPMapIP(List<String> IPList)
    {
        IPList.clear();

        IPList.add("ipv4:0.0.0.1");
        IPList.add("ipv4:10.1.2.3");
        IPList.add("ipv4:100.0.0.1");
        IPList.add("ipv4:100.0.1.1");
        IPList.add("ipv4:100.0.192.1");
        IPList.add("ipv4:100.0.64.1");
        IPList.add("ipv4:100.130.0.1");
        IPList.add("ipv4:100.200.0.1");
        IPList.add("ipv4:100.75.0.1");
        IPList.add("ipv4:101.0.0.1");
        IPList.add("ipv4:101.1.0.1");
        IPList.add("ipv4:102.0.0.1");
        IPList.add("ipv4:103.0.0.1");
        IPList.add("ipv4:104.0.0.1");
        IPList.add("ipv4:127.0.0.1");
        IPList.add("ipv4:127.255.255.255");
        IPList.add("ipv4:128.0.0.1");
        IPList.add("ipv4:129.0.0.1");
        IPList.add("ipv4:130.0.0.1");
        IPList.add("ipv4:131.0.0.1");
        IPList.add("ipv4:132.0.0.1");
        IPList.add("ipv4:135.0.0.1");
        IPList.add("ipv4:169.254.1.2");
        IPList.add("ipv4:201.0.0.1");
        IPList.add("ipv4:201.1.2.3");
        IPList.add("ipv4:202.0.0.1");
        IPList.add("ipv4:203.0.0.1");
        IPList.add("ipv4:204.0.0.1");
        IPList.add("ipv4:99.0.0.1");
        IPList.add("ipv6:::1");
        IPList.add("ipv6:::2");
        IPList.add("ipv6:2001:DB8::");
        IPList.add("ipv6:2001:DB8:8000::1");
        IPList.add("ipv6:FC00:1::");
        IPList.add("ipv6:FF80:1:2::");

        return true;
    }

    public static boolean compareEPPMapItem(EPProp aEPProp,EPProp bEPProp)
    {
        String aPid=aEPProp.getEndPointID();
        String aProperties=aEPProp.getProperties();
        String aValue=aEPProp.getPropertiesValue();

        String bPid=bEPProp.getEndPointID();
        String bProperties=bEPProp.getProperties();
        String bValue=bEPProp.getPropertiesValue();

        if((aPid.intern()==bPid.intern()) && (aProperties.intern()==bProperties.intern()) && (aValue.intern() == bValue.intern()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean compareEPPropMap(List<EPProp> aEPPropList,List<EPProp> bEPPropList,List<String> reasonList)
    {
        boolean bFlag=false;
        boolean bcompare=false;
        //System.out.println(aEPPropList);      System.out.println(bEPPropList);
        for(EPProp aLoop:aEPPropList)
        {
            bFlag=false;
            for(EPProp bLoop :bEPPropList)
            {
                bcompare=compareEPPMapItem(aLoop,bLoop);
                if(bcompare==true)
                {
                    bFlag=true;
                    break;
                }
            }
            if(bFlag==false)
            {
                reasonList.add("Error:There is an additional Endpoint Properties map item.\t" + aLoop +".");
            }
        }

        for(EPProp bLoop:bEPPropList)
        {
            bFlag=false;
            for(EPProp aLoop :aEPPropList)
            {
                bcompare=compareEPPMapItem(aLoop,bLoop);
                if(bcompare==true)
                {
                    bFlag=true;
                    break;
                }
            }
            if(bFlag==false)
            {
                reasonList.add("Error:There is an absent Endpoint Properties item.\t" + bLoop +".");
            }
        }
        return true;
    }


    public static boolean EndPropertiesMapTest(List<String> argsList,List<String> reasonList)
    {
        boolean bFlag=false;
        reasonList.clear();
        //first get what properties.
        List<String> propertiesTested=new ArrayList<String>();
        for(int i=2;i<argsList.size();i++)
        {

            propertiesTested.add(argsList.get(i));
        }
        for(String loop:propertiesTested)
        {
            reasonList.add("The tested ALTO server does provide Endpoint Properties service with "+loop+".");
            List<EPProp> endPointPropList;
            String operationStatus;
            AltoClientEPPropSwapInfo EPPInfo=new AltoClientEPPropSwapInfo();
            AltoClientEndPointProp altoClientEndPointProp;
            List<String> properties=new ArrayList<String>();
            List<String> endpoints=new ArrayList<String>();

            generateDefaulEPPMapIP(endpoints);
            properties.add(loop);
            altoClientEndPointProp=new AltoClientEndPointProp(argsList.get(0),argsList.get(1),properties,endpoints);
            altoClientEndPointProp.getInfo(EPPInfo);
            endPointPropList=EPPInfo.getEndPointPropList();
            operationStatus=EPPInfo.getOperationStatus();
            String propType="invalid";
            //ipv4:128.0.0.1 peer1 default peer
            if(endPointPropList.size()>0)
            {
                bFlag=false;
                for(EPProp aLoop:endPointPropList)
                {
                    if((aLoop.getEndPointID().intern()=="ipv4:128.0.0.1".intern()) && (aLoop.getPropertiesValue().intern()=="peer1".intern()))
                    {
                        propType="default";
                        bFlag=true;
                        break;
                    }
                    if((aLoop.getEndPointID().intern()=="ipv4:128.0.0.1".intern()) && (aLoop.getPropertiesValue().intern()=="default".intern()))
                    {
                        propType="alternate";
                        bFlag=true;
                        break;
                    }
                    if((aLoop.getEndPointID().intern()=="ipv4:128.0.0.1".intern()) && (aLoop.getPropertiesValue().intern()=="peer".intern()))
                    {
                        propType="priv:ietf-type";
                        bFlag=true;
                        break;
                    }
                }

                for(EPProp aLoop:endPointPropList)
                {
                    aLoop.setProperties(propType);
                }

                List<EPProp> EndPointPropList=new ArrayList<EPProp>();
                generateDefaultEPPMap(EndPointPropList,propType);
                compareEPPropMap(endPointPropList,EndPointPropList,reasonList);
            }
        }
        return true;
    }


    public static boolean generateECS441(List<EndPointCost> costMapList,String type)
    {
        costMapList.clear();

        if(type.contains("routingcost"))
        {
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.0.0.100", 0.0 ));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.8.1.100", 0.0 ));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.0.1.100", 2.0 ));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.64.0.100", 6.5 ));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.128.4.100", 8.0 ));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:130.0.1.100", 23.0 ));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:132.0.8.100", 38.0 ));

            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.0.0.100", 8.0 ));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.8.1.100", 8.0 ));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.0.1.100", 10.0 ));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.64.0.100", 9.0 ));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.128.4.100", 0.0 ));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:130.0.1.100", 25.0 ));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:132.0.8.100", 40.0 ));
        }
        if(type.contains("hopcount"))
        {
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.0.0.100",  0));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.8.1.100",  0));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.0.1.100",  2));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.64.0.100",  3));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:100.128.4.100",  3));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:130.0.1.100",  4));
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","ipv4:132.0.8.100",  5));

            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.0.0.100",  3));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.8.1.100",  3));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.0.1.100",  4));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.64.0.100",  3));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:100.128.4.100",  0));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:130.0.1.100",  4));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","ipv4:132.0.8.100", 5));
        }

        return true;
    }

    public static boolean generateECS441IP(List<String> srcList,List<String> dstList)
    {
        srcList.clear();
        dstList.clear();

        srcList.add("ipv4:100.0.0.128");
        srcList.add("ipv4:100.131.39.11");

        dstList.add("ipv4:100.0.0.100");
        dstList.add("ipv4:100.8.1.100");
        dstList.add("ipv4:100.0.1.100");
        dstList.add("ipv4:100.64.0.100");
        dstList.add("ipv4:100.128.4.100");
        dstList.add("ipv4:130.0.1.100");
        dstList.add("ipv4:132.0.8.100");

        return true;
    }
    public static boolean generateECS442(List<EndPointCost> costMapList,String type)
    {
        costMapList.clear();

        if(type.contains("routingcost"))
        {
            costMapList.add(new EndPointCost("ipv4:10.0.1.0","ipv4:10.0.1.1", 0.0 ));
            //costMapList.add(new EndPointCost("ipv4:10.0.1.0","ipv6:::1:2", 75.0 ));
            //costMapList.add(new EndPointCost("ipv6:::2","ipv4:10.0.1.1", 75.0 ));
            //costMapList.add(new EndPointCost("ipv6:::2","ipv6:::1:2", 1.0));
        }
        if(type.contains("hopcount"))
        {
            costMapList.add(new EndPointCost("ipv4:10.0.1.0","ipv4:10.0.1.1", 0));
            //costMapList.add(new EndPointCost("ipv4:10.0.1.0","ipv6:::1:2",  10));
            //costMapList.add(new EndPointCost("ipv6:::2","ipv4:10.0.1.1",  10));
            //costMapList.add(new EndPointCost("ipv6:::2","ipv6:::1:2",  1));
        }

        return true;
    }

    public static boolean generateECS442IP(List<String> srcList,List<String> dstList)
    {
        srcList.clear();
        dstList.clear();

        srcList.add("ipv4:10.0.1.0");
        srcList.add("ipv6:::2");

        dstList.add("ipv4:10.0.1.1");
        dstList.add("ipv6:::1:2");

        return true;
    }

    public static boolean generateECS443(List<EndPointCost> costMapList,String type)
    {
        costMapList.clear();

        if(type.contains("routingcost"))
        {
            costMapList.add(new EndPointCost("ipv4:127.0.0.1","ipv4:127.0.1.0", 0.0 ));
            costMapList.add(new EndPointCost("ipv6:::1","ipv4:127.0.1.0", 0.0 ));
        }
        if(type.contains("hopcount"))
        {
            costMapList.add(new EndPointCost("ipv4:127.0.0.1","ipv4:127.0.1.0", 0));
            costMapList.add(new EndPointCost("ipv6:::1","ipv4:127.0.1.0", 0));
        }

        return true;
    }
    public static boolean generateECS443IP(List<String> srcList,List<String> dstList)
    {
        srcList.clear();
        dstList.clear();

        srcList.add("ipv4:127.0.0.1");
        srcList.add("ipv6:::1");

        dstList.add("ipv4:127.0.1.0");

        return true;
    }

    public static boolean generateECS444D(List<EndPointCost> costMapList,String type)
    {
        costMapList.clear();

        if(type.contains("routingcost"))
        {
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","default", 63.0));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","default",65.0));
            costMapList.add(new EndPointCost("ipv4:100.200.0.1","default", 60.0));
            //costMapList.add(new EndPointCost("ipv4:0.0.0.1","default", 1.0));
            //costMapList.add(new EndPointCost("ipv4:10.0.0.1","default", 75.0));
            //costMapList.add(new EndPointCost("ipv6:::2","default", 1.0));
            //costMapList.add(new EndPointCost("ipv6:FC01::","default", 1.0));
        }
        if(type.contains("hopcount"))
        {
            costMapList.add(new EndPointCost("ipv4:100.0.0.128","default", 4));
            costMapList.add(new EndPointCost("ipv4:100.131.39.11","default",4));
            costMapList.add(new EndPointCost("ipv4:100.200.0.1","default", 3));
            //costMapList.add(new EndPointCost("ipv4:0.0.0.1","default", 1.0));
            //costMapList.add(new EndPointCost("ipv4:10.0.0.1","default", 75.0));
            //costMapList.add(new EndPointCost("ipv6:::2","default", 1.0));
            //costMapList.add(new EndPointCost("ipv6:FC01::","default", 1.0));
        }

        return true;
    }

    public static boolean generateECS444P(List<EndPointCost> costMapList,String type)
    {
        costMapList.clear();

        if(type.contains("routingcost"))
        {
            costMapList.add(new EndPointCost("ipv4:0.0.0.1","private", 0.0));
            costMapList.add(new EndPointCost("ipv6:FC01::","private", 0.0));
        }
        if(type.contains("hopcount"))
        {
            costMapList.add(new EndPointCost("ipv4:0.0.0.1","private", 0));
            costMapList.add(new EndPointCost("ipv6:FC01::","private", 0));
        }

        return true;
    }

    public static boolean generateECS444IP(List<String> srcList,List<String> dstList)
    {
        srcList.clear();
        dstList.clear();

        srcList.add("ipv4:100.0.0.128");
        srcList.add("ipv4:100.131.39.11");
        srcList.add("ipv4:100.200.0.1");
        srcList.add("ipv4:0.0.0.1");
        srcList.add("ipv4:10.0.0.1");
        srcList.add("ipv6:::2");
        srcList.add("ipv6:FC01::");

        return true;
    }

    public static boolean generateECS445D(List<EndPointCost> costMapList,String type)
    {
        costMapList.clear();

        if(type.contains("routingcost"))
        {
            costMapList.add(new EndPointCost("default","ipv4:100.0.0.128",63.0));
            costMapList.add(new EndPointCost("default","ipv4:100.131.39.11",65.0));
            costMapList.add(new EndPointCost("default","ipv4:100.200.0.1",60.0));
        }
        if(type.contains("hopcount"))
        {
            costMapList.add(new EndPointCost("default","ipv4:100.0.0.128",4.0));
            costMapList.add(new EndPointCost("default","ipv4:100.131.39.11",4.0));
            costMapList.add(new EndPointCost("default","ipv4:100.200.0.1",3.0));
        }

        return true;
    }

    public static boolean generateECS445P(List<EndPointCost> costMapList,String type)
    {
        costMapList.clear();

        if(type.contains("routingcost"))
        {
            costMapList.add(new EndPointCost("default","ipv4:10.0.0.1",0.0));
            costMapList.add(new EndPointCost("default","ipv6:FC01::",0.0));
        }
        if(type.contains("hopcount"))
        {
            costMapList.add(new EndPointCost("default","ipv4:10.0.0.1",0.0));
            costMapList.add(new EndPointCost("default","ipv6:FC01::",0.0));
        }

        return true;
    }

    public static boolean generateECS445IP(List<String> srcList,List<String> dstList)
    {
        srcList.clear();
        dstList.clear();

        dstList.add("ipv4:100.0.0.128");
        dstList.add("ipv4:100.131.39.11");
        dstList.add("ipv4:100.200.0.1");
        dstList.add("ipv4:0.0.0.1");
        dstList.add("ipv4:10.0.0.1");
        dstList.add("ipv6:::2");
        dstList.add("ipv6:FC01::");

        return true;
    }

    public static boolean compareECSMapItem(EndPointCost aEndPointCost,EndPointCost bEndPointCost)
    {
        String aSrcEP=aEndPointCost.getSrcEP();
        String aDstEP=aEndPointCost.getDstEP();
        double aCostValue=aEndPointCost.getCost();

        String bSrcEP=bEndPointCost.getSrcEP();
        String bDstEP=bEndPointCost.getDstEP();
        double bCostValue=bEndPointCost.getCost();

        if((aSrcEP.intern()==bSrcEP.intern()) && (aDstEP.intern()==bDstEP.intern()) && (aCostValue == bCostValue))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public static boolean compareECSMap(List<EndPointCost> aEndPointCostList,List<EndPointCost> bEndPointCostList,List<String> reasonList)
    {
        boolean bFlag=false;
        boolean bcompare=false;

        for(EndPointCost aLoop:aEndPointCostList)
        {
            bFlag=false;
            for(EndPointCost bLoop :bEndPointCostList)
            {
                bcompare=compareECSMapItem(aLoop,bLoop);
                if(bcompare==true)
                {
                    bFlag=true;
                    break;
                }
            }
            if(bFlag==false)
            {
                reasonList.add("Error:There is an additional ECS item.\t" + aLoop +".");
            }
        }

        for(EndPointCost bLoop:bEndPointCostList)
        {
            bFlag=false;
            for(EndPointCost aLoop :aEndPointCostList)
            {
                bcompare=compareECSMapItem(aLoop,bLoop);
                if(bcompare==true)
                {
                    bFlag=true;
                    break;
                }
            }
            if(bFlag==false)
            {
                reasonList.add("Error:There is an absent ECS item.\t" + bLoop +".");
            }
        }
        return true;
    }
    public static boolean ECSMapTest(List<String> argsList,List<String> reasonList)
    {
        String routingcost="routingcost";
        String hopcount="hopcount";
        List<String> metricTypeList=new ArrayList<String>();
        for(int i=2;i<argsList.size();i++)
        {
            metricTypeList.add(argsList.get(i));
        }
        boolean bFlag=false;
        reasonList.clear();

        List<String> srcEPs=new ArrayList<String>();
        List<String> dstEPs=new ArrayList<String>();
        CostType postCostType=new CostType();
        postCostType.setName("invalid");
        postCostType.setMode("numerical");
        postCostType.setMetric("invalid");

        AltoClientEndPointCostSwapInfo endPointCostInfo=new AltoClientEndPointCostSwapInfo();
        AltoClientEndPointCost altoClientEndPointCost=null;
        List<EndPointCost> endPointCostList=null;
        List<EndPointCost> EndPointCostList=new ArrayList<EndPointCost>();



        for(String loop:metricTypeList)
        {
            reasonList.add("result 1.1.45 1.1.50----"+"4.4.1. ECS Test 1"+" .");
            postCostType.setMetric(loop);
            //postCostType.setMode(routingcost);
            reasonList.add("The tested server supports numerical "+loop+" test.");
            EndPointCostList.clear();
            generateECS441(EndPointCostList,loop);
            generateECS441IP( srcEPs, dstEPs);
            altoClientEndPointCost=new AltoClientEndPointCost(argsList.get(0),argsList.get(1),postCostType,srcEPs,dstEPs);
            altoClientEndPointCost.getInfo(endPointCostInfo);
            endPointCostList=endPointCostInfo.getEndPointCostList();
            compareECSMap(endPointCostList,EndPointCostList,reasonList);
        }
        reasonList.add("result 1.1.46 1.1.51----"+"4.4.2. ECS Test 2"+" .");
        for(String loop:metricTypeList)
        {
            postCostType.setMetric(loop);
            reasonList.add("The tested server supports numerical "+loop+" test.");
            EndPointCostList.clear();
            generateECS442(EndPointCostList,loop);
            generateECS442IP( srcEPs, dstEPs);
            altoClientEndPointCost=new AltoClientEndPointCost(argsList.get(0),argsList.get(1),postCostType,srcEPs,dstEPs);
            altoClientEndPointCost.getInfo(endPointCostInfo);
            endPointCostList=endPointCostInfo.getEndPointCostList();
            compareECSMap(endPointCostList,EndPointCostList,reasonList);
        }
        reasonList.add("result 1.1.47 1.1.52----"+"4.4.3. ECS Test 3"+" .");
        for(String loop:metricTypeList)
        {
            postCostType.setMetric(loop);
            reasonList.add("The tested server supports numerical "+loop+" test.");
            EndPointCostList.clear();
            generateECS443(EndPointCostList,loop);
            generateECS443IP( srcEPs, dstEPs);
            altoClientEndPointCost=new AltoClientEndPointCost(argsList.get(0),argsList.get(1),postCostType,srcEPs,dstEPs);
            altoClientEndPointCost.getInfo(endPointCostInfo);
            endPointCostList=endPointCostInfo.getEndPointCostList();
            compareECSMap(endPointCostList,EndPointCostList,reasonList);
        }


        //judge to which PID does client belong ,add this function later.


        reasonList.add("result 1.1.48 1.1.53----"+"4.4.4. ECS Test 4."+" IF Client in Default .");

        for(String loop:metricTypeList)
        {
            reasonList.add("attention:we may need to check manually whethea the client belongs to default.\n");
            postCostType.setMetric(loop);
            reasonList.add("The tested server supports numerical "+loop+" test.");
            EndPointCostList.clear();
            generateECS444D(EndPointCostList,loop);
            generateECS444IP( srcEPs, dstEPs);
            altoClientEndPointCost=new AltoClientEndPointCost(argsList.get(0),argsList.get(1),postCostType,srcEPs,dstEPs);
            altoClientEndPointCost.getInfo(endPointCostInfo);
            endPointCostList=endPointCostInfo.getEndPointCostList();
            boolean tmp=true;
            for(EndPointCost aLoop:endPointCostList)
            {
                if(tmp==true)
                    reasonList.add("result 1.1.48 1.1.53----"+" Client address is "+aLoop.getDstEP()+" .");
                tmp=false;
            }
            for(EndPointCost aLoop:endPointCostList)
            {
                aLoop.setDstEP("default");
            }
            compareECSMap(endPointCostList,EndPointCostList,reasonList);

        }
        reasonList.add("\n\n");
        reasonList.add("result 1.1.48 1.1.53----"+"4.4.4. ECS Test 4."+" IF Client in Private .");
        for(String loop:metricTypeList)
        {

            reasonList.add("attention:we may need to check manually whethea the client belongs to private.\n");
            postCostType.setMetric(loop);
            reasonList.add("The tested server supports numerical "+loop+" test.");
            EndPointCostList.clear();
            generateECS444P(EndPointCostList,loop);
            generateECS444IP( srcEPs, dstEPs);
            altoClientEndPointCost=new AltoClientEndPointCost(argsList.get(0),argsList.get(1),postCostType,srcEPs,dstEPs);
            altoClientEndPointCost.getInfo(endPointCostInfo);
            endPointCostList=endPointCostInfo.getEndPointCostList();
            boolean tmp=true;
            for(EndPointCost aLoop:endPointCostList)
            {
                if(tmp==true)
                    reasonList.add("result 1.1.48 1.1.53----"+"Client address is "+aLoop.getDstEP()+" .");
                tmp=false;
            }
            for(EndPointCost aLoop:endPointCostList)
            {
                aLoop.setDstEP("private");
            }
            compareECSMap(endPointCostList,EndPointCostList,reasonList);
        }
        reasonList.add("\n\n\n\n\n.");
        reasonList.add("result 1.1.49 1.1.54----"+"\n4.4.5. ECS Test 5"+"IF Client in Default .");
        for(String loop:metricTypeList)
        {
            reasonList.add("attention:we may need to check manually whethea the client belongs to default.\n");
            postCostType.setMetric(loop);
            reasonList.add("The tested server supports numerical "+loop+" test.");
            EndPointCostList.clear();
            generateECS445D(EndPointCostList,loop);
            generateECS445IP( srcEPs, dstEPs);
            altoClientEndPointCost=new AltoClientEndPointCost(argsList.get(0),argsList.get(1),postCostType,srcEPs,dstEPs);
            altoClientEndPointCost.getInfo(endPointCostInfo);
            endPointCostList=endPointCostInfo.getEndPointCostList();
            boolean tmp=true;
            for(EndPointCost aLoop:endPointCostList)
            {
                if(tmp==true)
                    reasonList.add("result 1.1.49 1.1.54----"+"Client address is "+aLoop.getDstEP()+" .");
                tmp=false;
            }
            for(EndPointCost aLoop:endPointCostList)
            {
                aLoop.setSrcEP("default");
            }
            compareECSMap(endPointCostList,EndPointCostList,reasonList);
        }
        reasonList.add("\n\n\n\n\n.");
        reasonList.add("result 1.1.49 1.1.54----"+"4.4.5. ECS Test 5"+"IF Client in Private .");
        for(String loop:metricTypeList)
        {
            reasonList.add("attention:we may need to check manually whethea the client belongs to private.\n");
            postCostType.setMetric(loop);
            reasonList.add("The tested server supports numerical "+loop+" test.");
            EndPointCostList.clear();
            generateECS445P(EndPointCostList,loop);
            generateECS445IP( srcEPs, dstEPs);
            altoClientEndPointCost=new AltoClientEndPointCost(argsList.get(0),argsList.get(1),postCostType,srcEPs,dstEPs);
            altoClientEndPointCost.getInfo(endPointCostInfo);
            endPointCostList=endPointCostInfo.getEndPointCostList();
            boolean tmp=true;
            for(EndPointCost aLoop:endPointCostList)
            {
                if(tmp==true)
                    reasonList.add("result 1.1.49 1.1.54----"+"Client address is "+aLoop.getDstEP()+" .");
                tmp=false;
            }
            for(EndPointCost aLoop:endPointCostList)
            {
                aLoop.setSrcEP("private");
            }
            compareECSMap(endPointCostList,EndPointCostList,reasonList);
        }
        return true;
    }


    public static List<String> Error5Test(String[] args)
    {
        List<String> retStringList=new ArrayList<String>();
        List<String> getInfoList=null;
        ErrorInterOpTest errorInterOpTest=null;
        /*System.out.println("begin to start test");
        for(String loop:args)
        {
            System.out.println(loop);
        }
        */
        String testOperation=args[0];

        if(testOperation.contains("59IAH"))
        {
            errorInterOpTest=new ErrorInterOpTest(args[1],args[2],args[3]);
            getInfoList=new ArrayList<String>();
            errorInterOpTest.getInfo(getInfoList);
            System.out.println(getInfoList);

        }
        else if(testOperation.contains("51IFT") || testOperation.contains("52MPF") || testOperation.contains("53IPN") || testOperation.contains("54IEA") || testOperation.contains("58JSE") || testOperation.contains("510IAH") || testOperation.contains("511ICT"))
        {
            int propertiesCnt=Integer.parseInt(args[3]);
            int EPCnt=Integer.parseInt(args[propertiesCnt+4]);
            int EPPos=propertiesCnt+4;

            List<String> EPs=new ArrayList<String>();
            List<String> properties=new ArrayList<String>();
            while(propertiesCnt > 0)
            {
                properties.add(args[3+propertiesCnt]);
                propertiesCnt=propertiesCnt-1;
            }
            while(EPCnt > 0)
            {
                EPs.add(args[EPCnt+EPPos]);
                EPCnt=EPCnt-1;
            }

            System.out.println("EPs "+EPs.toString());
            System.out.println("properties "+properties.toString());
            errorInterOpTest=new ErrorInterOpTest(args[0],args[1],args[2],properties,EPs);
            getInfoList=new ArrayList<String>();
            errorInterOpTest.getInfo(getInfoList);

        }
        else
        {
            CostType postCostType=new CostType();
            postCostType.setMetric(args[3]);
            postCostType.setMode(args[4]);
            List<String> srcPids=new ArrayList<String>();
            List<String> dstPids=new ArrayList<String>();
            List<String> constraints=new ArrayList<String>();
            constraints.add(args[5]);

            System.out.println("CostType"+postCostType.toString());
            System.out.println("constraints "+constraints.toString());

            if(args[0].contains("57ICC")){
                errorInterOpTest=new ErrorInterOpTest(args[0],args[1],args[2],postCostType,srcPids,dstPids,constraints);
            }
            else
            {
                errorInterOpTest=new ErrorInterOpTest(args[0],args[1],args[2],postCostType,srcPids,dstPids,null);
            }
            getInfoList=new ArrayList<String>();
            errorInterOpTest.getInfo(getInfoList);

        }
        //System.out.println("done");
        return getInfoList;

    }
    private static String expandFour(String src)
    {
        if(src.intern()=="".intern())
            return "";

        String ret="";
        String separator=":";
        boolean bFirst=true;
        while(src.contains(separator))
        {
            int pos=src.indexOf(separator);
            String firstPart=src.substring(0,pos);
            while(firstPart.length()<4)
                firstPart="0"+firstPart;
            if(bFirst==true)
            {
                ret=firstPart;
            }
            else
                ret=ret+":"+firstPart;
            src=src.substring(pos+1);
            bFirst=false;
        }
        String lastPart="";
        if(src.intern()!="".intern())
        {
            lastPart=src;
        }
        while(lastPart.length()<4)
            lastPart="0"+lastPart;
        if(bFirst==true)
            ret=lastPart;
        else
            ret=ret+":"+lastPart;

        return ret;
    }
    private static String convertIPV6Addressbank(String src)
    {


        String suffix="";
        int pos=src.indexOf("/");
        if(pos!=-1)
        {
            suffix=src.substring(pos);
            src=src.substring(0,pos);
        }
        if(suffix.intern()=="/128".intern())
        {
            suffix="";
        }
        if(src.length()>=39)
            return src+suffix;


        String srcA="";
        String srcB="";
        if(src.contains("::"))
        {
            pos=src.indexOf("::");
            if(pos>2)
            srcA=src.substring(0,pos);
            else srcA="";
            if(pos+2<=src.length())
                srcB=src.substring(pos+2);
            else
                srcB="";
        }
        else
            srcA=src;

        srcA=expandFour(srcA);
        srcB=expandFour(srcB);

        if((srcA.intern()=="".intern()) && (srcB.intern()=="".intern()))
        {
            srcB="0000:0000:0000:0000:0000:0000:0000:0000";
        }
        else if(srcA.intern()=="".intern())
        {
            while(srcB.length()+1<39)
            {
                srcB="0000:"+srcB;
            }
        }
        else if(srcB.intern()=="".intern())
        {
            while(srcA.length()+1<39)
            {
                srcA=srcA+":0000";
            }
        }
        else
        {
            while(srcA.length()+srcB.length()+1<39)
            {
                srcA=srcA+":0000";
            }

            srcA=srcA+":"+srcB;
        }

        String strRet;
        if(srcA.intern()=="".intern())
        {
            strRet=srcB+suffix;
        }
        else
        {
            strRet=srcA+suffix;
        }
        return strRet.toUpperCase();
    }

    private static String convertIPV6Network(String src)
    {
        return IPv6Network.fromString(src).toString();
    }

    private static String convertIPV6AddressA(String src)
    {
        return IPv6Network.fromString(src).toString();
    }
    //
    /**
     * @param args
     */
    public static void maina(String[] args)
    {
        String a="::1";

        String b="::1/128";
        String c="::0";
        //String c="::0/0";
        String d="2001:DB8:0000::/33";
        //String d="2001:DB8:0000::/33";
        //System.out.println(IPv6Network.fromString(a).toString());
        System.out.println(IPv6Network.fromString(b).toString());
        System.out.println(IPv6Address.fromString(c).toString());
        System.out.println(IPv6Network.fromString(d).toString());
    }
    //
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        List<String> hostNameRequestLine=new ArrayList<String>();
        hostNameRequestLine.clear();
        AltoClientConst.methodName(args[0],hostNameRequestLine);
        AltoClientIRD altoClientIRD=new AltoClientIRD(hostNameRequestLine.get(0),hostNameRequestLine.get(1));
        AltoClientIRDSwapInfo IRDInfo=new AltoClientIRDSwapInfo();
        altoClientIRD.getInfo(IRDInfo);

        //print data
        List<CostType> costTypeList=IRDInfo.getCostTypeList();
        String defaultNetworkMapName=IRDInfo.getDefaultAltoNetworkMap();
        String defaultNetworkMapUrl=null;
        String alternateNetworkMapName=null;
        String alternateNetworkMapUrl=null;
        List<Resource> resourceList=IRDInfo.getResourceList();
        String operationStatus=IRDInfo.getOperationStatus();

        String numericalRoutingcost=null;
        String numericalHopcount=null;
        String ordinalRoutingcost=null;
        String ordinalHopcount=null;

        String routingcost="routingcost";
        String hopcount="hopcount";
        String numerical="numerical";
        String ordinal="ordinal";
        boolean bFlag=false;

        String def_num_rtg_name=null;
        String def_ord_rtg_name=null;
        String def_num_hop_name=null;
        String def_ord_hop_name=null;
        String def_num_rtg_url=null;
        String def_ord_rtg_url=null;
        String def_num_hop_url=null;
        String def_ord_hop_url=null;

        String alt_num_rtg_name=null;
        String alt_ord_rtg_name=null;
        String alt_num_hop_name=null;
        String alt_ord_hop_name=null;
        String alt_num_rtg_url=null;
        String alt_ord_rtg_url=null;
        String alt_num_hop_url=null;
        String alt_ord_hop_url=null;

        String defNetworkMapFilteredName=null;
        String defNetworkMapFilteredNameUrl=null;
        String altNetworkMapFilteredName=null;
        String altNetworkMapFilteredNameUrl=null;

        String defCostMapFilteredName=null;
        String defCostMapFilteredNameUrl=null;
        List<String> defCostMapFilteredSupportType=new ArrayList<String>();
        String altCostMapFilteredName=null;
        String altCostMapFilteredNameUrl=null;
        List<String> altCostMapFilteredSupportType=new ArrayList<String>();

        String EndpointCostMapName=null;
        String EndpointCostMapUrl=null;
        List<String> ECSSupportType=new ArrayList<String>();

        String endPropsName=null;
        String endPropsUrl=null;
        List<String> EPPPSupportTypesList=new ArrayList<String>();

        List<String> reasonList=new ArrayList<String>();


        for(CostType loop:costTypeList)
        {
            if((loop.getMetric().intern()==routingcost.intern()) && (loop.getMode().intern()==numerical.intern()) )
            {
                numericalRoutingcost=loop.getName();
            }
            if((loop.getMetric().intern()==routingcost.intern()) && (loop.getMode().intern()==ordinal.intern()) )
            {
                ordinalRoutingcost=loop.getName();
            }
            if((loop.getMetric().intern()==hopcount.intern()) && (loop.getMode().intern()==numerical.intern()) )
            {
                numericalHopcount=loop.getName();
            }
            if((loop.getMetric().intern()==hopcount.intern()) && (loop.getMode().intern()==ordinal.intern()) )
            {
                ordinalHopcount=loop.getName();
            }
        }

        if(numericalRoutingcost==null)
        {
            //System.out.println("The tested server dose not support numerical routingcost");
            numericalRoutingcost="invalid";
        }
        if(ordinalRoutingcost==null)
        {
            //System.out.println("The tested server dose not support ordinal routingcost");
            ordinalRoutingcost="invalid";
        }
        if(numericalHopcount==null)
        {
            //System.out.println("The tested server dose not support numerical hopcount");
            numericalHopcount="invalid";
        }
        if(ordinalHopcount==null)
        {
            //System.out.println("The tested server dose not support ordinal hopcount");
            ordinalHopcount="invalid";
        }

        System.out.println("result 1.1.1 -----  The URL is["+args[0]+"].");
        System.out.println();
        System.out.println("2.1. Default Network Map And Cost Maps Check.");

        for(Resource loop:resourceList)
        {
            if(loop.getResourceName().intern()==defaultNetworkMapName.intern())
            {
                System.out.println("result 1.1.2 ----2.1 A The tested ALTO server provides default network map:"+"\tname:"+loop.getResourceName()+"\turi:"+loop.getUri());
                defaultNetworkMapUrl=loop.getUri();
                bFlag=true;
                break;
            }
        }
        if(bFlag==false)
        {
            System.out.println("result 1.1.2 ----2.1 A Error:The tested ALTO server does not provide default network map");
        }
        bFlag=false;


        if(defaultNetworkMapUrl!=null)
        {
            reasonList.clear();
            hostNameRequestLine.clear();
            AltoClientConst.methodName(defaultNetworkMapUrl,hostNameRequestLine);
            checkNetworkMap(hostNameRequestLine,reasonList,"default");

            if(reasonList.size()>0){
                System.out.println("2.1.B/ 3.2 Error:The tested ALTO server provides default network map with some Errors as followings.");
                System.out.println("pls  check manually because of JAVA string comparing ........");
                for(String loopa:reasonList)
                    System.out.println("\t\t"+loopa);
                bFlag=true;
            }
        }

        if(bFlag==false)
        {
            System.out.println("2.1.B/3.2 The tested ALTO server provides default network map consistent with Figure 1 ");
        }
        bFlag=false;


        System.out.println("\n2.1.C The tested ALTO server provides following cost map");
        for(Resource loop:resourceList)
        {

            String mediaType=loop.getMediaType();
            String accepts=loop.getAccepts();
            if((mediaType.intern()==AltoClientConst.AltoClientResponseType.CostMapResponseType.intern())&&
               (accepts.intern()==AltoClientConst.AltoClientConstString.Invalid.intern())&&
               ( loop.getUsesList().contains(defaultNetworkMapName)) )
            {

                    Capabilities capabilities=loop.getCapabilities();
                    List<String> costTypeNamesList=capabilities.getCostTypeNamesList();

                    if(costTypeNamesList.contains(numericalRoutingcost) )
                    {
                        def_num_rtg_name=loop.getResourceName();def_num_rtg_url=loop.getUri();
                        System.out.println("\t2.1.C The tested ALTO server provides numerical routingcost map.\t"+ def_num_rtg_name+"  "+def_num_rtg_name);
                    }
                    if(costTypeNamesList.contains(ordinalRoutingcost) )
                    {
                        def_ord_rtg_name=loop.getResourceName();def_ord_rtg_url=loop.getUri();
                        System.out.println("\t2.1.C The tested ALTO server provides ordinal routingcost map.\t"+ def_ord_rtg_name+"  "+def_ord_rtg_url);
                    }
                    if(costTypeNamesList.contains(numericalHopcount) )
                    {
                        def_num_hop_name=loop.getResourceName();def_num_hop_url=loop.getUri();
                        System.out.println("\t2.1.C The tested ALTO server provides numerical hopcount map.\t"+ def_num_hop_name+"  "+def_num_hop_url);
                    }
                    if(costTypeNamesList.contains(ordinalHopcount) )
                    {
                        def_ord_hop_name=loop.getResourceName();def_ord_hop_url=loop.getUri();
                        System.out.println("\t2.1.C The tested ALTO server provides ordinal hopcount map.\t"+ def_ord_hop_name+"  "+def_ord_hop_url);
                    }
            }
        }
        if(def_num_rtg_url==null)
        {
            def_num_rtg_url="invalid";
            System.out.println("\t Error  1.1.12-----2.1.C The tested server does NOT provide a numerical \'routingcost\' cost map resource for default network map");
        }
        else
        {
            System.out.println("\tresult 1.1.12-----2.1.C The tested ALTO server provides numerical routingcost map.\t"+ def_num_rtg_name+"  "+def_num_rtg_name);
        }
        if(def_ord_rtg_url==null)
        {
            def_ord_rtg_url="invalid";
            System.out.println("\tError  1.1.13-----2.1.C The tested server does NOT provide a ordinal \'routingcost\' cost map resource for default network map");
        }
        else
        {
            System.out.println("\tresult 1.1.13-----2.1.C The tested ALTO server provides ordinal routingcost map.\t"+ def_ord_rtg_name+"  "+def_ord_rtg_url);
        }
        if(def_num_hop_url==null)
        {
            def_num_hop_url="invalid";
            System.out.println("\tError  1.1.14-----2.1.C The tested server does NOT provide a numerical \'hopcount\' cost map resource for default network map");
        }
        else
        {
            System.out.println("\tresult 1.1.14-----2.1.C The tested ALTO server provides numerical hopcount map.\t"+ def_num_hop_name+"  "+def_num_hop_url);
        }
        if(def_ord_hop_url==null)
        {
            def_ord_hop_url="invalid";
            System.out.println("\tError  1.1.14-----2.1.C The tested server does NOT provide a ordinal \'hopcount\' cost map resource for default network map");
        }
        else
        {
            System.out.println("\tresult 1.1.15-----2.1.C The tested ALTO server provides ordinal hopcount map.\t"+ def_ord_hop_name+"  "+def_ord_hop_url);
        }

        System.out.println();
        bFlag=false;
        if(!def_num_rtg_url.contains("invalid"))
        {
            System.out.println("\tresult1.1.12-----2.1.C /3.3 The tested ALTO server does provide a numerical routingcost map for default network map.");
            System.out.println("\tInfo:"+ "\tname:"+def_num_rtg_name+"\turi:"+def_num_rtg_url);

            reasonList.clear();
            hostNameRequestLine.clear();
            AltoClientConst.methodName(def_num_rtg_url,hostNameRequestLine);
            checkRoutingcostMap(hostNameRequestLine, reasonList,"default");
            if(reasonList.size()>0){

                System.out.println("\n\tresult1.1.12-----Error 2.1.C/3.3 The tested ALTO server provides numerical routingcost map with some Errors as followings.");
                for(String loopa:reasonList)
                    System.out.println("\t\t"+loopa);
                bFlag=true;
            }
            else
            {
                System.out.println("\tresult1.1.12-----2.1.C/3.3 The tested ALTO server does provides numerical routingcost map consistent with Figure 2");
            }
        }
        else
        {
            System.out.println("\tresult1.1.12-----2.1.C /3.3 Error:The tested ALTO server does not provide numerical routingcost map for default network map");
        }

        System.out.println();
        if(!def_num_hop_url.contains("invalid"))
        {
            System.out.println("result1.1.14-----2.1.C /3.3 The tested ALTO server does provide a numerical hopcount map for default network map.");
            System.out.println("\tInfo:"+ "\tname:"+def_num_hop_name+"\turi:"+def_num_hop_url);
            System.out.println("\tCAUTION:check order value manually.");

            reasonList.clear();
            hostNameRequestLine.clear();
            AltoClientConst.methodName(def_num_hop_url,hostNameRequestLine);
            checkHopcountMap(hostNameRequestLine, reasonList,"default");

            System.out.println("\tresult1.1.14-----2.1.C /3.3The tested ALTO server provides numerical hopcount map as followings.");
            if(reasonList.size()>0)
            {
                System.out.println("Check Manually!");
            }
            for(String loopa:reasonList)
                System.out.println("\t\t"+loopa);
        }
        else
        {
            System.out.println("\tresult1.1.14-----2.1.C /3.3 Error:The tested ALTO server does not provide numerical hopcount map for default network map");
        }

        //----------------------------------

        System.out.println();
        System.out.println("2.2. Alternate Network Map And Cost Maps Check.");

        bFlag=false;
        for(Resource loop:resourceList)
        {
            if((loop.getMediaType().intern()==AltoClientConst.AltoClientResponseType.NetworkMapResponseType.intern() &&
               (loop.getResourceName().intern()!=defaultNetworkMapName.intern()))&&
               (loop.getAccepts().intern()==AltoClientConst.AltoClientConstString.Invalid.intern()))
            {
                alternateNetworkMapName=loop.getResourceName();
                alternateNetworkMapUrl=loop.getUri();
                bFlag=true;
                System.out.println("\tresult 1.1.3 ----2.2.A The tested ALTO server does provide an alternate ntwork map:"+"\tname:"+loop.getResourceName()+"\turi:"+loop.getUri());
                break;
            }
        }

        if(bFlag==false)
        {
            System.out.println("\tresult 1.1.3 ----Error:2.2.A The tested ALTO server does not provide an alternate network map");
            alternateNetworkMapUrl="invalid";
        }
        bFlag=false;

        if(!alternateNetworkMapUrl.contains("invalid"))
        {

            reasonList.clear();
            hostNameRequestLine.clear();
            AltoClientConst.methodName(alternateNetworkMapUrl,hostNameRequestLine);
            checkNetworkMap(hostNameRequestLine,reasonList,"alternate");
            if(reasonList.size()>0){
                bFlag=true;
                System.out.println("\t2.2.B The tested ALTO server provides alternate network map with some Errors as followings.");
                if(reasonList.size()>0)
                {
                    System.out.println("Check Manually!");
                }
                for(String loopa:reasonList)
                    System.out.println("\t\t"+loopa);
            }

        }
        if(bFlag==false)
        {
            System.out.println("\t2.2.B The tested ALTO server provides alternate network map consistent with Figure4. ");
        }
        bFlag=false;
        //----------------------------------
        //----------------------------------
        System.out.println();
        System.out.println("2.2.C The tested ALTO server provides following alternate cost map");
        for(Resource loop:resourceList)
        {
            String accepts=loop.getAccepts();
            if((loop.getMediaType().intern()==AltoClientConst.AltoClientResponseType.CostMapResponseType.intern())&&
               (accepts.intern()==AltoClientConst.AltoClientConstString.Invalid.intern())&&
               ( loop.getUsesList().contains(alternateNetworkMapName)) )
            {
                    Capabilities capabilities=loop.getCapabilities();
                    List<String> costTypeNamesList=capabilities.getCostTypeNamesList();

                    if(costTypeNamesList.contains(numericalRoutingcost) )
                    {
                        alt_num_rtg_name=loop.getResourceName();alt_num_rtg_url=loop.getUri();
                        System.out.println("\t2.1.C The tested ALTO server provides numerical routingcost map.\t"+ alt_num_rtg_name+"  "+alt_num_rtg_url);
                    }
                    if(costTypeNamesList.contains(ordinalRoutingcost) )
                    {
                        alt_ord_rtg_name=loop.getResourceName();alt_ord_rtg_url=loop.getUri();
                        System.out.println("\t2.1.C The tested ALTO server provides ordinal routingcost map.\t"+ alt_ord_rtg_name+"  "+alt_ord_rtg_url);
                    }
                    if(costTypeNamesList.contains(numericalHopcount) )
                    {
                        alt_num_hop_name=loop.getResourceName();alt_num_hop_url=loop.getUri();
                        System.out.println("\t2.1.C The tested ALTO server provides numerical hopcount map.\t"+ alt_num_hop_name+"  "+alt_num_hop_url);
                    }
                    if(costTypeNamesList.contains(ordinalHopcount) )
                    {
                        alt_ord_hop_name=loop.getResourceName();alt_ord_hop_url=loop.getUri();
                        System.out.println("\t2.1.C The tested ALTO server provides ordinal hopcount map.\t"+ alt_ord_hop_name+"  "+alt_ord_hop_url);
                    }
            }
        }
        if(alt_num_rtg_url==null)
        {
            alt_num_rtg_url="invalid";
        }
        else
        {
            System.out.println("\tresult1.1.16------------2.1.C The tested ALTO server provides numerical routingcost map.\t"+ alt_num_rtg_name+"  "+alt_num_rtg_url);
        }
        if(alt_ord_rtg_url==null)
        {
            alt_ord_rtg_url="invalid";
        }
        else
        {
            System.out.println("\tresult1.1.17------------2.1.C The tested ALTO server provides ordinal routingcost map.\t"+ alt_ord_rtg_name+"  "+alt_ord_rtg_url);
        }
        if(alt_num_hop_url==null)
        {
            alt_num_hop_url="invalid";
        }
        else
        {
            System.out.println("\tresult1.1.18------------2.1.C The tested ALTO server provides numerical hopcount map.\t"+ alt_num_hop_name+"  "+alt_num_hop_url);
        }
        if(alt_ord_hop_url==null)
        {
            alt_ord_hop_url="invalid";
        }
        else
        {
            System.out.println("\tresult1.1.19------------2.1.C The tested ALTO server provides ordinal hopcount map.\t"+ alt_ord_hop_name+"  "+alt_ord_hop_url);
        }

        System.out.println();
        bFlag=false;
        if(!alt_num_rtg_url.contains("invalid"))
        {
            System.out.println("result 1.1.16------2.2.C/3.5 The tested ALTO server does provide a numerical routingcost map for alternate network map.");
            System.out.println("\tInfo:"+ "\tname:"+def_num_rtg_name+"\turi:"+def_num_rtg_url);

            reasonList.clear();
            hostNameRequestLine.clear();
            AltoClientConst.methodName(alt_num_rtg_url,hostNameRequestLine);
            checkRoutingcostMap(hostNameRequestLine, reasonList,"alternate");
            if(reasonList.size()>0){

                System.out.println("\tresult 1.1.16------2.2.C/3.5 The tested ALTO server provides numerical routingcost map with some Errors as followings.");
                for(String loopa:reasonList)
                    System.out.println("\t\t"+loopa);
                bFlag=true;
            }
            else
            {
                System.out.println("\tresult 1.1.16------2.2.C/3.5 The tested ALTO server does provides numerical routingcost map consistent with Figure 5");
            }
        }
        else
        {
            System.out.println("\tresult 1.1.16------2.2.C/3.5 Error:The tested ALTO server does not provide numerical routingcost map for alternte network map");
        }


        if(!alt_num_hop_url.contains("invalid"))
        {
            System.out.println("result 1.1.18------CAUTION:check order value manually.");
            System.out.println("\n2.2.D The tested ALTO server does provide a numerical hopcount map for alternate network map.");
            System.out.println("\tInfo:"+ "\tname:"+alt_num_hop_url+"\turi:"+alt_num_hop_url);

            reasonList.clear();
            hostNameRequestLine.clear();
            AltoClientConst.methodName(alt_num_hop_url,hostNameRequestLine);
            checkHopcountMap(hostNameRequestLine, reasonList,"alternate");
            System.out.println("\tThe tested ALTO server provides order routingcost map as followings.Check Manually!");
            if(reasonList.size()>0)
            {
                System.out.println("\tCheck Manually!");
            }
            for(String loopa:reasonList)
                System.out.println("\t\t"+loopa);
        }
        else
        {
            System.out.println("\tresult 1.1.18------2.2.D  Error:The tested ALTO server does not provide numerical hopcount map for alternate network map");
        }

        //System.exit(0);
//------------------------------------------------------------------------------------------------------
        System.out.println();
        System.out.println("2.3./3.4/3.10 Endpoint Properties");
        for(Resource loop:resourceList)
        {
            if(loop.getMediaType().intern()==AltoClientConst.AltoClientResponseType.EndPointPropResponseType.intern())
            {
                Capabilities capabilities=loop.getCapabilities();
                for(String aLoop:capabilities.getPropTypesList())
                {
                    EPPPSupportTypesList.add(aLoop);
                }

                if(EPPPSupportTypesList.contains("priv:ietf-type"))
                {
                    System.out.println("\t2.3/3.4 The tested ALTO server does provide Endpoint Properties service with 'priv:ietf-type'.");
                    System.out.println("\t2.3/3.4 Info:"+ "\tname:"+loop.getResourceName()+"\turi:"+loop.getUri());
                    endPropsName=loop.getResourceName();
                    endPropsUrl=loop.getUri();
                    bFlag=true;
                }
                if(EPPPSupportTypesList.size()>=2){
                    System.out.println("\t2.3/3.4 The tested ALTO server does provide Endpoint Properties service with "+ EPPPSupportTypesList +".");
                }
                break;

            }
        }
        if(bFlag==false)
        {
            System.out.println("\t2.3/3.4 Error:The tested ALTO server does not provide Endpoint Properties service with 'priv:ietf-type'");

        }
        bFlag=false;

        if(endPropsUrl!=null)
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
            reasonList.clear();
            checkEPPMap( hostNameRequestLine,reasonList);
            if(reasonList.size()>0)
            {
                for(String loop:reasonList)
                    System.out.println(loop);
            }
        }
        bFlag=false;
//------------------------------------------------------------------------------------------------------//
        //check for filtered network map.
        System.out.println();
        System.out.println("3. Server Resources and Configuration");
        System.out.println("3.7 Filtered Network Map resources for either or both network maps");
        System.out.println("3.7.1 Filtered Network Map resources for default network map");
        for(Resource loop:resourceList)
        {

            String mediaType=loop.getMediaType();
            String acceptsType=loop.getAccepts();
            if((mediaType.intern()==AltoClientConst.AltoClientResponseType.NetworkMapResponseType.intern())&&
               (acceptsType.intern()==AltoClientConst.AltoClientFilteredContentTypeString.NetworkMap)&&
               (loop.getUsesList().contains(defaultNetworkMapName)))
            {
                System.out.println("\tThe tested ALTO server does provide a Filtered Network Map for default network map.");
                System.out.println("\tInfo:"+ "\tname:"+loop.getResourceName()+"\turi:"+loop.getUri());
                defNetworkMapFilteredName=loop.getResourceName();
                defNetworkMapFilteredNameUrl=loop.getUri();
                bFlag=true;
            }
        }

        if(bFlag==false)
        {
            System.out.println("\tError:The tested ALTO server does not provide a Filtered Network Map for default network map");
        }
        bFlag=false;
        System.out.println("3.7.2 Filtered Network Map resources for alternate network map");
        for(Resource loop:resourceList)
        {

            String mediaType=loop.getMediaType();
            String acceptsType=loop.getAccepts();
            if((mediaType.intern()==AltoClientConst.AltoClientResponseType.NetworkMapResponseType.intern())&&
               (acceptsType.intern()==AltoClientConst.AltoClientFilteredContentTypeString.NetworkMap)&&
               (loop.getUsesList().contains(alternateNetworkMapName)))
            {
                System.out.println("\tThe tested ALTO server does provide a Filtered Network Map for alternate network map.");
                System.out.println("\tInfo:"+ "\tname:"+loop.getResourceName()+"\turi:"+loop.getUri());
                altNetworkMapFilteredName=loop.getResourceName();
                altNetworkMapFilteredNameUrl=loop.getUri();
                bFlag=true;
            }
        }

        if(bFlag==false)
        {
            System.out.println("\t Error:The tested ALTO server does not provide a Filtered Network Map for alternate network map");
        }
        bFlag=false;

        //------------------------------------------------------------------------------------------------------//
        //check for filtered cost map.
        System.out.println("3.8 Filtered Cost Map resources");
        System.out.println("3.8 Check Filtered Cost Maps for default network map.");
        for(Resource loop:resourceList)
        {
            String acceptsType=loop.getAccepts();
            if((loop.getMediaType().intern()==AltoClientConst.AltoClientResponseType.CostMapResponseType.intern())&&
               (acceptsType.intern()==AltoClientConst.AltoClientFilteredContentTypeString.CostMap)&&
               (loop.getUsesList().contains(defaultNetworkMapName)))
            {
                System.out.println("\tThe tested ALTO server does provide  Filtered Cost Maps for default network map.");
                System.out.println("\tInfo:"+ "\tname:"+loop.getResourceName()+"\turi:"+loop.getUri());
                defCostMapFilteredName=loop.getResourceName();
                defCostMapFilteredNameUrl=loop.getUri();
                bFlag=true;
                Capabilities capabilities=loop.getCapabilities();
                List<String> costTypeNamesList=capabilities.getCostTypeNamesList();
                Boolean costConstraints=capabilities.getCostConstraints();
                if(costTypeNamesList.contains(numericalRoutingcost))
                {
                    System.out.println("\tThe tested ALTO server does provide a numerical routingcost default network map.");
                    defCostMapFilteredSupportType.add(numericalRoutingcost);
                }
                if(costTypeNamesList.contains(numericalHopcount))
                {
                    System.out.println("\tThe tested ALTO server does provide a numerical hocount default network map.");
                    defCostMapFilteredSupportType.add(numericalHopcount);
                }
                if(costTypeNamesList.contains(ordinalRoutingcost))
                {
                    System.out.println("\tThe tested ALTO server does provide a ordinal routingcost default network map.");
                    defCostMapFilteredSupportType.add(ordinalRoutingcost);
                }
                if(costTypeNamesList.contains(ordinalHopcount))
                {
                    System.out.println("\tThe tested ALTO server does provide a ordinal hocount default network map.");
                    defCostMapFilteredSupportType.add(ordinalHopcount);
                }
                System.out.println();
                if(costConstraints.booleanValue()==true)
                {
                    System.out.println("\tThe Filtered Cost Map accepts constraints.");
                }
                else
                {
                    System.out.println("\tThe Filtered Cost Map does not accept constraints.");
                }
            }
        }

        if(bFlag==false)
        {
            System.out.println("\tError:The tested ALTO server does not provide a Filtered Cost Map for default network map");
        }
        bFlag=false;
        System.out.println();
        System.out.println("3.8 Check Filtered Cost Maps for alternate network map.");
        for(Resource loop:resourceList)
        {

            String mediaType=loop.getMediaType();
            String acceptsType=loop.getAccepts();
            if((mediaType.intern()==AltoClientConst.AltoClientResponseType.CostMapResponseType.intern())&&
               (acceptsType.intern()==AltoClientConst.AltoClientFilteredContentTypeString.CostMap)&&
               (loop.getUsesList().contains(alternateNetworkMapName)))
            {
                System.out.println("\tThe tested ALTO server does provide a Filtered Cost Map for alternate network map.");
                System.out.println("\tInfo:"+ "\tname:"+loop.getResourceName()+"\turi:"+loop.getUri());
                altCostMapFilteredName=loop.getResourceName();
                altCostMapFilteredNameUrl=loop.getUri();
                bFlag=true;
                Capabilities capabilities=loop.getCapabilities();
                List<String> costTypeNamesList=capabilities.getCostTypeNamesList();
                Boolean costConstraints=capabilities.getCostConstraints();
                if(costTypeNamesList.contains(numericalRoutingcost))
                {
                    System.out.println("\tThe tested ALTO server does provide a numerical routingcost alternate network map.");
                    altCostMapFilteredSupportType.add(numericalRoutingcost);
                }
                if(costTypeNamesList.contains(numericalHopcount))
                {
                    System.out.println("\tThe tested ALTO server does provide a numerical hocount alternate network map.");
                    altCostMapFilteredSupportType.add(numericalHopcount);
                }
                if(costTypeNamesList.contains(ordinalRoutingcost))
                {
                    System.out.println("\tThe tested ALTO server does provide a ordinal routingcost alternate network map.");
                    altCostMapFilteredSupportType.add(ordinalRoutingcost);
                }
                if(costTypeNamesList.contains(ordinalHopcount))
                {
                    System.out.println("\tThe tested ALTO server does provide a ordinal hocount alternate network map.");
                    altCostMapFilteredSupportType.add(ordinalHopcount);
                }
                System.out.println();
                if(costConstraints.booleanValue()==true)
                {
                    System.out.println("\tThe Filtered Cost Map accepts constraints.");
                }
                else
                {
                    System.out.println("\tThe Filtered Cost Map does not accept constraints.");
                }
            }
        }

        if(bFlag==false)
        {
            System.out.println("\tError:The tested ALTO server does not provide a Filtered Cost Map for alternate network map");
        }
        bFlag=false;

        //------------------------------------------------------------------------------------------------------//
        //Endpoint Cost Service
        System.out.println();
        System.out.println("3.9 Check Endpoint Cost Service.");
        for(Resource loop:resourceList)
        {

            String mediaType=loop.getMediaType();
            String acceptsType=loop.getAccepts();
            if((mediaType.intern()==AltoClientConst.AltoClientResponseType.EndPointCostResponseType.intern())&&
               (acceptsType.intern()==AltoClientConst.AltoClientFilteredContentTypeString.EndPointCost))
            {
                System.out.println("\tThe tested ALTO server does provide a Endpoint Cost Service.");
                System.out.println("\tInfo:"+ "\tname:"+loop.getResourceName()+"\turi:"+loop.getUri());
                EndpointCostMapName=loop.getResourceName();
                EndpointCostMapUrl=loop.getUri();
                bFlag=true;
                Capabilities capabilities=loop.getCapabilities();
                List<String> costTypeNamesList=capabilities.getCostTypeNamesList();
                Boolean costConstraints=capabilities.getCostConstraints();
                if(costTypeNamesList.contains(numericalRoutingcost))
                {
                    System.out.println("\tThe tested ALTO server does provide a numerical routingcost in Endpoint Cost Service.");
                    ECSSupportType.add(numericalRoutingcost);
                }
                if(costTypeNamesList.contains(numericalHopcount))
                {
                    System.out.println("\tThe tested ALTO server does provide a numerical hocount in Endpoint Cost Service.");
                    ECSSupportType.add(numericalHopcount);
                }
                if(costTypeNamesList.contains(ordinalRoutingcost))
                {
                    System.out.println("\tThe tested ALTO server does provide a ordinal routingcost in Endpoint Cost Service.");
                    ECSSupportType.add(ordinalRoutingcost);
                }
                if(costTypeNamesList.contains(ordinalHopcount))
                {
                    System.out.println("\tThe tested ALTO server does provide a ordinal hocount in Endpoint Cost Service.");
                    ECSSupportType.add(ordinalHopcount);
                }
                if(costConstraints.booleanValue()==true)
                {
                    System.out.println("\tThe Endpoint Cost Service accepts constraints.");
                }
                else
                {
                    System.out.println("\tThe Endpoint Cost Service does not accept constraints.");
                }
            }
        }

        if(bFlag==false)
        {
            System.out.println("\tError:The tested ALTO server does not provide a Endpoint Cost Service");
        }
        bFlag=false;

        //------------------------------------------------------------------------------------------------------//
        //Filtered Network Map Tests
        System.out.println();
        System.out.println("4.1 check Filtered Network Map Tests (default network map).");
        if(defNetworkMapFilteredNameUrl!=null)
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(defNetworkMapFilteredNameUrl,hostNameRequestLine);
            reasonList.clear();
            List<String> invalidPidList=new ArrayList<String>();
            invalidPidList.add("invalidPIDAAA1");
            invalidPidList.add("invalidPIDAAA2");
            List<String> validPidList=new ArrayList<String>();
            validPidList.add("loopback");
            filteredNetworkMapTest( hostNameRequestLine,reasonList,"default",invalidPidList,validPidList);
            System.out.println("\tThe tested ALTO server's response for Filtered Network Map Tests.");
            for(String loop:reasonList)
            {
                System.out.println("\t\t"+loop);
            }
        }
        else
        {
            System.out.println("\tError:The tested ALTO server does not support Filtered Network Map Tests.(default network map)");
        }
        System.out.println();
        System.out.println("4.1 check Filtered Network Map Tests (alternate network map).");
        if(altNetworkMapFilteredNameUrl!=null)
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(altNetworkMapFilteredNameUrl,hostNameRequestLine);
            reasonList.clear();
            List<String> invalidPidList=new ArrayList<String>();
            invalidPidList.add("invalidPIDAAA1");
            invalidPidList.add("invalidPIDAAA2");
            List<String> validPidList=new ArrayList<String>();
            validPidList.add("loopback");
            filteredNetworkMapTest( hostNameRequestLine,reasonList,"alternate",invalidPidList,validPidList);
            System.out.println("\tThe tested ALTO server's response for Filtered Network Map Tests.");
            for(String loop:reasonList)
            {
                System.out.println("\t\t"+loop);
            }
        }
        else
        {
            System.out.println("\tError:The tested ALTO server does not support Filtered Network Map Tests.(alternate network map)");
        }
        //System.exit(0);
//------------------------------------------------------------------------------------------------------//
        System.out.println();
        System.out.println("4.2 Filtered Cost Map Tests Check. Default Network Map. Routingcost Metric");
        if((defCostMapFilteredNameUrl!=null) &&(defCostMapFilteredSupportType.contains(numericalRoutingcost)))
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(defCostMapFilteredNameUrl,hostNameRequestLine);
            reasonList.clear();
            List<String> constraintList=new ArrayList<String>();
            constraintList.add("ge 20.0");constraintList.add("le 30.0");
            filteredCostMapTest(hostNameRequestLine,reasonList,"default", "routingcost",constraintList);
            System.out.println("\t4.2 The tested ALTO server's response for Filtered Cost Map Tests.");
            for(String loop:reasonList)
            {
                System.out.println("\t\t"+loop);
            }
        }
        else
        {
            System.out.println("\tError:The tested ALTO server does not support Filtered Cost Map Tests");
        }
        System.out.println();
        System.out.println("4.2 check Filtered Cost Map Tests. Default Network Map. Hopcount Metric");
        if((defCostMapFilteredNameUrl!=null) && (defCostMapFilteredSupportType.contains(numericalHopcount)) )
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(defCostMapFilteredNameUrl,hostNameRequestLine);
            reasonList.clear();
            List<String> constraintList=new ArrayList<String>();
            constraintList.add("ge 9");constraintList.add("le 30");
            filteredCostMapTest(hostNameRequestLine,reasonList,"default", "hopcount",constraintList);
            System.out.println("\t4.2 The tested ALTO server's response for Filtered Cost Map Tests.");
            for(String loop:reasonList)
            {
                System.out.println("\t\t"+loop);
            }
        }
        else
        {
            System.out.println("\tError:The tested ALTO server does not support Filtered Cost Map Tests" );
            System.out.println("\tdefCostMapFilteredSupportType:"+defCostMapFilteredSupportType);
        }



        System.out.println();
        System.out.println("4.2 check Filtered Cost Map Tests. Altrenate Network Map. Routingcost Metric");
        if((altCostMapFilteredNameUrl!=null) &&(altCostMapFilteredSupportType.contains(numericalRoutingcost)))
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(altCostMapFilteredNameUrl,hostNameRequestLine);
            reasonList.clear();

            List<String> constraintList=new ArrayList<String>();
            constraintList.add("ge 20.0");constraintList.add("le 30.0");
            filteredCostMapTest(hostNameRequestLine,reasonList,"alternate", "routingcost",constraintList);
            System.out.println("\t4.2 The tested ALTO server's response for Filtered Cost Map Tests.");
            for(String loop:reasonList)
            {
                System.out.println("\t\t"+loop);
            }
        }
        else
        {
            System.out.println("\tError:The tested ALTO server does not support Filtered Cost Map Tests");
        }
        System.out.println();
        System.out.println("4.2 check Filtered Cost Map Tests. Altrenate Network Map.Hopcount Metric");
        if((altCostMapFilteredNameUrl!=null) &&(altCostMapFilteredSupportType.contains(numericalHopcount)))
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(altCostMapFilteredNameUrl,hostNameRequestLine);
            reasonList.clear();

            List<String> constraintList=new ArrayList<String>();
            constraintList.add("ge 4");constraintList.add("le 11");
            filteredCostMapTest(hostNameRequestLine,reasonList,"alternate", "hopcount",constraintList);
            System.out.println("\t4.2 The tested ALTO server's response for Filtered Cost Map Tests.");
            for(String loop:reasonList)
            {
                System.out.println("\t\t"+loop);
            }
        }
        else
        {
            System.out.println("\tError:The tested ALTO server does not support Filtered Cost Map Tests");
        }
        //

        //------------------------------------------------------------------------------------------------------//
        System.out.println();
        System.out.println("result 1.1.44----4.3 check  Endpoint Property Service Tests.");
        if((endPropsUrl!=null) && (EPPPSupportTypesList.size()>=1))
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
            reasonList.clear();
            for(String loop:EPPPSupportTypesList)
            {
                hostNameRequestLine.add(loop);
            }
            EndPropertiesMapTest( hostNameRequestLine,reasonList);
            System.out.println(hostNameRequestLine);System.out.println("\t4.3 Endpoint Property Service Tests.");
            for(String loop:reasonList)
            {
                System.out.println("\t\t"+loop);

            }
        }
        else
        {
            System.out.println("\t Error:The tested ALTO server does not support Endpoint Property Service Tests");
        }

        //------------------------------------------------------------------------------------------------------//
        System.out.println();
        System.out.println("4.4 check Endpoint Cost Service Tests.");

        if((EndpointCostMapUrl!=null) && (ECSSupportType.size()>=1))
        {
            hostNameRequestLine.clear();
            AltoClientConst.methodName(EndpointCostMapUrl,hostNameRequestLine);
            reasonList.clear();
            if(ECSSupportType.contains(numericalRoutingcost))
            {
                hostNameRequestLine.add("routingcost");
            }
            else
            {
                System.out.println("\t4.4 Error:The tested ALTO server  does not support  Endpoint Cost Service Tests for numerical routing cost");
            }
            if(ECSSupportType.contains(numericalHopcount))
            {
                hostNameRequestLine.add("hopcount");
            }
            else
            {
                System.out.println("\t4.4 Error:The tested ALTO server  does not support  Endpoint Cost Service Tests for numerical hopcount cost");
            }

            ECSMapTest( hostNameRequestLine,reasonList);
            System.out.println("\n\t4.4 Endpoint Cost Service Tests result.");
            for(String loop:reasonList)
            {
                System.out.println(loop);
            }
        }
        else
        {
            System.out.println("\tError:The tested ALTO server  does not support Endpoint Cost Service Tests");
        }

        //----------------------//
        if(true)
        {
            System.out.println("\n5. Error Tests.");
            List<String> retStrList;
            String httpStatus=null;
            String httpCode=null;
            String httpFiled=null;
            String httpValue=null;
            JSONObject jsonobj =null;
            JSONObject metaObj= null;
            String ipAddress="invalid";
            String type="invalid";
            System.out.println();
            System.out.println("result 1.2.1----5.1. Invalid Field Type---- Related test item 1.2.1");
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"51IFT",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"1","ipv4:1.2.3.4"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_TYPE")) &&(httpFiled.contains("endpoints")))
                {
                    System.out.println("\t The tested ALTO server does PASS \"5.1. Invalid Field Type\" test.");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\tError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_TYPE"))
                    {
                        System.out.println("\tError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("endpoints"))
                    {
                        System.out.println("\tError: The tested ALTO server does return non-expecting STATUS. "+ httpFiled);
                    }
                    System.out.println("\tError: The tested ALTO server does FAIL \"5.1. Invalid Field Type\" test.");
                }

            }
            else
            {
                System.out.println("\tError: The tested ALTO server does not support \"5.1. Invalid Field Type\" test.");
            }

            System.out.println();
            System.out.println("result 1.2.2----5.2. Missing \"properties\" Field");
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"52MPF",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"1","ipv4:1.2.3.4"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_MISSING_FIELD")) &&(httpFiled.contains("properties")))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.2. Missing \"properties\" Field\" test.");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_TYPE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("endpoints"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+ httpFiled);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.2. Missing \"properties\" Field\" test.");
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.2. Missing \"properties\" Field\" test.");
            }
            System.out.println();
            System.out.println("result 1.2.3----5.3. Invalid Property Name");
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"53IPN",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1","invalid"+EPPPSupportTypesList.get(0),"1","ipv4:1.2.3.4"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("properties")) && (httpValue.contains("invalid"+EPPPSupportTypesList.get(0))))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.3. Invalid Property Name\" test.");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("properties"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+ httpFiled);
                    }
                    if(!httpValue.contains("invalid"+EPPPSupportTypesList.get(0)))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ "invalid"+EPPPSupportTypesList.get(0));
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.3. Invalid Property Name\" test.");
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.3. Invalid Property Name\" test.");
            }

            ipAddress="ipv4:1.2.3.256";
            type="type A";
            System.out.println();
            System.out.println("result 1.2.4----5.4. Invalid Endpoint Addresses"+"--"+type);
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"54IEA",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"1",ipAddress};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("endpoints")) && (httpValue.contains(ipAddress)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.4. Invalid Endpoint Addresses\" test. "+"--"+type);
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("endpoints"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+ httpFiled);
                    }
                    if(!httpValue.contains(ipAddress))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.4. Invalid Endpoint Addresses\" test."+"--"+type);
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.4. Invalid Endpoint Addresses\" test."+"--"+type);
            }

            ipAddress="ipv6:2001:db800::";
            type="type B";
            System.out.println();
            System.out.println("result 1.2.5----5.4. Invalid Endpoint Addresses"+"--"+type);
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"54IEA",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"1",ipAddress};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("endpoints")) && (httpValue.contains(ipAddress)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.4. Invalid Endpoint Addresses\" test. "+"--"+type);
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("endpoints"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+ httpFiled);
                    }
                    if(!httpValue.contains(ipAddress))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.4. Invalid Endpoint Addresses\" test."+"--"+type);
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.4. Invalid Endpoint Addresses\" test."+"--"+type);
            }

            ipAddress="ipv4:2001:db8::";
            type="type C";
            System.out.println();
            System.out.println("result 1.2.6----5.4. Invalid Endpoint Addresses"+"--"+type);
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"54IEA",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"1",ipAddress};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("endpoints")) && (httpValue.contains(ipAddress)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.4. Invalid Endpoint Addresses\" test. "+"--"+type);
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("endpoints"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+ httpFiled);
                    }
                    if(!httpValue.contains(ipAddress))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.4. Invalid Endpoint Addresses\" test."+"--"+type);
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.4. Invalid Endpoint Addresses\" test."+"--"+type);
            }

            ipAddress="ipv6:1.2.3.4";
            type="type D";
            System.out.println();
            System.out.println("result 1.2.7----5.4. Invalid Endpoint Addresses"+"--"+type);
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"54IEA",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"1",ipAddress};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("endpoints")) && (httpValue.contains(ipAddress)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.4. Invalid Endpoint Addresses\" test. "+"--"+type);
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("endpoints"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+ httpFiled);
                    }
                    if(!httpValue.contains(ipAddress))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.4. Invalid Endpoint Addresses\" test."+"--"+type);
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.4. Invalid Endpoint Addresses\" test."+"--"+type);
            }
            System.out.println();
            System.out.println("result 1.2.8----5.5. Invalid numerical Cost Type.");
            if((defCostMapFilteredNameUrl!=null) && defCostMapFilteredSupportType.contains(numericalHopcount)||defCostMapFilteredSupportType.contains(numericalRoutingcost))
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(defCostMapFilteredNameUrl,hostNameRequestLine);
                String metric="invalid";String mode="invalid";
                String value=null;
                metric="no-such-metric";mode="numerical";
                value=metric;

                String argList[]={"55ICC",hostNameRequestLine.get(0),hostNameRequestLine.get(1),metric,mode,"le 100.0"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");
                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("cost-type/cost-metric")) && (httpValue.contains(value)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.5. Invalid Cost Type\" test. ");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("cost-type/cost-metric"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting field. "+ httpFiled);
                    }
                    if(!httpValue.contains(value))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.5. Invalid Cost Type\" test.");
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.5. Numerical Invalid Cost Type\" test.");
            }

            System.out.println();
            System.out.println("result 1.2.9----5.5. Invalid ordinal Cost Type.");
            if((defCostMapFilteredNameUrl!=null) && (defCostMapFilteredSupportType.contains(ordinalHopcount)||defCostMapFilteredSupportType.contains(ordinalRoutingcost)) )
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(defCostMapFilteredNameUrl,hostNameRequestLine);
                String metric="invalid";String mode="invalid";
                String value=null;
                metric="no-such-metric";mode="ordinal";
                value=metric;

                String argList[]={"55ICC",hostNameRequestLine.get(0),hostNameRequestLine.get(1),metric,mode,"le 100"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("cost-type/cost-metric")) && (httpValue.contains(value)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.5. Invalid Cost Type\" test. ");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("cost-type/cost-metric"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting Field value. "+ httpFiled);
                    }
                    if(!httpValue.contains(value))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.5. Invalid Cost Type\" test.");
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.5. Invalid ordinal Cost Type\" test.");
            }

            System.out.println();
            System.out.println("result 1.2.10----5.6. Invalid numerical Cost Mode.");
            if((defCostMapFilteredNameUrl!=null) && defCostMapFilteredSupportType.contains(numericalRoutingcost))
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(defCostMapFilteredNameUrl,hostNameRequestLine);
                String metric="invalid";String mode="invalid";
                String value=null;
                metric="routingcost";mode="no-such-mode";
                value=mode;

                String argList[]={"56ICM",hostNameRequestLine.get(0),hostNameRequestLine.get(1),metric,mode,"le 100"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("cost-type/cost-mode")) && (httpValue.contains(value)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.6. Invalid Cost Mode\" test. ");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("cost-type/cost-mode"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting Field value. "+ httpFiled);
                    }
                    if(!httpValue.contains(value))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.6. Invalid Cost Mode\" test.");
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.6.  Invalid numerical Cost Mode\" test.");
            }
            System.out.println();
            System.out.println("result 1.2.11----5.6.  Invalid ordinal Cost Mode.");
            if((defCostMapFilteredNameUrl!=null) && (defCostMapFilteredSupportType.contains(ordinalRoutingcost)) )
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(defCostMapFilteredNameUrl,hostNameRequestLine);
                String metric="invalid";String mode="invalid";
                String value=null;
                metric="routingcost";mode="no-such-mode";
                value=mode;

                String argList[]={"56ICM",hostNameRequestLine.get(0),hostNameRequestLine.get(1),metric,mode,"le 100"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("cost-type/cost-mode")) && (httpValue.contains(value)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.6. Invalid Cost Mode\" test. ");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("cost-type/cost-mode"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting Field value. "+ httpFiled);
                    }
                    if(!httpValue.contains(value))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.6. Invalid Cost Mode\" test.");
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.6.  Invalid ordinal Cost Mode\" test.");
            }
            System.out.println();
            System.out.println("result 1.2.12----5.7. Invalid Cost Constraints.");
            if(defCostMapFilteredNameUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(defCostMapFilteredNameUrl,hostNameRequestLine);
                String metric="invalid";String mode="invalid";
                String value=null;
                metric="routingcost";mode="numerical";
                value="ne 100";

                String argList[]={"57ICC",hostNameRequestLine.get(0),hostNameRequestLine.get(1),metric,mode,"ne 100"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="no-such-mode";              httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                    httpFiled=metaObj.getString("field");
                    httpValue=metaObj.getString("value");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) &&(httpCode.contains("E_INVALID_FIELD_VALUE")) &&(httpFiled.contains("constraints")) && (httpValue.contains(value)))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.7. Invalid Cost Constraints\" test. ");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_INVALID_FIELD_VALUE"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    if(!httpFiled.contains("constraints"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+ httpFiled);
                    }
                    if(!httpValue.contains(value))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting value. "+ httpValue);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.7. Invalid Cost Constraints\" test.");
                }

            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.7. Invalid Cost Constraints\" test.");
            }

            System.out.println();
            System.out.println("result 1.2.13----5.8. JSON Syntax Error");
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"58JSE",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"2","ipv4:1.2.3.4","ipv4:100.131.39.11"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);

                try
                {
                    jsonobj = new JSONObject(retStrList.get(1));
                    metaObj=(JSONObject) (jsonobj.get("meta"));
                    httpCode=metaObj.getString("code");
                } catch (JSONException e){  e.printStackTrace();}
                System.out.println("\t the return httpStatus is that "+httpStatus+".");

                if((httpStatus.contains("400")) && ( (httpCode.contains("E_SYNTAX")) || (httpCode.contains("E_INVALID_FIELD_TYPE"))) )
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.8. JSON Syntax Error\" test.");
                }
                else
                {
                    if(!httpStatus.contains("400"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    if(!httpCode.contains("E_SYNTAX"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting CODE. "+httpCode);
                    }
                    System.out.println("Error: The tested ALTO server does FAIL \"5.8. JSON Syntax Error\" test.");
                }
            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.8. JSON Syntax Error\" test.");
            }
            System.out.println();
            System.out.println("result 1.2.14----5.9. Invalid Accept Header In GET Request");
            if(defaultNetworkMapUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(defaultNetworkMapUrl,hostNameRequestLine);
                String argList[]={"59IAH",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"text/html"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);
                System.out.println("\t the return httpStatus is that "+httpStatus+".");


                if(retStrList.get(0).contains("406") || retStrList.get(1).contains("406"))//("Not Acceptable"))
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.9. Invalid Accept Header In GET Request\" test.");
                }
                else
                {
                    if(!httpStatus.contains("406"))//("406 Not Acceptable"))
                    {
                        System.out.println("\nError: The tested ALTO server does return non-expecting STATUS. "+httpStatus);
                    }
                    System.out.println("\nError: The tested ALTO server does FAIL \"5.9. Invalid Accept Header In GET Request\" test.");
                }
            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.9. Invalid Accept Header In GET Request\" test.");
            }

            System.out.println();
            System.out.println("result 1.2.15----5.10. Invalid Accept Header In POST Request");
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"510IAH",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"2","ipv4:1.2.3.4","ipv4:100.131.39.11"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                if(retStrList.size()>=1)
                    httpStatus=retStrList.get(0);
                System.out.println("\t the return httpStatus is that "+httpStatus+".");


                if((httpStatus.contains("406")) )//("406 Not Acceptable")
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.10. Invalid Accept Header In POST Request\" test.");
                }
                else
                {
                    System.out.println("\n Tip: The tested ALTO server does FAIL \"5.10. Invalid Accept Header In POST Request\" test.");
                    System.out.println("\n Tip: httpStatus "+httpStatus);
                }
            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.10. Invalid Accept Header In POST Request\" test.");
            }
            System.out.println();
            System.out.println("result 1.2.16----5.11. Invalid Content-Type Header In POST Request");
            if(endPropsUrl!=null)
            {
                hostNameRequestLine.clear();
                AltoClientConst.methodName(endPropsUrl,hostNameRequestLine);
                String argList[]={"511ICT",hostNameRequestLine.get(0),hostNameRequestLine.get(1),"1",EPPPSupportTypesList.get(0),"2","ipv4:1.2.3.4","ipv4:100.131.39.11"};
                retStrList=Error5Test(argList);
                jsonobj =null;              metaObj= null;
                httpStatus="invalid";               httpCode="invalid";             httpFiled="invalid";                httpValue="invalid";
                httpStatus=retStrList.get(0);
                System.out.println("\t the return httpStatus is that "+httpStatus+".");
                //if((httpStatus.contains("400"))||(httpStatus.contains("404 Not Found"))||(httpStatus.contains("406 Not Acceptable"))||(httpStatus.contains("415 Unsupported Media Type")) )
                if((httpStatus.contains("400"))||(httpStatus.contains("404"))||(httpStatus.contains("406"))||(httpStatus.contains("415")) )
                {
                    System.out.println("\n The tested ALTO server does PASS \"5.11. Invalid Content-Type Header In POST Request\" test. "+ "returned status is "+httpStatus+" .");
                }
                else
                {
                    System.out.println("\n Tips: The tested ALTO server does FAIL \"5.11. Invalid Content-Type Header In POST Request\" test.");
                    System.out.println("\n Tip: httpStatus "+httpStatus);
                }
            }
            else
            {
                System.out.println("\nError: The tested ALTO server does not support \"5.11. Invalid Content-Type Header In POST Request\" test.");
            }

        }

        System.out.println("this test is done");
    }
}
