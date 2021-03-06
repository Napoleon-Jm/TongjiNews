package com.tongji.wangjimin.tongjinews.net;

import android.support.annotation.WorkerThread;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjimin on 17/2/25.
 *
 * NewsContent.
 * Couldn't initialized in main thread.
 */

public class NewsContent {
    /* could be initialized in constructor. */
    private News mNewsInfo;
    /* News content list, text and image. */
    private List<String> mContent;

    @WorkerThread
    public NewsContent(News newsInfo,Runnable callback){
        mNewsInfo = newsInfo;
        parseContentFromUrl(newsInfo.getUrl());
        if(callback != null)
            callback.run();
    }

    @WorkerThread
    public NewsContent(String url, Runnable callback){
        parseContentFromUrl(url);
        if(callback != null){
            callback.run();
        }
    }

    private void parseContentFromUrl(String url){
        mContent = new ArrayList<>();
        Document doc = Documenter.loadDoc(mNewsInfo.getUrl());
        if(doc == null){
            return;
        }
        Elements es = doc.select(".news_content");
        if(es != null){
            Element contents = es.first();
            //test
            //String html = contents.html();
            //String text = contents.text(); 包含<p>中的内容，不包含img等标签的内容。
            if(contents.select("p").size() > 2)
                for(Element e : contents.children()){
                    mContent.add(e.html());
                }
            else{
                mContent.add(contents.text());
            }
        }
    }

    public News getNewsInfo() {
        return mNewsInfo;
    }

    public List<String> getContent() {
        return mContent;
    }
}
