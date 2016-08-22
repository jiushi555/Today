package xml.org.today.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import xml.org.today.R;
import xml.org.today.activity.CommentActivity;
import xml.org.today.adapter.CommentItemAdapter2;
import xml.org.today.adapter.HeaderAndFooterRecyclerViewAdapter;
import xml.org.today.adapter.MyContentAdapter2;
import xml.org.today.adapter.SingleTypeAdapter;


public class SimpleFragment2 extends Fragment {
    private static final String ARG_TAB_NAME = "tab_name";
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Context mContext;
    private String mTabName;
    private ArrayList<String> mTabNames = new ArrayList<>();
    public static List<Map<String,String>> mList;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private CommentItemAdapter2 mAdapter;
    public SimpleFragment2(String tabName, List<Map<String, String>> list) {
        mTabName=tabName;
        mList=list;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_simple, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommentItemAdapter2(mContext, mList);
        mHeaderAndFooterRecyclerViewAdapter=new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        /*SimpleAdapter adapter = new SimpleAdapter(mTabNames, R.layout.item_home_fragment);

        mRecyclerView.setAdapter(adapter);
        adapter.setHandleClickListener(new BaseAdapter.HandleClickListener() {
            @Override
            public void handleClick(BaseAdapter.ViewHolder holder) {
                final TextView mItem = ButterKnife.findById(holder.getItemView(), R.id.item);
                mItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), mItem.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/
    }

    static class SimpleAdapter extends SingleTypeAdapter<String> {
        public SimpleAdapter(List<String> list, int layoutResId) {
            super(list, layoutResId);
        }

        @Override
        public void bindView(ViewHolder holder, int position, View itemView) {
            TextView mItem = ButterKnife.findById(itemView, R.id.item);
            mItem.setText(getDataList().get(holder.getAdapterPosition()) + "-" + holder.getAdapterPosition());
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
