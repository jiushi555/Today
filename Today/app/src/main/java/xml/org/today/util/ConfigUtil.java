package xml.org.today.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/7/5.
 */
public class ConfigUtil {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public ConfigUtil(Context context,String name){
        sp=context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if(editor==null){
            editor=sp.edit();
        }
    }
    public void shoreData(String key,String value){
        if(editor==null){
            editor=sp.edit();
        }
        editor.putString(key,value);
        editor.commit();
    }
    public void shoreData(String key,Bitmap bitmap){
        if(editor==null){
            editor=sp.edit();
        }
    }
    public String getData(String key,String name,Context context){
        if(sp==null){
            sp=context.getSharedPreferences(name,Context.MODE_PRIVATE);
        }
        if(editor==null){
            editor=sp.edit();
        }
        return sp.getString(key,null);
    }
    public void clearData(Context context){
        editor.clear();
        editor.commit();
    }

}
