package com.tongji.wangjimin.tongjinews.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.adapter.DigestImageAdapter;
import com.tongji.wangjimin.tongjinews.data.ImportNewsLoaderWithCache;
import com.tongji.wangjimin.tongjinews.net.News;
import com.tongji.wangjimin.tongjinews.view.RefreshRecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wangjimin on 17/3/17.
 */

public class DigestImageFragment extends Fragment {

    private static class RecvHandler extends Handler{
        private WeakReference<DigestImageFragment> wRef;
        private RecvHandler(DigestImageFragment fragment){
            wRef = new WeakReference<DigestImageFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DigestImageFragment actFragment = wRef.get();
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    actFragment.mAdapter.addAll(actFragment.mNewsList);
                case 2:
                    if(actFragment != null){
                        actFragment.mSwipeLayout.setRefreshing(false);
                    }
            }
        }
    }

    private SwipeRefreshLayout mSwipeLayout;
    private RefreshRecyclerView mRecyclerView;
    private RecvHandler mHandler;
    private List<News> mNewsList;
    private ImportNewsLoaderWithCache mDataLoader;
    private boolean isFirstVisibile;
    private DigestImageAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataLoader = ImportNewsLoaderWithCache.getInstance(getContext());
        mHandler = new RecvHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_digestimage, container, false);
        mSwipeLayout = (SwipeRefreshLayout)root.findViewById(R.id.swiperefresh_digestimage);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDataLoader.loadRefresh(null);
                        mHandler.sendEmptyMessage(2);
                    }
                }).start();
            }
        });
        mRecyclerView = (RefreshRecyclerView)root.findViewById(R.id.recyclerview_digestimage);
        mAdapter = new DigestImageAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setRefreshWork(new RefreshRecyclerView.Refresher() {
            @Override
            public void refresh() {
                mSwipeLayout.setRefreshing(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDataLoader.loadWithNet(new ImportNewsLoaderWithCache.ILoadingWithCacheDone() {
                            @Override
                            public void loadDone(List<News> newsList) {
                                mNewsList = newsList;
                                mHandler.sendEmptyMessage(1);
                                mRecyclerView.setLoadingDone();
                            }
                        }, false);
                    }
                }).start();
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getUserVisibleHint() && isFirstVisibile){
            loadData();
            isFirstVisibile = false;
        }
    }

    private void loadData() {
        mAdapter.notifyDataSetChanged();
    }

    public void reloadData(){
        Log.d("wjm", "reload " + mAdapter.getItemCount());
        mAdapter.notifyDataSetChanged();
    }
}
