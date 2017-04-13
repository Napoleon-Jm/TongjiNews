package com.tongji.wangjimin.tongjinews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tongji.wangjimin.tongjinews.adapter.MainViewPagerAdapter;
import com.tongji.wangjimin.tongjinews.fragment.DigestImageFragment;
import com.tongji.wangjimin.tongjinews.fragment.ImportNewsFragment;
import com.tongji.wangjimin.tongjinews.view.DoubleClickToolbar;

public class ImportNewsActivity extends AppCompatActivity implements DigestImageFragment.FlipOnListener{

    private AppBarLayout mAppBarLayout;
    private DoubleClickToolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private NavigationView mNav;
    private DrawerLayout mDrawer;
    private MainViewPagerAdapter mAdapter;
    private boolean mIsFlipOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * 为了解决系统4.4状态栏沉浸方案，设置状态栏为透明（此时为全屏模式，Toolbar 会显示在 StatusBar 下面），
         * 所以需要在 content 上面添加一个 view 当做状态栏的着色。
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏颜色，此处代码有待查证。没能很好解决全屏导致的 Toolbar 上移问题。
            ViewGroup contentLayout = (ViewGroup)findViewById(android.R.id.content);
            setupStatusBarView(contentLayout, ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
            contentLayout.setFocusableInTouchMode(true);
//            View contentChild = contentLayout.getChildAt(0);
//            contentChild.setFitsSystemWindows(true);
        }
        setContentView(R.layout.activity_import_news);
        /* work */
        mNav = (NavigationView) findViewById(R.id.nav_importnews);
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_fav:
                    case R.id.item_msg:
                    case R.id.item_friend:
                        showMsg("To do, please wait, 3Q.", Toast.LENGTH_SHORT);
                        break;
                    case R.id.item_about:
                        showMsg("WangJimin 7 years in Tongji University.", Toast.LENGTH_LONG);
                        break;
                }
                return false;
            }
        });
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_importnews);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_main);
        mToolbar = (DoubleClickToolbar) findViewById(R.id.toolbar_importnews);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    ((DigestImageFragment)mAdapter.getItem(1)).reloadData();
                } else {
                    ((ImportNewsFragment)mAdapter.getItem(0)).reloadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mTabLayout = (TabLayout)findViewById(R.id.tablayout_importnews);
        mTabLayout.setupWithViewPager(mViewPager);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_format_list_bulleted_white_24dp, null);
        mToolbar.setNavigationIcon(drawable);
        mToolbar.setDoubleClickListener(new DoubleClickToolbar.DoubleClickListener() {
            @Override
            public void onClick() {
                if(mViewPager.getCurrentItem() == 0){
                    Fragment fragment = mAdapter.getItem(0);
                    ((ImportNewsFragment)fragment).scrollToTop();
                }
            }
        });
    }

    private void setupStatusBarView(ViewGroup contentLayout, int color) {
        View statusBarView = new View(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this));
        contentLayout.addView(statusBarView, lp);
        statusBarView.setBackgroundColor(color);
    }

    private int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public void collapseToolbar(){
        mAppBarLayout.setExpanded(false, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showMsg(String text, int time){
        Toast.makeText(this, text, time).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() == 1){
            if(mIsFlipOn){
                ((DigestImageFragment)mAdapter.getItem(1)).whenFlipOnBack();
            } else {
                mViewPager.setCurrentItem(0);
            }
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void flipOn(boolean f) {
        mIsFlipOn = f;
    }
}
