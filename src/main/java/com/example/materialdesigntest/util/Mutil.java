package com.example.materialdesigntest.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/11/13.
 */

public class Mutil {
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, BaseApplication.getContext().getResources().getDisplayMetrics());
    }

    public static int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, BaseApplication.getContext().getResources().getDisplayMetrics());
    }

    public static DisplayMetrics getScreenMetrics() {
        WindowManager wm = (WindowManager) BaseApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 经过测试 返回point和返回DisplayMetrics都能获得屏幕分辨率
     */

    public static Point getScreenSize() {
        WindowManager wm = (WindowManager) BaseApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

    /**
     * 根据图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    public static int getImgResource(String imageName) {
//Context ctx=getBaseContext();。
        int resId = BaseApplication.getContext().getResources().getIdentifier(imageName, "drawable", BaseApplication.getContext().getPackageName());
        return resId;
    }


    private static Toast toast;

    public static void showToast(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }


    private static OkHttpClient okHttpClient = null;

    public static OkHttpClient getOkHttp() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * 根据url获取json数据（Str型） map为post时的键值对
     *
     * @param url
     * @param map
     * @param callback
     */
    public static void getJsonFromUrl(String url, @Nullable Map<String, String> map, Callback callback) {
        OkHttpClient okHttpClient = getOkHttp();
        if (map == null) {
            final Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(callback);
        } else {
//            FormBody.Builder formBody = new FormBody.Builder();
//            for(Map.Entry<String,String> entry : map.entrySet()){
//                String value = null;
//                try {
//                    value = URLEncoder.encode(entry.getValue(),"utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                formBody.add(entry.getKey(),value);
//                Mlog.d("ccy","kv="+entry.getKey()+entry.getValue()+value);
//            }
//            Request request = new Request.Builder().url(url).post(formBody.build()).build();
//            okHttpClient.newCall(request).enqueue(callback);
//            post不行可能是要中文编码 记得试试
            StringBuffer sb = new StringBuffer(url);
            if(!url.contains("?")) {  //表明源url没有键值参数  //不优雅
                sb.append("?");
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            Request request = new Request.Builder().url(sb.toString()).build();
            okHttpClient.newCall(request).enqueue(callback);

        }
    }


}
