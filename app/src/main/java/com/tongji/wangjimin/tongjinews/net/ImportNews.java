package com.tongji.wangjimin.tongjinews.net;

import android.support.annotation.WorkerThread;

import com.tongji.wangjimin.tongjinews.net.util.Config;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangjimin on 17/2/24.
 * ImportNews.
 * News Loader v1.0 is deprecated
 * see {@link ImportNewsListLoader}.
 */
class ImportNews {
    private List<News> mNewsList;
    private List<String> mUrlList;
    private ExecutorService mPool;
    /* left url to load news */
    private int mLeftUrls;
    private int mLoadNewsPiece;
    private static final int PIECE = 7;
    private static ImportNews instance;

    public static ImportNews getInstance(){
        if(instance == null)
            instance = new ImportNews(Executors.newFixedThreadPool(4));
        return instance;
    }

    @WorkerThread
    private ImportNews(ExecutorService pool) {
        mPool = pool;
        mNewsList = new ArrayList<>();
        mUrlList = new ArrayList<>();
        mLeftUrls = 0;
        loadNextPage(null);
    }

    @WorkerThread
    private void loadNewsUrl(String url){
        Document doc = Documenter.loadDoc(url);
        if (doc != null) {
            Element newsList = doc.select(".news_list").get(2);
            if (newsList != null) {
                List<String> urls = new ArrayList<>();
                Element ul = newsList.child(0);
                for (Element li : ul.children()) {
                    Element a = li.child(0);
                    urls.add(a.attr("href"));
                }
                mUrlList.addAll(urls);
                mLeftUrls += urls.size();
            }
        }
    }

    private static class LoadNews implements Runnable{
        private String url;
        private List<News> newsList;
        CountDownLatch cdl;
        LoadNews(String url, List<News> list, CountDownLatch cdl){
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

    private static class LoadDone implements Runnable{
        private CountDownLatch cdl;
        private Runnable task;
        LoadDone(Runnable task, CountDownLatch cdl){
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

    public void load(Runnable callback){
        mLoadNewsPiece = PIECE;
        if(mLeftUrls < PIECE){
            //TODO load next page
            mLoadNewsPiece = mLeftUrls;
        }
        CountDownLatch cdl = new CountDownLatch(mLoadNewsPiece);
        int index = mUrlList.size() - mLeftUrls;
        for (int i = index;i < index + mLoadNewsPiece;i++) {
            mPool.execute(new LoadNews(mUrlList.get(i), mNewsList, cdl));
        }
        mPool.execute(new LoadDone(
                () -> {
                    System.out.println(new Date().getTime() + "\nLoad done.");
                    for(int i = index;i < index + 7;i++){
                        System.out.println(mNewsList.get(i));
                    }
                    mLeftUrls -= mLoadNewsPiece;
                    callback.run();
                }, cdl
        ));
    }

    @WorkerThread
    private void loadNextPage(Runnable callback){
        String url = Config.getImportNewsUrl();
        loadNewsUrl(url);
        if(callback != null)
            callback.run();
    }

    public List<News> getNewsList(){
        return mNewsList;
    }

    public List<String> getUrlList(){
        return mUrlList;
    }
}
