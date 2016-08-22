package xml.org.today.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import xml.org.today.data.CoverData;
import xml.org.today.R;
import xml.org.today.util.ServerApi;

/**
 * Created by Administrator on 2016/7/1.
 */
public class AddActivity extends BaseActivity {
    private ImageView back;
    private ImageView cover_btn;
    private FloatingActionButton content_btn;
    private CoverData coverData;
    private TextView btn_sure;
    private TextView content_text;
    private String content,imgId;
    private static final int NOHTTP_WHAT_TEST = 0x005;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        initView();
    }

    private void initView() {
        coverData = new CoverData();
        back = (ImageView) findViewById(R.id.goback);
        cover_btn = (ImageView) findViewById(R.id.cover_img);
        content_btn = (FloatingActionButton) findViewById(R.id.content_btn);
        btn_sure = (TextView) findViewById(R.id.add_sure_btn);
        content_text = (TextView) findViewById(R.id.add_content_text);
        content_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddActivity.this, AddEditText.class);
                it.putExtra("FLAG", "0");
                it.putExtra("content", content_text.getText().toString());
                AddActivity.this.startActivityForResult(it, 1);
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content_text.getText().toString().equals("")) {
                    Toast.makeText(AddActivity.this, "发布内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    ServerApi.getAddContentResult(imgId,content,AddActivity.this,NOHTTP_WHAT_TEST,
                            onResponseListener);
                }
            }
        });
        content_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddActivity.this, AddEditText.class);
                it.putExtra("FLAG", "1");
                AddActivity.this.startActivityForResult(it, 1);
            }
        });
        cover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(AddActivity.this, CoverListActivity.class);
                AddActivity.this.startActivityForResult(mIntent, 0);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.this.finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            switch (resultCode) {
                case RESULT_OK:
                    Bundle b = data.getExtras();
                    int str = b.getInt("imgId");
                    imgId=String.valueOf(b.getInt("imgId"));
                    cover_btn.setImageResource(coverData.getImg(str));
                    break;
                default:
                    break;
            }
        } else {
            switch (resultCode) {
                case RESULT_OK:
                    Bundle b = data.getExtras();
                    content = b.getString("content");
                    content_btn.setVisibility(View.GONE);
                    content_text.setVisibility(View.VISIBLE);
                    content_text.setText(content);
                    break;
                default:
                    break;
            }
        }

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
                        Toast.makeText(AddActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        Intent it=new Intent();
                        it.setAction("add");
                        sendBroadcast(it);
                        AddActivity.this.finish();
                    } else {
                        Toast.makeText(AddActivity.this, "发布失败，请重试", Toast.LENGTH_SHORT)
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
            Toast.makeText(AddActivity.this, "发布失败，请重试", Toast.LENGTH_SHORT).show();
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
