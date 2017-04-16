package com.tongji.wangjimin.tongjinews.data;

import com.tongji.wangjimin.tongjinews.net.search.NewsItemDigest;
import com.tongji.wangjimin.tongjinews.net.search.NewsSearcher;

import java.util.List;

/**
 * Created by wangjimin on 17/4/16.
 */

public class AsyncSearcher {
    private static AsyncSearcher mInstance;
    private NewsSearcher mSearcher;

    private AsyncSearcher(){
        mSearcher = NewsSearcher.getInstance();
    }

    public static AsyncSearcher getInstance(){
        if(mInstance == null){
            mInstance = new AsyncSearcher();
        }
        return mInstance;
    }

    public void search(SearchCallback callback, String... keyWords){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NewsItemDigest> digests = mSearcher.search(keyWords);
                callback.searchDone(digests);
            }
        }).start();
    }

    public interface SearchCallback{
        void searchDone(List<NewsItemDigest> res);
    }
}
