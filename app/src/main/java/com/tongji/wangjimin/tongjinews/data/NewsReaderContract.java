package com.tongji.wangjimin.tongjinews.data;

import android.provider.BaseColumns;

/**
 * Created by wangjimin on 17/3/7.
 *
 * Db Contract.
 */

public class NewsReaderContract {

    private NewsReaderContract(){}

    public static class NewsEntry implements BaseColumns{
        /* Cache table */
        public static final String TABLE_NAME = "entry";
        /* Favorites table */
        public static final String TABLE_FAV_NAME = "favorites";
        /* News title. */
        public static final String COLUMN_NAME_TITLE = "title";
        /* News date */
        public static final String COLUMN_NAME_DATE = "date";
        /* News read number */
        public static final String COLUMN_NAME_READNUM = "readnum";
        /* News content url */
        public static final String COLUMN_NAME_URL = "url";
        /* News images url */
        public static final String COLUMN_NAME_IMAGES = "images";
    }

}
