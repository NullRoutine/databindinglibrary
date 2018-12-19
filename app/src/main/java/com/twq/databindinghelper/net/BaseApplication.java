package com.twq.databindinghelper.net;

import android.app.Application;
import android.content.Context;

/**
 * app入口
 * Created by Administrator on 2018/4/09 0018.
 */

public class BaseApplication extends Application {

    private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getInstance() {
        return application.getApplicationContext();
    }


}
