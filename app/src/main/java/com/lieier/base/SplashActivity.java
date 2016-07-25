package com.lieier.base;

import android.content.Intent;
import android.os.Bundle;

import com.lieier.base.R;

import java.util.Timer;
import java.util.TimerTask;

/****
 *
 *启动页
 * ***/

public class SplashActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Intent ii=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(ii);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    @Override
    public boolean onRecvData(String jsonStr, String method) {
        return true;
    }

}
