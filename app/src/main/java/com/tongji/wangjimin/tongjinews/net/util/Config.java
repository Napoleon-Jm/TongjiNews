package com.tongji.wangjimin.tongjinews.net.util;

/**
 * Created by wangjimin on 17/2/24.
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
}
