package xml.org.today.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import xml.org.today.R;
import xml.org.today.util.CalendarUtil;
import xml.org.today.util.DateFormatUtil;
import xml.org.today.util.UserInfo;

/**
 * Created by Administrator on 2016/7/27.
 */
public class CommentItemAdapter2 extends RecyclerView.Adapter<CommentItemAdapter2.CommentItem> {
    private List<Map<String,String>> mList;
    private Context mContext;
    public CommentItemAdapter2(Context context, List<Map<String, String>> list){
        mContext=context;
        mList=list;
    }
    @Override
    public CommentItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_comment_item,
                parent, false);
        return new CommentItem(view);
    }

    @Override
    public void onBindViewHolder(CommentItem holder, int position) {
        Map<String,String> mMap=mList.get(position);
        final String id=mMap.get("id");
        final String comment=mMap.get("comment");
        final String for_id=mMap.get("for_id");
        final String date=mMap.get("date");
        final String for_userid=mMap.get("for_userid");
        final String content=mMap.get("content");
        holder.mDayView.setText(DateFormatUtil.getDay(date));
        holder.mMonthView.setText(DateFormatUtil.getMonth(date)+"月");
        holder.mCommentView.setText(UserInfo.getUserName(mContext)+"评论："+comment);
        holder.mContentView.setText(content);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class CommentItem extends RecyclerView.ViewHolder {

        TextView mDayView,mCommentView,mMonthView,mContentView;
        RelativeLayout mContentLy;
        public CommentItem(View itemView) {
            super(itemView);
            mDayView= (TextView) itemView.findViewById(R.id.mecomment_time_day);
            mMonthView= (TextView) itemView.findViewById(R.id.mecomment_time_month);
            mCommentView= (TextView) itemView.findViewById(R.id.me_comment);
            mContentView= (TextView) itemView.findViewById(R.id.mecontent_content);
            mContentLy= (RelativeLayout) itemView.findViewById(R.id.mecontent_layout);
        }

    }
}
