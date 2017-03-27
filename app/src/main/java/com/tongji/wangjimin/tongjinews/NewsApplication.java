package com.tongji.wangjimin.tongjinews;

import android.app.Application;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tongji.wangjimin.tongjinews.net.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjimin on 17/2/28.
 */

public class NewsApplication extends Application {

    private List<News> mNewsList;
    private static NewsApplication instance;
    private List<List<String> > mImageList;

    public static NewsApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mNewsList = new ArrayList<>();
        mImageList = new ArrayList<>();
        Fresco.initialize(this);
    }

    @Nullable
    public List<News> getNewsList(){
        return mNewsList;
    }

    public List<List<String> > getImageList(){ return mImageList; }

    public void setNewsList(List<News> newsList){
        mNewsList = newsList;
        // clear image list first.
        mImageList.clear();
        addImage(newsList);
    }

    public void addNews(List<News> newsList){
        if(mNewsList != null){
            mNewsList.addAll(newsList);
        }
        addImage(newsList);
    }

    private void addImage(List<News> newsList){
        if(mImageList != null){
            for(News n : newsList){
                if(n.getImages() != null && n.getImages().size() > 0){
                    mImageList.add(n.getImages());
                }
            }
        }
    }

}
