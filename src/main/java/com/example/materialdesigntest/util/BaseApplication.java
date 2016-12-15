package com.example.materialdesigntest.util;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Administrator on 2016/12/10.
 */

public class BaseApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        //配置过一次后再配置是无效的（isInited == true) ，要重新配置得先destroy掉imageloader
    }
    public static Context getContext(){
        return context;
    }
}
