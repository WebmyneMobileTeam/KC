package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.GsonBuilder;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchLoginData;
import com.webmyne.kidscrown.api.FetchUpdateProfileData;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.LoginModelData;
import com.webmyne.kidscrown.model.LoginModelRequest;
import com.webmyne.kidscrown.model.UpdateProfileModelRequest;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.model.UserProfileModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button btnLogin;

    TextView txtRegister, txtForgot;
    EditText edtUsername, edtPassword;
    String name, password, url;

    // Google Integration
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    //    SignInButton btnGplus;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    private RelativeLayout linearFbLogin, linearGPLusLogin;
    private CallbackManager callbackManager;
    private String loginVia = "0";
    private ProgressDialog pd;
    String regNo;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }

                if (edtUsername.getText().toString().trim().length() == 0) {
                    //Functions.snack(v, "Username or Email is required");
                    Functions.showToast(LoginActivity.this, getString(R.string.invalid_username));
                } else if (edtPassword.getText().toString().trim().length() == 0) {
                    //Functions.snack(v, "Password is required");
                    Functions.showToast(LoginActivity.this, getString(R.string.invalid_password));
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

        linearGPLusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                } else {
                    loginVia = Constants.GPLUS;
//                    signInWithGplus();
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
                } else {
                    loginVia = Constants.FB;
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile, email, user_birthday, user_friends"));
                }
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
//                                    socialMediaLoginProcess(email, "F");

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
//        name = edtUsername.getText().toString().trim();
//        password = edtPassword.getText().toString().trim();
//        url = Constants.LOGIN_URL + name + "/" + password;

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

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        LoginModelRequest model = new LoginModelRequest();
        model.setDeviceID(telephonyManager.getDeviceId());
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

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                Log.e("tag", "responseModel.getRegistrationNumber(): " + responseModel.getRegistrationNumber());
                if (TextUtils.isEmpty(responseModel.getRegistrationNumber())) {

//                    PrefUtils.setUserProfile(LoginActivity.this, responseModel);

                    Log.e("tag", "Functions.jsonString(responseModel): " + Functions.jsonString(responseModel));

                    askRegistrationNo(responseModel);

                } else {

                    PrefUtils.setUserProfile(LoginActivity.this, responseModel);

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

//
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject();
//            jsonObject.put("ClinicName", model.getClinicName());
//            jsonObject.put("DeviceID", model.getDeviceID());
//            jsonObject.put("EmailID", model.getFirstName());
//            jsonObject.put("GCMToken", model.getGCMToken());
//            jsonObject.put("LastName", model.getLastName());
//            jsonObject.put("LoginVia", model.getLoginVia());
//            jsonObject.put("MobileNo", model.getMobileNo());
//            jsonObject.put("MobileOS", model.getMobileOS());
//            jsonObject.put("Password", model.getPassword());
//            jsonObject.put("RegistrationNumber", model.getRegistrationNumber());
//            jsonObject.put("SocialID", model.getSocialID());
//            jsonObject.put("UserName", model.getUserName());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.e("tag", "login url: " + URLConstants.BASE_URL_V03 + URLConstants.LOGIN);
//        Log.e("request", "" + jsonObject.toString());
//        new CallWebService(URLConstants.BASE_URL_V03 + URLConstants.LOGIN, CallWebService.TYPE_POST, jsonObject) {
//            @Override
//            public void response(String response) {
//                pd.dismiss();
//                Log.e("login_response", response);
////                try {
////                    JSONArray obj = new JSONArray(response);
////                    JSONObject description = obj.getJSONObject(0);
////                    UserProfile profile = new GsonBuilder().create().fromJson(description.toString(), UserProfile.class);
////
////                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginActivity.this, "user_pref", 0);
////                    complexPreferences.putObject("current-user", profile);
////                    complexPreferences.commit();
////
////                    SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
////                    SharedPreferences.Editor editor = preferences.edit();
////                    editor.putBoolean("isUserLogin", true);
////                    editor.putBoolean("isFirstTimeLogin", true);
////                    editor.commit();
////
////                    Intent i = new Intent(LoginActivity.this, MyDrawerActivity.class);
////                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                    startActivity(i);
////                    finish();
////
////                } catch (Exception e) {
////                    Snackbar snack = Snackbar.make(btnLogin, getString(R.string.unable_to_login), Snackbar.LENGTH_LONG);
////                    View view = snack.getView();
////                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
////                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), LoginActivity.this));
////                    snack.show();
////                    e.printStackTrace();
////                }
//            }
//
//            @Override
//            public void error(String error) {
//                pd.dismiss();
//                Log.e("error", "" + error);
//                Functions.snack(btnLogin, getString(R.string.invalid_login));
//            }
//        }.call();

    }

    private void askRegistrationNo(final LoginModelData responseModel) {

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
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }

                if (edtRegNo.getText().toString().trim().length() == 0) {
                    Functions.showToast(LoginActivity.this, "Registraion Number must required.");

                } else {
                    regNo = edtRegNo.getText().toString().trim();

                    Log.e("tag", "Functions.jsonString(responseModel): " + Functions.jsonString(responseModel));
                    registerWebService(responseModel);

                    dialog.dismiss();
                }

            }
        });
    }

    private void registerWebService(LoginModelData responseModel) {

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

    private void socialMediaLoginProcess(String email, String loginType) {
        url = Constants.SOCIAL_MEDIA_LOGIN_URL + email + "/" + loginType;

        pd = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.please_wait), true);
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
                    Snackbar snack = Snackbar.make(btnLogin, getString(R.string.unable_to_login), Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), LoginActivity.this));
                    snack.show();
                    Functions.logE("Exp", e.toString());
                    e.printStackTrace();

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
                }
            }

            @Override
            public void error(String error) {
                pd.dismiss();
                Snackbar snack = Snackbar.make(btnLogin, getString(R.string.unable_to_login) + " " + error, Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), LoginActivity.this));
                snack.show();
                Functions.logE("social_media error", error);

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
            }
        }.call();
    }

    private void init() {
        txtForgot = (TextView) findViewById(R.id.txtForgot);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        linearFbLogin = (RelativeLayout) findViewById(R.id.linearFbLogin);
        linearGPLusLogin = (RelativeLayout) findViewById(R.id.linearGPLusLogin);
//        btnGplus = (SignInButton) findViewById(R.id.btnGplus);
//        setGooglePlusButtonText(btnGplus, "Continue With Google Plus");

//        btnGplus.setSize(SignInButton.SIZE_STANDARD);

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
//                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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

//    private void resolveSignInError() {
//        if (mConnectionResult.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
//            } catch (IntentSender.SendIntentException e) {
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
//    }

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
//                resolveSignInError();
            }
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

