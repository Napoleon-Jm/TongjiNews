package com.tongji.wangjimin.tongjinews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tongji.wangjimin.tongjinews.net.News;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.COLUMN_NAME_DATE;
import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.COLUMN_NAME_IMAGES;
import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.COLUMN_NAME_READNUM;
import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.COLUMN_NAME_TITLE;
import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.COLUMN_NAME_URL;
import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.TABLE_NAME;

/**
 * Created by wangjimin on 17/3/7.
 *
 * DbHelper, could be optimized by ORM framework.
 */

public class NewsReaderDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "News.db";
    private static int DATABASE_VERSION = 1;
    /* Note the blank space between sql world. */
    private static final String TABLE_PARAMS = " (" +
            NewsReaderContract.NewsEntry._ID + " INTEGER PRIMARY KEY," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_TITLE + " TEXT," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_DATE + " TEXT," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_READNUM + " TEXT," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_URL + " TEXT," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_IMAGES + " TEXT)";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsReaderContract.NewsEntry.TABLE_NAME + TABLE_PARAMS;
    private static final String SQL_CREATE_FAV_ENTRIES =
            "CREATE TABLE " + NewsReaderContract.NewsEntry.TABLE_FAV_NAME + TABLE_PARAMS;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsReaderContract.NewsEntry.TABLE_NAME;
    private static final String SQL_DELETE_FAV_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsReaderContract.NewsEntry.TABLE_FAV_NAME;

    public NewsReaderDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_FAV_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_FAV_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertNews(SQLiteDatabase db ,String tableName, News news){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, news.getTitle());
        values.put(COLUMN_NAME_DATE, news.getDate());
        values.put(COLUMN_NAME_URL, news.getUrl());
        values.put(COLUMN_NAME_READNUM, news.getReadNum());
        values.put(COLUMN_NAME_IMAGES, new JSONArray(news.getImages()).toString());
        return db.insert(tableName, null, values);
    }

    public News cursorToNews(Cursor c){
        List<String> images = null;
        int index = c.getColumnIndex(COLUMN_NAME_TITLE);
        String title = c.getString(index);
        index = c.getColumnIndex(COLUMN_NAME_DATE);
        String date = c.getString(index);
        index = c.getColumnIndex(COLUMN_NAME_READNUM);
        String readNum = c.getString(index);
        index = c.getColumnIndex(COLUMN_NAME_URL);
        String url = c.getString(index);
        index = c.getColumnIndex(COLUMN_NAME_IMAGES);
        try {
            images = new ArrayList<>();
            JSONArray array = new JSONArray(c.getString(index));
            for(int i = 0;i < array.length();i++){
                images.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new News(title, date, readNum, url, images);
    }
}
