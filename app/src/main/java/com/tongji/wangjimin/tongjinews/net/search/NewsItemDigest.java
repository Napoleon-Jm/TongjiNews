package com.tongji.wangjimin.tongjinews.net.search;

/**
 * Created by wangjimin on 17/4/16.
 * NewsItemDigest.
 */

public class NewsItemDigest {
    private String mUrl;
    private String mTitle;
    private String mDigest;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDigest() {
        return mDigest;
    }

    public void setDigest(String digest) {
        this.mDigest = digest;
    }

    public NewsItemDigest(){}

    public NewsItemDigest(String url, String title, String digest){
        mUrl = url;
        mTitle = title;
        mDigest = digest;
    }

    @Override
    public String toString() {
        return mUrl + "\n" +
                mTitle + "\n" +
                mDigest + "\n";
    }
}
