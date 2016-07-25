package com.lieier.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.DialogInterface;
import android.widget.Toast;

/**
 *
 * 工具类
 *
 * @author zm_blj
 *
 */
public class Tools {
// 备注:

    // 当使用像 imageView.setBackgroundResource，imageView.setImageResource,
    // 或者 BitmapFactory.decodeResource 这样的方法来设置一张大图片的时候，
    // 这些函数在完成decode后，最终都是通过java层的createBitmap来完成的，需要消耗更多内存。
    // 因此，改用先通过BitmapFactory.decodeStream方法，创建出一个bitmap，再将其设为
    // ImageView的
    // source，decodeStream最大的秘密在于其直接调用JNI>>nativeDecodeAsset()来完成decode，
    // 无需再使用java层的createBitmap，从而节省了java层的空间。如果在读取时加上图片的Config参数，
    // 可以跟有效减少加载的内存，从而跟有效阻止抛out of Memory异常。
    // 另外，需要特别注意：
    // decodeStream是直接读取图片资料的字节码了， 不会根据机器的各种分辨率来自动适应，
    // 使用了decodeStream之后，需要在hdpi和mdpi，ldpi中配置相应的图片资源，
    // 否则在不同分辨率机器上都是同样大小（像素点数量），显示出来的大小就不对了。


