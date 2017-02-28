package com.tongji.wangjimin.tongjinews;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tongji.wangjimin.tongjinews.adapter.NewsContentAdapter;
import com.tongji.wangjimin.tongjinews.net.News;
import com.tongji.wangjimin.tongjinews.net.NewsContent;

import java.lang.ref.WeakReference;

public class NewsContentActivity extends AppCompatActivity {

    private static final class ReceiveHandler extends Handler{
        private WeakReference<NewsContentActivity> activity;
        private ReceiveHandler(NewsContentActivity activity){
            this.activity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewsContentActivity actualAct = activity.get();
            if(actualAct != null){
                actualAct.mAdapter.setDataAndNotify(actualAct.mNewsContent.getContent());
            }
        }
    }
    private final ReceiveHandler mHandler = new ReceiveHandler(this);

    private RecyclerView mRecyclerView;
    private NewsContentAdapter mAdapter;
    private NewsContent mNewsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        /* work */
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_newcontent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NewsContentAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        //接收 Activity 传递的对象.
        Intent intent = getIntent();
        News newsInfo = intent.getParcelableExtra("newsinfo");
        /**
         * Why also remind me worker thread annotation.
         */
//        new Thread(()->{
//            mNewsContent = new NewsContent(newsInfo);
//        }).start();
        new Thread(){
            @Override
            public void run() {
                super.run();
                /* Runnable 如果用 lambda 代替，会提示 lambda 没有实现 Runnable 接口 */
                mNewsContent = new NewsContent(newsInfo, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
