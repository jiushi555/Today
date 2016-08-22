package xml.org.today.util;

import android.content.Context;

/**
 * Created by Administrator on 2016/7/5.
 */
public class UserInfo {

    private static ConfigUtil configUtil;
    private static Context context;
    public UserInfo(){
    }
    public static void setUserInfo(String username,String user_id,Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        configUtil.shoreData("username",username);
        configUtil.shoreData("user_id",user_id);
    }
    public static void setUsername(String username,Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        configUtil.shoreData("username",username);
    }
    public static void setUserImg(String path,Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        configUtil.shoreData("path",path);
    }
    public static void setUserPortraitUrl(String url,Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        configUtil.shoreData("PortraitUrl",url);
    }
    public static String getUserName(Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        return configUtil.getData("username","user",context);
    }
    public static String getUserId(Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        return configUtil.getData("user_id","user",context);
    }
    public static String getUserImg(Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        return configUtil.getData("path","user",context);
    }
    public static void clearUser(Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        configUtil.clearData(context);
    }
    public static String getUserPortraitUrl(Context mContext){
        if(configUtil==null){
            context=mContext;
            configUtil=new ConfigUtil(context,"user");
        }
        return configUtil.getData("PortraitUrl","user",context);
    }

}
