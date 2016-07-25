package com.mirsfang.baselie.basehttp;

import android.os.SystemClock;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import im.amomo.volley.OkRequest;

/**
 * Created by MirsFang on 2016/6/12.
 */
public class BaseRequst extends OkRequest<JSONObject> {

    private long mRequestBeginTime = 0;

    public BaseRequst(int method, String url, Map<String,String> map, Response.Listener<JSONObject> listener,
                      Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        if (map!=null){
            params(map);
        }
        setReseponseListener(listener);
        acceptGzipEncoding();

    }

    @Override
    //解析返回的数据
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            byte[] data = response.data;
            String json = new String(data, HttpHeaderParser.parseCharset(response.headers));
            if (VolleyLog.DEBUG) {
                VolleyLog.d("response:%s", json);
            }
            return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public void addMarker(String tag) {
        super.addMarker(tag);
        if (mRequestBeginTime == 0) {
            mRequestBeginTime = SystemClock.elapsedRealtime();
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        super.deliverResponse(jsonObject);
        //请求用掉的总时间
        long requestTime = SystemClock.elapsedRealtime() - mRequestBeginTime;
    }

    @Override
    //可以自己在这里完成错误的自定义处理
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        //错误发生时候response的数据 byte[]
        //error.networkResponse.data;
    }
}
