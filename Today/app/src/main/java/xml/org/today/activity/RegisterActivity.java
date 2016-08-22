package xml.org.today.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
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
public class RegisterActivity extends BaseActivity {
    private ImageButton back;
    private TextInputLayout username_layout;
    private TextInputLayout pwd_layout;
    private TextInputLayout pwdz_layout;
    private EditText pwd_edt;
    private EditText pwdz_edt;
    private EditText username_edt;
    private TextView btn;
    private static int FLAG = 1;
    private static final int NOHTTP_WHAT_TEST = 0x001;
    private String username;
    private String pwd;
    private String pwdz;
    private TextView go_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        NoHttp.init(getApplication());
        initView();
    }

    private void initView() {
        go_login= (TextView) findViewById(R.id.go_login);
        username_layout = (TextInputLayout) findViewById(R.id.username_layout);
        username_layout.setHint("输入用户名");
        pwd_layout = (TextInputLayout) findViewById(R.id.pwd_layout);
        pwd_layout.setHint("输入密码");
        pwdz_layout = (TextInputLayout) findViewById(R.id.pwdz_layout);
        pwdz_layout.setHint("再次输入密码");
        username_edt = (EditText) findViewById(R.id.register_username);
        pwd_edt = (EditText) findViewById(R.id.register_pwd);
        pwdz_edt = (EditText) findViewById(R.id.register_pwdz);
        back = (ImageButton) findViewById(R.id.register_goback);
        btn = (TextView) findViewById(R.id.register_btn);
        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(RegisterActivity.this,LoginActivity.class);
                RegisterActivity.this.startActivity(it);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FLAG == 1) {

                } else {
                    username = username_edt.getText().toString();
                    pwd = pwd_edt.getText().toString();
                    pwdz = pwdz_edt.getText().toString();
                    if (pwd.equals(pwdz)) {
                        ServerApi.getRegisterResult(username, pwd, NOHTTP_WHAT_TEST, onResponseListener);
                    } else {
                        pwdz_layout.setErrorEnabled(true);
                        pwdz_layout.setError("两次密码不一致");
                    }
                }
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
                    if (!(username_edt.getText().toString().equals("")) &&
                            !(pwd_edt.getText().toString().equals("")) &&
                            !(pwdz_edt.getText().toString().equals(""))) {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_off));
                        FLAG = 2;
                    } else {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                        FLAG = 1;
                    }
                } else {
                    btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
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
                    if (!(username_edt.getText().toString().equals("")) &&
                            !(pwd_edt.getText().toString().equals("")) &&
                            !(pwdz_edt.getText().toString().equals(""))) {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_off));
                        FLAG = 2;
                    } else {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                        FLAG = 1;
                    }
                } else {
                    btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                    FLAG = 1;
                }
            }
        });
        pwdz_edt.addTextChangedListener(new TextWatcher() {
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
                    if (!(username_edt.getText().toString().equals("")) &&
                            !(pwd_edt.getText().toString().equals("")) &&
                            !(pwdz_edt.getText().toString().equals(""))) {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_off));
                        FLAG = 2;
                    } else {
                        btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                        FLAG = 1;
                    }
                } else {
                    btn.setBackground(getDrawable(R.mipmap.regiter_btn_on));
                    FLAG = 1;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
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
                        String username=jso.getString("username");
                        UserInfo.setUserInfo(username, user_id, RegisterActivity.this);
                        Intent it=new Intent(RegisterActivity.this,LoadPortraitActivity.class);
                        RegisterActivity.this.startActivity(it);
                        RegisterActivity.this.finish();
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT)
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
            Toast.makeText(RegisterActivity.this, "注册失败请重试", Toast.LENGTH_SHORT).show();
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
