package xml.org.today.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import xml.org.today.R;


public class ProgressDialog extends Dialog {
	private Context context = null;
	private static ProgressDialog customProgressDialog = null;
	
	public ProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static ProgressDialog createDialog(Context context){
		customProgressDialog = new ProgressDialog(context,R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		
		return customProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (customProgressDialog == null){
    		return;
    	}
    	
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
 

    public ProgressDialog setTitile(String strTitle){
    	return customProgressDialog;
    }

    public ProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return customProgressDialog;
    }
}
