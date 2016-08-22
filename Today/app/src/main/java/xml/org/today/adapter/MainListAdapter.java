package xml.org.today.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import xml.org.today.R;
import xml.org.today.activity.ContentActivity;
import xml.org.today.activity.MainActivity;
import android.support.v7.widget.CardView;

import xml.org.today.activity.MeActivity;
import xml.org.today.data.CoverData;
import xml.org.today.util.ServerApi;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListItem> {
    private List<Map<String, String>> mList;
    private Context mContext;
    private CoverData coverData = new CoverData();

    public MainListAdapter(Context context, List<Map<String, String>> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public MainListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mian_content_item,
                parent, false);
        return new MainListItem(view);
    }

    @Override
    public void onBindViewHolder(MainListItem holder, int position) {
        Map<String, String> mMap = mList.get(position);
        final String content = mMap.get("content");
        final String imgId = mMap.get("imgId");
        final String id = mMap.get("id");
        final String date = mMap.get("date");
        final String username = mMap.get("username");
        final String user_id = mMap.get("user_id");
        final String comment_num = mMap.get("comment_num");
        final String tx = mMap.get("tx");
        holder.content.setText(content);
        holder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ContentActivity.class);
                it.putExtra("id", id);
                it.putExtra("content", content);
                it.putExtra("imgId", imgId);
                it.putExtra("date", date);
                it.putExtra("username", username);
                it.putExtra("user_id", user_id);
                it.putExtra("comment_num", comment_num);
                mContext.startActivity(it);
            }
        });
        /*holder.l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ContentActivity.class);
                it.putExtra("id", id);
                it.putExtra("content", content);
                it.putExtra("imgId", imgId);
                it.putExtra("date", date);
                it.putExtra("username", username);
                it.putExtra("user_id", user_id);
                it.putExtra("comment_num", comment_num);
                mContext.startActivity(it);
            }
        });*/
        holder.content_bg.setImageResource(coverData.getImg(Integer.valueOf(imgId)));
        if (!(tx.equals("0"))) {
            String url = ServerApi.URL + "tx/" + tx;
            holder.portrait.setTag(url);
            MainActivity.IMAGELOADERUTIL.getImageByAsyncTask(holder.portrait, url, 1);
        }
        holder.content_name.setText(username);
        holder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mContext, MeActivity.class);
                it.putExtra("user_id",user_id);
                it.putExtra("url",ServerApi.URL+"tx/"+tx);
                it.putExtra("username",username);
                mContext.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MainListItem extends RecyclerView.ViewHolder {

        private TextView content_name, content;
        private ImageView portrait, content_bg;
        private LinearLayout l;
        private RelativeLayout r;
        private CardView card;
        private FrameLayout fl;
        public MainListItem(View itemView) {
            super(itemView);
            r= (RelativeLayout) itemView.findViewById(R.id.test_layout);
            content_bg = (ImageView) itemView.findViewById(R.id.content_bg);
            content_name = (TextView) itemView.findViewById(R.id.content_name);
            //l = (LinearLayout) itemView.findViewById(R.id.content_link_ly);
            portrait = (ImageView) itemView.findViewById(R.id.content_portrait);
            content = (TextView) itemView.findViewById(R.id.content);
            card= (CardView) itemView.findViewById(R.id.card);
            fl= (FrameLayout) itemView.findViewById(R.id.content_fly);
        }

    }
}
