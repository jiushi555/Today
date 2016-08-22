package xml.org.today.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xml.org.today.R;
import xml.org.today.adapter.CommentItemAdapter;
import xml.org.today.adapter.HeaderAndFooterRecyclerViewAdapter;
import xml.org.today.util.ServerApi;

/**
 * Created by Administrator on 2016/7/27.
 */
public class CommentActivity extends BaseActivity {
    private ImageView back;
    private TextView title;
    private EditText mEdt;
    private Button mBtn;
    private String for_id, for_userid;
    private static final int NOHTTP_WHAT_TEST = 0x008;
    private RecyclerView mRecyclerView;
    private static final int NOHTTP_WHAT_TEST2 = 0x009;
    private JSONArray commentData;
    private List<Map<String, String>> mList = new ArrayList<>();
    private CommentItemAdapter mAdapter;
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = null;
    private LinearLayoutManager mLinearLayoutManager;
    private RelativeLayout mRl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initData();
        initView();
        ServerApi.getSelectCommentResult(NOHTTP_WHAT_TEST2, for_id, onResponseListener2);
    }

    private void initData() {
        Intent it = super.getIntent();
        for_id = it.getStringExtra("id");
        for_userid = it.getStringExtra("user_id");
    }

    private void initView() {
        mRl= (RelativeLayout) findViewById(R.id.no_comment_ly);
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_list);
        mLinearLayoutManager= (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        back = (ImageView) findViewById(R.id.comment_goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.this.finish();
            }
        });
        title = (TextView) findViewById(R.id.comment_title_text);
        mEdt = (EditText) findViewById(R.id.comment_edt);
        mBtn = (Button) findViewById(R.id.comment_btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mEdt.getText().toString().trim();
                ServerApi.getAddCommentResult(NOHTTP_WHAT_TEST, CommentActivity.this, comment,
                        for_id, for_userid, onResponseListener);
            }
        });
        mEdt.addTextChangedListener(new EdtTextWatcher());
    }

    private void showList(JSONObject jsonObject) throws JSONException {
        mList.clear();
        JSONObject jso = jsonObject;
        Log.d("TAGTAG", jso.toString());
        commentData = jso.getJSONArray("content");
        for (int i = 0; i < commentData.length(); i++) {
            Map<String, String> map = new HashMap<>();
            JSONObject data = commentData.getJSONObject(i);
            map.put("comment", data.getString("comment"));
            map.put("username", data.getString("username"));
            map.put("date", data.getString("date"));
            map.put("tx",data.getString("tx"));
            mList.add(map);
        }
        Log.d("TAGTAG", "ad" + mList.toString());
        mAdapter = new CommentItemAdapter(CommentActivity.this, mList);
        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
        closeDialog();
    }

    /**
     * 查询回调对象，接受请求结果
     */
    private OnResponseListener<JSONObject> onResponseListener2 = new OnResponseListener<JSONObject>() {
        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == NOHTTP_WHAT_TEST2) {// 判断what是否是刚才指定的请求
                // 请求成功
                String result = response.get().toString();// 响应结果
                try {
                    JSONObject jso = new JSONObject(result);
                    Log.d("TAGTAG", result.toString());
                    String success = jso.getString("success");
                    if (success.equals("0")) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mRl.setVisibility(View.GONE);
                        String comment_num=jso.getString("comment_num");
                        title.setText(comment_num+"条评论");
                        showList(jso);
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        mRl.setVisibility(View.VISIBLE);
                        closeDialog();
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
            showNetError();
        }

        @Override
        public void onStart(int what) {
            // 请求开始，显示dialog
            showDialog();
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
        }


    };
    /**
     * 发布回调对象，接受请求结果
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
                    Log.d("TAGTAG", result.toString());
                    String success = jso.getString("success");
                    if (success.equals("0")) {
                        Toast.makeText(CommentActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        Intent it=new Intent();
                        it.setAction("addcomment");
                        CommentActivity.this.finish();
                    } else {
                        Toast.makeText(CommentActivity.this, "发布失败，请重试", Toast.LENGTH_SHORT)
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
            showNetError();
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

    private class EdtTextWatcher implements android.text.TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String commentNr = mEdt.getText().toString().trim();
            if (commentNr.length() > 0) {
                mBtn.setEnabled(true);
            } else {
                mBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
