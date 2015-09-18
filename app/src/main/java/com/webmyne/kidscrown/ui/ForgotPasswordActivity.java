package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.GsonBuilder;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForgotPasswordActivity extends ActionBarActivity {

    private EditText edtEmail;
    private Button btnForgotPassword;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().length() == 0) {
                    View view = findViewById(android.R.id.content);
                    Functions.snack(view, "Please Enter Your Email Id");
                } else {
                    callForgotPassword();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        return;
    }

    private void callForgotPassword() {
        //JSONObject emailObject = null;

        String url = Constants.FORGOT_PASSWORD_URL + edtEmail.getText().toString().trim() + "/kc";

        pd = ProgressDialog.show(ForgotPasswordActivity.this, "Loading", "Please wait..", true);
        Functions.logE("login request url", url);

        new CallWebService(url, CallWebService.TYPE_GET, null) {
            @Override
            public void response(String response) {
                Log.e("response", response);
                pd.dismiss();
                try {
                    JSONArray obj = new JSONArray(response);
                    //JSONObject description = obj.getJSONObject(0);

                    View view = findViewById(android.R.id.content);
                    Functions.snack(view, "Reset Password Link sent to your Email Id");

                    Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String error) {
                Log.e("error", error);

            }
        }.call();
    }

}
