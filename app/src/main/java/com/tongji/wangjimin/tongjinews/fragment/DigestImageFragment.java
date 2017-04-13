package com.tongji.wangjimin.tongjinews.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongji.wangjimin.tongjinews.ImportNewsActivity;
import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.adapter.DigestImageAdapter;
import com.tongji.wangjimin.tongjinews.adapter.FlipAdapter;
import com.tongji.wangjimin.tongjinews.data.ImportNewsLoaderWithCache;
import com.tongji.wangjimin.tongjinews.net.News;
import com.tongji.wangjimin.tongjinews.view.FlipRecyclerView;
import com.tongji.wangjimin.tongjinews.view.RefreshRecyclerView;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by wangjimin on 17/3/17.
 * DigestImageFragment.
 */

public class DigestImageFragment extends Fragment {

    /* No leak memory */
    private static class RecvHandler extends Handler {
        private WeakReference<DigestImageFragment> wRef;

        private RecvHandler(DigestImageFragment fragment) {
            wRef = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DigestImageFragment actFragment = wRef.get();
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    actFragment.mAdapter.addAll(actFragment.mNewsList);
                case 2:
                    if (actFragment != null) {
                        actFragment.mSwipeLayout.setRefreshing(false);
                    }
            }
        }
    }

    private SwipeRefreshLayout mSwipeLayout;
    private RefreshRecyclerView mRecyclerView;
    private FlipRecyclerView mFlipRecyclerView;
    private FlipAdapter mFlipAdapter;
    private RecvHandler mHandler;
    private List<News> mNewsList;
    private ImportNewsLoaderWithCache mDataLoader;
    private boolean isFirstVisibile;
    private FrameLayout mFlipLayout;
    private ImageView mBack;
    private TextView mImageIndex;
    private DigestImageAdapter mAdapter;
    private FlipOnListener mFlipOnListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataLoader = ImportNewsLoaderWithCache.getInstance(getContext());
        mHandler = new RecvHandler(this);
        mFlipOnListener = (FlipOnListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_digestimage, container, false);
        mSwipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefresh_digestimage);
        mSwipeLayout.setOnRefreshListener(() -> new Thread(() -> {
            mDataLoader.loadRefresh(null);
            mHandler.sendEmptyMessage(2);
        }).start());
        mFlipLayout = (FrameLayout) root.findViewById(R.id.fliplayout);
        mBack = (ImageView) root.findViewById(R.id.back_image);
        mImageIndex = (TextView) root.findViewById(R.id.image_index);
        mFlipAdapter = new FlipAdapter(getContext());
        mRecyclerView = (RefreshRecyclerView) root.findViewById(R.id.recyclerview_digestimage);
        mFlipRecyclerView = (FlipRecyclerView) root.findViewById(R.id.fliprecyclerview);
        mFlipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mFlipRecyclerView.setAdapter(mFlipAdapter);
        mFlipRecyclerView.setFlipListener((cur ,dir) -> {
            String index = cur + 1 + "/" + mFlipAdapter.getItemCount();
            mImageIndex.setText(index);
        });
        mAdapter = new DigestImageAdapter(getContext());
        mAdapter.setItemClickListener((v, pos) -> {
            mFlipLayout.setVisibility(View.VISIBLE);
            mFlipAdapter.setData(mAdapter.getDataSet().get(pos));
            mImageIndex.setText(MessageFormat.format("1/{0}", mFlipAdapter.getItemCount()));
            ((ImportNewsActivity) getActivity()).collapseToolbar();
            if (mFlipOnListener != null) {
                mFlipOnListener.flipOn(true);
            }
        });
        mBack.setOnClickListener(v -> {
            whenFlipOnBack();
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setRefreshWork(() -> {
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
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getUserVisibleHint() && isFirstVisibile) {
            loadData();
            isFirstVisibile = false;
        }
    }

    private void loadData() {
        mAdapter.notifyDataSetChanged();
    }

    public void reloadData() {
        mAdapter.notifyDataSetChanged();
    }

    public void whenFlipOnBack() {
        // TODO, 只有当 FlipRecyclerView 滑动回第一个时，ViewPager 才正常反应，
        // 当 View.GONE 时，即使 reload ViewPager 也不正常作用，原因有待纠察。
        mFlipRecyclerView.reload();
        mFlipLayout.setVisibility(View.INVISIBLE);
        if (mFlipOnListener != null) {
            mFlipOnListener.flipOn(false);
        }
    }

    public interface FlipOnListener{
        void flipOn(boolean f);
    }
}
