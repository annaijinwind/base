package com.mirsfang.baselie.basehttp;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by MirsFang on 2016/6/12.
 */
public class PostRequst  extends  BaseRequst{

    public PostRequst(String url, Map<String, String> map, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, HttpUrl.baseUrl+url, map, listener, errorListener);
    }
}
