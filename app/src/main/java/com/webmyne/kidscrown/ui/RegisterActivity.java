package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapter.QualificationAdapter;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.Qualification;
import com.webmyne.kidscrown.model.Salutation;
import com.webmyne.kidscrown.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    TextView btnLogin;
    EditText edtFirstname, edtLastName, edtMobile, edtEmail, edtPassword, edtConfirmPassword, edtRegNo, edtUserName;
    String firstName, lastName, mobile, emailId, password, registartionNo, username, salutation;
    Button btnRegister;
    ProgressDialog pd;
    View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        parentView = findViewById(android.R.id.content);

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void checkValidation() {
        if (edtFirstname.getText().toString().trim().length() == 0) {
            Functions.snack(parentView, "First name is required");
        } else if (edtLastName.getText().toString().trim().length() == 0) {
            Functions.snack(parentView, "Last name is requird.");
        } else if (edtMobile.getText().toString().trim().length() == 0) {
            Functions.snack(parentView, "Mobile number is required");
        } else if (edtEmail.getText().toString().trim().length() == 0) {
            Functions.snack(parentView, "Email-id is required");
        } else if (edtPassword.getText().toString().trim().length() == 0) {
            Functions.snack(parentView, "Password is required");
        } else if (edtRegNo.getText().toString().trim().length() == 0) {
            Functions.snack(parentView, "Registration number is required");
        } else if (edtUserName.getText().toString().trim().length() == 0) {
            Functions.snack(parentView, "Username is required");
        } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            Functions.snack(parentView, "Password and confirm password does not match");
        } else {
            registerWebService();
        }
    }

    private void registerWebService() {
        username = edtUserName.getText().toString().trim();
        firstName = edtFirstname.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        mobile = edtMobile.getText().toString().trim();
        emailId = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString();
        registartionNo = edtRegNo.getText().toString().trim();

        JSONObject userObject = null;
        try {
            userObject = new JSONObject();
            userObject.put("EmailID", emailId);
            userObject.put("FirstName", firstName);
            userObject.put("IsActive", true);
            userObject.put("IsDelete", false);
            userObject.put("LastName", lastName);
            userObject.put("MobileNo", mobile);
            userObject.put("MobileOS", "A");
            userObject.put("Password", password);
            userObject.put("Qualification", "");
            userObject.put("RegistrationNumber", registartionNo);
            userObject.put("Salutation", 0);
            userObject.put("UserID", 0);
            userObject.put("UserName", username);
            userObject.put("UserRoleID", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pd = ProgressDialog.show(RegisterActivity.this, "Loading", "Please wait..", true);
        Functions.logE("register request", userObject.toString());

        new CallWebService(Constants.REGISTRATION_URL, CallWebService.TYPE_POST, userObject) {
            @Override
            public void response(String response) {
                pd.dismiss();
                JSONArray data;
                Log.e("register response", response + "");
                try {
                    data = new JSONArray(response);
                    JSONObject description = data.getJSONObject(0);

                    UserProfile profile = new GsonBuilder().create().fromJson(description.toString(), UserProfile.class);

                    Functions.snack(parentView, "Registration Successfull");
                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(RegisterActivity.this, "user_pref", 0);
                    complexPreferences.putObject("current-user", profile);
                    complexPreferences.commit();

                    SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isUserLogin", true);
                    editor.commit();

                    Intent i = new Intent(RegisterActivity.this, MyDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

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

        btnRegister = (Button) findViewById(R.id.btnRegister);

    }
}
