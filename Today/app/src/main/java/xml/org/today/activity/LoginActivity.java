package xml.org.today.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
 * Created by Administrator on 2016/6/28.
 */
public class LoginActivity extends BaseActivity {
    private TextInputLayout username_layout;
    private TextInputLayout pwd_layout;
    private EditText username_edt;
    private EditText pwd_edt;
    private TextView button;
    private String username;
    private String pwd;
    private TextView go_register;
    private static int FLAG = 1;
    private static int NOHTTP_WHAT_TEST=0x002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
    }

    private void initView() {
        button = (TextView) findViewById(R.id.login_btn);
        go_register = (TextView) findViewById(R.id.go_register);
        username_layout = (TextInputLayout) findViewById(R.id.login_username_layout);
        pwd_layout = (TextInputLayout) findViewById(R.id.login_pwd_layout);
        username_edt = (EditText) findViewById(R.id.login_username);
        pwd_edt = (EditText) findViewById(R.id.login_pwd);
        username_layout.setHint("输入用户名");
        pwd_layout.setHint("输入密码");
        go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        username_edt.addTextChangedListener(new TextWatcher() {
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
                    if (!(pwd_edt.getText().toString().equals(""))) {
                        button.setBackground(getDrawable(R.mipmap.regiter_btn_off));
                        FLAG = 2;
                    } else {
                        button.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                        FLAG = 1;
                    }
                } else {
                    button.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                    FLAG = 1;
                }
            }
        });
        pwd_edt.addTextChangedListener(new TextWatcher() {
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
                    if (!(username_edt.getText().toString().equals(""))) {
                        button.setBackground(getDrawable(R.mipmap.regiter_btn_off));
                        FLAG = 2;
                    } else {
                        button.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                        FLAG = 1;
                    }
                } else {
                    button.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                    FLAG = 1;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FLAG == 2) {
                    username=username_edt.getText().toString();
                    pwd=pwd_edt.getText().toString();
                    ServerApi.getLoginResult(username,pwd,NOHTTP_WHAT_TEST,onResponseListener);
                } else {

                }
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
                        String user_id=jso.getString("user_id");
                        UserInfo.setUserInfo(username, user_id, LoginActivity.this);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent it=new Intent(LoginActivity.this,MainActivity.class);
                        LoginActivity.this.startActivity(it);
                        LoginActivity.this.finish();
                    } else if(success.equals("2")) {
                        Toast.makeText(LoginActivity.this, "用户未注册", Toast.LENGTH_SHORT)
                                .show();
                        username_layout.setErrorEnabled(true);
                        pwd_layout.setErrorEnabled(false);
                        username_layout.setError("用户未注册");
                    } else if(success.equals("3")){
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT)
                                .show();
                        pwd_layout.setErrorEnabled(true);
                        username_layout.setErrorEnabled(false);
                        pwd_layout.setError("密码错误");
                    }else{
                        Toast.makeText(LoginActivity.this, "登录失败，请重试", Toast.LENGTH_SHORT)
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
            Toast.makeText(LoginActivity.this, "登录失败请重试", Toast.LENGTH_SHORT).show();
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