package com.example.materialdesigntest.util;

import android.util.Log;

/**
 * Created by Administrator on 2016/11/13.
 */

public class Mlog {
    public static final int LEVEL = 0;
    public static final int D=1;
    public static final int E=2;
    public static void d(String tag,String content){
        if(D >= LEVEL){
            Log.d(tag,content);
        }
    }
    public static void e(String tag,String content){
        if(E>=LEVEL){
            Log.e(tag,content);
        }
    }
}
