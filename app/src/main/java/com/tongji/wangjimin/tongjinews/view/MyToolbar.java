package com.tongji.wangjimin.tongjinews.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Date;

/**
 * Created by wangjimin on 17/3/1.
 */

public class MyToolbar extends Toolbar{

    private long mTime;
    private DoubleClickListener mListener;
    private static final int DOUBLE_CLICK_TIME = 230;

    public MyToolbar(Context context) {
        super(context);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
        final int action = ev.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                final long time = new Date().getTime();
                if(time - mTime < DOUBLE_CLICK_TIME){
//                    Toast.makeText(getContext(), "Double click.", Toast.LENGTH_LONG).show();
                    if(mListener != null){
                        mListener.onClick();
                    }
                    return true;
                }
                mTime = time;
        }
        return super.onTouchEvent(ev);
    }

    public void setDoubleClickListener(DoubleClickListener listener){
        mListener = listener;
    }

    public interface DoubleClickListener{
        void onClick();
    }
}
