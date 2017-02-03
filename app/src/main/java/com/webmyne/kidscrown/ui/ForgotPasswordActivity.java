package com.webmyne.kidscrown.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.AppApi;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.ForgetPasswordModelResponse;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnForgotPassword;
    private AppApi appApi;
    private SpotsDialog dialog;
    private Toolbar toolbar;
    private TextView txtCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText(getString(R.string.forgot_pass));
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(ForgotPasswordActivity.this, v);

                if (!Functions.isConnected(ForgotPasswordActivity.this)) {
                    Functions.showToast(ForgotPasswordActivity.this, getString(R.string.no_internet));
                    return;
                }

                if (TextUtils.isEmpty(Functions.getStr(edtEmail))) {
                    Functions.showToast(ForgotPasswordActivity.this, "Please Enter Your User Name");

                } else {
                    callForgotPassword();
                }
            }
        });

    }

    private void callForgotPassword() {

        showProgress();

        Call<ForgetPasswordModelResponse> call = appApi.fetchForgotPasswordData(edtEmail.getText().toString().trim());

        call.enqueue(new Callback<ForgetPasswordModelResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordModelResponse> call, Response<ForgetPasswordModelResponse> response) {

                hideProgress();

                Log.e("response", MyApplication.getGson().toJson(response.body(), ForgetPasswordModelResponse.class));

                if (response.isSuccessful()) {

                    if (response.body().getResponse().getResponseCode() == Constants.SUCCESS) {

                        Functions.showToast(ForgotPasswordActivity.this, response.body().getResponse().getResponseMsg());

                        finish();

                    } else {
                        Functions.showToast(ForgotPasswordActivity.this, response.body().getResponse().getResponseMsg());
                    }

                } else {
                    Functions.showToast(ForgotPasswordActivity.this, getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<ForgetPasswordModelResponse> call, Throwable t) {

                hideProgress();

                RetrofitErrorHelper.showErrorMsg(t, ForgotPasswordActivity.this);

            }
        });

    }

    private void hideProgress() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void showProgress() {
        if (dialog == null) {
            dialog = new SpotsDialog(this, "Loading..", R.style.Custom);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

}
