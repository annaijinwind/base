package com.mirsfang.baselie.app;

import android.app.Application;
import android.content.Context;

import com.android.volley.VolleyLog;

import im.amomo.volley.BuildConfig;
import im.amomo.volley.toolbox.OkVolley;

/**
 * Created by MirsFang on 2016/6/12.
 */
public class BaseApplication extends Application {
    private static BaseApplication instance = null;
    private Context appContext;
    private static boolean s_isInited = false;
    private CrashHandler crashHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        OkVolley.getInstance().init(this)
                .setUserAgent(OkVolley.generateUserAgent(this))
                .trustAllCerts();

        VolleyLog.DEBUG = BuildConfig.DEBUG;

       com.mirsfang.baselie.core.BuildConfig.DEBUG= com.mirsfang.baselie.BuildConfig.DEBUG;
        instance = this;
        appContext = this.getApplicationContext();
        if(!BuildConfig.DEBUG) {
            crashHandler = CrashHandler.getInstance();
            crashHandler.init(appContext);
        }
        Init(appContext);

    }


    public static BaseApplication getInstance() {
        return instance;
    }

    private void Init(Context appContext) {
        if (s_isInited) {
            return;
        }
        s_isInited = true;
    }
}
