package com.tongji.wangjimin.tongjinews.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wangjimin on 17/4/15.
 * This ViewGroup could hide soft input keyboard, when
 * click area that do not contain the EditText.
 */

public class HideSoftInputLinearLayout extends LinearLayout {
    private OnInterceptTouchEvent mListener;

    public HideSoftInputLinearLayout(Context context) {
        super(context);
    }

    public HideSoftInputLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HideSoftInputLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mListener != null){
            return mListener.onInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setOnInterceptTouchEvent(OnInterceptTouchEvent listener){
        mListener = listener;
    }

    public interface OnInterceptTouchEvent{
        boolean onInterceptTouchEvent(MotionEvent ev);
    }
}
