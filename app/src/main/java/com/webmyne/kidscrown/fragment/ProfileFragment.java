package com.webmyne.kidscrown.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchUpdateProfileData;
import com.webmyne.kidscrown.api.FetchUsereProfileData;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.LoginModelData;
import com.webmyne.kidscrown.model.UpdateProfileModelRequest;
import com.webmyne.kidscrown.model.UserProfileModel;
import com.webmyne.kidscrown.ui.MyDrawerActivity;


public class ProfileFragment extends Fragment {

    EditText edtFirstname, edtLastName, edtMobile, edtEmail, edtPassword, edtConfirmPassword, edtRegNo, edtUserName, edtClinicName;
    Button btnUpdate;
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

        edtUserName.setEnabled(false);
        edtEmail.setEnabled(false);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(getActivity(), v);
                if (!Functions.isConnected(getActivity())) {
                    Functions.showToast(getActivity(), getString(R.string.no_internet));
                    return;
                }
                checkValidation();
            }
        });

        ((MyDrawerActivity) getActivity()).setTitle("Profile");

        getUserProfileData();
    }

    private void getUserProfileData() {

        new FetchUsereProfileData(getActivity(), new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                UserProfileModel responseModel = (UserProfileModel) responseBody;

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                LoginModelData modelData = PrefUtils.getUserProfile(getActivity());
                modelData.setEmailID(responseModel.getEmailID());
                modelData.setFirstName(responseModel.getFirstName());
                modelData.setLastName(responseModel.getLastName());
                modelData.setMobileNo(responseModel.getMobileNo());
                modelData.setPassword(responseModel.getPassword());
                modelData.setRegistrationNumber(responseModel.getRegistrationNumber());
                modelData.setUserID(responseModel.getUserID());
                modelData.setUserName(responseModel.getUserName());
                modelData.setClinicName(responseModel.getClinicName());

                edtFirstname.setText(responseModel.getFirstName());
                edtLastName.setText(responseModel.getLastName());
                edtUserName.setText(responseModel.getUserName());
                edtMobile.setText(responseModel.getMobileNo());
                edtEmail.setText(responseModel.getEmailID());
                edtPassword.setText(responseModel.getPassword());
                edtConfirmPassword.setText(responseModel.getPassword());
                edtRegNo.setText(responseModel.getRegistrationNumber());
                edtClinicName.setText(responseModel.getClinicName());

                PrefUtils.setUserProfile(getActivity(), modelData);
            }

            @Override
            public void onFail() {

            }
        });

    }

    private void checkValidation() {

        if (TextUtils.isEmpty(Functions.getStr(edtFirstname))) {
            Functions.showToast(getActivity(), "First name is required");

        } else if (TextUtils.isEmpty(Functions.getStr(edtLastName))) {
            Functions.showToast(getActivity(), "Last name is required");

        } else if (edtUserName.getText().toString().trim().length() == 0) {
            Functions.showToast(getActivity(), "Username is required");

        } else if (TextUtils.isEmpty(Functions.getStr(edtMobile))) {
            Functions.showToast(getActivity(), "Mobile number is required");

        } else if (edtMobile.getText().toString().trim().length() != 10) {
            Functions.showToast(getActivity(), "Mobile number should contains 10 digits");

        } else if (TextUtils.isEmpty(Functions.getStr(edtEmail))) {
            Functions.showToast(getActivity(), "Email-id is required");

        } else if (!(Functions.emailValidation(edtEmail.getText().toString().trim()))) {
            Functions.showToast(getActivity(), "Email-id is not valid");

        } else if (edtPassword.getText().toString().trim().length() < 6) {
            Functions.showToast(getActivity(), "Password must be of minimum 6 characters");

        } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            Functions.showToast(getActivity(), "Password and confirm password does not match");

        } else if (edtRegNo.getText().toString().trim().length() == 0) {
            Functions.showToast(getActivity(), "Registration number is required");

        } else {
            registerWebService();
        }
    }

    private void registerWebService() {

        UpdateProfileModelRequest model = new UpdateProfileModelRequest();
        model.setClinicName(edtClinicName.getText().toString().trim());
        model.setEmailID(edtEmail.getText().toString().trim());
        model.setFirstName(edtFirstname.getText().toString().trim());
        model.setLastName(edtLastName.getText().toString().trim());
        model.setMobileNo(edtMobile.getText().toString().trim());
        model.setPassword(edtPassword.getText().toString().trim());
        model.setRegistrationNumber(edtRegNo.getText().toString().trim());
        model.setUserID(PrefUtils.getUserId(getActivity()));
        model.setUserName(edtUserName.getText().toString().trim());

        new FetchUpdateProfileData(getActivity(), model, new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                UserProfileModel responseModel = (UserProfileModel) responseBody;

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                LoginModelData modelData = PrefUtils.getUserProfile(getActivity());
                modelData.setEmailID(responseModel.getEmailID());
                modelData.setFirstName(responseModel.getFirstName());
                modelData.setLastName(responseModel.getLastName());
                modelData.setMobileNo(responseModel.getMobileNo());
                modelData.setPassword(responseModel.getPassword());
                modelData.setRegistrationNumber(responseModel.getRegistrationNumber());
                modelData.setUserID(responseModel.getUserID());
                modelData.setUserName(responseModel.getUserName());

                PrefUtils.setUserProfile(getActivity(), modelData);

                Functions.showToast(getActivity(), getString(R.string.update_profile_success));
            }

            @Override
            public void onFail() {

            }
        });
    }

}
