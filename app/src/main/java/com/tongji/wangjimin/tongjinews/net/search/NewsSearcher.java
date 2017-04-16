package com.tongji.wangjimin.tongjinews.net.search;

import android.support.annotation.WorkerThread;
import android.util.Log;

import com.tongji.wangjimin.tongjinews.log.LogMsg;
import com.tongji.wangjimin.tongjinews.net.Documenter;
import com.tongji.wangjimin.tongjinews.net.util.Config;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjimin on 17/4/16.
 * NewSearch.
 */

public class NewsSearcher {
    private static NewsSearcher mInstance;
    private static List<NewsItemDigest> mDigests;

    private NewsSearcher(){
        mDigests = new ArrayList<>();
    }

    public static NewsSearcher getInstance(){
        if(mInstance == null){
            mInstance = new NewsSearcher();
        }
        return mInstance;
    }

    /**
     * Search function.
     * @param kewWorks search key words.
     * @return null: parse error.
     */
    @WorkerThread
    public List<NewsItemDigest> search(String kewWorks){
        Document doc = Documenter.loadDoc(Config.getSearchUrl(kewWorks));
        Elements eles = doc.select(".list");
        /* Parse failed or no results. */
        if(eles == null || eles.size() != 1){
            return null;
        }
        mDigests.clear();
        /* Digests div */
        Element ele = eles.first();
        Elements dlTag = ele.select("dl");
        for(int i = 0;i < dlTag.size();i++){
            /* Digest item */
            Element digestEle = dlTag.get(i);
            /* Tag a */
            Element a = digestEle.select("a").first();
            String title = a.text();
            Log.d(LogMsg.LOG_TAG, title);
            String url = a.attr("href");
            Log.d(LogMsg.LOG_TAG, url);
            String digest = digestEle.select(".text").first().text();
            Log.d(LogMsg.LOG_TAG, digest);
            mDigests.add(new NewsItemDigest(url, title, digest));
        }
        return mDigests;
    }
}
