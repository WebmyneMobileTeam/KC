package com.webmyne.kidscrown.ui;

import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;

public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new CountDownTimer(100, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Functions.fireIntent(SplashActivity.this, LoginActivity.class);
                finish();
            }
        }.start();

    }

}
