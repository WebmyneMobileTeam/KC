package com.webmyne.kidscrown.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchLoginData;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.LoginModelData;
import com.webmyne.kidscrown.model.LoginModelRequest;

public class RegisterActivity extends AppCompatActivity {

    private TextView btnLogin;
    private EditText edtFirstname, edtLastName, edtMobile, edtEmail, edtPassword, edtConfirmPassword, edtRegNo, edtUserName, edtClinicName;
    private String password;
    private Button btnRegister;
    private Toolbar toolbar;
    private TextView txtCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

    }

    private void checkValidation() {

        if (edtFirstname.getText().toString().trim().length() == 0) {
            Functions.showToast(RegisterActivity.this, "First name is required");

        } else if (edtLastName.getText().toString().trim().length() == 0) {
            Functions.showToast(RegisterActivity.this, "Last name is required");

        } else if (edtUserName.getText().toString().trim().length() == 0) {
            Functions.showToast(RegisterActivity.this, "Username is required");

        } else if (edtMobile.getText().toString().trim().length() == 0) {
            Functions.showToast(RegisterActivity.this, "Mobile number is required");

        } else if (edtMobile.getText().toString().trim().length() != 10) {
            Functions.showToast(RegisterActivity.this, "Mobile number should contains 10 digits");

        } else if (edtEmail.getText().toString().trim().length() == 0) {
            Functions.showToast(RegisterActivity.this, "Email-id is required");

        } else if (!(Functions.emailValidation(edtEmail.getText().toString().trim()))) {
            Functions.showToast(RegisterActivity.this, "Email-id is not valid");

        } else if (edtPassword.getText().toString().trim().length() < 6) {
            Functions.showToast(RegisterActivity.this, "Password must be of minimum 6 characters");

        } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            Functions.showToast(RegisterActivity.this, "Password and confirm password does not match");

        } else if (edtRegNo.getText().toString().trim().length() == 0) {
            Functions.showToast(RegisterActivity.this, "Registration number is required");

        } else {
            registerWebService();
        }
    }

    private void registerWebService() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        LoginModelRequest model = new LoginModelRequest();
        model.setMobileOS("A");
        model.setPassword(edtPassword.getText().toString());
        model.setUserName(edtUserName.getText().toString().trim());
        model.setFirstName(edtFirstname.getText().toString().trim());
        model.setLastName(edtLastName.getText().toString().trim());
        model.setMobileNo(edtMobile.getText().toString().trim());
        model.setEmailID(edtEmail.getText().toString().trim());
        model.setRegistrationNumber(edtRegNo.getText().toString().trim());
        model.setDeviceID(telephonyManager.getDeviceId());
        model.setGCMToken(PrefUtils.getFCMToken(this));
        model.setLoginVia("1");
        model.setClinicName(edtClinicName.getText().toString().trim());

        new FetchLoginData(this, model, new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                LoginModelData responseModel = (LoginModelData) responseBody;

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                PrefUtils.setUserProfile(RegisterActivity.this, responseModel);

                PrefUtils.setFirstTime(RegisterActivity.this, true);

                Functions.showToast(RegisterActivity.this, getString(R.string.register_success));

                Intent i = new Intent(RegisterActivity.this, MyDrawerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }

            @Override
            public void onFail() {

            }
        });
    }

    private void init() {
        btnLogin = (TextView) findViewById(R.id.btnLogin);

        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtFirstname = (EditText) findViewById(R.id.edtFirstname);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
        edtRegNo = (EditText) findViewById(R.id.edtRegNo);
        edtClinicName = (EditText) findViewById(R.id.edtClinicName);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText("User Registration");
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clickListener();
    }

    private void clickListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(RegisterActivity.this, v);
                if (!Functions.isConnected(RegisterActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(RegisterActivity.this, v);

                if (!Functions.isConnected(RegisterActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }
                checkValidation();
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }
        });

        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(password)) {

                } else {
                    edtConfirmPassword.setError("Does not match to password");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
