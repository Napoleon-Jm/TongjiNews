package com.tongji.wangjimin.tongjinews.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.res.ResourcesCompat;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by wangjimin on 17/4/14.
 * Utils class.
 */

public class Utils {
    // context.getDrawable is api21.
    public static Drawable getDrawable(Context context, int id){
        return ResourcesCompat.getDrawable(context.getResources(), id, null);
    }

    public static int getColor(Context context, int id){
        return ResourcesCompat.getColor(context.getResources(), id, null);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnectedOrConnecting());
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
