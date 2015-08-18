package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends ActionBarActivity {

    Button btnLogin, btnFb, btnGplus;
    TextView txtRegister, txtForgot;
    EditText edtUsername, edtPassword;
    String name, password, url;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUsername.getText().toString().trim().length() == 0) {
                    Functions.snack(v, "Username or Email is required");
                } else if (edtPassword.getText().toString().trim().length() == 0) {
                    Functions.snack(v, "Password is required");
                } else {
                    loginProcess();
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(LoginActivity.this, RegisterActivity.class);
            }
        });
    }

    private void loginProcess() {
        name = edtUsername.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        url = Constants.LOGIN_URL + name + "/" + password;

        pd = ProgressDialog.show(LoginActivity.this, "Loading", "Please wait..", true);
        Functions.logE("login request url", url);

        new CallWebService(url, CallWebService.TYPE_GET, null) {
            @Override
            public void response(String response) {
                pd.dismiss();
                Log.e("login response", response);
                JSONArray data;
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("ResponseMessage").equals("Success")) {
                        data = obj.getJSONArray("Data");
                        JSONObject description = data.getJSONObject(0);

                        UserProfile profile = new GsonBuilder().create().fromJson(description.toString(), UserProfile.class);

                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginActivity.this, "user_pref", 0);
                        complexPreferences.putObject("current-user", profile);
                        complexPreferences.commit();

                        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isUserLogin", true);
                        editor.commit();

                        Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(VolleyError error) {

            }
        }.call();
    }

    private void init() {
        txtForgot = (TextView) findViewById(R.id.txtForgot);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnFb = (Button) findViewById(R.id.btnFb);
        btnGplus = (Button) findViewById(R.id.btnGplus);
    }

}
