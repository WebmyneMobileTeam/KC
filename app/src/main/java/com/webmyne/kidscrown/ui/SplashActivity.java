package com.webmyne.kidscrown.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchVersionData;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.VersionModelResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        printKeyHash(SplashActivity.this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        checkAppVersion();

    }

    private void checkAppVersion() {

        new FetchVersionData(SplashActivity.this, new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                Log.e("tag", "responseBody: " + Functions.jsonString(responseBody));

                VersionModelResponse modelResponse = (VersionModelResponse) responseBody;

                if (modelResponse.getResponse().getResponseCode() == 2) {

                    if (modelResponse.getData().isIsMendatory()) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                        dialog.setCancelable(false);
                        dialog.setMessage(getString(R.string.update_app));
                        dialog.setPositiveButton(getString(R.string.goto_playstore), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.webmyne.kidscrown&hl=en")));

                            }
                        });
                        dialog.show();

                    } else {
                        continueProcess();
                    }

                } else {
                    continueProcess();
                }

            }

            @Override
            public void onFail() {

                Functions.showToast(SplashActivity.this, getString(R.string.try_again));

            }
        });

    }

    private void continueProcess() {

        new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
//                if (preferences.contains("isUserLogin")) {
                if (PrefUtils.getLoggedIn(SplashActivity.this)) {
                    Intent i = new Intent(SplashActivity.this, MyDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else if (!preferences.contains("isSplash")) {
                    Functions.fireIntent(SplashActivity.this, IntroActivity.class);

                } else {
                    Functions.fireIntent(SplashActivity.this, LoginActivity.class);
                }

                finish();
            }
        }.start();

    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}
