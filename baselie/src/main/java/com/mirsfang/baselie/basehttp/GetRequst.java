package com.mirsfang.baselie.basehttp;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by fengjianqi on 2016/7/2.
 */
public class GetRequst extends BaseRequst{
    public GetRequst(String url, Map<String, String> map, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, HttpUrl.baseUrl+url, map, listener, errorListener);
    }
}
