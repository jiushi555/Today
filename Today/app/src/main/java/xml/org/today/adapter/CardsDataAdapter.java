package xml.org.today.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.okhttp.internal.Util;

import java.util.List;
import java.util.Map;

import xml.org.today.R;
import xml.org.today.activity.ContentActivity;
import xml.org.today.activity.MainActivity;
import xml.org.today.activity.MeActivity;
import xml.org.today.data.CoverData;
import xml.org.today.util.DateFormatUtil;
import xml.org.today.util.ServerApi;

public class CardsDataAdapter extends ArrayAdapter<String> {
    private List<Map<String, String>> mList;
    private CoverData coverData = new CoverData();
    private Context mContext;
    private String url="0";
    public CardsDataAdapter(Context context, List<Map<String, String>> list) {
        super(context, R.layout.card_content);
        mList = list;
        mContext=context;
    }


    @Override
    public View getView(int position, final View contentView, ViewGroup parent) {
        Map<String, String> mMap = mList.get(position);
        TextView v = (TextView) (contentView.findViewById(R.id.content));
        final String content=mMap.get("content");
        final String imgId=mMap.get("imgId");
        final String id=mMap.get("id");
        final String date= DateFormatUtil.getMonth(mMap.get("date"))+"月"
                +DateFormatUtil.getDay(mMap.get("date"))+"日";
        final String username=mMap.get("username");
        final String user_id=mMap.get("user_id");
        final String comment_num=mMap.get("comment_num");
        final String tx=mMap.get("tx");
        v.setText(content);
        ImageView content_bg= (ImageView) contentView.findViewById(R.id.content_bg);
        TextView content_name= (TextView) contentView.findViewById(R.id.content_name);
        LinearLayout l= (LinearLayout) contentView.findViewById(R.id.content_link_ly);
        ImageView portrait= (ImageView) contentView.findViewById(R.id.content_portrait);
        TextView content_date= (TextView) contentView.findViewById(R.id.content_date);
        content_date.setText(date);
        l.setOnClickListener(new View.OnClickListener() {
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
        content_bg.setImageResource(coverData.getImg(Integer.valueOf(imgId)));
        if(!(tx.equals("0"))){
            url= ServerApi.URL+"tx/"+tx;
            portrait.setTag(url);
            MainActivity.IMAGELOADERUTIL.getImageByAsyncTask(portrait,url,1);
        }
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mContext, MeActivity.class);
                it.putExtra("user_id",user_id);
                it.putExtra("url",ServerApi.URL+"tx/"+tx);
                it.putExtra("username",username);
                mContext.startActivity(it);
            }
        });
        content_name.setText(username);
        /*f.setOnClickListener(new View.OnClickListener() {
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
        });*/
        Log.d("TAGTAG", getItem(position));
        return contentView;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

