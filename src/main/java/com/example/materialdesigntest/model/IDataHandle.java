package com.example.materialdesigntest.model;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.materialdesigntest.util.Mutil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/12.
 */

public interface IDataHandle<Clz> {



    List<Clz> parseJsonArray(String jsonStr);
    Clz parseJsonObject(String jsonStr);
}
