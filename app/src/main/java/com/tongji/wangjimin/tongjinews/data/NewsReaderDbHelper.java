package com.tongji.wangjimin.tongjinews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangjimin on 17/3/7.
 */

public class NewsReaderDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "News.db";
    private static int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsReaderContract.NewsEntry.TABLE_NAME + " (" +
                    NewsReaderContract.NewsEntry._ID + " INTEGER PRIMARY KEY," +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_DATE + "TEXT," +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_READNUM + "TEXT," +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_URL + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsReaderContract.NewsEntry.TABLE_NAME;

    public NewsReaderDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
