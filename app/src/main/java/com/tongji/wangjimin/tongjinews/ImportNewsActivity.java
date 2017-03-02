package com.tongji.wangjimin.tongjinews;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tongji.wangjimin.tongjinews.adapter.MainViewPagerAdapter;
import com.tongji.wangjimin.tongjinews.fragment.ImportNewsFragment;
import com.tongji.wangjimin.tongjinews.view.MyToolbar;

public class ImportNewsActivity extends AppCompatActivity {

    private MyToolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_news);
        /* work */
        mToolbar = (MyToolbar) findViewById(R.id.toolbar_importnews);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout = (TabLayout)findViewById(R.id.tablayout_importnews);
        mTabLayout.setupWithViewPager(mViewPager);
        mToolbar.setDoubleClickListener(new MyToolbar.DoubleClickListener() {
            @Override
            public void onClick() {
                if(mViewPager.getCurrentItem() == 0){
                    Fragment fragment = mAdapter.getItem(0);
                    ((ImportNewsFragment)mAdapter.getItem(0)).scrollToTop();
                }
            }
        });
    }
}
