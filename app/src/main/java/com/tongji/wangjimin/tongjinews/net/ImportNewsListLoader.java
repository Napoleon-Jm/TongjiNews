package com.tongji.wangjimin.tongjinews.net;

import android.support.annotation.WorkerThread;
import android.util.Log;

import com.tongji.wangjimin.tongjinews.log.LogMsg;
import com.tongji.wangjimin.tongjinews.net.util.Config;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangjimin on 17/2/27.
 * ImportNewsLoader.
 * Import news loader v2.0.
 */

public class ImportNewsListLoader {

    /**
     * Callback for {#load}.
     */
    public interface ILoadingDone{
        void loadingDone(List<News> newsList);
    }

    private static final String IMPORTNEWS_URL_LIST_CLASS_SELECTOR = ".news_list";
    private static final int IMPORTNEWS_URL_LIST_CLASS_INDEX_IN_HTML = 2;
    private static final String IMPORTNEWS_URL_ATTRIBUTE_NAME = "href";

    /**
     * News page urls, which is loaded by Jsoup.
     * All the urls can be used to parse to {@link News} object.
     */
    private LinkedList<String> mLoadedUrls;
    /* unused news page urls size. */
    private int mLeftUrls;
    /* Load news size once {#load} called. */
    private int mLoadBlockSize;
    /* Thread pool */
    private ExecutorService mPool;
    /** Thread lock used by {@link LoadNewsTask}. */
    private static final Object lock = new Object();
    /* Singleton. */
    private static ImportNewsListLoader instance;

    private ImportNewsListLoader(){
        mLoadedUrls = new LinkedList<>();
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
        List<String> urls = loadUrl(Config.getImportNewsUrl());
        if(urls != null){
            mLoadedUrls.addAll(urls);
            mLeftUrls += urls.size();
        }
        if(urlCallback != null)
            urlCallback.run();
    }

    private List<String> loadUrl(String pageUrl){
        String url = pageUrl;
        Log.d(LogMsg.LOG_TAG, url);
        Document doc = Documenter.loadDoc(url);
        if (doc != null) {
            Elements newsLists = doc.select(IMPORTNEWS_URL_LIST_CLASS_SELECTOR);
            Element newsList = newsLists.get(IMPORTNEWS_URL_LIST_CLASS_INDEX_IN_HTML);
            if (newsList != null) {
                List<String> urls = new ArrayList<>();
                Element ul = newsList.child(0);
                for (Element li : ul.children()) {
                    Element a = li.child(0);
                    urls.add(a.attr(IMPORTNEWS_URL_ATTRIBUTE_NAME));
                }
                return urls;
            }
        }
        return null;
    }

    @WorkerThread
    public void loadRefresh(ILoadingDone callback){
        List<String> refreshUrl = loadUrl(Config.getFreshImportNewsUrl());
        if(refreshUrl != null){
            for(String url : refreshUrl){
                if(!refreshUrl.contains(url)){
                    // TODO: 17/3/27
                    // load refresh news, and return news list;
                    callback.loadingDone(null);
                }
            }
        }
    }

    private void loadNews(int loadNum, ILoadingDone callback){
        List<News> newsList = new ArrayList<>();
        CountDownLatch cdl = new CountDownLatch(loadNum);
        for(int i = 0;i < loadNum;i++){
            // 需要先判断是否为空，防止后续 Jsoup 出错。
            if(mLoadedUrls.size() > 0)
                mPool.execute(new LoadNewsTask(mLoadedUrls.pop(), newsList, cdl));
        }
        mPool.execute(new LoadDoneTask(cdl, new Runnable() {
            @Override
            public void run() {
                mLeftUrls -= loadNum;
                Collections.sort(newsList);
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
            synchronized (lock){
                newsList.add(new News(url));
            }
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
