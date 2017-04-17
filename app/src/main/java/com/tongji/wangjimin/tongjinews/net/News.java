package com.tongji.wangjimin.tongjinews.net;
import android.annotation.TargetApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.tongji.wangjimin.tongjinews.net.util.Config;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjimin on 17/2/24.
 * News.
 */
public class News implements Parcelable, Comparable<News>{
    private static final String NEWS_CONTENT_PAGE_TILE_CLASS_SELECTOR = ".news_title";
    private static final String NEWS_CONTENT_PAGE_NEWSINFO_CALSS_SELECTOR = ".news_info";
    private static final String NEWS_CONTENT_PAGE_NEWSCONTENT_CLASS_SELECTOR = ".news_content";

    private static final String NEWSCONTENT_IMAGE_TAG_NAME = "img";
    private static final String NEWSCONTENT_IMAGE_URL_ATTRIBUTE_NAME = "src";

    private String mTitle;
    private String mDate;
    private String mReadNum;
    private final String mUrl;
    private final List<String> mImages;

    public static final String TAG = "newsinfo";

    @WorkerThread
    News(String url){
        mUrl = Config.HOST + url;
        Document doc = Documenter.loadDoc(mUrl);
        Element title = null;
        Element dateAndNum = null;
        Element content = null;
        mImages =new ArrayList<>();
        if(doc != null){
            title = doc.select(NEWS_CONTENT_PAGE_TILE_CLASS_SELECTOR).first();
            dateAndNum = doc.select(NEWS_CONTENT_PAGE_NEWSINFO_CALSS_SELECTOR).first();
            content = doc.select(NEWS_CONTENT_PAGE_NEWSCONTENT_CLASS_SELECTOR).first();
        }
        if(title != null){
            mTitle = title.text();
        }
        if(dateAndNum != null){
            String str = dateAndNum.text().trim();
            int indexStart = str.indexOf("时间") + 3;
            mDate = str.substring(indexStart + 6, indexStart + 10) + "/" +
                    str.substring(indexStart, indexStart + 5);
            indexStart = str.indexOf("次数") + 3;
            mReadNum = str.substring(indexStart);
        }
        if(content != null){
            //collect. @TargetApi(24);
            // 此时的 Android Studio 还不支持 Java8 的 steam 特性。
            // mImages.addAll(content.select("img").stream().map(e -> e.attr("src")).collect(Collectors.toList()));
            for(Element img:content.select(NEWSCONTENT_IMAGE_TAG_NAME)){
                mImages.add(img.attr(NEWSCONTENT_IMAGE_URL_ATTRIBUTE_NAME));
            }
        }
    }

    public News(String title, String date, String readNum, String url, List<String> images){
        mTitle = title;
        mDate = date;
        mReadNum = readNum;
        mUrl = url;
        mImages = images;
    }

    private News(Parcel in) {
        mTitle = in.readString();
        mDate = in.readString();
        mReadNum = in.readString();
        mUrl = in.readString();
        mImages = in.createStringArrayList();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getReadNum() {
        return mReadNum;
    }

    public List<String> getImages() {
        return mImages;
    }

    @Override
    public String toString() {
        return mUrl + ", \n" +
                mTitle + ", \n" +
                mDate + ", \n" +
                mReadNum + ", \n" +
                mImages.toString() + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDate);
        dest.writeString(mReadNum);
        dest.writeString(mUrl);
        dest.writeStringList(mImages);
    }

    /* Date is bigger, position is nearer. */
    @Override
    public int compareTo(@NonNull News o) {
        if(o.getDate() == null || this.getDate() == null)
            return 1;
        return o.getDate().compareTo(this.getDate());
    }
}
