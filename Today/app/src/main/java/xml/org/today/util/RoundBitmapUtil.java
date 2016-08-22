package xml.org.today.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Administrator on 2016/8/8.
 */
public class RoundBitmapUtil {
    public RoundBitmapUtil(){

    }
    public static Bitmap toRoundBitmap(Bitmap bitmap){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Rect rect;
        if (width >= height) {
            rect = new Rect((width - height) / 2, 0, (width - height) / 2
                    + height, height);
        } else {
            rect = new Rect(0, (height - width) / 2, width, width
                    + (height - width) / 2);
        }
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return output;
    }
}
