package xml.org.today.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import xml.org.today.R;
import xml.org.today.activity.MainActivity;
import xml.org.today.util.ServerApi;

/**
 * Created by Administrator on 2016/7/27.
 */
public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.CommentItem> {
    private List<Map<String,String>> mList;
    private Context mContext;
    public CommentItemAdapter(Context context,List<Map<String,String>> list){
        mContext=context;
        mList=list;
    }
    @Override
    public CommentItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,
                parent, false);
        return new CommentItem(view);
    }

    @Override
    public void onBindViewHolder(CommentItem holder, int position) {
        Map<String,String> mMap=mList.get(position);
        final String username=mMap.get("username");
        final String comment=mMap.get("comment");
        final String date=mMap.get("date");
        Log.d("TAGTAG","aa"+mList.toString());
        holder.mDateView.setText(date);
        holder.mCommentView.setText(comment);
        holder.mNameView.setText(username);
        if(!(mMap.get("tx").equals("0"))){
            String url= ServerApi.URL+"tx/"+mMap.get("tx");
            holder.mPortrait.setTag(url);
            MainActivity.IMAGELOADERUTIL.getImageByAsyncTask(holder.mPortrait,url,1);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class CommentItem extends RecyclerView.ViewHolder {

        TextView mDateView,mNameView,mCommentView;
        ImageView mPortrait;
        public CommentItem(View itemView) {
            super(itemView);
            mDateView= (TextView) itemView.findViewById(R.id.commment_date);
            mNameView= (TextView) itemView.findViewById(R.id.comment_username);
            mCommentView= (TextView) itemView.findViewById(R.id.comment_text);
            mPortrait= (ImageView) itemView.findViewById(R.id.mg_tx);
        }

    }
}
