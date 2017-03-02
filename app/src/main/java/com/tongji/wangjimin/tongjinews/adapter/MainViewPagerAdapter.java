package com.tongji.wangjimin.tongjinews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tongji.wangjimin.tongjinews.fragment.ImportNewsFragment;

/**
 * Created by wangjimin on 17/2/28.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = {"News", "Images"};
    Fragment[] fragments = {new ImportNewsFragment(), new ImportNewsFragment()};

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
