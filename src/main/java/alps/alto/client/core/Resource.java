package alps.alto.client.core;

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

public final class Resource implements Cloneable
{
    private String resourceName;
    private String uri;
    private String mediaType;
    private String accepts;
    private Capabilities capabilities;
    private List<String> usesList;

    public Resource()
    {
        this(AltoClientConst.AltoClientConstString.Invalid,AltoClientConst.AltoClientConstString.Invalid,AltoClientConst.AltoClientConstString.Invalid,AltoClientConst.AltoClientConstString.Invalid,null,null);
    }
    public Resource(String resourceName,String uri, String mediaType,String accepts,Capabilities capabilities,List<String> usesList)
    {
        this.resourceName=resourceName;
        this.uri=uri;
        this.mediaType=mediaType;
        this.accepts=accepts;
        this.capabilities=capabilities;
        this.usesList=usesList;
    }

    public void setResourceName(String resourceName )
    {
        this.resourceName=resourceName;
    }
    public String getResourceName()
    {
        return resourceName;
    }

    public void setUri(String uri )
    {
        this.uri=uri;
    }
    public String getUri()
    {
        return uri;
    }

    public void setMediaType(String mediaType )
    {
        this.mediaType=mediaType;
    }
    public String getMediaType()
    {
        return mediaType;
    }

    public void setAccepts(String accepts )
    {
        this.accepts=accepts;
    }
    public String getAccepts()
    {
        return accepts;
    }

    public void setCapabilities(Capabilities capabilities )
    {
        this.capabilities=capabilities;
    }
    public Capabilities getCapabilities()
    {
        return capabilities;
    }

    public void setUsesList(List<String> usesList )
    {
        this.usesList=usesList;
    }
    public List<String> getUsesList()
    {
        return usesList;
    }

    @Override
    public String toString()
    {
        JSONObject json=new JSONObject();
        JSONArray usesListJsonMembers = new JSONArray();
        for(String loop:usesList)
        {
            usesListJsonMembers.put(loop);
        }

        try
        {
            json.put("uri", this.getUri());
            json.put("mediaType", this.getMediaType());
            if(this.getAccepts().intern()!=AltoClientConst.AltoClientConstString.Invalid.intern())
            {
                json.put("accepts", this.getAccepts());
            }
            if(this.getCapabilities()!=null)
            {
                json.put("capabilities", this.getCapabilities());
            }
            if(this.getUsesList()!=null)
            {
                json.put("usesList", this.getUsesList());
            }
            json.put("name", this.getResourceName());
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
        maps.put(altoClientConstMethod.add("name"), altoClientConstMethod.add(this.getResourceName()));
        maps.put(altoClientConstMethod.add("uri"), altoClientConstMethod.add(this.getUri()));
        maps.put(altoClientConstMethod.add("mediaType"), altoClientConstMethod.add(this.getMediaType()));
        if(this.getAccepts().intern()!=AltoClientConst.AltoClientConstString.Invalid.intern())
        {
            maps.put(altoClientConstMethod.add("accepts"), altoClientConstMethod.add(this.getAccepts()));
        }
        if(this.getCapabilities()!=null)
        {
            maps.put(altoClientConstMethod.add("capabilities"), this.getCapabilities().toString());
        }
        if(this.getUsesList()!=null)
        {
            maps.put(altoClientConstMethod.add("usesList"), this.getUsesList().toString());
        }
        String ret=maps.toString();
        return ret;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        Resource ret=(Resource) super.clone();

        Capabilities capabilities=(Capabilities)this.getCapabilities().clone();

        List<String> usesListOrigin=this.getUsesList();
        List<String> usesListCopy=new ArrayList<String>();

        for(String loop:usesListOrigin)
        {
            String tmp=loop;
            usesListCopy.add(tmp);
        }
        ret.setCapabilities(capabilities);
        ret.setUsesList(usesListCopy);

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
