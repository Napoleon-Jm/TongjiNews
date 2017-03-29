package com.tongji.wangjimin.tongjinews.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.tongji.wangjimin.tongjinews.R;

/**
 * Created by wangjimin on 17/3/28.
 * FlipRecyclerView.
 */

public class FlipRecyclerView extends RecyclerView {

    private int mMoved;
    private int mPre;
    private boolean mIsFirstMove;
    private int mOrientation;
    private Point mVelocity;
    private VelocityTracker mVelocityTracker;

    public FlipRecyclerView(Context context) {
        this(context, null);
    }

    public FlipRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlipRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mVelocity = new Point(0, 0);
        mPre = 0;
        mMoved = 0;
        mIsFirstMove = true;

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                mOrientation = ((LinearLayoutManager)FlipRecyclerView.this.getLayoutManager())
                        .getOrientation();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        LinearLayoutManager manager = (LinearLayoutManager)this.getLayoutManager();
        int firstIndex = manager.findFirstVisibleItemPosition();
        Log.d("@", "first index : " + firstIndex);

        int index = e.getActionIndex();
        int action = e.getActionMasked();
        int pointerId = e.getPointerId(index);

        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
        switch(action){
            case MotionEvent.ACTION_DOWN:
                Log.d("@", "down");
                mVelocityTracker.addMovement(e);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("@", "move");
                mVelocityTracker.addMovement(e);
                mVelocityTracker.computeCurrentVelocity(1000);
                mVelocity.x = (int)VelocityTrackerCompat.getXVelocity(
                        mVelocityTracker,  pointerId);
                mVelocity.y = (int)VelocityTrackerCompat.getYVelocity(
                        mVelocityTracker, pointerId);
                if(mIsFirstMove){
                    mPre = getMovePos(e);
                    mIsFirstMove = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("@", "up");
                /*
                * 在 RecyclerView 中，manager.getChildAt 中的 index 并不是 adapter 中的 index，而是
                * 视野中的子 View index。
                TextView textView = (TextView)(manager.getChildAt(firstIndex))
                        .findViewById(R.id.test_textview_item);
                Log.d("wjm", textView.getText().toString());
                */
                View v = manager.getChildAt(0);
                Log.d("@", "up v ? " + ((TextView)v.findViewById(R.id.test_textview_item)).getText());
                int flag = getMovePos(e) - mPre;
                Log.d("@", mPre + " " + e.getRawY());
                Log.d("@", "up velocity is " + mVelocity.y + "/n " +
                        "flag is " + flag);

                int itemEdge = getItemEdge(v);
                int itemBound = getItemBound(v);
                /* down */
                if(flag < 0){
                    if(itemEdge < itemBound*0.66f){
                        /* 当 index 的item 已经在屏幕中能看到时，这个方法失效
                         smoothScrollToPosition(0);
                         smoothScrollBy(0, v.getBottom());
                        */
                        // This is work, but don't smooth.
                        //scrollBy(0, v.getBottom());
                        Log.d("@", "down right");
                        performAni(itemEdge);
                    } else {
                        performAni(itemEdge - itemBound);
                    }
                } else {
                    /* up */
                    Log.d("@", itemEdge + " " + itemBound*0.33f);
                    if(itemEdge > itemBound*0.33f){
                        performAni(itemEdge - itemBound);
                    } else {
                        performAni(itemEdge);
                    }
                }
                mVelocity.set(0, 0);
                mIsFirstMove = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.recycle();
                break;
        }
        return super.onTouchEvent(e);
    }

    // 防止 RecyclerView 快速滑动。
    @Override
    public boolean fling(int velocityX, int velocityY) {
        //return super.fling(velocityX, velocityY);
        return false;
    }

    private void performAni(int pos){
        ValueAnimator ani = ValueAnimator.ofInt(pos);
        ani.setDuration(200);
        ani.setInterpolator(new DecelerateInterpolator());
        mMoved = 0;
        ani.addUpdateListener(animation -> {
            int newPos = (int)animation.getAnimatedValue();
            //FlipRecyclerView.this.scrollBy(0, newPos - mMoved);
            autoScrollBy(newPos - mMoved);
            mMoved = newPos;
        });
        ani.start();
    }

    private void autoScrollBy(int offset){
        if(mOrientation == LinearLayoutManager.VERTICAL){
            this.scrollBy(0, offset);
        }else {
            this.scrollBy(offset, 0);
        }
    }

    private int getMovePos(MotionEvent e){
        return (int)(mOrientation == LinearLayoutManager.VERTICAL? e.getRawY():
                e.getRawX());
    }

    private int getItemEdge(View v){
        return mOrientation == LinearLayoutManager.VERTICAL? v.getBottom():
                v.getRight();
    }

    private int getItemBound(View v){
        return mOrientation == LinearLayoutManager.VERTICAL? v.getHeight():
                v.getWidth();
    }
}
