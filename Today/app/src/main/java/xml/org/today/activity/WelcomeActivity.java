package xml.org.today.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import xml.org.today.R;
import xml.org.today.util.ServerApi;
import xml.org.today.util.UserInfo;

/**
 * Created by Administrator on 2016/6/28.
 */
public class WelcomeActivity extends BaseActivity {
    private final Timer mTimer = new Timer(true);
    private TimerTask mTimerTask;
    private int FLAG=0;           //表示是否用户已登录
    private int NOHTTP_FLAG=0x017;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        initView();
        mTimer.schedule(mTimerTask, 2000);
    }

    private void initView() {
        ServerApi.init();
        if(UserInfo.getUserId(WelcomeActivity.this)==null){
            FLAG=0;
        }else{
            FLAG=1;
        }
        ServerApi.getCheckVersionResult(NOHTTP_FLAG,WelcomeActivity.this,onResponseListener);
        mTimerTask = new TimerTask() {
            @Override
            public void run() {

                if(FLAG==0){
                    Intent it = new Intent(WelcomeActivity.this, GuideActivity.class);
                    startActivity(it);
                }else{
                    Intent it = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(it);
                }
                //Intent it=new Intent(WelcomeActivity.this,MainActivity.class);
                WelcomeActivity.this.finish();
            }
        };
    }
    private OnResponseListener<JSONObject> onResponseListener=new OnResponseListener<JSONObject>() {
        @Override
        public void onStart(int i) {

        }

        @Override
        public void onSucceed(int i, Response<JSONObject> response) {
            if(i==NOHTTP_FLAG){
                String result=response.get().toString();
                try {
                    JSONObject jso=new JSONObject(result);
                    String success=jso.getString("success");
                    if(success.equals("0")){
                        String version=jso.getString("version");
                        String url=jso.getString("url");
                    }else{
                        Toast.makeText(WelcomeActivity.this,"网络请求失败，检查网络",Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                long time=response.getNetworkMillis();
            }
        }

        @Override
        public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
            Toast.makeText(WelcomeActivity.this,"网络请求失败，检查网络",Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onFinish(int i) {

        }
    };



}
