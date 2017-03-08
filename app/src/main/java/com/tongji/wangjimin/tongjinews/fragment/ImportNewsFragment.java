package com.tongji.wangjimin.tongjinews.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tongji.wangjimin.tongjinews.NewsContentActivity;
import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.adapter.ImportNewsAdapter;
import com.tongji.wangjimin.tongjinews.data.NewsReaderDbHelper;
import com.tongji.wangjimin.tongjinews.net.ImportNewsListLoader;
import com.tongji.wangjimin.tongjinews.net.News;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wangjimin on 17/2/28.
 * ImportNewsFragment.
 */

public class ImportNewsFragment extends Fragment {
    /**
     * NoLeak Handler
     */
    private static class ReceiveHandler extends Handler {
        private final WeakReference<ImportNewsFragment> fragment;
        ReceiveHandler(ImportNewsFragment fragment){
            this.fragment = new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImportNewsFragment actualFragment = fragment.get();
            if(actualFragment != null){
                if(msg.what == 0)
                    actualFragment.mAdapter.addAll(actualFragment.mNewsList);
                else {
                    actualFragment.mAdapter.addAll(actualFragment.mNewsList);
                    actualFragment.mAdapter.removeData(msg.what);
                }
            }
        }
    }
    private final ReceiveHandler mHandler = new ReceiveHandler(this);
    private RecyclerView mRecyclerView;
    private ImportNewsListLoader mNewsListLoader;
    private ImportNewsAdapter mAdapter;
    private List<News> mNewsList;
    private boolean isFristVisiable = true;
    private NewsReaderDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsListLoader = ImportNewsListLoader.getInstance();
        mAdapter = new ImportNewsAdapter(getContext());
        mDbHelper = new NewsReaderDbHelper(getContext());
        //todo Background?
        mDb = mDbHelper.getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_importnews, container, false);
        mRecyclerView = (RecyclerView)root.findViewById(R.id.recyclerview_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getActivity(), NewsContentActivity.class);
            //Activity 之间传递对象.
            intent.putExtra("newsinfo", mAdapter.getNews(position));
            startActivity(intent);
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem = -1;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItem + 1== recyclerView.getAdapter().getItemCount())){
                    if(!mAdapter.isLoading()){
                        //loading next page.
                        mAdapter.setLoadingFlag();
                        new Thread(){
                            @Override
                            public void run() {
                                mNewsListLoader.load(new ImportNewsListLoader.ILoadingDone() {
                                    @Override
                                    public void loadingDone(List<News> newsList) {
                                        mNewsList = newsList;
                                        mHandler.sendEmptyMessage(lastVisibleItem);
                                        mAdapter.setLoadingDone();
                                    }
                                });
                            }
                        }.start();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getUserVisibleHint() && isFristVisiable){
            isFristVisiable = false;
            loadData();
        }
    }

    /**
     * Quick scroll to top of the screen.
     */
    public void scrollToTop(){
        if(mRecyclerView == null){
            return;
        }
        LinearLayoutManager lManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int firstvisableItem = lManager.findFirstVisibleItemPosition();
        int visibleCount = lManager.findLastVisibleItemPosition() - firstvisableItem;
        if(firstvisableItem > visibleCount*2){
            mRecyclerView.scrollToPosition(visibleCount*2);
        }
        mRecyclerView.smoothScrollToPosition(0);
    }

    private void loadData(){
        new FirstLoadTask().execute(this);
    }

    /**
     * No Memory Leak.
     */
    private static class FirstLoadTask extends AsyncTask<Object, Void, Void>{
        private WeakReference<ImportNewsFragment> fragment;
        @Override
        protected Void doInBackground(Object... params) {
            fragment = new WeakReference<>((ImportNewsFragment) params[0]);
            ImportNewsFragment actFragment = fragment.get();
            if(actFragment != null){
                actFragment.mNewsListLoader.load(new ImportNewsListLoader.ILoadingDone() {
                    @Override
                    public void loadingDone(List<News> newsList) {
                        actFragment.mNewsList = newsList;
                        actFragment.mHandler.sendEmptyMessage(0);
                    }
                });
            }
            return null;
        }
    }

    private static class DbCallback implements ImportNewsListLoader.ILoadingDone{

        @Override
        public void loadingDone(List<News> newsList) {

        }
    }
}
