package com.example.materialdesigntest.util;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2016/12/14.
 */

public class MyTransformation implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.90f;
    @Override
    public void transformPage(View page, float position) {
        float scaleFactor = Math.max(MIN_SCALE , 1-Math.abs(position));
        float rotate = Math.min(13,13 * Math.abs(position));
        if(position < -1){
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(rotate);

        }else if(position<0){
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(rotate);
        }else if(position >=0 && position < 1){
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(-rotate);
        }else if(position  >= 1){
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(-rotate);
        }
    }
}
