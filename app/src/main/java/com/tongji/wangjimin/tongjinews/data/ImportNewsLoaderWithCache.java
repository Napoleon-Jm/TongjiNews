package com.tongji.wangjimin.tongjinews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tongji.wangjimin.tongjinews.net.ImportNewsListLoader;
import com.tongji.wangjimin.tongjinews.net.News;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.*;

/**
 * Created by wangjimin on 17/3/8.
 */

public class ImportNewsLoaderWithCache {
    private static ImportNewsListLoader mNetLoader;
    private static NewsReaderDbHelper mDbHelper;
    private static ImportNewsLoaderWithCache instance;

    private ImportNewsLoaderWithCache(Context context){
        mNetLoader = ImportNewsListLoader.getInstance();
        mDbHelper = new NewsReaderDbHelper(context);
    }

    public static ImportNewsLoaderWithCache getInstance(Context context){
        if(instance == null)
            instance = new ImportNewsLoaderWithCache(context);
        return instance;
    }

    public List<News> loadWithCache(ILoadingWithCacheDone callback){
        //
        loadWithNet(callback, true);
        return loadWithDb();
    }

    private List<News> loadWithDb(){
        //
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c =  db.query(TABLE_NAME, null, null, null, null, null, COLUMN_NAME_DATE + " DESC", null);
        if(c.getCount() < 1)
            return null;
        List<News> news = new ArrayList<>();
        int index = -1;
        String title = null;
        String date = null;
        String url = null;
        String readNum = null;
        List<String> images = null;
        while(c.moveToNext()){
            index = c.getColumnIndex(COLUMN_NAME_TITLE);
            title = c.getString(index);
            index = c.getColumnIndex(COLUMN_NAME_DATE);
            date = c.getString(index);
            index = c.getColumnIndex(COLUMN_NAME_READNUM);
            readNum = c.getString(index);
            index = c.getColumnIndex(COLUMN_NAME_URL);
            url = c.getString(index);
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
            news.add(new News(title, date, readNum, url, images));
        }
        c.close();
        return news;
    }

    public void loadWithNet(ILoadingWithCacheDone callback, boolean cache){
        mNetLoader.load(new ImportNewsListLoader.ILoadingDone() {
            @Override
            public void loadingDone(List<News> newsList) {
                callback.loadDone(newsList);
                //todo db insert.
                if(cache){
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.delete(TABLE_NAME, null, null);
                    for(News news : newsList){
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_NAME_TITLE, news.getTitle());
                        values.put(COLUMN_NAME_DATE, news.getDate());
                        values.put(COLUMN_NAME_URL, news.getUrl());
                        values.put(COLUMN_NAME_READNUM, news.getReadNum());
                        values.put(COLUMN_NAME_IMAGES, new JSONArray(news.getImages()).toString());
                        db.insert(TABLE_NAME, null, values);
                    }
                }

            }
        });
    }

    public void clearCache(){
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        db.execSQL("delete from entry where id not in (select top 5 id from entry order by id");
//        Log.d("wjm", "delete cache.");
    }

    public interface ILoadingWithCacheDone{
        void loadDone(List<News> newsList);
    }

}
