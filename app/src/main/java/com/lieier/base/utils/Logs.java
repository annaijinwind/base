package com.lieier.base.utils;

import android.util.Log;

/**
 * 打印类
 */
public class Logs {

    /**
     * 后台打印
     */
    public final static int type =1;

    /**
     * 全部打印，且弹出来, 而且写到log.log里面
     */
    private static void out(String s){
        try {
            System.out.println(s);
            Log.d("com.comm", s);
        } catch (Exception e) {
            Logs.p("p() is null");
        }
    }


    public static void p(String s) {
        if (0 == type)
            return;
        out(s);
    }
    public static void p(String str, String s) {
        if (0 == type)
            return;
        out(s);
    }

    public static void p(int s) {
        p("" + s);
    }
    public static void p(long s) {
        p("" + s);
    }
    public static void p(Object s) {
        p("" + s);
    }
    public static void p(Class<?> cls, Object s) {
        p(cls.getName() + "]: " + s);
    }

    public static void p(Exception e) {
        if (0 == type)
            return;
        e.printStackTrace();
        out("ERROR:" +e.getMessage());
    }
}
