package com.tongji.wangjimin.tongjinews.net.util;

import java.util.Arrays;

/**
 * Created by wangjimin on 17/2/24.
 * Config info for news loader.
 */
public class Config {
    private static int sCurrentPage = 0;
    public static final String HOST = "http://news.tongji.edu.cn/";
    private static final String IMPORT_NEWS_URL = "http://news.tongji.edu.cn/classid-15.html";
    public static String getImportNewsUrl(){
        sCurrentPage++;
        if(sCurrentPage < 2){
            return IMPORT_NEWS_URL;
        } else {
            return "http://news.tongji.edu.cn/classid-15-" + sCurrentPage + ".html";
        }
    }

    public static String getFreshImportNewsUrl(){
        return IMPORT_NEWS_URL;
    }

    /* In case malformed url error, must add prefix "http://". */
    private static final String SEARCH_URL = "http://sou.tongji.edu.cn/content.php?keyword=";

    public static String getSearchUrl(String... keyWords){
        String keyWordStr = Arrays.toString(keyWords);
        keyWordStr = keyWordStr.substring(1, keyWordStr.length()-1);
        return SEARCH_URL + keyWordStr.replace(',', '+');
    }
}
