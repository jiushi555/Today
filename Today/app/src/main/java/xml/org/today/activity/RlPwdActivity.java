package xml.org.today.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import xml.org.today.R;
import xml.org.today.util.ProgressDialog;
import xml.org.today.util.ServerApi;
import xml.org.today.util.UserInfo;

/**
 * Created by Administrator on 2016/7/5.
 */
public class RlPwdActivity extends BaseActivity {
    private ImageView back;
    private TextInputLayout oldpwd_layout, newpwd_layout, newpwdz_layout;
    private EditText oldpwd_edt, newpwd_edt, newpwdz_edt;
    private static int FLAG = 0;  //标记button的状态:1可以 0 不能
    private TextView btn;
    private static final int NOHTTP_WHAT_TEST = 0x004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlpwd);
        initView();
    }

    private void initView() {
        btn = (TextView) findViewById(R.id.rlpwd_btn);
        oldpwd_layout = (TextInputLayout) findViewById(R.id.oldpwd_layout);
        oldpwd_layout.setHint("输入旧密码");
        newpwd_layout = (TextInputLayout) findViewById(R.id.newpwd_layout);
        newpwd_layout.setHint("输入新密码");
        newpwdz_layout = (TextInputLayout) findViewById(R.id.pwdz_layout);
        newpwdz_layout.setHint("再次输入新密码");
        oldpwd_edt = (EditText) findViewById(R.id.oldpwd_edt);
        newpwd_edt = (EditText) findViewById(R.id.newpwd_edt);
        newpwdz_edt = (EditText) findViewById(R.id.pwdz_edt);
        newpwdz_edt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() > 0) {
                    if (!(oldpwd_edt.getText().toString().equals("")) &&
                            !(newpwd_edt.getText().toString().equals(""))) {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_off));
                        FLAG = 1;
                    } else {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                        FLAG = 0;
                    }
                } else {
                    btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                    FLAG = 0;
                }
            }
        });
        newpwd_edt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() > 0) {
                    if (!(oldpwd_edt.getText().toString().equals("")) &&
                            !(newpwdz_edt.getText().toString().equals(""))) {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_off));
                        FLAG = 1;
                    } else {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                        FLAG = 0;
                    }
                } else {
                    btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                    FLAG = 0;
                }
            }
        });
        oldpwd_edt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() > 0) {
                    if (!(newpwd_edt.getText().toString().equals("")) &&
                            !(newpwdz_edt.getText().toString().equals(""))) {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_off));
                        FLAG = 1;
                    } else {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                        FLAG = 0;
                    }
                } else {
                    btn.setBackground(getDrawable(R.mipmap.regiter_btn_on ));
                    FLAG = 0;
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FLAG == 0) {

                } else {
                    if (!(newpwd_edt.getText().toString().equals(newpwdz_edt.getText().toString()))) {
                        newpwdz_layout.setErrorEnabled(true);
                        newpwdz_layout.setError("两次密码不一致");
                    } else {
                        String newpwd = newpwdz_edt.getText().toString();
                        String user_id = UserInfo.getUserId(RlPwdActivity.this);
                        Log.d("TAGTAG",user_id);
                        ServerApi.getRlSetPwd(user_id,NOHTTP_WHAT_TEST,newpwd, onResponseListener);
                    }
                }
            }
        });
        back = (ImageView) findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RlPwdActivity.this.finish();
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
                        Toast.makeText(RlPwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        RlPwdActivity.this.finish();
                    } else {
                        Toast.makeText(RlPwdActivity.this, "修改失败，请重试", Toast.LENGTH_SHORT)
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
            Toast.makeText(RlPwdActivity.this, "注册失败请重试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart(int what) {
            // 请求开始，显示dialog
            showDialog();
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
            closeDialog();
        }
    };
}
