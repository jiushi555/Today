package xml.org.today.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import xml.org.today.adapter.CoverListAdapter;
import xml.org.today.data.CoverData;
import xml.org.today.R;

/**
 * Created by Administrator on 2016/7/2.
 */
public class CoverListActivity extends BaseActivity {
    private ImageView back;
    private RecyclerView recyclerView;
    private CoverData coverData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coverlist);
        initView();
    }

    private void initView() {
        recyclerView= (RecyclerView) findViewById(R.id.cover_list_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        coverData=new CoverData();
        CoverListAdapter coverListAdapter=new CoverListAdapter(coverData,CoverListActivity.this);
        recyclerView.setAdapter(coverListAdapter);
        back= (ImageView) findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoverListActivity.this.finish();
            }
        });
    }
    public void finishThis(int imgId){
        Intent intent=new Intent();
        intent.putExtra("imgId", imgId);
        CoverListActivity.this.setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
        CoverListActivity.this.finish();
    }

}
