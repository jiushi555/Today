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


public class SimpleFragment3 extends Fragment {
    private Context mContext;
    private int mStep;
    private TextView mTextView;
    public SimpleFragment3(int step) {
        mStep=step;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();

        View view = inflater.inflate(R.layout.frgment_simple_nothing, container, false);
        ButterKnife.bind(this, view);
        mTextView= (TextView) view.findViewById(R.id.me_no_text);
        if(mStep==1){
            mTextView.setText("您没有发布任何内容");
        }else if(mStep==2){
            mTextView.setText("您没有参与任何评论");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
