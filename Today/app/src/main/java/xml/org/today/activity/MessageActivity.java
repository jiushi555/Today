package xml.org.today.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import xml.org.today.adapter.HeaderAndFooterRecyclerViewAdapter;
import xml.org.today.adapter.MessageAdapter;
import xml.org.today.util.ServerApi;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MessageActivity extends BaseActivity{
    private ImageView back;
    private RecyclerView mRecyclerView;
    private static final int NOHTTP_WHAT_TEST = 0x012;
    private JSONArray messageData;
    private List<Map<String,String>> mList=new ArrayList<>();
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = null;
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        ServerApi.getSelectMessageResult(NOHTTP_WHAT_TEST,MessageActivity.this,onResponseListener);
    }
    private void initView(){
        back= (ImageView) findViewById(R.id.msg_goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });
        mRecyclerView= (RecyclerView) findViewById(R.id.msg_list);
        mLinearLayoutManager= (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Log.d("TAGTAG",result);
                try {
                    JSONObject jso = new JSONObject(result);
                    String success = jso.getString("success");
                    if(success.equals("0")){
                        messageData=jso.getJSONArray("comment");
                        for(int i=0;i<messageData.length();i++){
                            Map<String,String> mMap= new HashMap<>();
                            JSONObject data=messageData.getJSONObject(i);
                            mMap.put("id",data.getString("id"));
                            mMap.put("comment",data.getString("comment"));
                            mMap.put("for_id",data.getString("for_id"));
                            mMap.put("date",data.getString("date"));
                            mMap.put("from_name",data.getString("from_name"));
                            mMap.put("for_content",data.getString("for_content"));
                            mMap.put("tx",data.getString("tx"));
                            mList.add(mMap);
                        }
                        Log.d("TAGTAG","listlist"+mList.toString());
                        mAdapter=new MessageAdapter(mList,MessageActivity.this);
                        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
                        mRecyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);
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
