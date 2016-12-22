package com.example.materialdesigntest.model;

import com.example.materialdesigntest.gsonBean.DataOverview;
import com.example.materialdesigntest.util.Mlog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class BookListImp implements IDataHandle<DataOverview> {


    @Override
    public List<DataOverview> parseJsonArray(String jsonStr) {
        final List<DataOverview> bookList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int start = jsonObject.getInt("start");
            int total = jsonObject.getInt("total");
            int count = jsonObject.getInt("count");
            JSONArray jsonArray = jsonObject.optJSONArray("books");
            for(int i =0 ; i<jsonArray.length() ; i++){
                JSONObject obj = jsonArray.optJSONObject(i);
                String id = obj.optString("id");
                String title = obj.optString("title");
                String imgurl = obj.optJSONObject("images").optString("large");
                StringBuffer content = new StringBuffer();
                JSONArray genres = obj.optJSONArray("author");
                for (int k = 0; k < genres.length() ; k++) {
                    content.append(genres.getString(k)).append("\t");
                }
                content.append("\n");
                JSONArray casts = obj.optJSONArray("tags");
                for(int j = 0 ; j < casts.length() ; j++){
                    JSONObject castsObj = casts.optJSONObject(j);
                    content.append(castsObj.optString("name")).append("/");
                }
                DataOverview a = new DataOverview(id,content.toString(),title,imgurl);
                a.setCount(count);
                a.setStart(start);
                a.setTotal(total);
                bookList.add(a);

            }
        } catch (JSONException e) {
            Mlog.e("ccy","获取一组书籍失败");
            e.printStackTrace();
        }
        return bookList;
    }

    @Override
    public DataOverview parseJsonObject(String jsonStr) {
        return null;
    }
}
