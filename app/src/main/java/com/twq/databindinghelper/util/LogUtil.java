package com.twq.databindinghelper.util;

import android.util.Log;

import com.twq.databindinghelper.BuildConfig;


public class LogUtil {

    private static final String TAG = "ChronicMGT";
    public static boolean isDebug = BuildConfig.DEBUG;

    public static int e(String msg) {
        return isDebug ? ee(TAG, msg) : 0;
    }

    public static int d(String msg) {
        return isDebug ? dd(TAG, msg) : 0;
    }

    public static int d(Class tag, String msg) {
        return isDebug ? dd(tag.getSimpleName() + " " + TAG, msg) : 0;
    }

    public static int d(String tag, String msg) {
        return isDebug ? dd(tag + " " + TAG, msg) : 0;
    }

    public static int d(Object obj, String msg) {
        return isDebug ? dd(TAG, obj.getClass().getSimpleName() + ":" + msg) : 0;
    }

    public static int dd(String tag, String msg) {
//        LogCatch.addLog(com.tools.com.commonlib.application.util.DateUtil.formatAll(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss SSS") + " " + tag + "/D " + msg);
        Log.d(tag, msg);
        return 0;
    }

    public static int ee(String tag, String msg) {
//        LogCatch.addLog(com.tools.com.commonlib.application.util.DateUtil.formatAll(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss SSS") + " " + tag + "/E " + msg);
        Log.e(tag, msg);
        return 0;
    }

}
