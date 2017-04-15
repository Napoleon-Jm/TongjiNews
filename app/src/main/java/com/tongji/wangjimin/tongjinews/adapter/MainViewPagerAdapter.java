package com.tongji.wangjimin.tongjinews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tongji.wangjimin.tongjinews.fragment.DigestImageFragment;
import com.tongji.wangjimin.tongjinews.fragment.ImportNewsFragment;

/**
 * Created by wangjimin on 17/2/28.
 * MainViewPagerAdapter, for
 * {@link com.tongji.wangjimin.tongjinews.activity.ImportNewsActivity}'s
 * mViewPager.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = {"News", "Images"};
    private Fragment[] fragments = {new ImportNewsFragment(), new DigestImageFragment()};
    public static final int IMPORTNEWS_INDEX = 0;
    public static final int DIGESTIMAGE_INDEX = 1;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
