package xml.org.today.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DateFormatUtil {
    private static String pat1 = "yyyy-MM-dd HH:mm:ss" ;
    private static String pat2="MM月dd日" ;

    /**
     * 格式化时间形式
     *
     * @param date
     * @return
     */
    public static final String dateFormat(String date) {
        if (date == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(pat2) ;        // 实例化模板对象
        Date d = null ;
        try{
            d = sdf1.parse(date) ;   // 将给定的字符串中的日期提取出来
        }catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace() ;       // 打印异常信息
        }
        return sdf2.format(d);
    }

    /**
     * 获取月
     *
     * @param date
     * @return
     */
    public static final String getMonth(String date){
        if(date==null){
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;        // 实例化模板对象
        Date d=null;
        try {
            d=sdf1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int month=c.get(Calendar.MONTH)+1;
        return String.valueOf(month);
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static final String getDay(String date){
        if(date==null){
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;        // 实例化模板对象
        Date d=null;
        try {
            d=sdf1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int day=c.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(day);
    }
    public static final String getTime(String date){
        if(date==null){
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;        // 实例化模板对象
        Date d=null;
        try {
            d=sdf1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        String time=String.valueOf(hour)+":"+String.valueOf(minute);
        return time;
    }
}
