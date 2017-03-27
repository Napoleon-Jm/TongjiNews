package com.tongji.wangjimin.tongjinews;

import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.tongji.wangjimin.tongjinews.adapter.MainViewPagerAdapter;
import com.tongji.wangjimin.tongjinews.fragment.DigestImageFragment;
import com.tongji.wangjimin.tongjinews.fragment.ImportNewsFragment;
import com.tongji.wangjimin.tongjinews.view.MyToolbar;

public class ImportNewsActivity extends AppCompatActivity {

    private MyToolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private NavigationView mNav;
    private DrawerLayout mDrawer;
    private MainViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_news);
        /* work */
        mNav = (NavigationView) findViewById(R.id.nav_importnews);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_importnews);
        mToolbar = (MyToolbar) findViewById(R.id.toolbar_importnews);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d("wjm", "scroll " + position);
            }

            @Override
            public void onPageSelected(int position) {
//                Log.d("wjm", "seleted " + position);
                if(position == 1){
                    ((DigestImageFragment)mAdapter.getItem(1)).reloadData();
                } else {
                    ((ImportNewsFragment)mAdapter.getItem(0)).reloadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d("wjm", "state " + state);
            }
        });
        mTabLayout = (TabLayout)findViewById(R.id.tablayout_importnews);
        mTabLayout.setupWithViewPager(mViewPager);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_format_list_bulleted_white_24dp, null);
        mToolbar.setNavigationIcon(drawable);
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}
