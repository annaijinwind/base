package com.lieier.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by fengjianqi on 2016/7/2.
 */
public class Utils {
    /**
     * 是否有网络
     *
     * @return
     */
    public static boolean isNetWorkConnect(Context ctx) {
        Logs.p("ctx: " + ctx);
        if (ctx == null) {
            return true;
        }
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .isConnected()
                    || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnected();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 连接网络是否为wifi
     *
     * @return
     */
    public static boolean isNetWorkConnectWifi(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

    public static boolean isNetWorkConnectMobile(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
    }
}
