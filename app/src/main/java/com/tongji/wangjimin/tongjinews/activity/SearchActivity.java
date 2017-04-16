package com.tongji.wangjimin.tongjinews.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.adapter.SearchAdapter;
import com.tongji.wangjimin.tongjinews.data.AsyncSearcher;
import com.tongji.wangjimin.tongjinews.net.News;
import com.tongji.wangjimin.tongjinews.net.search.NewsItemDigest;
import com.tongji.wangjimin.tongjinews.utils.Utils;
import com.tongji.wangjimin.tongjinews.view.HideSoftInputLinearLayout;
import com.tongji.wangjimin.tongjinews.view.RefreshRecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    /* No leak memory. */
    private class RecvHandler extends Handler {
        private WeakReference<SearchActivity> wkAct;
        private static final int SEARCH_BACK = 0;

        private RecvHandler(SearchActivity activity){
            wkAct = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == SEARCH_BACK){
                SearchActivity act = wkAct.get();
                if(act != null){
                    mAdapter.setDataAndNotify(mSearchData);
                }
            }
        }
    }

    private RecvHandler mHandler;
    private HideSoftInputLinearLayout mContainer;
    private EditText mSearchEditText;
    private TextView mCanel;
    private RefreshRecyclerView mRecycler;
    private SearchAdapter mAdapter;
    private List<NewsItemDigest> mSearchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        /* work */
        mHandler = new RecvHandler(this);
        mContainer = (HideSoftInputLinearLayout)findViewById(R.id.container_search_activity);
        mContainer.setOnInterceptTouchEvent(new HideSoftInputLinearLayout.OnInterceptTouchEvent() {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                int[] location = new int[2];
                mSearchEditText.getLocationOnScreen(location);
                Rect area = new Rect(location[0], location[1],
                        location[0] + mSearchEditText.getWidth(),
                        location[1] + mSearchEditText.getHeight());
                if(!area.contains((int)ev.getRawX(), (int)ev.getRawY())){
                    mSearchEditText.clearFocus();
                    mSearchEditText.setCursorVisible(false);
                    Utils.hideSoftKeyboard(SearchActivity.this);
                }
                return false;
            }
        });
        mSearchEditText = (EditText)findViewById(R.id.search_edittext);
        /* 将回车按键显示为放大镜, 同时 inputType 也要设置 */
        mSearchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setCursorVisible(true);
            }
        });
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String keyWrod = mSearchEditText.getText().toString();
                    Utils.hideSoftKeyboard(SearchActivity.this);
                    AsyncSearcher.getInstance().search(new AsyncSearcher.SearchCallback() {
                        @Override
                        public void searchDone(List<NewsItemDigest> res) {
                            mSearchData = res;
                            mHandler.sendEmptyMessage(RecvHandler.SEARCH_BACK);
                        }
                    }, keyWrod);
                }
                return false;
            }
        });
        mRecycler = (RefreshRecyclerView)findViewById(R.id.search_recyclerview);
        mAdapter = new SearchAdapter(this);
        mAdapter.setOnItemClickListener(new SearchAdapter.SearchItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(SearchActivity.this, NewsContentActivity.class);
                NewsItemDigest digest = mAdapter.getData().get(pos);
                News news = new News(digest.getTitle(), null, null, digest.getUrl(), null);
                intent.putExtra("newsinfo", news);
                startActivity(intent);
            }
        });
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mCanel = (TextView)findViewById(R.id.cancel);
        mCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
