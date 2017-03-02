package com.tongji.wangjimin.tongjinews.net;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by wangjimin on 17/2/24.
 * Get Document.
 */
class Documenter {
    static Document loadDoc(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
