package com.crash.k.crashuncaughtexception;

import android.app.Application;

import com.crash.k.crashuncaughtexception.utils.CrashUncaugtExceptionUtils;

/**
 * Created by Keno on 2015/12/21.
 */
public class CrashApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化
        CrashUncaugtExceptionUtils.install(this);
    }
}
