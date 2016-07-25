package com.lieier.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mirsfang.baselie.app.BaseApplication;

/**
 * Created by fengjianqi on 2016/7/9.
 */
public class MyApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        Fresco.initialize(getApplicationContext());
    }
}
