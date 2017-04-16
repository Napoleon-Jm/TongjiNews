package com.tongji.wangjimin.tongjinews.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.adapter.NewsContentAdapter;
import com.tongji.wangjimin.tongjinews.adapter.NewsContentImageAdapter;
import com.tongji.wangjimin.tongjinews.data.NewsReaderContract;
import com.tongji.wangjimin.tongjinews.data.NewsReaderDbHelper;
import com.tongji.wangjimin.tongjinews.net.News;
import com.tongji.wangjimin.tongjinews.net.NewsContent;
import com.tongji.wangjimin.tongjinews.net.util.NetUtils;
import com.tongji.wangjimin.tongjinews.utils.Utils;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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

    private CollapsingToolbarLayout mCollapsLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ViewPager mViewPager;
    private ImageView mFavorites;
    private NewsContentAdapter mAdapter;
    private NewsContent mNewsContent;
    private News mNewsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        // 总体效果是，状态栏全透明，导航栏半透明。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            // 此处是全部透明
            w.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            // 有阴影，半透明
            // w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            // WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        /* work */
        mCollapsLayout = (CollapsingToolbarLayout)findViewById(R.id.collapslayout);
        mToolbar = (Toolbar)findViewById(R.id.content_toolbar);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_newcontent);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mViewPager = (ViewPager)findViewById(R.id.newscontent_viewpager);
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
        mAdapter = new NewsContentAdapter(this);
        mAdapter.setClickListener(new NewsContentAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                List<String> data = new ArrayList<String>();
                String url = NetUtils.parseImageUrl(mAdapter.getItemData(position));
                data.add(url);
                NewsContentImageAdapter adapter =
                        new NewsContentImageAdapter(data, NewsContentActivity.this, mViewPager);
                mViewPager.setVisibility(View.VISIBLE);
                mViewPager.setAdapter(adapter);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mFavorites = (ImageView)findViewById(R.id.newcontent_fav);
        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsReaderDbHelper dbHelper = NewsReaderDbHelper
                        .getInstance(NewsContentActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if(!dbHelper.isExist(db, NewsReaderContract.NewsEntry.TABLE_FAV_NAME,
                        mNewsInfo, NewsReaderDbHelper.MODE_INSERT)){
                    mFavorites.setImageDrawable(Utils
                            .getDrawable(NewsContentActivity.this, R.drawable.ic_favorite_white_24dp));
                    Toast.makeText(NewsContentActivity.this, R.string.collect_msg, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    dbHelper.deleteNews(db, NewsReaderContract.NewsEntry.TABLE_FAV_NAME, mNewsInfo);
                    mFavorites.setImageDrawable(Utils.getDrawable(NewsContentActivity.this,
                            R.drawable.ic_favorite_border_white_24dp));
                    Toast.makeText(NewsContentActivity.this
                            , R.string.remove_fav_msg, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        //接收 Activity 传递的对象.
        Intent intent = getIntent();
        mNewsInfo = intent.getParcelableExtra("newsinfo");
        NewsReaderDbHelper dbHelper = NewsReaderDbHelper
                .getInstance(NewsContentActivity.this);
        if(dbHelper.isCollected(mNewsInfo)){
            mFavorites.setImageDrawable(Utils.getDrawable(this, R.drawable.ic_favorite_white_24dp));
        }
        mCollapsLayout.setTitle(mNewsInfo.getTitle());
        /*
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
                mNewsContent = new NewsContent(mNewsInfo, new Runnable() {
                    @Override
                    public void run() {
                        // no work to do.
                    }
                });
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search_bar:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
