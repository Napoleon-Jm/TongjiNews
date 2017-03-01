package com.tongji.wangjimin.tongjinews.net.util;

import com.tongji.wangjimin.tongjinews.adapter.NewsContentAdapter;

/**
 * Created by wangjimin on 17/2/28.
 */

public class Utils {
    public static String parseImageUrl(String tag){
        //test
        int startIndex = tag.indexOf("http");
        int endIndex = tag.lastIndexOf("jpg");
        if(endIndex == -1){
            endIndex = tag.lastIndexOf("png");
        }
        endIndex += 3;
        if(startIndex < endIndex){
            //substring, 不包含endIndex位置上的字符.
            return tag.substring(startIndex, endIndex);
        }
        return null;
    }
}
