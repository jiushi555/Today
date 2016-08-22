package xml.org.today.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import xml.org.today.R;

/**
 * Created by Administrator on 2016/7/5.
 */
public class AboutUsActivity extends BaseActivity{
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            Log.d("TAGTAG", "down");
        }
        if(event.getAction()==MotionEvent.ACTION_MOVE){
            Log.d("TAGTAG","move");
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            Log.d("TAGTAG","up");
        }
        return super.onTouchEvent(event);
    }
}
