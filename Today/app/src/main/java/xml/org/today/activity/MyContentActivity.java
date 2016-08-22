package xml.org.today.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
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
import xml.org.today.adapter.HeaderAndFooterRecyclerViewAdapter;
import xml.org.today.adapter.MyContentAdapter;
import xml.org.today.listener.EndlessRecyclerOnScrollListener;
import xml.org.today.util.RecyclerViewStateUtils;
import xml.org.today.util.RecyclerViewUtils;
import xml.org.today.util.ServerApi;
import xml.org.today.widget.LoadingFooter;

/**
 * Created by Administrator on 2016/7/20.
 */
public class MyContentActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private int NOHTTP_WHAT_TEST = 0x007;
    private int begin = 0, length = 10;
    private List<Map<String, String>> mList = new ArrayList<>();
    private ImageView goBack;
    private MyContentAdapter mAdapter;
    private JSONArray contentData;
    private SwipeRefreshLayout mRefreshLayout;
    private int mLastItem, mPage = 1;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private int mCurrentCounter = 0;
    private String ResultFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycontent);
        initView();
        ServerApi.getSelectMyContentResult(MyContentActivity.this, String.valueOf(begin),
                String.valueOf(length), NOHTTP_WHAT_TEST,
                onResponseListener);
    }
    private void showList_(JSONObject jsonObject) throws JSONException {
        JSONObject jso = jsonObject;
        Log.d("TAGTAG",jso.toString());
        contentData = jso.getJSONArray("content");
        for (int i = 0; i < contentData.length(); i++) {
            Map<String, String> mMap = new HashMap<>();
            JSONObject data = contentData.getJSONObject(i);
            mMap.put("id", data.getString("id"));
            mMap.put("content", data.getString("content"));
            mMap.put("date", data.getString("date"));
            mMap.put("imgId", data.getString("imgId"));
            mMap.put("user_id", data.getString("user_id"));
            mMap.put("username", data.getString("username"));
            mMap.put("comment_num",data.getString("comment_num"));
            mList.add(mMap);
            mAdapter.notifyDataSetChanged();
        }
        mPage++;

    }
    private void showList(JSONObject jsonObject) throws JSONException {
        mList.clear();
        mAdapter = new MyContentAdapter(mContext, mList);
        mHeaderAndFooterRecyclerViewAdapter=new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        JSONObject jso = jsonObject;
        contentData = jso.getJSONArray("content");
        for (int i = 0; i < contentData.length(); i++) {
            Map<String, String> mMap = new HashMap<>();
            JSONObject data = contentData.getJSONObject(i);
            mMap.put("id", data.getString("id"));
            mMap.put("content", data.getString("content"));
            mMap.put("date", data.getString("date"));
            mMap.put("imgId", data.getString("imgId"));
            mMap.put("user_id", data.getString("user_id"));
            mMap.put("username", data.getString("username"));
            mMap.put("comment_num",data.getString("comment_num"));

            mList.add(mMap);
        }
        mPage++;
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mCurrentCounter=mAdapter.getItemCount();

        closeDialog();
    }

    /**
     * 滑动监听
     */
    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }


        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);

            if (ResultFlag.equals("0")) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(MyContentActivity.this, mRecyclerView,
                        length, LoadingFooter.State.Loading, null);
                new AppendData().execute();
            } else if(ResultFlag.equals("4")){
                //the end
                RecyclerViewStateUtils.setFooterViewState(MyContentActivity.this, mRecyclerView,
                        length, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    /**
     * 数据添加
     */
    public class AppendData extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            int mBegin=countBegin(mPage,length);
            ServerApi.getSelectMyContentResult(MyContentActivity.this,String.valueOf(mBegin),
                    String.valueOf(length),NOHTTP_WHAT_TEST,onResponseListenerAppData);
            return null;
        }
    }
    private void initView() {
        mRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.mycontentrefresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorMain, R.color.white, R.color.colorGray);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        double f = Math.random();
                    }
                }, 3000);
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.mycontent_list);
        mLinearLayoutManager= (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        goBack = (ImageView) findViewById(R.id.mycontent_goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyContentActivity.this.finish();
            }
        });
    }

    /**
     * 计算起始
     *
     * @param page
     * @param length
     * @return
     */
    private int countBegin(int page,int length){
        int begin;
        begin=(page-1)*length;
        return begin;
    }
    /**
     *  添加数据回调对象，接受请求结果
     */
    private OnResponseListener<JSONObject> onResponseListenerAppData = new OnResponseListener<JSONObject>() {
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
                        showList_(jso);
                        ResultFlag="0";
                    }else if(success.equals("4")){
                        ResultFlag="4";
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
            Toast.makeText(MyContentActivity.this, "登录失败请重试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart(int what) {
            // 请求开始，显示dialog
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
        }
    };
    /**
     * 加载数据回调对象，接受请求结果
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
                        showList(jso);
                        ResultFlag="0";
                    }else if(success.equals("4")){
                        ResultFlag="4";
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
            Toast.makeText(MyContentActivity.this, "登录失败请重试", Toast.LENGTH_SHORT).show();
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
