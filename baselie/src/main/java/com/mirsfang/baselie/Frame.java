package com.mirsfang.baselie;

import android.content.Context;

import com.mirsfang.baselie.utils.crashmanager.CrashManager;

/**
 * Created by MirsFang on 2016/6/16.
 */
public class Frame {
    private Context appContext;
    private static Frame  self = null;

    private boolean isInited;
    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context ctx) {
        if (ctx != null) {
            appContext = ctx.getApplicationContext();
        }
    }

    private Frame() {
        super();
    }


    public synchronized static Frame getInstance() {
        if (self == null) {
            self = new Frame();
        } else if (!self.isInited && self.appContext != null) {
            self.init(self.appContext);
        }

        return self;
    }

    /**
     * 上层初始化
     * @param context
     * @return
     */
    public synchronized static Frame getInstanceWithInit(Context context) {
        if (self == null) {
            self = new Frame();
            self.init(context);
        }

        return self;
    }

    /**
     * 1、自己初始化
     * 2、三方sdk初始化
     * @param context
     * @return
     */
    public synchronized boolean init(Context context) {
        if (isInited)
            return true;

        if (context == null) {
            return false;
        }

        isInited = true;
        setAppContext(context);
        initImageLoader(context); // imageloader

        CrashManager.registerHandler(context); // crash log

        /**
         * 初始化数据库
         * */
        return true;
    }


    /**
     * 初始化图片加载工具
     * */
    private void initImageLoader(Context appContext){

    }

}
