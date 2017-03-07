package com.tongji.wangjimin.tongjinews.data;

import android.provider.BaseColumns;

/**
 * Created by wangjimin on 17/3/7.
 */

public class NewsReaderContract {

    private NewsReaderContract(){}

    public static class NewsEntry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_READNUM = "readnum";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_IMAGES = "images";
    }

}
