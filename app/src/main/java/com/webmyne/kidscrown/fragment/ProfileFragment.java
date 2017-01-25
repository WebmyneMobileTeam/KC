package com.webmyne.kidscrown.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.UpdateProfileModelRequest;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

import org.json.JSONArray;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {

    EditText edtFirstname, edtLastName, edtMobile, edtEmail, edtPassword, edtConfirmPassword, edtRegNo, edtUserName, edtClinicName;
    String firstName, lastName, mobile, emailId, password, registartionNo, username, salutation, userId, clinicName;
    Button btnUpdate;
    ProgressDialog pd;
    View parentView;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_profile, container, false);

        init(parentView);

        return parentView;
    }

    private void init(View view) {
        edtFirstname = (EditText) view.findViewById(R.id.edtFirstname);
        edtLastName = (EditText) view.findViewById(R.id.edtLastName);
        edtUserName = (EditText) view.findViewById(R.id.edtUserName);
        edtMobile = (EditText) view.findViewById(R.id.edtMobile);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) view.findViewById(R.id.edtConfirmPassword);
        edtRegNo = (EditText) view.findViewById(R.id.edtRegNo);
        edtClinicName = (EditText) view.findViewById(R.id.edtClinicName);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        userId = currentUserObj.UserID;

        Log.e("userId", userId);

        firstName = currentUserObj.FirstName;
        lastName = currentUserObj.LastName;
        username = currentUserObj.UserName;
        emailId = currentUserObj.EmailID;
        mobile = currentUserObj.MobileNo;
        password = currentUserObj.Password;
        registartionNo = currentUserObj.RegistrationNumber;
        salutation = currentUserObj.Salutation;
        clinicName = currentUserObj.ClinicName;

        edtFirstname.setText(firstName);
        edtLastName.setText(lastName);
        edtUserName.setText(username);
        edtMobile.setText(mobile);
        edtEmail.setText(emailId);
        edtPassword.setText(password);
        edtConfirmPassword.setText(password);
        edtRegNo.setText(registartionNo);
        edtClinicName.setText(clinicName);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        ((MyDrawerActivity) getActivity()).setTitle("Profile");
    }

    private void checkValidation() {
        Snackbar snack = Snackbar.make(parentView, "", Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setSingleLine(false);
        tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), getActivity()));

        if (edtFirstname.getText().toString().trim().length() == 0) {
            snack.setText("First name is required");
            snack.show();
        } else if (edtLastName.getText().toString().trim().length() == 0) {
            snack.setText("Last name is required");
            snack.show();
        } else if (edtMobile.getText().toString().trim().length() != 10) {
            snack.setText("Mobile number should contains 10 digits");
            snack.show();
        } else if (edtEmail.getText().toString().trim().length() == 0 || !(Functions.emailValidation(edtEmail.getText().toString().trim()))) {
            snack.setText("Email-id is not valid");
            snack.show();
        } else if (edtPassword.getText().toString().trim().length() < 6) {
            snack.setText("Password must be of minimum 6 characters");
            snack.show();
        } else if (edtRegNo.getText().toString().trim().length() == 0) {
            snack.setText("Registration number is required");
            snack.show();
        } else if (edtUserName.getText().toString().trim().length() == 0) {
            snack.setText("Username is required");
            snack.show();
        } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            snack.setText("Password and confirm password does not match");
            snack.show();
        } else {
            registerWebService();
        }
    }

    private void registerWebService() {

        UpdateProfileModelRequest model = new UpdateProfileModelRequest();
        model.setClinicName("");
        model.setEmailID(edtEmail.getText().toString().trim());
        model.setFirstName(edtFirstname.getText().toString().trim());
        model.setLastName(edtLastName.getText().toString().trim());
        model.setMobileNo(edtMobile.getText().toString().trim());
        model.setPassword(edtPassword.getText().toString().trim());
        model.setRegistrationNumber(edtRegNo.getText().toString().trim());
        model.setUserID(PrefUtils.getUserId(getActivity()));
        model.setUserName(edtUserName.getText().toString().trim());

//        new FetchLoginData(this, new LoginModelRequest(), new CommonRetrofitResponseListener() {
//            @Override
//            public void onSuccess(Object responseBody) {
//
//                pd.dismiss();
//
//                LoginModelData responseModel = (LoginModelData) responseBody;
//
//                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));
//
//                PrefUtils.setUserProfile(LoginActivity.this, responseModel);
//
//            }
//
//            @Override
//            public void onFail() {
//
//                pd.dismiss();
//
//            }
//        });


//        username = edtUserName.getText().toString().trim();
//        firstName = edtFirstname.getText().toString().trim();
//        lastName = edtLastName.getText().toString().trim();
//        mobile = edtMobile.getText().toString().trim();
//        emailId = edtEmail.getText().toString().trim();
//        password = edtPassword.getText().toString();
//        registartionNo = edtRegNo.getText().toString().trim();
//        clinicName = edtClinicName.getText().toString().trim();
//
//        JSONObject userObject = null;
//        try {
//            userObject = new JSONObject();
//            userObject.put("ClinicName", clinicName);
//            userObject.put("EmailID", emailId);
//            userObject.put("FirstName", firstName);
//            userObject.put("IsActive", true);
//            userObject.put("IsDelete", true);
//            userObject.put("LastName", lastName);
//            userObject.put("MobileNo", mobile);
//            userObject.put("MobileOS", "A");
//            userObject.put("Password", password);
//            userObject.put("RegistrationNumber", registartionNo);
//            userObject.put("Salutation", 0);
//            userObject.put("UserID", Integer.parseInt(userId));
//            userObject.put("UserName", username);
//            userObject.put("UserRoleID", 2);
//            userObject.put("PriorityID", 5);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        pd = ProgressDialog.show(getActivity(), "Loading", "Please wait..", true);
//        Functions.logE("update request", userObject.toString());
//
//        new CallWebService(Constants.UPDATE_URL, CallWebService.TYPE_POST, userObject) {
//            @Override
//            public void response(String response) {
//                pd.dismiss();
//                JSONArray data;
//                Log.e("update response", response + "");
//                try {
//                    data = new JSONArray(response);
//                    JSONObject description = data.getJSONObject(0);
//
//                    UserProfile profile = new GsonBuilder().create().fromJson(description.toString(), UserProfile.class);
//
//                    Snackbar snack = Snackbar.make(parentView, "Update Profile Successfully", Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), getActivity()));
//                    snack.show();
//
//                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
//                    complexPreferences.putObject("current-user", profile);
//                    complexPreferences.commit();
//
//                    Intent i = new Intent(getActivity(), MyDrawerActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getActivity().startActivity(i);
//
//                } catch (Exception e) {
//                    Snackbar snack = Snackbar.make(parentView, "Unable To Update Profile", Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), getActivity()));
//                    snack.show();
//                }
//            }
//
//            @Override
//            public void error(String error) {
//                pd.dismiss();
//                Snackbar snack = Snackbar.make(parentView, "Unable To Update Profile. " + error, Snackbar.LENGTH_LONG);
//                View view = snack.getView();
//                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//                tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), getActivity()));
//                snack.show();
//
//                Log.e("error", error);
//            }
//        }.call();

    }

}
