package xml.org.today.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import xml.org.today.R;
import xml.org.today.activity.ContentActivity;
import xml.org.today.activity.MainActivity;
import xml.org.today.util.DateFormatUtil;
import xml.org.today.util.RoundBitmapUtil;
import xml.org.today.util.UserInfo;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MyContentAdapter2 extends RecyclerView.Adapter<MyContentAdapter2.MyContentItem2> {
    private List<Map<String, String>> mList;
    private Context mContext;
    private String mUrl, mUserName;
    private int mFlag;

    public MyContentAdapter2(Context context, List<Map<String, String>> list, int flag, String url, String username) {
        mList = list;
        mContext = context;
        mUrl = url;
        mFlag = flag;
        mUserName = username;
    }

    @Override
    public MyContentItem2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycontent_item2,
                parent, false);
        return new MyContentItem2(view);
    }

    @Override
    public void onBindViewHolder(MyContentItem2 holder, int position) {
        final Map<String, String> mMap = mList.get(position);
        final String username = UserInfo.getUserName(mContext);
        final String num = mMap.get("comment_num");
        final String content = mMap.get("content");
        final String date = mMap.get("date");
        final String month = DateFormatUtil.getMonth(date);
        final String day = DateFormatUtil.getDay(date);
        final String time = DateFormatUtil.getTime(date);
        if (mFlag == 0) {
            holder.mNameView.setText(username);
            if (UserInfo.getUserImg(mContext) == null |
                    BitmapFactory.decodeFile(UserInfo.getUserImg(mContext)) == null) {
                String url = UserInfo.getUserPortraitUrl(mContext);
                holder.mPortrait.setTag(url);
                MainActivity.IMAGELOADERUTIL.getImageByAsyncTask(holder.mPortrait, url, 1);
            } else {
                Bitmap b = BitmapFactory.decodeFile(UserInfo.getUserImg(mContext));
                holder.mPortrait.setImageBitmap(RoundBitmapUtil.toRoundBitmap(b));
            }
        } else {
            holder.mNameView.setText(mUserName);
            MainActivity.IMAGELOADERUTIL.getImageByAsyncTask(holder.mPortrait, mUrl, 1);
        }
        holder.mNumView.setText(num + "条评论");
        holder.mContentView.setText(content);
        holder.mMonthView.setText(month + "月");
        holder.mDayView.setText(day);
        holder.mTimeView.setText(time);
        holder.mLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ContentActivity.class);
                it.putExtra("id", mMap.get("id"));
                it.putExtra("content", content);
                it.putExtra("user_id", mMap.get("user_id"));
                it.putExtra("username", mMap.get("username"));
                it.putExtra("date", date);
                it.putExtra("imgId", mMap.get("imgId"));
                it.putExtra("comment_num", num);
                mContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyContentItem2 extends RecyclerView.ViewHolder {
        TextView mMonthView, mDayView, mTimeView;
        TextView mNameView, mNumView, mContentView;
        ImageView mPortrait;
        LinearLayout mLy;

        public MyContentItem2(View itemView) {
            super(itemView);
            mTimeView = (TextView) itemView.findViewById(R.id.me_date_time);
            mMonthView = (TextView) itemView.findViewById(R.id.me_date_month);
            mDayView = (TextView) itemView.findViewById(R.id.me_date_day);
            mNameView = (TextView) itemView.findViewById(R.id.me_mycontent_name);
            mNumView = (TextView) itemView.findViewById(R.id.me_mycontent_commentnum);
            mContentView = (TextView) itemView.findViewById(R.id.me_content);
            mLy = (LinearLayout) itemView.findViewById(R.id.me_mycontent_ly);
            mPortrait = (ImageView) itemView.findViewById(R.id.me_portrait_img);
        }
    }
}


