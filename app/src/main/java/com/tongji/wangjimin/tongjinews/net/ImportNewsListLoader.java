package com.tongji.wangjimin.tongjinews.net;

import android.support.annotation.WorkerThread;
import android.util.Log;

import com.tongji.wangjimin.tongjinews.net.util.Config;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangjimin on 17/2/27.
 * ImportNewsLoader.
 */

public class ImportNewsListLoader {

    /**
     * Callback for @{#load}.
     */
    public interface ILoadingDone{
        void loadingDone(List<News> newsList);
    }

    private LinkedList<String> mLoadedUrls;
    private int mLeftUrls;
    private List<News> mLoadedNewsList;
    private int mLoadBlockSize;
    private ExecutorService mPool;
    private static ImportNewsListLoader instance;

    private ImportNewsListLoader(){
        mLoadedUrls = new LinkedList<>();
        mLoadedNewsList = new ArrayList<>();
        mLeftUrls = 0;
        mLoadBlockSize = 7;
        mPool = Executors.newFixedThreadPool(4);
    }

    public static ImportNewsListLoader getInstance(){
        if(instance == null)
            instance = new ImportNewsListLoader();
        return instance;
    }

    @WorkerThread
    public void load(ILoadingDone callback){
        if(mLeftUrls == 0){
            loadNewsUrl(new Runnable(){
                @Override
                public void run() {
                    loadNews(mLoadBlockSize, callback);
                }
            });
        } else if(mLeftUrls <= mLoadBlockSize){
            loadNews(mLeftUrls, callback);
            loadNewsUrl(null);
        } else {
            loadNews(mLoadBlockSize, callback);
        }
    }

    @WorkerThread
    private void loadNewsUrl(Runnable urlCallback){
        String url = Config.getImportNewsUrl();
        Document doc = Documenter.loadDoc(url);
        if (doc != null) {
            Log.d("wjm", url);
            Element newsList = doc.select(".news_list").get(2);
            if (newsList != null) {
                List<String> urls = new ArrayList<>();
                Element ul = newsList.child(0);
                for (Element li : ul.children()) {
                    Element a = li.child(0);
                    urls.add(a.attr("href"));
                }
                mLoadedUrls.addAll(urls);
                mLeftUrls = mLoadedUrls.size();
                if(urlCallback != null)
                    urlCallback.run();
            }
        }
    }

    private void loadNews(int loadNum, ILoadingDone callback){
        List<News> newsList = new ArrayList<>();
        CountDownLatch cdl = new CountDownLatch(loadNum);
        for(int i = 0;i < loadNum;i++){
            mPool.execute(new LoadNewsTask(mLoadedUrls.pop(), newsList, cdl));
        }
        mPool.execute(new LoadDoneTask(cdl, new Runnable() {
            @Override
            public void run() {
                mLeftUrls = mLoadedUrls.size();
                callback.loadingDone(newsList);
            }
        }));
    }

    private static class LoadNewsTask implements Runnable{
        private String url;
        private List<News> newsList;
        CountDownLatch cdl;
        LoadNewsTask(String url, List<News> list, CountDownLatch cdl){
            this.url = url;
            newsList = list;
            this.cdl = cdl;
        }
        @Override
        public void run() {
            newsList.add(new News(url));
            cdl.countDown();
        }
    }

    private static class LoadDoneTask implements Runnable{
        private CountDownLatch cdl;
        private Runnable task;
        LoadDoneTask(CountDownLatch cdl, Runnable task){
            this.cdl = cdl;
            this.task = task;
        }
        @Override
        public void run() {
            try {
                cdl.await();
                task.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
