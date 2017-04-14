package com.tongji.wangjimin.tongjinews.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.adapter.ImportNewsAdapter;
import com.tongji.wangjimin.tongjinews.data.ImportNewsLoaderWithCache;
import com.tongji.wangjimin.tongjinews.data.NewsReaderContract;
import com.tongji.wangjimin.tongjinews.net.News;
import com.tongji.wangjimin.tongjinews.view.DoubleClickToolbar;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private DoubleClickToolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ImportNewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        /* work */
        setToolbar();
        mRecyclerView = (RecyclerView)findViewById(R.id.fav_recyclerview);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new ImportNewsAdapter(this);
        mAdapter.clearAdapterOwnData();
        List<News> data = ImportNewsLoaderWithCache.getInstance(this)
                .loadWithDb(NewsReaderContract.NewsEntry.TABLE_FAV_NAME);
        mAdapter.setDataAndNotify(data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(FavoritesActivity.this, NewsContentActivity.class);
            //Activity 之间传递对象.
            intent.putExtra("newsinfo", mAdapter.getNews(position));
            startActivity(intent);
        });
        mAdapter.setOnItemFavoritesClickListener(new ImportNewsAdapter.FavoritesClickListener() {
            @Override
            public void onClick(boolean isCollect, int pos) {
                mAdapter.removeData(pos);
            }
        });
    }

    private void setToolbar() {
        mToolbar = (DoubleClickToolbar)findViewById(R.id.fav_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Favorites");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
