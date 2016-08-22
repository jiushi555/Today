package xml.org.today.activity;

import android.app.Activity;
import android.os.Bundle;

import xml.org.today.R;

/**
 * Created by Administrator on 2016/7/1.
 */
public class PersonActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person);
    }
}
