package com.tongji.wangjimin.tongjinews.data;

import android.database.Cursor;

import com.tongji.wangjimin.tongjinews.net.ImportNewsListLoader;
import com.tongji.wangjimin.tongjinews.net.News;

import java.util.List;

/**
 * Created by wangjimin on 17/3/8.
 */

public class ImportNewsLoaderWithCache {
    private NewsReaderDbHelper mDbHelper;
    private ImportNewsListLoader mNetLoader;
    private static ImportNewsLoaderWithCache instance;

    private ImportNewsLoaderWithCache(){
        mNetLoader = ImportNewsListLoader.getInstance();
    }

    public static ImportNewsLoaderWithCache getInstance(){
        if(instance == null)
            instance = new ImportNewsLoaderWithCache();
        return instance;
    }

    public List<News> loadWithCache(ImportNewsListLoader.ILoadingDone callback){
        return null;
    }

    private Cursor loadWithDb(){
        return null;
    }

    private void loadWithNet(ImportNewsListLoader.ILoadingDone callback){
        mNetLoader.load(callback);
    }

}
