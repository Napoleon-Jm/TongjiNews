package com.tongji.wangjimin.tongjinews.net;
import android.util.Log;

import com.tongji.wangjimin.tongjinews.log.LogMsg;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by wangjimin on 17/2/24.
 * Get Document object.
 */
class Documenter {
    static Document loadDoc(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            // 方便定位 Exception。
            Log.w(LogMsg.ERROR_TAG, e.getMessage());
        }
        return null;
    }
}
