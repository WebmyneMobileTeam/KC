package com.webmyne.kidscrown.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapter.QualificationAdapter;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.model.Qualification;
import com.webmyne.kidscrown.model.Salutation;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText edtFirstname, edtLastName, edtMobile, edtEmail, edtPassword, edtConfirmPassword, edtRegNo, edtUserName;
    String firstName, lastName, mobile, emailId, password, registartionNo, qualification, username, salutation;
    Spinner edtQualification;
    RadioGroup rGroup;
    int salutationId = 0;
    ArrayList<Salutation> salutations = new ArrayList<>();
    ArrayList<Qualification> qualifications = new ArrayList<>();
    RadioButton rButton;
    Button btnUpdate;
    ProgressDialog pd;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        fetchSalutation();

        fetchQualification();

        return view;
    }

    private void fetchQualification() {
        new CallWebService(Constants.QUALIFICATION_URL, CallWebService.TYPE_GET, null) {
            @Override
            public void response(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray data = obj.getJSONArray("Data");

                    Type listType = new TypeToken<List<Qualification>>() {
                    }.getType();

                    qualifications = new GsonBuilder().create().fromJson(data.toString(), listType);
                    QualificationAdapter adapter = new QualificationAdapter(getActivity(), qualifications);
                    edtQualification.setAdapter(adapter);
                    edtQualification.setSelection(getIndex(edtQualification, qualification));
                    edtQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            qualification = qualifications.get(position).CodeValue;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String error) {

            }
        }.call();
    }

    private int getIndex(Spinner edtQualification, String qualification) {
        int index = 0;
        for (int i = 0; i < edtQualification.getCount(); i++) {
            if (edtQualification.getItemAtPosition(i).equals(qualification)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void fetchSalutation() {
        new CallWebService(Constants.SALUTATION_URL, CallWebService.TYPE_GET, null) {
            @Override
            public void response(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray data = obj.getJSONArray("Data");

                    Type listType = new TypeToken<List<Salutation>>() {
                    }.getType();

                    salutations = new GsonBuilder().create().fromJson(data.toString(), listType);

                    RadioGroup.LayoutParams rprms;
                    rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    rprms.setMargins(6, 0, 6, 0);
                    for (int i = 0; i < salutations.size(); i++) {
                        RadioButton rButton = new RadioButton(getActivity());
                        rButton.setTextColor(Color.WHITE);
                        rButton.setPadding(6, 6, 6, 6);
                        rButton.setButtonDrawable(R.drawable.custom_radio_drawable);
                        rButton.setId(salutations.get(i).CodeID);
                        rButton.setText(salutations.get(i).CodeValue);
                        if (salutations.get(i).CodeID == Integer.parseInt(salutation)) {
                            rButton.setChecked(true);
                        }
                        rGroup.addView(rButton, rprms);
                        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                salutationId = checkedId;
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String error) {

            }
        }.call();
    }

    private void init(View view) {
        edtFirstname = (EditText) view.findViewById(R.id.edtFirstname);
        edtLastName = (EditText) view.findViewById(R.id.edtLastName);
        edtUserName = (EditText) view.findViewById(R.id.edtUserName);
        rGroup = (RadioGroup) view.findViewById(R.id.rGroup);
        edtMobile = (EditText) view.findViewById(R.id.edtMobile);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) view.findViewById(R.id.edtConfirmPassword);
        edtRegNo = (EditText) view.findViewById(R.id.edtRegNo);
        edtQualification = (Spinner) view.findViewById(R.id.edtQualification);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        Log.e("ID", currentUserObj.UserID);
        firstName = currentUserObj.FirstName;
        lastName = currentUserObj.LastName;
        username = currentUserObj.UserName;
        emailId = currentUserObj.EmailID;
        mobile = currentUserObj.MobileNo;
        password = currentUserObj.Password;
        registartionNo = currentUserObj.RegistrationNumber;
        qualification = currentUserObj.Qualification;
        salutation = currentUserObj.Salutation;

        edtFirstname.setText(firstName);
        edtLastName.setText(lastName);
        edtUserName.setText(username);
        edtMobile.setText(mobile);
        edtEmail.setText(emailId);
        edtPassword.setText(password);
        edtConfirmPassword.setText(password);
        edtRegNo.setText(registartionNo);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
