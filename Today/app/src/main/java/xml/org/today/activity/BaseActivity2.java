package xml.org.today.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import xml.org.today.util.ProgressDialog;

/**
 * Created by Administrator on 2016/8/1.
 */
public class BaseActivity2 extends AppCompatActivity {
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        registerBoradcastReceiver();

    }

    public ProgressDialog mProgressDialog;
    public void showDialog(){
        mProgressDialog = ProgressDialog.createDialog(mContext);
        mProgressDialog.show();
    }
    public void closeDialog(){
        mProgressDialog.dismiss();
    }
    public void showNetError(){
        Toast.makeText(mContext,"网络错误,请检查网络",Toast.LENGTH_SHORT).show();
    }

    //全部退出广播
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("cancel")){
                Log.d("TAGTAG","已接受");
                ((Activity)mContext).finish();

            }
        }

    };

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("cancel");
        myIntentFilter.addAction("finish");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}

