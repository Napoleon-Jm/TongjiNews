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

    public static NewsApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mNewsList = new ArrayList<>();
        Fresco.initialize(this);
    }

    @Nullable
    public List<News> getNewsList(){
        return mNewsList;
    }

    public void setNewsList(List<News> newsList){
        mNewsList = newsList;
    }

    public void addNews(List<News> newsList){
        if(mNewsList != null){
            mNewsList.addAll(newsList);
        }
    }
}
