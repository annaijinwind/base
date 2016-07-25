package com.lieier.base.bean;


import java.io.Serializable;

/**
 * Created by fengjianqi on 2016/7/7.
 */
public class TextBean implements Serializable {
    private String text;
    private String url;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
