package com.tongji.wangjimin.tongjinews;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tongji.wangjimin.tongjinews.adapter.MainViewPagerAdapter;

public class ImportNewsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_news);
        /* work */
        Fresco.initialize(this);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_importnews);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        mTabLayout = (TabLayout)findViewById(R.id.tablayout_importnews);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