    /**
     * drawable转Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 设置引导
     *
     * @param act
     * @param viewId
     *            在哪个view上显示引导
     * @param imageId
     *            引导的图片集
     * @param preferenceName
     *            保存firstin
     */
    public static void setGuidImage(Activity act, int viewId,
                                    final int[] imageId, String preferenceName) {

        SharedPreferences preferences = act.getSharedPreferences(
                preferenceName, act.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final String key = act.getClass().getName() + "_firstin";
        if (!preferences.contains(key)) {
            editor.putBoolean(key, true);
            editor.commit();
        }

        if (!preferences.getBoolean(key, true)) {

            Log.d("setGuidImage", "key = true");
            return;
        }

        View view = act.getWindow().getDecorView().findViewById(viewId);
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {

            final Context ctx = act.getApplicationContext();
            final FrameLayout frameLayout = (FrameLayout) viewParent;
            final ImageView guideImage = new ImageView(act.getApplication());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            guideImage.setLayoutParams(params);
            guideImage.setScaleType(ImageView.ScaleType.FIT_XY);

            // 创建出一个bitmap
            Bitmap bitmap = readBitMap(ctx, imageId[0]);
            guideImage.setImageBitmap(bitmap);

            guideImage.setOnClickListener(new View.OnClickListener() {

                int clickCount = 0;

                @Override
                public void onClick(View v) {
                    clickCount++;
                    if (clickCount >= imageId.length) {
                        frameLayout.removeView(guideImage);
                        editor.putBoolean(key, false);
                        editor.commit();
                        clickCount = 0;
                    } else {
                        Bitmap bitmap = readBitMap(ctx, imageId[clickCount]);
                        guideImage.setImageBitmap(bitmap);
                    }
                }
            });
            frameLayout.addView(guideImage);

        }

    }

    /**
     *
     *  @Description    : 这个包名的程序是否在运行
     *  @Method_Name    : isRunningApp
     *  @param context 上下文
     *  @param packageName 判断程序的包名
     *  @return 必须加载的权限
     *      <uses-permission android:name="android.permission.GET_TASKS">
     *  @return         : boolean
     *  @Creation Date  : 2014-10-31 下午1:14:15
     *  @version        : v1.00
     *  @Author         : JiaBin

     *  @Update Date    :
     *  @Update Author  : JiaBin
     */
    public static boolean isRunningApp(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                // find it, break
                break;
            }
        }
        return isAppRunning;
    }
    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 拨打电话
     *
     * @param phone
     * @param mContext
     */

    public static void call(final String phone, final Context mContext) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialoginterface, int i) {
                Uri callUri = Uri.parse("tel:" + phone);
                Intent in = new Intent(Intent.ACTION_CALL, callUri);
                mContext.startActivity(in);
            }
        }).setTitle("是否呼叫?").setMessage("Tel: " + phone)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public static void callPhone(final String phone, final Context mContext  ,DialogInterface.OnClickListener callListener) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setPositiveButton("呼叫", callListener).setTitle("是否呼叫?").setMessage("Tel: " + phone)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    /**
     * 根据网址跳转
     *
     * @param path
     * @param activity
     */
    public static void getIntent(String path, final Activity activity) {
        Uri uri = Uri.parse(path);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(it);
    }

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    public static void sendSMS(String phoneNumber, String message,
                               final Activity activity, Context context) {

        // 处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
                deliverIntent, 0);
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager
                .getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(activity, 0,
                new Intent(), 0);

        if (message.length() >= 60) {
            List<String> ms = smsManager.divideMessage(message);

            for (String str : ms) {
                // 短信发送
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent,
                        deliverPI);
            }
        } else {

            smsManager.sendTextMessage(phoneNumber, null, message, sentIntent,
                    deliverPI);
        }

    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public static void doSendSMSTo(final Context mContext, String phoneNumber,
                                   String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                    + phoneNumber));
            intent.putExtra("sms_body", message);
            mContext.startActivity(intent);
        }
    }

    /**
     * 时间戳转化为时、分、秒
     *
     * @param str
     * @return
     */

    public static String TimestampToTime(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        str = sdf.format(Long.parseLong(str) * 1000L);

        return str;
    }

    /**
     * 时间戳转化为年、月、日
     *
     * @param str
     * @return
     */

    public static String TimestampToData(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        str = sdf.format(Long.parseLong(str) * 1000L);

        return str;
    }

    /**
     * 时间戳转化为年、月、日
     *
     * @param str
     * @return
     */

    public static long TimestampToData_three(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date.getTime();
    }

    // 将字符串转为时间戳

    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {

            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 时间戳转化为年、月、日
     *
     * @param str
     * @return
     */

    public static String TimestampToData_two(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        str = sdf.format(Long.parseLong(str) * 1000L);

        return str;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     * */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param time
     * @return
     */
    public static long getStringtoDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date().getTime();
        }
    }

    public static long getStringtoTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getLongTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }




    /**
     * 将字符串转为时间戳
     *
     * @param time
     * @return
     */
    public static long getStringtoDateTwo(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return date.getTime();

    }

    /**
     * Toast显示
     *
     * @param string
     */
    public static void showToast(String string, Context context) {
        // TODO Auto-generated method stub
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    /**
     * 字符串转义
     *
     * @param str
     * @return
     */
    public static String replaceString(String str) {

        str = str.replaceAll("\r\n", "<br>");
        str = str.replaceAll("\r", "<br>");
        str = str.replaceAll("\n", "<br>");

        return str;
    }

    public static String replaceSpace(String str) {
        if(!TextUtils.isEmpty(str)){
            str = str.replaceAll(" ", "");
        }
        return str;
    }

    /**
     * 字符串转义
     *
     * @param str
     * @return
     */
    public static String replaceAll(String str){

        str=str.replaceAll("<br>", "\r\n");
        str=str.replaceAll("<br>", "\r");
        str=str.replaceAll("<br>", "\n");

        return str;
    }

    /**
     * 根据物品属性，获取颜色
     *
     * @param "type"
     *
     * @return color
     */

    // public static int getColor(String type){
    //
    // int color = 0;
    //
    // if(type.equals("普通")){
    //
    // color = R.color.putong;
    // }
    // else if(type.equals("罕见")){
    // color = R.color.hanjian;
    // }
    // else if(type.equals("稀有")){
    // color = R.color.xiyou;
    // }
    //
    // else if(type.equals("神话")){
    // color = R.color.shenhua;
    // }
    // else if(type.equals("不朽")){
    // color = R.color.buxiou;
    // }
    // else if(type.equals("传说")){
    // color = R.color.chuanshuo;
    // }
    // else if(type.equals("至宝")){
    // color = R.color.zhibao;
    // }
    // else if(type.equals("标准")){
    // color = R.color.biaozhun;
    // }
    // else if(type.equals("吉祥")){
    // color = R.color.jixiang;
    // }
    // else if(type.equals("铭刻")){
    // color = R.color.mingke;
    // }
    // else if(type.equals("英雄传世")){
    // color = R.color.yingxiongchuanshi;
    // }
    // else if(type.equals("纯正")){
    // color = R.color.chunzheng;
    // }
    // else if(type.equals("凶煞")){
    // color = R.color.xiongsha;
    // }
    // else if(type.equals("冻人")){
    // color = R.color.dongren;
    // }
    // else if(type.equals("独特")){
    // color = R.color.dute;
    // }
    // else if(type.equals("Corrupted")){
    // color = R.color.corrupted;
    // }
    // else if(type.equals("亲笔签名")){
    // color = R.color.qinbiqianming;
    // }
    // else if(type.equals("上古")){
    // color = R.color.shanggu;
    // }
    // else if(type.equals("尊享")){
    // color = R.color.zunxiang;
    // }
    // else if(type.equals("基础")){
    // color = R.color.jichu;
    // }
    // else if(type.equals("绝版")){
    // color = R.color.jueban;
    // }
    //
    // return color;
    //
    // }

//	 public static void popAlarmSetToast(Context context, long timeInMillis) {
//	        String toastText = formatToast(context, timeInMillis);
//	        Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_LONG);
//	        ToastMaster.setToast(toast);
//	        toast.show();
//	    }
//


//    public static String formatToast(Context context, long timeInMillis) {
//        long delta = timeInMillis - System.currentTimeMillis();
//        long hours = delta / (1000 * 60 * 60);
//        long minutes = delta / (1000 * 60) % 60;
//        long days = hours / 24;
//        hours = hours % 24;
//
//        String daySeq = (days == 0) ? "" :
//                (days == 1) ? context.getString(R.string.day) :
//                        context.getString(R.string.days, Long.toString(days));
//
//        String minSeq = (minutes == 0) ? "" :
//                (minutes == 1) ? context.getString(R.string.minute) :
//                        context.getString(R.string.minutes, Long.toString(minutes));
//
//        String hourSeq = (hours == 0) ? "" :
//                (hours == 1) ? context.getString(R.string.hour) :
//                        context.getString(R.string.hours, Long.toString(hours));
//
//        boolean dispDays = days > 0;
//        boolean dispHour = hours > 0;
//        boolean dispMinute = minutes > 0;
//
//        int index = (dispDays ? 1 : 0) |
//                (dispHour ? 2 : 0) |
//                (dispMinute ? 4 : 0);
//
//        String[] formats = context.getResources().getStringArray(R.array.alarm_set);
//        return String.format(formats[index], daySeq, hourSeq, minSeq);
//    }


//	public static void popAlarmSetToast(HandleSetAlarm context,
//			long timeInMillis) {
//		// TODO Auto-generated method stub
//
//	}

    public static String StringData(){
        String mYear;
        String mMonth;
        String mDay;
        String mWay;

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="7";
        }else if("2".equals(mWay)){
            mWay ="1";
        }else if("3".equals(mWay)){
            mWay ="2";
        }else if("4".equals(mWay)){
            mWay ="3";
        }else if("5".equals(mWay)){
            mWay ="4";
        }else if("6".equals(mWay)){
            mWay ="5";
        }else if("7".equals(mWay)){
            mWay ="6";
        }
        return  mWay;
    }
}
