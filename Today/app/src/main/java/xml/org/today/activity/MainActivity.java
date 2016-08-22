package xml.org.today.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xml.org.today.adapter.HeaderAndFooterRecyclerViewAdapter;
import xml.org.today.adapter.MainListAdapter;
import xml.org.today.cardstack.cardstack.CardStack;
import xml.org.today.R;
import xml.org.today.util.CalendarUtil;
import xml.org.today.util.ImageLoaderUtil;
import xml.org.today.util.RoundBitmapUtil;
import xml.org.today.util.ServerApi;
import xml.org.today.util.ShareApi;
import xml.org.today.util.UserInfo;
import xml.org.today.widget.RippleView;


public class MainActivity extends BaseActivity2
        implements View.OnClickListener {
    public static CardStack mCardStack;
    private FloatingActionButton btn_add;
    private String date;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private String username;
    private String user_id;
    private static int PAGE = 1;
    private static int LENGTH = 10;
    private static int BEGIN;
    private static int LASTITEM;
    public static ImageView head_view, mian_list, go_list, go_card;
    private TextView name_view;
    private static final int NOHTTP_WHAT_TEST = 0x014;
    private int NOHTTP_FLAG = 0x017;

    public String TX_URL;
    public static ImageLoaderUtil IMAGELOADERUTIL = new ImageLoaderUtil();
    private LinearLayout mDrawList;
    private RippleView mGoContent, mGoMessage, mGoDate, mGoSet, mGoShare;
    private ImageView mPoint1;
    private TextView mPointSet, mPointMessage, mPoint2;
    public static boolean CHECKVERSION;
    public static String VERSIONURL;

    private List<Map<String, String>> mList;
    private RecyclerView mRecyclerView;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private MainListAdapter mMainListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
    }


    private void initView() {
        mian_list = (ImageView) findViewById(R.id.mian_list);
        go_list = (ImageView) findViewById(R.id.go_list);
        go_card = (ImageView) findViewById(R.id.go_card);

        mPoint1 = (ImageView) findViewById(R.id.notive_point);
        mPoint2 = (TextView) findViewById(R.id.message_point);
        mPointSet = (TextView) findViewById(R.id.mian_set_point);
        mPointMessage = (TextView) findViewById(R.id.mian_message_point);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mian_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        name_view = (TextView) this.findViewById(R.id.mian_name);
        name_view.setText(UserInfo.getUserName(MainActivity.this));
        head_view = (ImageView) this.findViewById(R.id.mian_head);
        if (UserInfo.getUserImg(MainActivity.this) == null |
                BitmapFactory.decodeFile(UserInfo.getUserImg(MainActivity.this)) == null) {
            ServerApi.getPortraitNameResult(NOHTTP_WHAT_TEST, MainActivity.this, onResponseListener);
        } else {
            Bitmap b = BitmapFactory.decodeFile(UserInfo.getUserImg(MainActivity.this));
            head_view.setImageBitmap(RoundBitmapUtil.toRoundBitmap(b));
        }
        ServerApi.getCheckVersionResult(NOHTTP_FLAG, MainActivity.this, onResponseListener2);
        head_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, MeActivity.class);
                startActivity(it);
            }
        });
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, AddActivity.class);
                MainActivity.this.startActivity(it);
            }
        });

        //显示CardStack
        mCardStack = (CardStack) findViewById(R.id.container_);
        mCardStack.setContentResource(R.layout.card_content);
        mCardStack.setStackMargin(20);

        mGoContent = (RippleView) this.findViewById(R.id.mian_content_ly);
        mGoMessage = (RippleView) this.findViewById(R.id.mian_message_layout);
        mGoDate = (RippleView) this.findViewById(R.id.mian_date_layout);
        mGoSet = (RippleView) this.findViewById(R.id.mian_set_layout);
        mGoShare = (RippleView) this.findViewById(R.id.mian_share_layout);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.maincontent_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.maincontent_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        double f = Math.random();
                    }
                }, 3000);
            }
        });

        mGoContent.setOnClickListener(this);
        mGoMessage.setOnClickListener(this);
        mGoDate.setOnClickListener(this);
        mGoSet.setOnClickListener(this);
        mGoShare.setOnClickListener(this);
        if (mCardStack.getAdapter() != null) {
            Log.i("MyActivity", "Card Stack size: " + mCardStack.getAdapter().getCount());
        }

        if (mList == null) {
            mList = mCardStack.getList();
        }
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mMainListAdapter = new MainListAdapter(MainActivity.this, mList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mMainListAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);


        go_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardStack.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                go_list.setVisibility(View.GONE);
                go_card.setVisibility(View.VISIBLE);
            }
        });

        go_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardStack.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                go_list.setVisibility(View.VISIBLE);
                go_card.setVisibility(View.GONE);
            }
        });


    }

    /**
     * 检查版本回调对象
     */
    private OnResponseListener<JSONObject> onResponseListener2 = new OnResponseListener<JSONObject>() {
        @Override
        public void onStart(int i) {

        }

        @Override
        public void onSucceed(int i, Response<JSONObject> response) {
            if (i == NOHTTP_FLAG) {
                String result = response.get().toString();
                try {
                    JSONObject jso = new JSONObject(result);
                    Log.d("TAGTAG", jso.toString());
                    String success = jso.getString("success");
                    if (success.equals("0")) {
                        String version = jso.getString("version");
                        String url = jso.getString("url");
                        String message = jso.getString("message");
                        Log.d("TAGTAG", "netversion" + version);
                        Log.d("TAGTAG", "bdversion" + String.valueOf(getVersion()));
                        if (message.equals("0")) {
                            if (version.equals(String.valueOf(getVersion()))) {
                                mPoint1.setVisibility(View.GONE);
                                CHECKVERSION = false;
                            } else {
                                mPoint1.setVisibility(View.VISIBLE);
                                mPointSet.setVisibility(View.VISIBLE);
                                CHECKVERSION = true;
                                VERSIONURL = url;
                            }
                            mPointMessage.setVisibility(View.GONE);
                            mPoint2.setVisibility(View.GONE);
                        } else {
                            mPoint2.setVisibility(View.VISIBLE);
                            mPoint2.setText(message);
                            mPointMessage.setVisibility(View.VISIBLE);
                            mPointMessage.setText(message);
                            if (version.equals(String.valueOf(getVersion()))) {
                                CHECKVERSION = false;
                            } else {
                                mPointSet.setVisibility(View.VISIBLE);
                                CHECKVERSION = true;
                                VERSIONURL = url;
                            }
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "网络请求失败，检查网络", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
            Toast.makeText(MainActivity.this, "网络请求失败，检查网络", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onFinish(int i) {

        }
    };

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
                        if (!(jso.getString("tx").equals("0"))) {
                            TX_URL = ServerApi.URL + "tx/" + jso.getString("tx");
                            UserInfo.setUserPortraitUrl(TX_URL, MainActivity.this);
                            head_view.setTag(TX_URL);
                            IMAGELOADERUTIL.getImageByAsyncTask(head_view, TX_URL, 1);
                        }
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
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
        }


    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return false;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mian_content_ly:
                Intent content_it = new Intent(MainActivity.this, MyContentActivity.class);
                MainActivity.this.startActivity(content_it);
                break;
            case R.id.mian_message_layout:
                mPoint2.setVisibility(View.GONE);
                mPointMessage.setVisibility(View.GONE);
                Intent message_it = new Intent(MainActivity.this, MessageActivity.class);
                MainActivity.this.startActivity(message_it);
                break;
            case R.id.mian_date_layout:
                new CalendarUtil(MainActivity.this, mCardStack);
                break;
            case R.id.mian_set_layout:
                Intent set_it = new Intent(MainActivity.this, SetActivity.class);
                MainActivity.this.startActivity(set_it);
                break;
            case R.id.mian_share_layout:
                ShareApi.showShare(MainActivity.this);
                break;
        }
    }

    private int getVersion() {
        PackageManager packageManager = getPackageManager();
        String versionName;
        int versionCode;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
