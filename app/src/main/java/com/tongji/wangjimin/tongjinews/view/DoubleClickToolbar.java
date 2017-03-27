package com.tongji.wangjimin.tongjinews.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Date;

/**
 * Created by wangjimin on 17/3/1.
 * DoubleClickToolbar
 */

public class DoubleClickToolbar extends Toolbar{

    private long mTime;
    private DoubleClickListener mListener;
    // Double click interval.
    private static final int DOUBLE_CLICK_TIME = 230;

    public DoubleClickToolbar(Context context) {
        super(context);
    }

    public DoubleClickToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DoubleClickToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
     * 覆盖此方法，为了给 ToolBar 增加双击的监听器
     * 从而将 RecyclerView 快速滑动到顶部。
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                final long time = new Date().getTime();
                if(time - mTime < DOUBLE_CLICK_TIME){
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
