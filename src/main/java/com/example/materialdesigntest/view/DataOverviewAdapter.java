package com.example.materialdesigntest.view;

import com.example.materialdesigntest.gsonBean.DataOverview;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 * 将传入的bean转换成对应DataOverview类型
 * 注意java堆栈内存的知识，别只改变了指针没改变堆中数据，会导致notifyDataSetChange无效等
 */

public interface DataOverviewAdapter<T>{
    void changeToDataOverView(List<T> t , List<DataOverview> dataOverviews);
    void changeToDataOverView(T t , DataOverview dataOverview);
}
