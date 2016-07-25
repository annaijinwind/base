package com.lieier.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lieier.base.utils.Tools;
import com.lieier.base.utils.Utils;
import com.mirsfang.baselie.basehttp.GetRequst;
import com.mirsfang.baselie.basehttp.PostRequst;

import org.json.JSONObject;

import java.util.HashMap;

import im.amomo.volley.toolbox.OkVolley;

/**
 * 。。。。。
 */
public abstract class BaseActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
    }
    private boolean checkNetwork() {
        if (!Utils.isNetWorkConnect(BaseActivity.this)) {
            String sError = getString(R.string.network_unavailable);
            Tools.showToast(sError,this);
            return false;
        }
        return true;
    }
    public abstract boolean onRecvData(final String jsonStr, final String method);

    protected void sendData(HashMap<String,String> params, final String method , String... ways) {
        String[] way=ways;
        if (checkNetwork() && params != null) {
            if (way[0].equals("post")){
            PostRequst postRequst=new PostRequst("", params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    BaseActivity.this.onRecvData(response.toString(), method);
                    Log.e("msg",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("errmsg",error.getMessage());
                }
            });
            postRequst.setTag("request");
            OkVolley.getInstance().getRequestQueue().add(postRequst);
            }else if (way[0].equals("get")){
                GetRequst getRequst=new GetRequst("",null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        BaseActivity.this.onRecvData(response.toString(), method);
                        Log.e("test",response.toString());
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("test",error.getMessage());
                    }
                }
                );
                getRequst.setTag("request");
                OkVolley.getInstance().getRequestQueue().add(getRequst);
            }
        }
    }
}
