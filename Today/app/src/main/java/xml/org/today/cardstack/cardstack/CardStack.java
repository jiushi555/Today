package xml.org.today.cardstack.cardstack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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
import xml.org.today.adapter.CardsDataAdapter;
import xml.org.today.util.ProgressDialog;
import xml.org.today.util.ServerApi;

import static xml.org.today.activity.MainActivity.*;


public class CardStack extends RelativeLayout {
    private int mColor = -1;
    private int mIndex = 0;
    private int mNumVisible = 4;
    private boolean canSwipe = true;
    private BaseAdapter mAdapter;
    private OnTouchListener mOnTouchListener;
    private CardAnimator mCardAnimator;

    private CardEventListener mEventListener = new DefaultStackEventListener(300);
    private int mContentResource = 0;

    private Context mContext;
    private ProgressDialog mDialog;
    private static final int NOHTTP_WHAT_TEST = 0x006;
    private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
    private JSONArray contentData;
    private CardsDataAdapter mCardAdapter;
    private int flag;    //标记数据加载类型   1、初次加载   2、追加数据
    private int PAGE = 1;
    private int LENGTH = 10;
    private int BEGIN;
    private String mDate;

    public interface CardEventListener {
        //section
        // 0 | 1
        //--------
        // 2 | 3
        // swipe distance, most likely be used with height and width of a view ;

        boolean swipeEnd(int section, float distance);

        boolean swipeStart(int section, float distance);

        boolean swipeContinue(int section, float distanceX, float distanceY);

        void discarded(int mIndex, int direction);

        void topCardTapped();
    }

