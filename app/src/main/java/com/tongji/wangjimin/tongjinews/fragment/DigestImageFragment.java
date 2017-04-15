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

import com.tongji.wangjimin.tongjinews.activity.ImportNewsActivity;
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
        private static final int MSG_NO_OP = 0;
        private static final int MSG_NETWORK_LOAD_MORE_DONE = 1;
        private static final int MSG_NETWORK_PULL_TO_REFRESH = 2;
        private WeakReference<DigestImageFragment> wRef;

        private RecvHandler(DigestImageFragment fragment) {
            wRef = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DigestImageFragment actFragment = wRef.get();
            switch (msg.what) {
                case MSG_NO_OP:
                    break;
                case MSG_NETWORK_LOAD_MORE_DONE:
                    actFragment.mAdapter.addAll(actFragment.mNewsList);
                case MSG_NETWORK_PULL_TO_REFRESH:
                    if (actFragment != null) {
                        actFragment.mSwipeLayout.setRefreshing(false);
                    }
            }
        }
    }

    /* Responsible for pull down refresh, and first loading animation. */
    private SwipeRefreshLayout mSwipeLayout;
    /* Image list. */
    private RefreshRecyclerView mRecyclerView;
    /* Show big image, which can flip to next one. */
    private FlipRecyclerView mFlipRecyclerView;
    private RecvHandler mHandler;
    /* Reference for pull up loaded data. */
    private List<News> mNewsList;
    /* Data source, responsible for load data. */
    private ImportNewsLoaderWithCache mDataLoader;
    private boolean isFirstVisibile;
    /* Container for flip view. */
    private FrameLayout mFlipLayout;
    /* Back indicator. */
    private ImageView mBack;
    /* Image index text view. */
    private TextView mImageIndex;
    private FlipAdapter mFlipAdapter;
    private DigestImageAdapter mAdapter;
    private FlipShowListener mFlipShowListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataLoader = ImportNewsLoaderWithCache.getInstance(getContext());
        mHandler = new RecvHandler(this);
        mFlipShowListener = (FlipShowListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_digestimage, container, false);
        mSwipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefresh_digestimage);
        mSwipeLayout.setOnRefreshListener(() -> new Thread(() -> {
            mDataLoader.loadRefresh(null);
            mHandler.sendEmptyMessage(RecvHandler.MSG_NETWORK_PULL_TO_REFRESH);
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
            if (mFlipShowListener != null) {
                mFlipShowListener.flipShow(true);
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
                            mHandler.sendEmptyMessage(RecvHandler.MSG_NETWORK_LOAD_MORE_DONE);
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
        mFlipRecyclerView.reload();/* Reset flip view to position 0. */
        mFlipLayout.setVisibility(View.INVISIBLE);
        if (mFlipShowListener != null) {
            mFlipShowListener.flipShow(false);
        }
    }

    /**
     * Notify the listener, when the flip view's visibility changed.
     */
    public interface FlipShowListener {
        void flipShow(boolean f);
    }
}
