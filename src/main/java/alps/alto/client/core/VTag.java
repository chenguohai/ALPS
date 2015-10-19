package alps.alto.client.core;
/**
 * @author chenguohai:chenguohai67@outlook.com, chenguohai@huawei.com
 *
 */




import org.json.JSONException;
import org.json.JSONObject;

public final class VTag implements Cloneable
{

    private String resourceID;
    private String tag;

    public VTag(String resourceID,String tag)
    {
        this.resourceID=resourceID;
        this.tag=tag;
    }

    public void setResourceID(String resourceID )
    {
        this.resourceID=resourceID;
    }
    public String getResourceID()
    {
        return resourceID;
    }

    public void setTag(String tag )
    {
        this.tag=tag;
    }
    public String getTag()
    {
        return tag;
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
            json.put("resourceID", this.getResourceID());
            json.put("tag", this.getTag());
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
        VTag aInst=new VTag("AA","BB");// just used for test
        System.out.println(aInst.toString());
    }

}
