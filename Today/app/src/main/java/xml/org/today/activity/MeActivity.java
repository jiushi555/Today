package xml.org.today.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import xml.org.today.R;
import xml.org.today.helper.SystemBarHelper;
import xml.org.today.util.RoundBitmapUtil;
import xml.org.today.util.ServerApi;
import xml.org.today.util.UserInfo;
import xml.org.today.widget.SimpleFragment;
import xml.org.today.widget.SimpleFragment2;
import xml.org.today.widget.SimpleFragment3;

/**
 * Created by Administrator on 2016/7/22.
 */
public class MeActivity extends BaseActivity2 {
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.appbar)
    AppBarLayout mAppbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    @Bind(R.id.nickname)
    TextView mNickname;
    @Bind(R.id.fab)
    ImageView mfab;
    private static final int NOHTTP_WHAT_TEST1 = 0x010;
    private static final int NOHTTP_WHAT_TEST2 = 0x011;
    private JSONArray contentData1,contentData2;
    private List<Map<String,String>> mList1=new ArrayList<>();
    private List<Map<String,String>> mList2=new ArrayList<>();
    private static SimpleViewPagerAdapter adapter;
    private int flag=0;   //标记是查看自己还是查看他人  0、自己 1、他人
    private String url,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        if(super.getIntent().getStringExtra("user_id")!=null){
            if(super.getIntent().getStringExtra("user_id").equals(UserInfo.getUserId(MeActivity.this))){
                ServerApi.getSelectMyContentResult(MeActivity.this, "0", "1000", NOHTTP_WHAT_TEST1, onResponseListener);
            }else {
                ServerApi.getSelectOtherContentResult(super.getIntent().getStringExtra("user_id"),
                        "0", "1000", NOHTTP_WHAT_TEST1, onResponseListener3);
                flag=1;
                url=super.getIntent().getStringExtra("url");
                username=super.getIntent().getStringExtra("username");
            }

        }else{
            ServerApi.getSelectMyContentResult(MeActivity.this, "0", "1000", NOHTTP_WHAT_TEST1, onResponseListener);
        }

        initView();
    }
    private void initView(){
        if(flag==0){
            if (UserInfo.getUserImg(mContext) == null  |
                    BitmapFactory.decodeFile(UserInfo.getUserImg(mContext)) == null) {
                String url = UserInfo.getUserPortraitUrl(mContext);
                mfab.setTag(url);
                MainActivity.IMAGELOADERUTIL.getImageByAsyncTask(mfab, url, 1);
            } else {
                Bitmap b = BitmapFactory.decodeFile(UserInfo.getUserImg(mContext));
                mfab.setImageBitmap(RoundBitmapUtil.toRoundBitmap(b));
            }
        }else{
            if(!(url.equals("0"))){
                MainActivity.IMAGELOADERUTIL.getImageByAsyncTask(mfab, url, 1);
            }
        }

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(MeActivity.this,ResetMeActivity.class);
                MeActivity.this.startActivity(it);
            }
        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeActivity.this.finish();
            }
        });

        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mToolbar);

        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                boolean showTitle = mCollapsingToolbar.getHeight() + verticalOffset <= mToolbar.getHeight();
                boolean showTitle = mCollapsingToolbar.getHeight() + verticalOffset <= mToolbar.getHeight() * 2;
                mNickname.setVisibility(showTitle ? View.VISIBLE : View.GONE);
                mfab.setVisibility(showTitle ? View.GONE : View.VISIBLE);
            }
        });

        adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(SimpleFragment.newInstance("我的发布"), "我的发布");

    }

    /**
     * 加载我的发布回调对象，接受请求结果
     */
    private OnResponseListener<JSONObject> onResponseListener2 = new OnResponseListener<JSONObject>() {
        @SuppressWarnings("unused")

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == NOHTTP_WHAT_TEST2) {// 判断what是否是刚才指定的请求
                // 请求成功
                String result = response.get().toString();// 响应结果
                Log.d("TAGTAG","ss"+result);
                try {
                    JSONObject jso = new JSONObject(result);
                    String success = jso.getString("success");
                    if (success.equals("0")){
                        contentData2=jso.getJSONArray("comment");
                        Log.d("TAGTAG","ss"+contentData2.toString());
                        for(int i=0;i<contentData2.length();i++){
                            Map<String,String> mMap=new HashMap<>();
                            JSONObject data=contentData2.getJSONObject(i);
                            Log.d("TAGTAG","ss啊"+contentData2.getJSONObject(i).toString());
                            mMap.put("id",data.getString("id"));
                            mMap.put("comment",data.getString("comment"));
                            mMap.put("for_id",data.getString("for_id"));
                            mMap.put("date",data.getString("date"));
                            mMap.put("for_userid",data.getString("for_userid"));
                            mMap.put("content",data.getString("content"));
                            mList2.add(mMap);
                        }
                        adapter.addFragment(new SimpleFragment2("我的评论", mList2), "我的评论");
                        mViewpager.setAdapter(adapter);
                        mTabs.setupWithViewPager(mViewpager);
                    }else if(success.equals("4")){
                        adapter.addFragment(new SimpleFragment3(2), "我的评论");
                        mViewpager.setAdapter(adapter);
                        mTabs.setupWithViewPager(mViewpager);
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

        }

        @Override
        public void onFinish(int what) {
            closeDialog();
        }


    };
    /**
     * 加载他人的发布回调对象，接受请求结果
     */
    private OnResponseListener<JSONObject> onResponseListener3 = new OnResponseListener<JSONObject>() {
        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == NOHTTP_WHAT_TEST1) {// 判断what是否是刚才指定的请求
                // 请求成功
                String result = response.get().toString();// 响应结果
                Log.d("TAGTAG","other"+result);
                try {
                    JSONObject jso = new JSONObject(result);
                    String success = jso.getString("success");
                    if(success.equals("0")){
                        contentData1=jso.getJSONArray("content");
                        for (int i=0;i<contentData1.length();i++){
                            Map<String,String> mMap=new HashMap<>();
                            JSONObject data=contentData1.getJSONObject(i);
                            mMap.put("id",data.getString("id"));
                            mMap.put("content",data.getString("content"));
                            mMap.put("imgId",data.getString("imgId"));
                            mMap.put("date",data.getString("date"));
                            mMap.put("comment_num",data.getString("comment_num"));
                            mMap.put("user_id",data.getString("user_id"));
                            mMap.put("username",data.getString("username"));
                            mList1.add(mMap);
                        }
                        adapter.addFragment(new SimpleFragment("发布", mList1,flag,url,username), "发布");
                        mViewpager.setAdapter(adapter);
                        mTabs.setupWithViewPager(mViewpager);
                    }else if(success.equals("4")){
                        adapter.addFragment(new SimpleFragment3(1), "发布");
                        mViewpager.setAdapter(adapter);
                        mTabs.setupWithViewPager(mViewpager);
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
            showDialog();
        }

        @Override
        public void onFinish(int what) {
            closeDialog();
        }


    };
    /**
     * 加载我的发布回调对象，接受请求结果
     */
    private OnResponseListener<JSONObject> onResponseListener = new OnResponseListener<JSONObject>() {
        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == NOHTTP_WHAT_TEST1) {// 判断what是否是刚才指定的请求
                // 请求成功
                String result = response.get().toString();// 响应结果
                //Log.d("TAGTAG",result);
                try {
                    JSONObject jso = new JSONObject(result);
                    String success = jso.getString("success");
                    if(success.equals("0")){
                        contentData1=jso.getJSONArray("content");
                        for (int i=0;i<contentData1.length();i++){
                            Map<String,String> mMap=new HashMap<>();
                            JSONObject data=contentData1.getJSONObject(i);
                            mMap.put("id",data.getString("id"));
                            mMap.put("content",data.getString("content"));
                            mMap.put("imgId",data.getString("imgId"));
                            mMap.put("date",data.getString("date"));
                            mMap.put("comment_num",data.getString("comment_num"));
                            mMap.put("user_id",data.getString("user_id"));
                            mMap.put("username",data.getString("username"));
                            mList1.add(mMap);
                        }
                        adapter.addFragment(new SimpleFragment("我的发布", mList1,flag,url,username), "我的发布");
                    }else if(success.equals("4")){
                        adapter.addFragment(new SimpleFragment3(1), "我的发布");
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
            showDialog();
        }

        @Override
        public void onFinish(int what) {
            ServerApi.getSelectMyCommentResult(NOHTTP_WHAT_TEST2, MeActivity.this, onResponseListener2);
        }


    };

    class SimpleViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public SimpleViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
