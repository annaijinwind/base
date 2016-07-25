package com.lieier.base.utils;


import com.jdsoft.os.data.SaveData;

/**
 * 缓存基本信息
 */
public class SharedDataUtil {
    static private SaveData Obj = new SaveData();
    //判断是否是第一次使用app
    private static final String APP_BEEN_USED_LOGIN = "app_been_used_login";


    public static boolean getAppBeenUsedLogin() {
        return Obj.getBoolean(APP_BEEN_USED_LOGIN, false);
    }
    public static void setAppBeenUsedLogin(boolean value) {
        Obj.setBoolean(APP_BEEN_USED_LOGIN, value);
    }
    public static void clear(){
    }
}
