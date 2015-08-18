package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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

        new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                if (preferences.contains("isUserLogin")) {
                    Intent i = new Intent(SplashActivity.this, MyDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } else {
                    Functions.fireIntent(SplashActivity.this, LoginActivity.class);
                }
                finish();
            }
        }.start();

    }

}
