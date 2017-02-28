package com.tongji.wangjimin.tongjinews;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tongji.wangjimin.tongjinews.adapter.ImportNewsAdapter;
import com.tongji.wangjimin.tongjinews.net.ImportNewsListLoader;
import com.tongji.wangjimin.tongjinews.net.News;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * NoLeak Handler
     */
    private static class ReceiveHandler extends Handler{
        private final WeakReference<MainActivity> activity;
        ReceiveHandler(MainActivity activity){
            this.activity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity actualAct = activity.get();
            if(actualAct != null){
                if(msg.what == 0)
//                    actualAct.mAdapter.setDataAndNotify(actualAct.mNews.getNewsList());
                    actualAct.mAdapter.addAll(actualAct.mNewsList);
                else {
//                    actualAct.mAdapter.setDataAndNotify(actualAct.mNews.getNewsList());
                    actualAct.mAdapter.addAll(actualAct.mNewsList);
                    actualAct.mAdapter.removeData(msg.what);
                }
            }
        }
    }
    private final ReceiveHandler mHandler = new ReceiveHandler(this);
    private RecyclerView mRecyclerView;
//    private ImportNews mNews;
    private ImportNewsListLoader mNewsListLoader;
    private ImportNewsAdapter mAdapter;

    private List<News> mNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        /* work */

        new Thread(() -> {
            //crawler v1.0
            //mNews = ImportNews.getInstance();
            //mNews.load(()->mHandler.sendEmptyMessage(0));

            //TODO Memory Leak.
            //crawler v2.0
            mNewsListLoader = ImportNewsListLoader.getInstance();
            mNewsListLoader.load(new ImportNewsListLoader.ILoadingDone() {
                @Override
                public void loadingDone(List<News> newsList) {
                    mNewsList = newsList;
                    mHandler.sendEmptyMessage(0);
                }
            });
        }).start();
    }
}
