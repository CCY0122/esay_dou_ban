package com.example.materialdesigntest.diy_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2016/12/14.
 */

public class HeaderViewPager extends ViewPager {
    public HeaderViewPager(Context context) {
        super(context);
    }

    public HeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

//    int lasty;
//    int lastx;
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//        switch (ev.getAction()){
////            case MotionEvent.ACTION_DOWN:
////                return true;
//            case MotionEvent.ACTION_MOVE:
//                int dy = y - lasty;
//                int dx = x - lastx;
//                if(dy > 0 ){
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }else {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//        }
//        lastx = x;
//        lasty = y;
//        return super.onInterceptTouchEvent(ev);
//    }
}
