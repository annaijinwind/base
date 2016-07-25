package com.lieier.base;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lieier.base.adapters.TextAdapter;
import com.lieier.base.bean.TextBean;
import com.lieier.base.utils.Logs;
import com.lieier.base.utils.Tools;
import com.lieier.base.yulan.ViewPagerActivity;
import com.mirsfang.baselie.basehttp.BaseRequst;
import com.mirsfang.baselie.basehttp.PostRequst;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.amomo.volley.toolbox.OkVolley;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.button_1)
    Button button_1;
    private TextAdapter adapter;
    @BindView(R.id.list_text)
    ListView list_text;
    long mExitTime;
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    @BindView(R.id.container)
    CoordinatorLayout layoutRoot;
    @BindView(R.id.id_swipe_ly)SwipeRefreshLayout id_swipe_ly;
    @BindView(R.id.button_2)Button button_2;
    List<TextBean>listData=new ArrayList<TextBean>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setData();
        setEvent();
//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//            int checkNetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
//            }
//            if (checkNetPermission!=PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_ASK_CALL_PHONE);
//            }
//        }


    }
    private void setData(){
        String[] photo_url={"https://static.pexels.com/photos/5854/sea-woman-legs-water-medium.jpg","http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=7c000a71fcfaaf5184b689bbb964b8d8/f7246b600c33874459e38d2b500fd9f9d72aa047.gif","http://c.hiphotos.baidu.com/image/pic/item/eac4b74543a98226e523cd238882b9014b90ebd0.jpg",
                "http://g.hiphotos.baidu.com/image/pic/item/0df3d7ca7bcb0a46bc0b4d3d6963f6246b60af8d.jpg","http://b.hiphotos.baidu.com/image/pic/item/d53f8794a4c27d1e93d181db19d5ad6eddc4383d.jpg","http://b.hiphotos.baidu.com/image/pic/item/d53f8794a4c27d1e93d181db19d5ad6eddc4383d.jpg","http://h.hiphotos.baidu.com/image/pic/item/2cf5e0fe9925bc31e324ed9c5cdf8db1cb1370bd.jpg"};
        for (int i=0;i<photo_url.length;i++){
            TextBean tb=new TextBean();
            tb.setUrl(photo_url[i]);
            tb.setText(i+"");
            listData.add(tb);
        }
    }
    private void setEvent(){
        adapter=new TextAdapter(MainActivity.this,listData);
        list_text.setAdapter(adapter);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.showToast("hehe", getApplicationContext());
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("records","SFSDF");
                params.put("op", "BatchAddPurchase");
                sendData(params, "BatchAddPurchase", "get");

            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii=new Intent(MainActivity.this,TextActivity.class);
                startActivity(ii);
            }
        });
        id_swipe_ly.setOnRefreshListener(this);
        list_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ii=new Intent(MainActivity.this, ViewPagerActivity.class);
                ii.putExtra("position",position);
                ii.putExtra("list",(Serializable) listData);
                startActivity(ii);
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                id_swipe_ly.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    private void dealBatchAddPurchase(String jsonStr){
        Tools.showToast(jsonStr, getApplicationContext());
//        try {
//            JSONObject jsonObject = new JSONObject(jsonStr);
//            String status = jsonObject.getString("status");
//
//            if (!"0".equals(status)) {
//                Log.d("", "获取数据失败！status = " + status);
//                Tools.showToast(jsonObject.getString("result"), getApplicationContext());
//                return;
//            }else{
//                Tools.showToast("",this);
//                finish();
//            }
//        }catch (Exception e){
//            Logs.p(e);
//            Tools.showToast("系统繁忙，请稍后再试", getApplicationContext());
//        }
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.openQiutDialog();// 这是自定义的代码
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    /**
     * 连续按两次返回键，退出应用
     */
    private void openQiutDialog() {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Snackbar.make(layoutRoot, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            //退至后台
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);

//			Intent service = new Intent(MainActivity.this,DemonService.class);
//	    	stopService(service);
//	    	System.exit(0);
//	    	MyApplication.getInstance().logout();
        }
    }
    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onRecvData(String jsonStr, String method) {
        if ("BatchAddPurchase".equalsIgnoreCase(method)){
            dealBatchAddPurchase(jsonStr);
        }
        return true;
    }
}
