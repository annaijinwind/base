package com.lieier.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TextActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
    }

    @Override
    public boolean onRecvData(String jsonStr, String method) {
        return false;
    }
}
