package xml.org.today.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;
import java.util.Map;

import xml.org.today.R;
import xml.org.today.activity.ContentActivity;
import xml.org.today.util.DateFormatUtil;


/**
 * Created by Administrator on 2016/7/20.
 */
public class MyContentAdapter extends RecyclerView.Adapter<MyContentAdapter.MyContentItem>{
    private List<Map<String,String>> mList;
    private Context mContext;


    public MyContentAdapter(Context context,List<Map<String, String>> list){
        mContext=context;
        mList=list;
    }
    @Override
    public MyContentItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycontent_item,
                parent, false);
        return new MyContentItem(view);
    }

    @Override
    public void onBindViewHolder(MyContentItem holder, int position) {
        Map<String,String> mMap=mList.get(position);
        final String content=mMap.get("content");
        final String imgId=mMap.get("imgId");
        final String id=mMap.get("id");
        final String date=mMap.get("date");
        final String username=mMap.get("username");
        final String user_id=mMap.get("user_id");
        final String comment_num=mMap.get("comment_num");
        holder.mContentView.setText(content);
        holder.mContentDayView.setText(DateFormatUtil.getDay(date));
        holder.mContentMonthView.setText(DateFormatUtil.getMonth(date)+"月");
        holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mContext, ContentActivity.class);
                it.putExtra("id",id);
                it.putExtra("content",content);
                it.putExtra("imgId",imgId);
                it.putExtra("date",date);
                it.putExtra("username",username);
                it.putExtra("user_id",user_id);
                it.putExtra("comment_num",comment_num);
                mContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public static class MyContentItem extends RecyclerView.ViewHolder{
        TextView mContentView,mContentDayView,mContentMonthView;
        RelativeLayout mContentLayout;
        public MyContentItem(View itemView) {
            super(itemView);
            mContentView= (TextView) itemView.findViewById(R.id.mycontent_content);
            mContentDayView= (TextView) itemView.findViewById(R.id.mycontent_time_day);
            mContentMonthView= (TextView) itemView.findViewById(R.id.mycontent_time_month);
            mContentLayout= (RelativeLayout) itemView.findViewById(R.id.mycontent_layout);
        }
    }

    /*public MyContentAdapter(Context context,int resource,List<Map<String, String>> list){
        mContext=context;
        mList=list;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource=resource;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, null);
        }
        final Map<String,String> mMap=mList.get(position);
        final String content=mMap.get("content");
        final String imgId=mMap.get("imgId");
        final String id=mMap.get("id");
        final String date=mMap.get("date");
        final String username=mMap.get("username");
        final String user_id=mMap.get("user_id");
        mContentView= (TextView) convertView.findViewById(R.id.mycontent_content);
        mContentView.setText(content);
        mContent_ly= (RelativeLayout) convertView.findViewById(R.id.mycontent_layout);
        mMonthView= (TextView) convertView.findViewById(R.id.mycontent_time_month);
        mDayView= (TextView) convertView.findViewById(R.id.mycontent_time_day);
        mMonthView.setText(DateFormatUtil.getMonth(date));
        mDayView.setText(DateFormatUtil.getDay(date));
        mContent_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mContext, ContentActivity.class);
                it.putExtra("id",id);
                it.putExtra("content",content);
                it.putExtra("imgId",imgId);
                it.putExtra("date",date);
                it.putExtra("username",username);
                it.putExtra("user_id",user_id);
                mContext.startActivity(it);
            }
        });
        return convertView;
    }*/
}
