package xml.org.today.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import xml.org.today.R;
import xml.org.today.activity.MainActivity;
import xml.org.today.util.DateFormatUtil;
import xml.org.today.util.ImageLoaderUtil;
import xml.org.today.util.ServerApi;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageItem>  {
    private List<Map<String,String>> mList;
    private Context mContext;
    private String url;
    public MessageAdapter(List<Map<String,String>> list,Context context){
        mList=list;
        mContext=context;
    }
    @Override
    public MessageAdapter.MessageItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,
                parent, false);
        return new MessageItem(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageItem holder, int position) {
        Map<String,String> mMap=mList.get(position);
        Log.d("TAGTAG", "list" + mList.toString());
        holder.mMsgName.setText(mMap.get("from_name")+"评论说：");
        holder.mMsgContent.setText(mMap.get("for_content"));
        holder.mMsgComment.setText(mMap.get("comment"));
        String date=mMap.get("date");
        holder.mMsgDate.setText(DateFormatUtil.getMonth(date) + "月" + DateFormatUtil.getDay(date) + "日  "
                + DateFormatUtil.getTime(date));
        if(!(mMap.get("tx").equals("0"))){
            url= ServerApi.URL+"tx/"+mMap.get("tx");
            holder.mPortrait.setTag(url);
            MainActivity.IMAGELOADERUTIL.getImageByAsyncTask(holder.mPortrait, url, 1);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public  class MessageItem extends RecyclerView.ViewHolder {

        private TextView mMsgName,mMsgComment,mMsgContent,mMsgDate;
        private ImageView mPortrait;
        public MessageItem(View itemView) {
            super(itemView);
            mMsgName= (TextView) itemView.findViewById(R.id.msg_from_name);
            mMsgComment= (TextView) itemView.findViewById(R.id.msg_content);
            mMsgContent= (TextView) itemView.findViewById(R.id.msg_for_content);
            mMsgDate= (TextView) itemView.findViewById(R.id.msg_date);
            mPortrait= (ImageView) itemView.findViewById(R.id.msg_icon);
        }

    }
}
