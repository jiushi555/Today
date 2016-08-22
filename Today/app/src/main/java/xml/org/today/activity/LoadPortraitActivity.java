package xml.org.today.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import xml.org.today.R;
import xml.org.today.util.ModifyAvatarDialog;
import xml.org.today.util.RoundBitmapUtil;
import xml.org.today.util.ServerApi;
import xml.org.today.util.UserInfo;

/**
 * Created by Administrator on 2016/7/29.
 */
public class LoadPortraitActivity extends BaseActivity{
    // 头像参数
    private String TAG = "InformationActivity";
    public static final String IMAGE_PATH = "My_weixin";
    private static String localTempImageFileName = "";
    private static final int FLAG_CHOOSE_IMG = 5;
    private static final int FLAG_CHOOSE_PHONE = 6;
    private static final int FLAG_MODIFY_FINISH = 7;
    public static final File FILE_SDCARD = Environment
            .getExternalStorageDirectory();
    public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
    public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
            "images/screenshots");
    private String path;

    private String filename;


    //View
    private TextView mPassView;
    private ImageView mPortraitView;
    private Context mContext=LoadPortraitActivity.this;
    private static final int NOHTTP_WHAT_TEST = 0x013;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadportrai);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        filename = timeStamp + "." + "jpg";
        initView();
    }
    private void initView(){
        mPassView= (TextView) findViewById(R.id.pass);
        mPortraitView= (ImageView) findViewById(R.id.portrait_img);
        mPortraitView.setOnClickListener(new PortraitOnClick());
    }
    private class PortraitOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // 调用选择那种方式的dialog
            ModifyAvatarDialog modifyAvatarDialog = new ModifyAvatarDialog(mContext) {
                // 选择本地相册
                @Override
                public void doGoToImg() {
                    this.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, FLAG_CHOOSE_IMG);
                }

                // 选择相机拍照
                @Override
                public void doGoToPhone() {
                    this.dismiss();
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        try {
                            localTempImageFileName = "";
                            localTempImageFileName = String
                                    .valueOf((new Date()).getTime()) + ".png";
                            File filePath = FILE_PIC_SCREENSHOT;
                            if (!filePath.exists()) {
                                filePath.mkdirs();
                            }
                            Intent intent = new Intent(
                                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(filePath, localTempImageFileName);
                            // localTempImgDir和localTempImageFileName是自己定义的名字
                            Uri u = Uri.fromFile(f);
                            intent.putExtra(
                                    MediaStore.Images.Media.ORIENTATION, 0);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                            startActivityForResult(intent, FLAG_CHOOSE_PHONE);
                        } catch (ActivityNotFoundException e) {
                            //
                        }
                    }
                }
            };
            AlignmentSpan span = new AlignmentSpan.Standard(
                    Layout.Alignment.ALIGN_CENTER);
            AbsoluteSizeSpan span_size = new AbsoluteSizeSpan(25, true);
            SpannableStringBuilder spannable = new SpannableStringBuilder();
            String dTitle = "请选择";
            spannable.append(dTitle);
            spannable.setSpan(span, 0, dTitle.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(span_size, 0, dTitle.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            modifyAvatarDialog.setTitle(spannable);
            modifyAvatarDialog.show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    Cursor cursor = getContentResolver().query(uri,
                            new String[] { MediaStore.Images.Media.DATA },
                            null, null, null);
                    if (null == cursor) {
                        Toast.makeText(mContext, "图片没找到", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                    Log.i(TAG, "path=" + path);
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("path", path);
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                } else {
                    Log.i(TAG, "path=" + uri.getPath());
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("path", uri.getPath());
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                }
            }
        } else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
            File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("path", f.getAbsolutePath());
            startActivityForResult(intent, FLAG_MODIFY_FINISH);
        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                Log.i(TAG, "截取到的图片路径是 = " + path);
                LoadPortraitActivity.this.path = path;
                mPassView.setText("完成");
                mPassView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        uploadFile();
                    }

                });
                Bitmap b = BitmapFactory.decodeFile(path);
                UserInfo.setUserImg(path,LoadPortraitActivity.this);
                mPortraitView.setImageBitmap(RoundBitmapUtil.toRoundBitmap(b));

            }
        }
    }



    private void uploadFile() {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        showDialog();
        try {
            URL url = new URL(ServerApi.UPLOADPORTRAIT_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/*
			 * Output to the connection. Default is false, set to true because
			 * post method must write something to the connection
			 */
            con.setDoOutput(true);
			/* Read from the connection. Default is true. */
            con.setDoInput(true);
			/* Post cannot use caches */
            con.setUseCaches(false);
			/* Set the post method. Default is GET */
            con.setRequestMethod("POST");
			/* 设置请求属性 */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设置StrictMode 否则HTTPURLConnection连接失败，因为这是在主进程中进行网络连接 */
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());
			/* 设置DataOutputStream，getOutputStream中默认调用connect() */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream()); // output
            // to
            // the
            // connection
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file\";filename=\"" + filename + "\"" + end);
            ds.writeBytes(end);
			/* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(path);
			/* 设置每次写入8192bytes */
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize]; // 8k
            int length = -1;
			/* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* 关闭流，写入的东西自动生成Http正文 */
            fStream.close();
			/* 关闭DataOutputStream */
            ds.close();
			/* 从返回的输入流读取响应信息 */
            InputStream is = con.getInputStream(); // input from the connection
            // 正式建立HTTP连接
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
			/* 显示网页响应内容 */
            // Toast.makeText(Tx_zc.this, b.toString().trim(),
            // Toast.LENGTH_SHORT).show();//Post成功
            ServerApi.getUploadUserResult(0x013,filename,LoadPortraitActivity.this,onResponseListener);
        } catch (Exception e) {
            closeDialog();
            Toast.makeText(LoadPortraitActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 回调对象，接受请求结果
     */
    private OnResponseListener<JSONObject> onResponseListener = new OnResponseListener<JSONObject>() {
        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == NOHTTP_WHAT_TEST) {// 判断what是否是刚才指定的请求
                // 请求成功
                String result = response.get().toString();// 响应结果
                try {
                    JSONObject jso = new JSONObject(result);
                    String success = jso.getString("success");
                    if (success.equals("0")) {
                        Toast.makeText(LoadPortraitActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        Intent it=new Intent(LoadPortraitActivity.this,MainActivity.class);
                        LoadPortraitActivity.this.startActivity(it);
                        LoadPortraitActivity.this.finish();
                    } else {
                        Toast.makeText(LoadPortraitActivity.this, "上传失败，请重试", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 响应头
                Headers headers = response.getHeaders();
                headers.getResponseCode();// 响应码
                response.getNetworkMillis();// 请求花费的时间
            }
        }

        @Override
        public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
            Toast.makeText(LoadPortraitActivity.this, "发布失败，请重试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart(int what) {
            // 请求开始，显示dialog
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
            closeDialog();
        }


    };
}
