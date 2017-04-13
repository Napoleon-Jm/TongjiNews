package com.tongji.wangjimin.tongjinews.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tongji.wangjimin.tongjinews.net.ImportNewsListLoader;
import com.tongji.wangjimin.tongjinews.net.News;

import java.util.ArrayList;
import java.util.List;

import static com.tongji.wangjimin.tongjinews.data.NewsReaderContract.NewsEntry.*;

/**
 * Created by wangjimin on 17/3/8.
 * ImportNewsLoaderWithCache.
 *
 * 1. Data sources, include
 * database and network.
 * 2. Two level cache.
 * 3. Supported by Jsoup.
 *
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
        loadWithNet(callback, true);
        return loadWithDb();
    }

    private List<News> loadWithDb(){
        //
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c =  db.query(TABLE_NAME, null, null, null, null, null, COLUMN_NAME_DATE + " DESC", null);
        if(c.getCount() < 1)
            return null;
        List<News> newsList = new ArrayList<>();
        while(c.moveToNext()){
            News news = mDbHelper.cursorToNews(c);
            newsList.add(news);
        }
        c.close();
        return newsList;
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
                        mDbHelper.insertNews(db, TABLE_NAME, news);
                    }
                }

            }
        });
    }

    public void loadRefresh(ILoadingWithCacheDone callback){
        mNetLoader.loadRefresh(new ImportNewsListLoader.ILoadingDone() {
            @Override
            public void loadingDone(List<News> newsList) {
                if(callback != null){
                    callback.loadDone(newsList);
                }
            }
        });
    }

    public void clearCache(){
        // Other way, delete db every time when fresh news come.
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        db.execSQL("delete from entry where id not in (select top 5 id from entry order by id");
//        Log.d("wjm", "delete cache.");
    }

    public interface ILoadingWithCacheDone{
        void loadDone(List<News> newsList);
    }

}