    public void discardTop(final int direction) {
        mCardAnimator.discard(direction, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                mCardAnimator.initLayout();
                mIndex++;
                loadLast();

                viewCollection.get(0).setOnTouchListener(null);
                viewCollection.get(viewCollection.size() - 1).setOnTouchListener(mOnTouchListener);
                mEventListener.discarded(mIndex - 1, direction);
            }
        });
    }

    public int getCurrIndex() {
        //sync?
        return mIndex;
    }

    //only necessary when I need the attrs from xml, this will be used when inflating layout
    public CardStack(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CardStack);
            mColor = array.getColor(R.styleable.CardStack_backgroundColor, mColor);
            array.recycle();
        }

        //get attrs assign minVisiableNum
        for (int i = 0; i < mNumVisible; i++) {
            addContainerViews();
        }
        mContext = context;
        registerBoradcastReceiver();
        new LoadData().execute();
        setupAnimation();
    }

    private void addContainerViews() {
        FrameLayout v = new FrameLayout(getContext());
        viewCollection.add(v);
        addView(v);
    }

    public void setStackMargin(int margin) {
        mCardAnimator.setStackMargin(margin);
        mCardAnimator.initLayout();
    }

    public void setContentResource(int res) {
        mContentResource = res;
    }

    public void setCanSwipe(boolean can) {
        this.canSwipe = can;
    }

    public void reset(boolean resetIndex) {
        if (resetIndex) mIndex = 0;
        removeAllViews();
        viewCollection.clear();
        for (int i = 0; i < mNumVisible; i++) {
            addContainerViews();
        }
        setupAnimation();
        loadData();
    }

    public void setVisibleCardNum(int visiableNum) {
        mNumVisible = visiableNum;
        reset(false);
    }

    public void setThreshold(int t) {
        mEventListener = new DefaultStackEventListener(t);
    }

    public void setListener(CardEventListener cel) {
        mEventListener = cel;
    }

    public void setupAnimation() {
        final View cardView = viewCollection.get(viewCollection.size() - 1);
        mCardAnimator = new CardAnimator(viewCollection, mColor);
        //final FrameLayout mFrameLayout= (FrameLayout) cardView.findViewById(R.id.content_fly);
        mCardAnimator.initLayout();

        final DragGestureDetector dd = new DragGestureDetector(CardStack.this.getContext(), new DragGestureDetector.DragListener() {

            @Override
            public boolean onDragStart(MotionEvent e1, MotionEvent e2,
                                       float distanceX, float distanceY) {
                if (canSwipe) {
                    mCardAnimator.drag(e1, e2, distanceX, distanceY);
                }
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                final int direction = CardUtils.direction(x1, y1, x2, y2);
                float distance = CardUtils.distance(x1, y1, x2, y2);
                mEventListener.swipeStart(direction, distance);
                return true;
            }

            @Override
            public boolean onDragContinue(MotionEvent e1, MotionEvent e2,
                                          float distanceX, float distanceY) {
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                final int direction = CardUtils.direction(x1, y1, x2, y2);
                if (canSwipe) {
                    mCardAnimator.drag(e1, e2, distanceX, distanceY);
                }
                mEventListener.swipeContinue(direction, Math.abs(x2 - x1), Math.abs(y2 - y1));
                return true;
            }

            @Override
            public boolean onDragEnd(MotionEvent e1, MotionEvent e2) {
                //reverse(e1,e2);
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                float distance = CardUtils.distance(x1, y1, x2, y2);
                final int direction = CardUtils.direction(x1, y1, x2, y2);

                boolean discard = mEventListener.swipeEnd(direction, distance);
                if (discard) {
                    if (canSwipe) {
                        mCardAnimator.discard(direction, new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationEnd(Animator arg0) {
                                mCardAnimator.initLayout();
                                mIndex++;
                                mEventListener.discarded(mIndex, direction);

                                //mIndex = mIndex%mAdapter.getCount();
                                loadLast();
                                viewCollection.get(0).setOnTouchListener(null);
                                viewCollection.get(viewCollection.size() - 1)
                                        .setOnTouchListener(mOnTouchListener);
                            }

                        });
                    }
                } else {
                    if (canSwipe) {
                        mCardAnimator.reverse(e1, e2);
                    }
                }

                if ((countLastItem() - getCurrIndex()) < 6) {
                    new AppendData().execute();
                }
                return true;
            }

            @Override
            public boolean onTapUp() {
                mEventListener.topCardTapped();
                return true;
            }
        }
        );

        mOnTouchListener = new OnTouchListener() {
            private static final String DEBUG_TAG = "MotionEvents";

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                dd.onTouchEvent(event);
                return true;
            }
        };
        cardView.setOnTouchListener(mOnTouchListener);
        //mFrameLayout.setOnTouchListener(mOnTouchListener);
    }

    public int countBegin() {
        int result;
        result = (PAGE - 1) * LENGTH;
        return result;
    }

    public int countLastItem() {
        int result;
        result = (PAGE - 1) * LENGTH;
        return result;
    }

    private DataSetObserver mOb = new DataSetObserver() {
        @Override
        public void onChanged() {
            reset(false);
        }
    };


    //ArrayList

    ArrayList<View> viewCollection = new ArrayList<View>();

    public CardStack(Context context) {
        super(context);
    }

    public void setAdapter(final BaseAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mOb);
        }
        mAdapter = adapter;
        adapter.registerDataSetObserver(mOb);

        loadData();
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public View getTopView() {
        return ((ViewGroup) viewCollection.get(viewCollection.size() - 1)).getChildAt(0);
    }

    private void loadData() {
        for (int i = mNumVisible - 1; i >= 0; i--) {
            ViewGroup parent = (ViewGroup) viewCollection.get(i);
            int index = (mIndex + mNumVisible - 1) - i;
            if (index > mAdapter.getCount() - 1) {
                parent.setVisibility(View.GONE);
            } else {
                View child = mAdapter.getView(index, getContentView(), this);
                parent.addView(child);
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

    private View getContentView() {
        View contentView = null;
        if (mContentResource != 0) {
            LayoutInflater lf = LayoutInflater.from(getContext());
            contentView = lf.inflate(mContentResource, null);
        }
        return contentView;

    }

    private void loadLast() {
        ViewGroup parent = (ViewGroup) viewCollection.get(0);

        int lastIndex = (mNumVisible - 1) + mIndex;


        if (lastIndex > mAdapter.getCount() - 1) {
            parent.setVisibility(View.GONE);
            return;
        }

        View child = mAdapter.getView(lastIndex, getContentView(), parent);
        parent.removeAllViews();
        parent.addView(child);
    }

    public int getStackSize() {
        return mNumVisible;
    }

    /**
     * 添加数据
     */
    class AppendData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            BEGIN = countBegin();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            flag = 2;
            ServerApi.getSelectContentResult(mDate, String.valueOf(BEGIN), String.valueOf(LENGTH),
                    NOHTTP_WHAT_TEST, onResponseListener);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    /**
     * 初始加载数据
     */
    class LoadData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            BEGIN = countBegin();
            mDialog = ProgressDialog.createDialog(mContext);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            flag = 1;
            ServerApi.getSelectContentResult(mDate, String.valueOf(BEGIN), String.valueOf(LENGTH),
                    NOHTTP_WHAT_TEST, onResponseListener);
            return null;
        }


        @Override
        protected void onPostExecute(String s) {

        }
    }

    /**
     * 回调对象，接受请求结果
     */
    private OnResponseListener<JSONObject> onResponseListener = new OnResponseListener<JSONObject>() {
        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            // 请求成功
            String result = response.get().toString();// 响应结果
            try {
                JSONObject jso = new JSONObject(result);
                Log.d("TAGTAG", jso.toString());
                String success = jso.getString("success");
                if (success.equals("0")) {
                    if (flag == 1) {
                        showList(jso);
                    } else {
                        showList_(jso);
                    }
                    PAGE++;
                } else {

                }
            } catch (JSONException e) {
                Toast.makeText(mContext, "网络加载错误", Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }
            // 响应头
            Headers headers = response.getHeaders();
            headers.getResponseCode();// 响应码
            response.getNetworkMillis();// 请求花费的时间
        }

        @Override
        public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
            Toast.makeText(mContext, "网络加载错误", Toast.LENGTH_SHORT)
                    .show();        }

        @Override
        public void onStart(int what) {
            // 请求开始，显示dialog
            Log.d("TAGTAG", "开始");
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
            Log.d("TAGTAG", "结束");
        }


    };

    private void showList_(JSONObject jsonObject) {
        JSONObject jso = jsonObject;
        try {
            contentData = jso.getJSONArray("content");
            for (int i = 0; i < contentData.length(); i++) {
                Map<String, String> map = new HashMap<>();
                JSONObject data = contentData.getJSONObject(i);
                map.put("id", data.getString("id"));
                map.put("content", data.getString("content"));
                map.put("date", data.getString("date"));
                map.put("imgId", data.getString("imgId"));
                map.put("user_id", data.getString("user_id"));
                map.put("username", data.getString("username"));
                map.put("comment_num",data.getString("comment_num"));
                map.put("tx",data.getString("tx"));
                mList.add(map);
                mCardAdapter.notifyDataSetChanged();
                mCardAdapter.add(data.getString("id"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showList(JSONObject jsonObject) {
        mList.clear();
        mCardAdapter = new CardsDataAdapter(mContext, mList);
        JSONObject jso = jsonObject;
        try {
            contentData = jso.getJSONArray("content");
            for (int i = 0; i < contentData.length(); i++) {
                Map<String, String> map = new HashMap<>();
                JSONObject data = contentData.getJSONObject(i);
                map.put("id", data.getString("id"));
                map.put("content", data.getString("content"));
                map.put("date", data.getString("date"));
                map.put("imgId", data.getString("imgId"));
                map.put("user_id", data.getString("user_id"));
                map.put("username", data.getString("username"));
                map.put("comment_num",data.getString("comment_num"));
                map.put("tx",data.getString("tx"));
                mCardAdapter.add(data.getString("id"));
                mList.add(map);
            }
            mCardStack.setAdapter(mCardAdapter);
            mDialog.dismiss();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void selectByDate(String date) {
        mDate=date;
        new LoadData().execute();
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("add")){
                Log.d("TAGTAG", "已发布");
                PAGE = 1;
                LENGTH = 10;
                new LoadData().execute();
            }
        }

    };

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add");
        //注册广播
        mContext.registerReceiver(mBroadcastReceiver,myIntentFilter);
    }
    public List<Map<String,String>> getList(){
        return mList;
    }
}
