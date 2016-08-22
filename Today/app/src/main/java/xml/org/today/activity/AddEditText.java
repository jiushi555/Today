package xml.org.today.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import xml.org.today.R;

/**
 * Created by Administrator on 2016/7/4.
 */
public class AddEditText extends BaseActivity {
    private ImageView back;
    private TextView save;
    private EditText content_edt;
    private final Timer mTimer = new Timer(true);
    private TimerTask mTimerTask;
    private static String FLAG;   //0content有内容，1content为空

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addedittext);
        initView();
        mTimer.schedule(mTimerTask, 500);
    }

    private void initView() {
        Intent it=super.getIntent();
        FLAG=it.getStringExtra("FLAG");
        back = (ImageView) findViewById(R.id.goback);
        save = (TextView) findViewById(R.id.add_save_btn);
        content_edt = (EditText) findViewById(R.id.add_edt);
        if(FLAG.equals("0")){
            content_edt.setText(it.getStringExtra("content"));
            content_edt.setSelection(it.getStringExtra("content").length());
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEditText.this.finish();
            }
        });
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                //自动弹出键盘
                content_edt.setFocusableInTouchMode(true);
                content_edt.requestFocus();
                InputMethodManager inputManager =
                        (InputMethodManager) content_edt.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(content_edt, 0);
            }
        };
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = content_edt.getText().toString();
                if (content.equals("")) {
                    Toast.makeText(AddEditText.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    Intent it=new Intent();
                    it.putExtra("content",content);
                    AddEditText.this.setResult(RESULT_OK, it);
                    AddEditText.this.finish();
                }
            }
        });

    }
}