//            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
//            String email = acct.getEmail();
//
//            Log.e("tag", "Name: " + personName + ", email: " + email
//                    + ", Image: " + personPhotoUrl);

//            loginProcess();

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

//        if (requestCode == RC_SIGN_IN) {
//            if (responseCode != RESULT_OK) {
//                mSignInClicked = false;
//            }
//
//            mIntentInProgress = false;
//
//            if (!mGoogleApiClient.isConnecting()) {
//                mGoogleApiClient.connect();
//            }
//        } else {
//            callbackManager.onActivityResult(requestCode, responseCode, intent);
//        }
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

//    @Override
//    public void onConnected(Bundle arg0) {
//        mSignInClicked = false;
////        getGPlusProfileInformation();
//    }

//    private void getGPlusProfileInformation() {
//        try {
//            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//                Person currentPerson = Plus.PeopleApi
//                        .getCurrentPerson(mGoogleApiClient);
//                String personName = currentPerson.getDisplayName();
//                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
//
//                String fName = personName.split(" ")[0];
//                String lName = personName.split(" ")[1];
//                loginProcess(email, "", "", fName, lName);
////                socialMediaLoginProcess(email, "G");
//
//            } else {
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        mGoogleApiClient.connect();
//    }
//
//    private void signInWithGplus() {
//        if (!mGoogleApiClient.isConnecting()) {
//            mSignInClicked = true;
//            resolveSignInError();
//        }
//    }
//
//    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
//        for (int i = 0; i < signInButton.getChildCount(); i++) {
//            View v = signInButton.getChildAt(i);
//
//
//            if (v instanceof TextView) {
//                TextView tv = (TextView) v;
//                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.S_TEXT_SIZE));
//                tv.setTypeface(null, Typeface.BOLD);
//                tv.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
//                tv.setText(buttonText);
//                tv.setPadding(0, 0, 0, 0);
//                return;
//            }
//        }
//    }

}
