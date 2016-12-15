package com.example.materialdesigntest.model;

import android.support.annotation.Nullable;

import com.example.materialdesigntest.gsonBean.GMovie;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/12.
 */

public class MovieImp implements IDataHandle<GMovie> {

    private GMovie gMovie;
    private Gson gson;


    /**
     * 根据id返回具体一个电影的信息
     */
    public MovieImp() {

    }

    @Override
    public GMovie parseJsonObject(String jsonStr) {
        gson = new Gson();
        gMovie = gson.fromJson(jsonStr , GMovie.class);
        return gMovie;
    }

    @Override
    public List<GMovie> parseJsonArray(String jsonStr) {
        return null;
    }
}
