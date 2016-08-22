package xml.org.today.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import xml.org.today.R;
import xml.org.today.data.CoverData;
import xml.org.today.util.DateFormatUtil;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ContentActivity extends BaseActivity {
    private int mScreenWidth, mScreenHeigth;
    private RelativeLayout mCover, mCotent;
    private ViewGroup.LayoutParams mCoverParams, mContentParams;
    private String id, content, user_id, username, date, imgId, comment_num;
    private ImageView cover_img;
    private TextView cover_writer, cover_date,comment_num_text;
    private TextView content_text;
    private CoverData mCoverData;
    private ScrollView mScrollView;
    private FrameLayout mFab1, mFab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        WindowManager mWm = this.getWindowManager();
        mScreenWidth = mWm.getDefaultDisplay().getWidth();
        mScreenHeigth = mWm.getDefaultDisplay().getHeight();
        initData();
        initView();

    }

    private void initData() {
        Intent it = super.getIntent();
        id = it.getStringExtra("id");
        content = it.getStringExtra("content");
        user_id = it.getStringExtra("user_id");
        username = it.getStringExtra("username");
        date = it.getStringExtra("date");
        imgId = it.getStringExtra("imgId");
        comment_num = it.getStringExtra("comment_num");
    }

    private void initView() {
        mFab1 = (FrameLayout) findViewById(R.id.content_fab1);
        if (comment_num.equals("0")) {
            mFab2 = (FrameLayout) findViewById(R.id.content_fab3);
        } else {
            mFab2 = (FrameLayout) findViewById(R.id.content_fab2);
            comment_num_text= (TextView) findViewById(R.id.comment_num);
            comment_num_text.setText(comment_num);
        }
        mFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentActivity.this.finish();
            }
        });
        mFab2.setOnClickListener(new Fab2OnClick());
        mCover = (RelativeLayout) findViewById(R.id.content_cover);
        mCotent = (RelativeLayout) findViewById(R.id.content_layout);
        mCoverParams = mCover.getLayoutParams();
        mContentParams = mCotent.getLayoutParams();
        mCoverParams.height = mScreenHeigth;
        mCoverParams.width = mScreenWidth;
        mContentParams.height = mScreenHeigth;
        mContentParams.width = mScreenWidth;
        mCover.setLayoutParams(mCoverParams);
        mCotent.setLayoutParams(mContentParams);
        cover_img = (ImageView) findViewById(R.id.content_img);
        cover_writer = (TextView) findViewById(R.id.content_writer);
        cover_date = (TextView) findViewById(R.id.content_date);
        content_text = (TextView) findViewById(R.id.content_text);
        mCoverData = new CoverData();
        cover_img.setImageResource(mCoverData.getImg(Integer.valueOf(imgId)));
        cover_date.setText(DateFormatUtil.dateFormat(date));
        cover_writer.setText(username);
        content_text.setText(content);
        mScrollView = (ScrollView) findViewById(R.id.content_scroll);
        mScrollView.setOnTouchListener(new OnTouch());

    }

    /**
     * 屏幕滑动事件监听
     */
    private class OnTouch implements View.OnTouchListener {

        float y1, y2;
        Boolean flag = true;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                y1 = event.getY();
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                y2 = event.getY();
                if (y1 > y2) {
                    //向下滑动
                    mFab1.setVisibility(View.GONE);
                    mFab2.setVisibility(View.GONE);
                } else if (y2 - y1 > 10) {
                    //向上滑动
                    mFab1.setVisibility(View.VISIBLE);
                    mFab2.setVisibility(View.VISIBLE);
                } else if (y2 == y1) {
                    if (flag) {
                        mFab1.setVisibility(View.VISIBLE);
                        mFab2.setVisibility(View.VISIBLE);
                    } else {
                        flag = true;
                    }

                } else {
                    mFab1.setVisibility(View.GONE);
                    mFab2.setVisibility(View.GONE);
                }

            }
            return false;
        }
    }

    /**
     * 评论按钮点击事件
     */
    private class Fab2OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(ContentActivity.this, CommentActivity.class);
            it.putExtra("id", id);
            it.putExtra("user_id", user_id);
            ContentActivity.this.startActivity(it);
        }
    }

}
