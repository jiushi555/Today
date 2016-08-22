package xml.org.today.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import xml.org.today.R;
import xml.org.today.util.ProgressDialog;
import xml.org.today.util.ServerApi;

/**
 * Created by Administrator on 2016/7/5.
 */
public class FeedActivity extends BaseActivity{
    private ImageView back;
    private EditText edt;
    private Timer timer=new Timer(true);
    private TimerTask timerTask;
    private TextView sure_btn;
    private static final int NOHTTP_WHAT_TEST = 0x003;
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initView();
        timer.schedule(timerTask,500);
    }

    private void initView() {
        edt= (EditText) findViewById(R.id.feed_edt);
        timerTask=new TimerTask() {
            @Override
            public void run() {
                //自动弹出键盘
                edt.setFocusableInTouchMode(true);
                edt.requestFocus();
                InputMethodManager inputManager =
                        (InputMethodManager) edt.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edt, 0);
            }
        };
        sure_btn= (TextView) findViewById(R.id.post_btn);
        sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=edt.getText().toString();
                if(content.equals("")){
                    Toast.makeText(FeedActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    ServerApi.getFeedbackResult(content,NOHTTP_WHAT_TEST,onResponseListener);
                }
            }
        });
        back= (ImageView) findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedActivity.this.finish();
            }
        });
    }

    /**
     * 回调对象，接受请求结果
     */
    private OnResponseListener<JSONObject> onResponseListener = new OnResponseListener<JSONObject>() {
        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == NOHTTP_WHAT_TEST) {// 判断what是否是刚才指定的请求
                // 请求成功
                String result = response.get().toString();// 响应结果
                try {
                    JSONObject jso = new JSONObject(result);
                    String success = jso.getString("success");
                    if (success.equals("0")) {
                        Toast.makeText(FeedActivity.this, "提交成功，感谢您的建议", Toast.LENGTH_SHORT).show();
                        FeedActivity.this.finish();
                    } else {
                        Toast.makeText(FeedActivity.this, "提交失败，刷新试试", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 响应头
                Headers headers = response.getHeaders();
                headers.getResponseCode();// 响应码
                response.getNetworkMillis();// 请求花费的时间
            }
        }

        @Override
        public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
            Toast.makeText(FeedActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart(int what) {
            // 请求开始，显示dialog
            mDialog = ProgressDialog.createDialog(FeedActivity.this);
            mDialog.show();
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
            mDialog.dismiss();
        }


    };
}
