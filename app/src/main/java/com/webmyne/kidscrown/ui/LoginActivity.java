package com.webmyne.kidscrown.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.gun0912.tedpermission.PermissionListener;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchLoginData;
import com.webmyne.kidscrown.api.FetchUpdateProfileData;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.LoginModelData;
import com.webmyne.kidscrown.model.LoginModelRequest;
import com.webmyne.kidscrown.model.UpdateProfileModelRequest;
import com.webmyne.kidscrown.model.UserProfileModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button btnLogin;

    TextView txtRegister, txtForgot;
    EditText edtUsername, edtPassword;

    // Google Integration
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;

    private RelativeLayout linearFbLogin, linearGPLusLogin;
    private CallbackManager callbackManager;
    private String loginVia = "0";
    String regNo;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String deviceId;

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
                Functions.hideKeyPad(LoginActivity.this, v);

                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.showToast(LoginActivity.this, getString(R.string.no_internet));
                    return;
                }

                if (edtUsername.getText().toString().trim().length() == 0) {
                    Functions.showToast(LoginActivity.this, getString(R.string.invalid_username));

                } else if (edtPassword.getText().toString().trim().length() == 0) {
                    Functions.showToast(LoginActivity.this, getString(R.string.invalid_password));

                } else if (edtPassword.getText().toString().trim().length() < getResources().getInteger(R.integer.pwd_min)) {
                    Functions.showToast(LoginActivity.this, getString(R.string.password_length));

                } else {
                    loginVia = Constants.NORMAL;
                    loginProcess(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim(), "", "", "");
                }
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }
                Functions.fireIntent(LoginActivity.this, RegisterActivity.class);
            }
        });

        linearGPLusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                } else {
                    loginVia = Constants.GPLUS;
                    signIn();
                }

            }
        });

        linearFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }

                loginVia = Constants.FB;
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile, email, user_birthday, user_friends"));
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
                                Log.e("fb_reponse", response.getJSONObject().toString());
                                JSONObject profile = response.getJSONObject();
                                try {
                                    String social_id = profile.getString("id").toString();
                                    String email = profile.getString("email").toString();
                                    String fName = profile.getString("name").toString().split(" ")[0];
                                    String lName = profile.getString("name").toString().split(" ")[1];
                                    loginProcess(email, "", social_id, fName, lName);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void loginProcess(String email, String password, String social_id, String fName, String lName) {
        Log.e("tag", "email: " + email + "  password: " + password + "  social_id: " + social_id + "  fName: " + fName + "  lName: " + lName);

        try {
            // Facebook logout
            LoginManager.getInstance().logOut();
            //GooglePlus Logout
            if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            }
        } catch (Exception e1) {
            Log.e("EXP LOGOUT", e1.toString());
        }

        if (TextUtils.isEmpty(deviceId)) {
            Functions.setPermission(this, new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    deviceId = telephonyManager.getDeviceId();
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                }
            });
            return;
        }
        LoginModelRequest model = new LoginModelRequest();
        model.setDeviceID(deviceId);
        model.setGCMToken(PrefUtils.getFCMToken(this));
        model.setLoginVia(loginVia);
        model.setMobileOS("A");
        model.setPassword(password);
        model.setUserName(email);
        model.setFirstName(fName);
        model.setLastName(lName);
        if (loginVia.equals(Constants.FB)) {
            model.setFacebookID(social_id);
        } else if (loginVia.equals(Constants.GPLUS)) {
            model.setGoogleID(social_id);
        }

        new FetchLoginData(this, model, new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                LoginModelData responseModel = (LoginModelData) responseBody;

                PrefUtils.setUserProfile(LoginActivity.this, responseModel);

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                Log.e("tag", "responseModel.getRegistrationNumber(): " + responseModel.getRegistrationNumber());
                if (TextUtils.isEmpty(responseModel.getRegistrationNumber())) {

                    askRegistrationNo(responseModel);

                } else {

                    PrefUtils.setUserProfile(LoginActivity.this, responseModel);

                    PrefUtils.setLoggedIn(LoginActivity.this, true);

                    PrefUtils.setFirstTime(LoginActivity.this, true);

                    Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }

            }

            @Override
            public void onFail() {

            }
        });
    }

    private void askRegistrationNo(final LoginModelData responseModel) {

        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptView = li.inflate(R.layout.social_registration, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(promptView);

        final EditText edtRegNo = (EditText) promptView.findViewById(R.id.edtRegNo);
        final Button btnNext = (Button) promptView.findViewById(R.id.btnNext);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(LoginActivity.this, v);
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }

                if (edtRegNo.getText().toString().trim().length() == 0) {
                    Functions.showToast(LoginActivity.this, "Registraion Number must required.");

                } else {
                    regNo = edtRegNo.getText().toString().trim();

                    Log.e("tag", "Functions.jsonString(responseModel): " + Functions.jsonString(responseModel));
                    registerWebService(dialog, responseModel);

                    // dialog.dismiss();
                }

            }
        });
    }

    private void registerWebService(final AlertDialog dialog, LoginModelData responseModel) {

        UpdateProfileModelRequest model = new UpdateProfileModelRequest();
        model.setClinicName(responseModel.getClinicName());
        model.setEmailID(responseModel.getEmailID());
        model.setFirstName(responseModel.getFirstName());
        model.setLastName(responseModel.getLastName());
        model.setMobileNo(responseModel.getMobileNo());
        model.setPassword(responseModel.getPassword());
        model.setRegistrationNumber(regNo);
        model.setUserID(responseModel.getUserID());
        model.setUserName(responseModel.getUserName());

        new FetchUpdateProfileData(this, model, new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                dialog.dismiss();

                UserProfileModel responseModel = (UserProfileModel) responseBody;

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                LoginModelData modelData = PrefUtils.getUserProfile(LoginActivity.this);
                modelData.setEmailID(responseModel.getEmailID());
                modelData.setFirstName(responseModel.getFirstName());
                modelData.setLastName(responseModel.getLastName());
                modelData.setMobileNo(responseModel.getMobileNo());
                modelData.setPassword(responseModel.getPassword());
                modelData.setRegistrationNumber(responseModel.getRegistrationNumber());
                modelData.setUserID(responseModel.getUserID());
                modelData.setUserName(responseModel.getUserName());

                PrefUtils.setUserProfile(LoginActivity.this, modelData);

                PrefUtils.setLoggedIn(LoginActivity.this, true);

                Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
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
        txtForgot = (TextView) findViewById(R.id.txtForgot);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        linearFbLogin = (RelativeLayout) findViewById(R.id.linearFbLogin);
        linearGPLusLogin = (RelativeLayout) findViewById(R.id.linearGPLusLogin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Functions.setPermission(this, new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = telephonyManager.getDeviceId();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        });

    }

    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e("tag", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e("tag", "display name: " + acct.getDisplayName().split(" ")[0]);
            Log.e("tag", "display name: " + acct.getDisplayName().split(" ")[1]);
            Log.e("tag", "display name: " + acct.getEmail());
            Log.e("tag", "display name: " + acct.getId());

            String fName = acct.getDisplayName().split(" ")[0];
            String lName = acct.getDisplayName().split(" ")[1];
            String email = acct.getEmail();
            String social_id = acct.getId();

            loginProcess(email, "", social_id, fName, lName);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

        Log.e("tag", "" + requestCode + "  " + responseCode + "  " + intent);

        if (requestCode == RC_SIGN_IN) {
            Log.e("Success", "Success");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }
        if (callbackManager.onActivityResult(requestCode, responseCode, intent)) {
            return;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

}
