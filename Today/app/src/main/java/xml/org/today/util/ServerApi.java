package xml.org.today.util;


import android.content.Context;
import android.graphics.Bitmap;


import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;

import org.json.JSONObject;


/**
 * Created by Administrator on 2016/6/28.
 */
public class ServerApi {
    public static String URL = "http://192.168.1.210:8080/Today/";
    //public static String URL="http://10.0.2.2:8080/Today/";
    public static RequestQueue requestQueue;
    /**
     * 注册URL
     */
    public static String REGISTER_URL = URL + "register.php";
    /**
     * 登录URL
     */
    public static String LOGIN_URL = URL + "login.php";
    /**
     * 意见反馈URL
     */
    public static String FEEDBACK_URL = URL + "feedback.php";
    /**
     * 修改密码URL
     */
    public static String RLSETPWD_URL = URL + "rlsetpwd.php";
    /**
     * 发布内容URL
     */
    public static String ADDCONTENT_URL = URL + "addcontent.php";
    /**
     * 查找内容URL
     */
    public static String SELECTCONTENT_URL = URL + "content_select.php";
    /**
     * 查找我的内容URL
     */
    public static String SELECTMYCONTENT_URL = URL + "mycontent_select.php";
    /**
     * 发布评论
     */
    public static String ADDCOMMENT_URL = URL + "addcomment.php";

    /**
     * 加载评论
     */
    public static String SELECTCOMMENT_URL = URL + "comment_select.php";
    /**
     * 加载我的评论
     */
    public static String SELECTMYCOMMENT_URL = URL + "mycomment_select.php";
    /**
     * 获取消息
     */
    public static String SELECTMESSAGE_URL = URL + "message_select.php";
    /**
     * 上传头像
     */
    public static String UPLOADPORTRAIT_URL = URL + "upload.php";
    /**
     * 修改头像上传参数
     */
    public static String UPLOADUSER_URL = URL + "uploaduser.php";
    /**
     * 查找头像
     */
    public static String GETPORTRAITNAME_URL = URL + "portrait_select.php";
    /**
     * 重设个人信息
     */
    public static String RESETME_URL = URL + "resetme_name.php";

    /**
     * 检查版本信息
     */
    public static String CHECKVERSION_URL = URL + "version_select.php";

    public static void init() {
        requestQueue = NoHttp.newRequestQueue();
    }

    public static void getCheckVersionResult(int key, Context context, OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(CHECKVERSION_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(context));
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 获取他人发布
     * @param user_id
     * @param begin
     * @param length
     * @param key
     * @param onResponseListener
     */
    public static void getSelectOtherContentResult(String user_id, String begin, String length, int key,
                                                OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SELECTMYCONTENT_URL, RequestMethod.POST);
        request.add("user_id", user_id);
        request.add("begin", begin);
        request.add("length", length);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 修改个人资料
     * @param key
     * @param context
     * @param username
     * @param onResponseListener
     */
    public static void getResetMeResult(int key, Context context, String username,
                                        OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(RESETME_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(context));
        request.add("username", username);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 获取头像名称
     *
     * @param key
     * @param context
     * @param onResponseListener
     */
    public static void getPortraitNameResult(int key, Context context, OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(GETPORTRAITNAME_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(context));
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 修改头像上传参数
     *
     * @param key
     * @param context
     * @param onResponseListener
     * @param filename
     */
    public static void getUploadUserResult(int key, String filename, Context context, OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(UPLOADUSER_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(context));
        request.add("filename", filename);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 获取消息通知
     *
     * @param key
     * @param context
     * @param onResponseListener
     */
    public static void getSelectMessageResult(int key, Context context, OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SELECTMESSAGE_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(context));
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 加载我的评论
     *
     * @param key
     * @param context
     * @param onResponseListener
     */
    public static void getSelectMyCommentResult(int key, Context context, OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SELECTMYCOMMENT_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(context));
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 加载评论
     *
     * @param key
     * @param for_id
     * @param onResponseListener
     */
    public static void getSelectCommentResult(int key, String for_id, OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SELECTCOMMENT_URL, RequestMethod.POST);
        request.add("for_id", for_id);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 发布评论
     *
     * @param key
     * @param context
     * @param comment
     * @param for_id
     * @param for_userid
     * @param onResponseListener
     */
    public static void getAddCommentResult(int key, Context context, String comment, String for_id, String for_userid,
                                           OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ADDCOMMENT_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(context));
        request.add("comment", comment);
        request.add("for_id", for_id);
        request.add("for_userid", for_userid);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 查找我的内容
     *
     * @param mContext
     * @param begin
     * @param length
     * @param key
     * @param onResponseListener
     */
    public static void getSelectMyContentResult(Context mContext, String begin, String length, int key,
                                                OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SELECTMYCONTENT_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(mContext));
        request.add("begin", begin);
        request.add("length", length);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 查找内容
     *
     * @param begin
     * @param length
     * @param key
     * @param onResponseListener
     */
    public static void getSelectContentResult(String date, String begin, String length, int key,
                                              OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SELECTCONTENT_URL, RequestMethod.POST);
        if (date != null) {
            request.add("date", date);
        }
        request.add("begin", begin);
        request.add("length", length);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 发表内容
     *
     * @param imgId
     * @param content
     * @param context
     * @param key
     * @param onResponseListener
     */
    public static void getAddContentResult(String imgId, String content, Context context, int key,
                                           OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ADDCONTENT_URL, RequestMethod.POST);
        request.add("user_id", UserInfo.getUserId(context));
        request.add("content", content);
        if (imgId != null) {
            request.add("imgid", imgId);
        }
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 用户注册
     *
     * @param username
     * @param key
     * @param onResponseListener
     */
    public static void getRegisterResult(String username, String pwd, int key,
                                         OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(REGISTER_URL, RequestMethod.POST);

        // 添加请求参数
        request.add("username", username);
        request.add("pwd", pwd);
        // 添加请求头
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 用户登录
     *
     * @param username
     * @param pwd
     * @param key
     * @param onResponseListener
     */
    public static void getLoginResult(String username, String pwd, int key,
                                      OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(LOGIN_URL, RequestMethod.POST);

        request.add("username", username);
        request.add("pwd", pwd);

        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 意见反馈
     *
     * @param content
     * @param key
     * @param onResponseListener
     */
    public static void getFeedbackResult(String content, int key,
                                         OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(FEEDBACK_URL, RequestMethod.POST);
        request.add("content", content);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }

    /**
     * 修改密码
     *
     * @param user_id
     * @param key
     * @param newPwd
     * @param onResponseListener
     */
    public static void getRlSetPwd(String user_id, int key, String newPwd,
                                   OnResponseListener onResponseListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(RLSETPWD_URL, RequestMethod.POST);
        request.add("newpwd", newPwd);
        request.add("user_id", user_id);
        request.addHeader("Author", "nohttp_sample");
        ServerApi.requestQueue.add(key, request, onResponseListener);
    }
}


