package com.webmyne.kidscrown.ui.ui;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.ui.helper.Functions;

public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new CountDownTimer(800, 100) {
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
