package com.tongji.wangjimin.tongjinews.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

/**
 * Created by wangjimin on 17/3/29.
 * ScalePictureView.
 */

public class ScalePictureView extends AppCompatImageView {

    private float mStartDis;
    private float mEndDis;
    private PointF mOrigin;
    /*
    { MSCALE_X, MSKEW_X, MTRANS_X,
      MSKEW_Y, MSCALE_Y, MTRANS_Y,
      MPERSP_0,MPERSP_1, MPERSP_2 }
     */
    private Matrix mMatrix;
    private Matrix mCurrentMatrix;
    private int mOriginBpWidth;
    private int mOriginBpHeight;
    private int mMode = 0;
    private static final float THRESHLOD = 2.f;
    private static final int SCALE = 1;
    private static final int DRAG = 2;

    public ScalePictureView(Context context) {
        this(context, null);
    }

    public ScalePictureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalePictureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMatrix = new Matrix();
        mCurrentMatrix = new Matrix();
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                mOrigin = new PointF(getWidth()/2.f, getHeight()/2.f);
                Matrix m = new Matrix();
                m.set(ScalePictureView.this.getImageMatrix());
                RectF r = new RectF();
                m.mapRect(r);
                Log.d("@", "test " + r.left + " " + r.top);

                Log.d("@", "ImageView size " + getWidth() + " " + getHeight());

                float[] v = new float[9];
                m.getValues(v);
                Log.d("@", "Matrix: \n" +
                        v[0] + " \t" + v[1] + " \t" + v[2] + "\n" +
                        v[3] + " \t" + v[4] + " \t" + v[5] + "\n" +
                        v[6] + " \t" + v[7] + " \t" + v[8] + "\n" );

                mOriginBpWidth = (int)(getWidth()/v[0]);
                mOriginBpHeight = (int)(getHeight()/v[4]);
                ScalePictureView.this.setScaleType(ScaleType.MATRIX);
                ScalePictureView.this.setDrawingCacheEnabled(true);
                Bitmap b = Bitmap.createBitmap(ScalePictureView.this.getDrawingCache());
                Log.d("@", "Bitmap " + b.getWidth() + " " + b.getHeight());
                ScalePictureView.this.setDrawingCacheEnabled(false);

                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN://第一个手指按下
//                Log.d("@", "one down");
                mMode = DRAG;
                mCurrentMatrix.set(this.getImageMatrix());
                mMatrix.set(mCurrentMatrix);
                break;
            case MotionEvent.ACTION_POINTER_DOWN://第二个手指按下
//                Log.d("@", "two down");
                mMode = SCALE;
                mStartDis = spacing(event);
                break;
            case MotionEvent.ACTION_MOVE://移动（共享）
//                Log.d("@", "move");
                switch (mMode){
                    case DRAG:
                        break;
                    case SCALE:
                        mEndDis = spacing(event);
                        if(Math.abs(mEndDis - mStartDis) > 0){
                            float scale = mEndDis/mStartDis;
                            PointF mid = getMidPoint(event);
                            Log.d("@", "scale " + scale + " mode " + mMode + " endDis " + mEndDis);
                            mMatrix.set(mCurrentMatrix);
                            mMatrix.postScale(scale, scale, mid.x, mid.y);
                        }
                        break;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP://其中一个手指松开
//                Log.d("@", "one up");
                mMode = DRAG;
                break;
            case MotionEvent.ACTION_UP://最后一个手指松开
//                Log.d("@", "two up");
                moveToOrigin();
                mMode = 0;
                break;
        }
        this.setImageMatrix(mMatrix);
        return true;
    }

    private void moveToOrigin() {
        float[] v = new float[9];
        mCurrentMatrix.getValues(v);
        float bpWidth = (mOriginBpWidth * v[0]);
        float bpHeight = (mOriginBpHeight * v[4]);
        Log.d("@", "current size " + bpWidth + " " + bpHeight);
        Log.d("@", "origin" + mOrigin.x + " " + mOrigin.y);
        PointF currentCenter = new PointF(v[2] + bpWidth/2.f, v[5] + bpHeight/2.f);
        mMatrix.set(mCurrentMatrix);
        Log.d("@", "trans params " + (mOrigin.x - currentCenter.x) + " " + (mOrigin.y - currentCenter.y));
        mMatrix.postTranslate(mOrigin.x - currentCenter.x, mOrigin.y - currentCenter.y);
//        mMatrix.postTranslate(0, 0);

        float[] v1 = new float[9];
        mMatrix.getValues(v1);
        Log.d("@", "trans " + v[2] + " " + v[5]);

        ScalePictureView.this.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(ScalePictureView.this.getDrawingCache());
        Log.d("@", "Bitmap " + b.getWidth() + " " + b.getHeight());
        ScalePictureView.this.setDrawingCacheEnabled(false);

        this.setImageMatrix(mMatrix);
    }

    private float spacing(MotionEvent event){
        float x = event.getX(0) - event.getY(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private PointF getMidPoint(MotionEvent event){
        float midX = event.getX(0) + event.getX(1);
        float midY = event.getY(0) + event.getY(1);
        return new PointF(midX/2, midY/2);
    }
}
