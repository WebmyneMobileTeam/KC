package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.GsonBuilder;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button btnLogin;
    String regNo;
    SignInButton btnGplus;
    TextView txtRegister, txtForgot;
    EditText edtUsername, edtPassword;
    String name, password, url;
    ProgressDialog pd;

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private LinearLayout linearFbLogin;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUsername.getText().toString().trim().length() == 0) {
                    //Functions.snack(v, "Username or Email is required");
                    Snackbar snack = Snackbar.make(btnLogin, "Username or Email is required", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), LoginActivity.this));
                    snack.show();
                } else if (edtPassword.getText().toString().trim().length() == 0) {
                    //Functions.snack(v, "Password is required");
                    Snackbar snack = Snackbar.make(btnLogin, "Password is required", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), LoginActivity.this));
                    snack.show();

                } else {
                    loginProcess();
                }
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(LoginActivity.this, RegisterActivity.class);
            }
        });

        btnGplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askRegistrationNo2();

            }
        });


        linearFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askRegistrationNo();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.e("Login_from_Fb Resp", response.toString());
                                JSONObject profile = response.getJSONObject();
                                try {
                                    String email = profile.getString("email").toString();
                                    String fName = profile.getString("name").toString().split(" ")[0];
                                    String lName = profile.getString("name").toString().split(" ")[1];
                                    socialMediaLoginProcess(email, fName, lName, "F");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.e("reponse", response.getJSONObject().toString());


                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("CANCEL_FB", "CANCELLED");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("ERROR_FB", e.toString());
            }
        });
    }

    private void askRegistrationNo() {
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptView = li.inflate(R.layout.social_registration, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(promptView);

        final EditText edtRegNo = (EditText) promptView.findViewById(R.id.edtRegNo);
        final Button btnNext = (Button) promptView.findViewById(R.id.btnNext);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtRegNo.length() == 0) {
                    Functions.snack(v, "Registration Number must be entered to proceed ahead..");
                } else {
                    regNo = edtRegNo.getText().toString().trim();
                    dialog.dismiss();
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile, email, user_birthday, user_friends"));
                }

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
                Log.e("login_response", response);
                try {
                    JSONArray obj = new JSONArray(response);
                    JSONObject description = obj.getJSONObject(0);
                    UserProfile profile = new GsonBuilder().create().fromJson(description.toString(), UserProfile.class);

                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginActivity.this, "user_pref", 0);
                    complexPreferences.putObject("current-user", profile);
                    complexPreferences.commit();

                    SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isUserLogin", true);
                    editor.putBoolean("isFirstTimeLogin", true);
                    editor.commit();

                    Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                } catch (Exception e) {
                    Snackbar snack = Snackbar.make(btnLogin, "Unable To Login", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), LoginActivity.this));
                    snack.show();
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String error) {
                pd.dismiss();
                Functions.snack(btnLogin, "Invalid Login Credentials");
            }
        }.call();
    }

    private void socialMediaLoginProcess(String email, String fName, String lName, String loginType) {
        url = Constants.SOCIAL_MEDIA_LOGIN_URL + email + "/" + fName + "/" + lName + "/" + loginType + "/" + regNo;

        pd = ProgressDialog.show(LoginActivity.this, "Loading", "Please wait..", true);
        Functions.logE("login request url", url);

        new CallWebService(url, CallWebService.TYPE_GET, null) {
            @Override
            public void response(String response) {
                pd.dismiss();
                try {
                    JSONArray obj = new JSONArray(response);
                    JSONObject description = obj.getJSONObject(0);
                    UserProfile profile = new GsonBuilder().create().fromJson(description.toString(), UserProfile.class);
                    Functions.logE("social_media login resp", obj.toString());

                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginActivity.this, "user_pref", 0);
                    complexPreferences.putObject("current-user", profile);
                    complexPreferences.commit();

                    SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isUserLogin", true);
                    editor.putBoolean("isFirstTimeLogin", true);
                    editor.commit();

                    Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                } catch (Exception e) {
                    pd.dismiss();
                    Snackbar snack = Snackbar.make(btnLogin, "Unable To Login", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), LoginActivity.this));
                    snack.show();
                    Functions.logE("Exp", e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String error) {
                pd.dismiss();
                Snackbar snack = Snackbar.make(btnLogin, "Unable To Login", Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), LoginActivity.this));
                snack.show();
                Functions.logE("social_media error", error);
            }
        }.call();
    }

    private void init() {
        txtForgot = (TextView) findViewById(R.id.txtForgot);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        linearFbLogin = (LinearLayout) findViewById(R.id.linearFbLogin);
        btnGplus = (SignInButton) findViewById(R.id.btnGplus);
        setGooglePlusButtonText(btnGplus, "SignIn With Google Plus");

        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .addConnectionCallbacks(LoginActivity.this)
                .addOnConnectionFailedListener(LoginActivity.this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            callbackManager.onActivityResult(requestCode, responseCode, intent);
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        getGPlusProfileInformation();
    }

    private void getGPlusProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                String fName = personName.split(" ")[0];
                String lName = personName.split(" ")[1];
                socialMediaLoginProcess(email, fName, lName, "G");

            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void askRegistrationNo2() {
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptView = li.inflate(R.layout.social_registration, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(promptView);

        final EditText edtRegNo = (EditText) promptView.findViewById(R.id.edtRegNo);
        final Button btnNext = (Button) promptView.findViewById(R.id.btnNext);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtRegNo.length() == 0) {
                    Functions.snack(v, "Registration Number must be entered to proceed ahead..");
                } else {
                    regNo = edtRegNo.getText().toString().trim();
                    dialog.dismiss();
                    signInWithGplus();

                }

            }
        });
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);


            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.S_TEXT_SIZE));
                tv.setTypeface(null, Typeface.BOLD);
                tv.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                tv.setText(buttonText);
                tv.setPadding(0, 0, 0, 0);
                return;
            }
        }
    }

}
