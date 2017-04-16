package com.tongji.wangjimin.tongjinews.net.search;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangjimin on 17/4/16.
 * NewsItemDigest.
 */

public class NewsItemDigest implements Parcelable{
    private String mUrl;
    private String mTitle;
    private String mDigest;

    protected NewsItemDigest(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
        mDigest = in.readString();
    }

    public static final Creator<NewsItemDigest> CREATOR = new Creator<NewsItemDigest>() {
        @Override
        public NewsItemDigest createFromParcel(Parcel in) {
            return new NewsItemDigest(in);
        }

        @Override
        public NewsItemDigest[] newArray(int size) {
            return new NewsItemDigest[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeString(mTitle);
        dest.writeString(mDigest);
    }
}
