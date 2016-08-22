package xml.org.today.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import xml.org.today.activity.CoverListActivity;
import xml.org.today.data.CoverData;
import xml.org.today.R;

/**
 * Created by Administrator on 2016/7/2.
 */
public class CoverListAdapter extends RecyclerView.Adapter<CoverListAdapter.MasonryView> {
    private CoverData mCoverData;
    private CoverListActivity mContext;
    private static int mPositon;

    public CoverListAdapter(CoverData coverData, CoverListActivity context) {
        mCoverData = coverData;
        mContext = context;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cover_listitem,
                parent, false);
        return new MasonryView(view);
    }

    public int getId(int position) {
        return mPositon;
    }

    @Override
    public void onBindViewHolder(MasonryView holder, final int position) {
        holder.imageView.setImageResource(mCoverData.getImg(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.finishThis(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCoverData.getSize();
    }

    public static class MasonryView extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MasonryView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cover_item_img);
        }

    }
}
