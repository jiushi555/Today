package xml.org.today.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import xml.org.today.R;
import xml.org.today.util.ShareApi;
import xml.org.today.util.UpdateUtil;
import xml.org.today.util.UserInfo;

/**
 * Created by Administrator on 2016/7/4.
 */
public class SetActivity extends BaseActivity {
    private ImageView back;
    private RelativeLayout rlSetPwd,rlFeedback,rlAboutUs,update,tj_friend;
    private TextView cancel_btn,versionTv1,versionTv2;
    private final String ACTION_NAME="finish";
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        initView();
    }

    private void initView() {
        versionTv1= (TextView) findViewById(R.id.version1);
        versionTv2= (TextView) findViewById(R.id.version2);
        if(MainActivity.CHECKVERSION){
            versionTv2.setVisibility(View.VISIBLE);
            versionTv1.setVisibility(View.GONE);
        }else {
            versionTv2.setVisibility(View.GONE);
            versionTv1.setVisibility(View.VISIBLE);
            versionTv1.setText(getVersion());
        }
        cancel_btn= (TextView) findViewById(R.id.cancel_btn);
        back= (ImageView) findViewById(R.id.goback);
        rlSetPwd= (RelativeLayout) findViewById(R.id.rlSetPassword);
        rlFeedback= (RelativeLayout) findViewById(R.id.rlFeedback);
        rlAboutUs= (RelativeLayout) findViewById(R.id.rlAboutUs);
        update= (RelativeLayout) findViewById(R.id.update);
        tj_friend= (RelativeLayout) findViewById(R.id.tj_friends);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.clearUser(SetActivity.this);
                Intent it=new Intent();
                it.setAction("cancel");
                it.putExtra("type", "finish");
                sendBroadcast(it);
                Toast.makeText(SetActivity.this,"退出成功",Toast.LENGTH_SHORT).show();
                flag=1;
            }
        });
        rlSetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(SetActivity.this,RlPwdActivity.class);
                SetActivity.this.startActivity(it);
            }
        });
        rlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(SetActivity.this,FeedActivity.class);
                SetActivity.this.startActivity(it);
            }
        });
        rlAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(SetActivity.this,AboutUsActivity.class);
                SetActivity.this.startActivity(it);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUtil manager = new UpdateUtil(SetActivity.this);
                // 检查软件更新
                manager.checkUpdate();
            }
        });
        tj_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareApi.showShare(SetActivity.this);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetActivity.this.finish();
            }
        });

    }
    private String getVersion() {
        PackageManager packageManager = getPackageManager();
        String versionName;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "null";
        }
    }

    @Override
    protected void onDestroy() {
        if(flag==1){
            Intent it=new Intent(SetActivity.this,GuideActivity.class);
            SetActivity.this.startActivity(it);
        }
        super.onDestroy();
    }
}
