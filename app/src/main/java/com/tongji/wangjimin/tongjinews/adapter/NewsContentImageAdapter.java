package com.tongji.wangjimin.tongjinews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.tongji.wangjimin.tongjinews.R;

import java.util.List;

/**
 * Created by wangjimin on 17/2/28.
 * NewsContentImageAdapter, for
 * {@link com.tongji.wangjimin.tongjinews.activity.NewsContentActivity}'smViewPager,
 * Responsible for show Image in NewsContentActivity.
 */

public class NewsContentImageAdapter extends PagerAdapter{

    private List<String> mData;
    private Context mContext;
    private View mView;
    private View mViewPager;

    public NewsContentImageAdapter(List<String> data, Context context, View v){
        mData = data;
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_image, new FrameLayout(mContext), false);
        mViewPager = v;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mView);
        SimpleDraweeView image = (SimpleDraweeView)mView.findViewById(R.id.viewpager_image);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(mData.get(position));
        if(imagePipeline.isInBitmapMemoryCache(uri) || imagePipeline.isInDiskCacheSync(uri)){
            imagePipeline.evictFromCache(uri);
        }
        image.setImageURI(mData.get(position));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setVisibility(View.GONE);
            }
        });
        return mView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mView);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
