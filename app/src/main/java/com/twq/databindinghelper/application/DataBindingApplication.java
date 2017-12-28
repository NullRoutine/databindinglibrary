package com.twq.databindinghelper.application;

import android.app.Application;
import android.content.Context;

/**
 * 初始化
 * Created by Administrator on 2017/12/28 0028.
 */

public class DataBindingApplication extends Application {

    private static DataBindingApplication mApplication;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mContext = this.getApplicationContext();
    }

    public static Context getInstance() {
        if (mApplication != null) {
            return mApplication.getApplicationContext();
        } else {
            return mContext;
        }

    }
}
