package com.webmyne.kidscrown.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.GsonBuilder;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

import org.json.JSONArray;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText edtFirstname, edtLastName, edtMobile, edtEmail, edtPassword, edtConfirmPassword, edtRegNo, edtUserName, edtClinicName;
    String firstName, lastName, mobile, emailId, password, registartionNo, username, salutation, userId, clinicName;
    Button btnUpdate;
    ProgressDialog pd;
    View parentView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        clinicName = edtClinicName.getText().toString().trim();

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
            userObject.put("ClinicName", clinicName);
            userObject.put("Salutation", 0);
            userObject.put("UserID", userId);
            userObject.put("UserName", username);
            userObject.put("UserRoleID", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pd = ProgressDialog.show(getActivity(), "Loading", "Please wait..", true);
        Functions.logE("update request", userObject.toString());

        new CallWebService(Constants.UPDATE_URL, CallWebService.TYPE_POST, userObject) {
            @Override
            public void response(String response) {
                pd.dismiss();
                JSONArray data;
                Log.e("update response", response + "");
                try {
                    data = new JSONArray(response);
                    JSONObject description = data.getJSONObject(0);

                    UserProfile profile = new GsonBuilder().create().fromJson(description.toString(), UserProfile.class);

                    Functions.snack(parentView, "Update Profile Successfully");
                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
                    complexPreferences.putObject("current-user", profile);
                    complexPreferences.commit();

                    Intent i = new Intent(getActivity(), MyDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(i);

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
