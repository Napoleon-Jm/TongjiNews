package com.tongji.wangjimin.tongjinews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.TABLE_FAV_NAME;
import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.TABLE_NAME;

/**
 * Created by wangjimin on 17/3/7.
 * DbHelper, could be optimized by ORM framework.
 */

public class NewsReaderDbHelper extends SQLiteOpenHelper {
    /* Database name */
    private static final String DATABASE_NAME = "News.db";
    /* Version code */
    private static int DATABASE_VERSION = 1;
    /* Note the blank space between sql world. */
    private static final String TABLE_PARAMS = " (" +
            NewsReaderContract.NewsEntry._ID + " INTEGER PRIMARY KEY," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_TITLE + " TEXT," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_DATE + " TEXT," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_READNUM + " TEXT," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_URL + " TEXT," +
            NewsReaderContract.NewsEntry.COLUMN_NAME_IMAGES + " TEXT)";
    /* Create cache table */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsReaderContract.NewsEntry.TABLE_NAME + TABLE_PARAMS;
    /* Create favorites table */
    private static final String SQL_CREATE_FAV_ENTRIES =
            "CREATE TABLE " + NewsReaderContract.NewsEntry.TABLE_FAV_NAME + TABLE_PARAMS;
    /* Delete cache table */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsReaderContract.NewsEntry.TABLE_NAME;
    /* Delete favorites table */
    private static final String SQL_DELETE_FAV_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsReaderContract.NewsEntry.TABLE_FAV_NAME;

    /*
    Mode {@see fun isExit}
    MODE_NO_OP: No other work to do.
    MODE_INSERT: If data is not exist, then insert data.
    MODE_DELETE: If data is exist, then delete data.
     */
    public static final int MODE_NO_OP = 0;
    public static final int MODE_INSERT = 1;
    public static final int MODE_DELETE = 2;

    /* Singleton */
    private static NewsReaderDbHelper instance;

    private NewsReaderDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static NewsReaderDbHelper getInstance(Context context){
        if(instance == null){
            instance = new NewsReaderDbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_FAV_ENTRIES);
    }

    /* When database upgrade, delete origin database, and create a new one. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_FAV_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        Log.d("@", "down grade");
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

    public int deleteNews(SQLiteDatabase db, String tableName, News news){
        return db.delete(tableName, COLUMN_NAME_URL + "=?", new String[]{news.getUrl()});
    }

    /**
     * Insert data if its not exist, when isInsert is true.
     * @param db database to operation.
     * @param news data to operation.
     * @param mode operation when data is exist or not.
     * @return if data is exist.
     */
    public boolean isExist(SQLiteDatabase db ,String tableName ,News news, int mode){
        Cursor c = db.query(tableName, new String[]{COLUMN_NAME_URL} ,COLUMN_NAME_URL + "=?",
                new String[]{news.getUrl()}, null, null, null);
        boolean exist = c.moveToNext();
        c.close();
        // if not exist and mode equals to 1, then insert.
        if(!exist && mode == 1){
            insertNews(db, tableName, news);
            // if exist and mode equals to 2, then delete.
        } else if (exist && mode == 2){
            deleteNews(db, tableName, news);
        }
        return exist;
    }

    /* Construct News from cursor */
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

    /* Just return if data is exist in favorites table */
    public boolean isCollected(News news){
        SQLiteDatabase db = getWritableDatabase();
        return isExist(db, TABLE_FAV_NAME, news, MODE_NO_OP);
    }
}
