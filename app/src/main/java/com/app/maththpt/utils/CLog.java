package com.app.maththpt.utils;

import android.util.Log;

import com.app.maththpt.BuildConfig;

/**
 * Created by manhi on 26/12/2015.
 */
public class CLog {

    public static int w(String tag, String msg) {
        if (BuildConfig.DEBUG)
            return Log.w(tag, msg);
        return -1;
    }

    public static int d(String tag, String msg) {
        if (BuildConfig.DEBUG)
            return Log.d(tag, msg);
        return -1;
    }

    public static int e(String tag, String msg) {
        if (BuildConfig.DEBUG)
            return Log.e(tag, msg);
        return -1;
    }

    public static int v(String tag, String msg) {
        if (BuildConfig.DEBUG)
            return Log.v(tag, msg);
        return -1;
    }

    public static int i(String tag, String msg) {
        if (BuildConfig.DEBUG)
            return Log.i(tag, msg);
        return -1;
    }
}
