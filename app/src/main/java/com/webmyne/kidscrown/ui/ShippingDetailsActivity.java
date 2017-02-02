package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.StateSpinnerAdapter;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.Address;
import com.webmyne.kidscrown.model.AddressModel;
import com.webmyne.kidscrown.model.BillingAndShippingAddress;
import com.webmyne.kidscrown.model.CountryResponse;
import com.webmyne.kidscrown.model.PlaceOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderResponse;
import com.webmyne.kidscrown.model.StateModel;
import com.webmyne.kidscrown.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ShippingDetailsActivity extends AppCompatActivity {

    ArrayList<Address> addresses = new ArrayList<>();
    ArrayList<AddressModel> addressModels = new ArrayList<>();
    LinearLayout linearParent;
    UserProfile currentUserObj = new UserProfile();
    ProgressDialog pd, pd1;
    Toolbar toolbar;

    SegmentedGroup segmented2;
    int shippingStateId = 0, billingStateId = 0;
    String shippingStateName, billingStateName;
    BillingAndShippingAddress billingAndShippingAddress = new BillingAndShippingAddress();
    private EditText edtBillingAddress1, edtBillingAddress2, edtBillingCity, edtBillingPincode, edtShippingAddress1, edtShippingAddress2, edtShippingCity, edtShippingPincode;
    private AppCompatCheckBox isSameAsBilling;
    ArrayList<StateModel> states = new ArrayList<>();
    boolean sameAsBilling = false;

    PlaceOrderResponse.DataBean resBean;
    PlaceOrderRequest reqBean;
    private Button btnConfirm;
    private TextView txtCustomTitle;
    private RelativeLayout shippingLayout, billingLayout;
    private ComplexPreferences complexPreferences;
    private AppCompatSpinner stateShippingSpinner, stateBillingSpinner;
    private CountryResponse countryResponse;
    private int billingState = 0, shippingState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);

        complexPreferences = ComplexPreferences.getComplexPreferences(ShippingDetailsActivity.this, Constants.PREF_NAME, 0);
        resBean = complexPreferences.getObject("placeOrderRes", PlaceOrderResponse.DataBean.class);
        reqBean = complexPreferences.getObject("placeOrderReq", PlaceOrderRequest.class);
        countryResponse = complexPreferences.getObject("state", CountryResponse.class);

        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);

        init();

        displayAddress();

       /* pd = ProgressDialog.show(ShippingDetailsActivity.this, "Loading", "Please wait..", true);
        fetchAddress(pd);*/
    }

    private void fetchAddress(final ProgressDialog progressDialog) {
        String user = "?UserId=" + PrefUtils.getUserId(this);
        Log.e("Address URL", Constants.GET_EXISTING_ADDRESS + user);

        new CallWebService(Constants.GET_EXISTING_ADDRESS + user, CallWebService.TYPE_GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("Response Address", response);
                Type listType = new TypeToken<List<Address>>() {
                }.getType();
                addresses = new GsonBuilder().create().fromJson(response, listType);

                displayAddress();

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Log.e("Error", error);

            }
        }.call();
    }

    private void displayAddress() {

        edtShippingAddress1.setText(resBean.getShippingAddressDC().getAddress1());
        edtShippingAddress2.setText(resBean.getShippingAddressDC().getAddress2());
        edtShippingCity.setText(resBean.getShippingAddressDC().getCity());
        edtShippingPincode.setText(resBean.getShippingAddressDC().getPinCode());

        edtBillingAddress1.setText(resBean.getBillingAddressDC().getAddress1());
        edtBillingAddress2.setText(resBean.getBillingAddressDC().getAddress2());
        edtBillingCity.setText(resBean.getBillingAddressDC().getCity());
        edtBillingPincode.setText(resBean.getBillingAddressDC().getPinCode());

        /*for (int k = 0; k < addresses.size(); k++) {
            boolean isShipping = addresses.get(k).IsShipping;
            if (isShipping) {
                edtShippingAddress1.setText(addresses.get(k).Address1);
                edtShippingAddress2.setText(addresses.get(k).Address2);
                edtShippingCity.setText(addresses.get(k).City);
                edtShippingCountry.setText(addresses.get(k).Country);
                edtShippingPincode.setText(addresses.get(k).PinCode);
                edtShipingStateSpinner.setSelection(getIndex(states, addresses.get(k).StateID));

            } else {
                edtBillingAddress1.setText(addresses.get(k).Address1);
                edtBillingAddress2.setText(addresses.get(k).Address2);
                edtBillingCity.setText(addresses.get(k).City);
                edtBillingCountry.setText(addresses.get(k).Country);
                edtBillingPincode.setText(addresses.get(k).PinCode);
                edtBillingStateSpinner.setSelection(getIndex(states, addresses.get(k).StateID));
            }
        }*/

    }

    private void init() {

        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

        loadStateSpinner();

        shippingLayout = (RelativeLayout) findViewById(R.id.shippingLayout);
        billingLayout = (RelativeLayout) findViewById(R.id.billingLayout);
        shippingLayout.setVisibility(View.GONE);

        stateShippingSpinner = (AppCompatSpinner) findViewById(R.id.stateShippingSpinner);
        stateBillingSpinner = (AppCompatSpinner) findViewById(R.id.stateBillingSpinner);

        stateShippingSpinner.setAdapter(new StateSpinnerAdapter(ShippingDetailsActivity.this, R.layout.spinner_layout_transperent, countryResponse.getData()));
        stateBillingSpinner.setAdapter(new StateSpinnerAdapter(ShippingDetailsActivity.this, R.layout.spinner_layout_transperent, countryResponse.getData()));

        stateBillingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CountryResponse.DataBean selectedDataBean = countryResponse.getData().get(i);
                billingStateId = selectedDataBean.getStateID();
                Log.e("billingStateId", billingStateId + "$$");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        stateShippingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CountryResponse.DataBean selectedDataBean = countryResponse.getData().get(i);
                shippingStateId = selectedDataBean.getStateID();
                Log.e("shippingStateId", shippingStateId + "$$");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        linearParent = (LinearLayout) findViewById(R.id.linearParent);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        edtBillingAddress1 = (EditText) findViewById(R.id.edtBillingAddress1);
        edtBillingAddress2 = (EditText) findViewById(R.id.edtBillingAddress2);
        edtBillingCity = (EditText) findViewById(R.id.edtBillingCity);
        edtBillingPincode = (EditText) findViewById(R.id.edtBillingPincode);

        edtBillingAddress1.addTextChangedListener(new CustomTextWatcher(edtBillingAddress1));
        edtBillingAddress2.addTextChangedListener(new CustomTextWatcher(edtBillingAddress2));
        edtBillingCity.addTextChangedListener(new CustomTextWatcher(edtBillingCity));
        edtBillingPincode.addTextChangedListener(new CustomTextWatcher(edtBillingPincode));

        edtShippingAddress1 = (EditText) findViewById(R.id.edtShippingAddress1);
        edtShippingAddress2 = (EditText) findViewById(R.id.edtShippingAddress2);
        edtShippingCity = (EditText) findViewById(R.id.edtShippingCity);
        edtShippingPincode = (EditText) findViewById(R.id.edtShippingPincode);

        edtShippingAddress1.addTextChangedListener(new CustomTextWatcher(edtShippingAddress1));
        edtShippingAddress2.addTextChangedListener(new CustomTextWatcher(edtShippingAddress2));
        edtShippingCity.addTextChangedListener(new CustomTextWatcher(edtShippingCity));
        edtShippingPincode.addTextChangedListener(new CustomTextWatcher(edtShippingPincode));

        isSameAsBilling = (AppCompatCheckBox) findViewById(R.id.checkbox);

        segmented2 = (SegmentedGroup) findViewById(R.id.segmented2);
        segmented2.setTintColor(Color.parseColor("#727272"));

        segmented2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                FrameLayout frame = (FrameLayout) findViewById(R.id.frame);

                if (checkedId == R.id.tabBilling) {
                    frame.getChildAt(0).setVisibility(View.GONE);
                    frame.getChildAt(1).setVisibility(View.VISIBLE);

                } else {
                    frame.getChildAt(0).setVisibility(View.VISIBLE);
                    frame.getChildAt(1).setVisibility(View.GONE);
                }
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            toolbar.setTitle("");
            txtCustomTitle.setText("Shipping Details");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Functions.isConnected(ShippingDetailsActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }

                if (validationDone(v)) {
                    saveAddressDetails();

                    /*pd1 = ProgressDialog.show(ShippingDetailsActivity.this, "Loading", "Please wait..", true);
                    sendAddressDetails(pd1);*/
                }
            }
        });

        isSameAsBilling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sameAsBilling = true;

                    edtShippingAddress1.setEnabled(false);
                    edtShippingAddress1.setText(edtBillingAddress1.getText().toString().trim());

                    edtShippingAddress2.setEnabled(false);
                    edtShippingAddress2.setText(edtBillingAddress2.getText().toString().trim());

                    edtShippingCity.setEnabled(false);
                    edtShippingCity.setText(edtBillingCity.getText().toString().trim());

                    edtShippingPincode.setEnabled(false);
                    edtShippingPincode.setText(edtBillingPincode.getText().toString().trim());

                    stateShippingSpinner.setEnabled(false);
                    if (billingStateId != 0) {
                        stateShippingSpinner.setSelection(getIndex(countryResponse.getData(), billingStateId));
                        shippingStateId = billingStateId;
                    } else {
                        stateShippingSpinner.setSelection(0);
                    }

                    Log.e("billingStateId", billingStateId + "$$");
                    Log.e("shippingStateId", shippingStateId + "$$");


                } else {
                    sameAsBilling = false;
                    edtShippingAddress1.setEnabled(true);
                    edtShippingAddress2.setEnabled(true);
                    edtShippingCity.setEnabled(true);
                    edtShippingPincode.setEnabled(true);
                    stateShippingSpinner.setEnabled(true);
                }
            }
        });

    }

    private int getIndex(List<CountryResponse.DataBean> states, int stateId) {
        int index = 0;
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).getStateID() == stateId) {
                index = i;
            }
        }
        return index;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean validationDone(View v) {
        boolean isValid = true;

        if (edtBillingAddress1.getText().toString().length() == 0) {
            Functions.showToast(this, "Enter Billing Address 1");
            isValid = false;

        } else if (edtBillingAddress2.getText().toString().length() == 0) {
            Functions.showToast(this, "Enter Billing Address 2");
            isValid = false;

        } else if (edtBillingCity.getText().toString().length() == 0) {
            Functions.showToast(this, "Enter Billing City");
            isValid = false;

        } else if (edtBillingPincode.getText().toString().length() == 0) {
            Functions.showToast(this, "Enter Billing Address Pincode");
            isValid = false;

        } else if (edtBillingPincode.getText().toString().length() != 6) {
            Functions.showToast(this, "Enter Billing Address Pincode with exactly 6 digits");
            isValid = false;

        } else if (sameAsBilling) {
            isValid = true;

        } else {
            if (edtShippingAddress1.getText().toString().length() == 0) {
                Functions.showToast(this, "Enter Shipping Address 1");
                isValid = false;

            } else if (edtShippingAddress2.getText().toString().length() == 0) {
                Functions.showToast(this, "Enter Shipping Address 2");
                isValid = false;

            } else if (edtShippingCity.getText().toString().length() == 0) {
                Functions.showToast(this, "Enter Shipping City");
                isValid = false;

            } else if (edtShippingPincode.getText().toString().length() == 0) {
                Functions.showToast(this, "Enter Shipping Address Pincode");
                isValid = false;

            } else if (edtShippingPincode.getText().toString().length() != 6) {
                Functions.showToast(this, "Enter Shipping Address Pincode with exactly 6 digits");
                isValid = false;
            }
        }

        return isValid;
    }

    private void saveAddressDetails() {

        // set billing address
        PlaceOrderRequest.BillingAddressDCBean billingAddressDCBean = new PlaceOrderRequest.BillingAddressDCBean();
        billingAddressDCBean.setAddress1(Functions.getStr(edtBillingAddress1));
        billingAddressDCBean.setAddress2(Functions.getStr(edtBillingAddress2));
        billingAddressDCBean.setBillingAddressID(resBean.getBillingAddressDC().getBillingAddressID());
        billingAddressDCBean.setCity(Functions.getStr(edtBillingCity));
        billingAddressDCBean.setIsUpdated(true);
        billingAddressDCBean.setMobileNo(PrefUtils.getUserProfile(this).getMobileNo());
        billingAddressDCBean.setPinCode(Functions.getStr(edtBillingPincode));

        billingStateId = ((CountryResponse.DataBean)stateBillingSpinner.getSelectedItem()).getStateID();
        billingAddressDCBean.setStateID(billingStateId);
        Log.e("billingAddressDCBean", Functions.jsonString(billingAddressDCBean));

        // set shipping address
        PlaceOrderRequest.ShippingAddressDCBean shippingAddressDCBean = new PlaceOrderRequest.ShippingAddressDCBean();
        shippingAddressDCBean.setAddress1(Functions.getStr(edtShippingAddress1));
        shippingAddressDCBean.setAddress2(Functions.getStr(edtShippingAddress2));
        shippingAddressDCBean.setShippingAddressID(resBean.getShippingAddressDC().getShippingAddressID());
        shippingAddressDCBean.setCity(Functions.getStr(edtShippingCity));
        shippingAddressDCBean.setIsUpdated(true);
        shippingAddressDCBean.setMobileNo(PrefUtils.getUserProfile(this).getMobileNo());
        shippingAddressDCBean.setPinCode(Functions.getStr(edtShippingPincode));

        shippingStateId = ((CountryResponse.DataBean)stateBillingSpinner.getSelectedItem()).getStateID();
        shippingAddressDCBean.setStateID(shippingStateId);
        Log.e("shippingAddressDCBean", Functions.jsonString(shippingAddressDCBean));

        reqBean.setBillingAddressDC(billingAddressDCBean);
        reqBean.setShippingAddressDC(shippingAddressDCBean);
        complexPreferences.putObject("placeOrderReq", reqBean);
        complexPreferences.commit();

        Intent i = new Intent(ShippingDetailsActivity.this, ConfirmOrderActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

        /*addresses.clear();
        addressModels = new ArrayList<>();
        addressModels.clear();
        AddressModel billingAddress = new AddressModel();
        AddressModel shippingAddress = new AddressModel();

        // Set Billing Address Details
        billingAndShippingAddress.setBillingAddress1(edtBillingAddress1.getText().toString().trim());
        billingAndShippingAddress.setBillingAddress2(edtBillingAddress2.getText().toString().trim());
        billingAndShippingAddress.setBillingCity(edtBillingCity.getText().toString().trim());
        billingAndShippingAddress.setBillingPincode(edtBillingPincode.getText().toString().trim());
        billingAndShippingAddress.setBillingState(billingStateId);

        // Save Billing Address object
        billingAddress.setAddress1(edtBillingAddress1.getText().toString().trim());
        billingAddress.setAddress2(edtBillingAddress2.getText().toString().trim());
        billingAddress.setCity(edtBillingCity.getText().toString().trim());
        billingAddress.setState(billingStateName);
        billingAddress.setPincode(edtBillingPincode.getText().toString().trim());
        billingAddress.setShipping("false");
        addressModels.add(billingAddress);

        // Set Shipping Address Details and save object
        if (sameAsBilling) {
            billingAndShippingAddress.setShippingAddress1(edtBillingAddress1.getText().toString().trim());
            billingAndShippingAddress.setShippingAddress2(edtBillingAddress2.getText().toString().trim());
            billingAndShippingAddress.setShippingCity(edtBillingCity.getText().toString().trim());
            billingAndShippingAddress.setShippingPincode(edtBillingPincode.getText().toString().trim());
            billingAndShippingAddress.setShippingState(billingStateId);

            shippingAddress = billingAddress;
            shippingAddress.setShipping("true");
            addressModels.add(shippingAddress);
        } else {
            billingAndShippingAddress.setShippingAddress1(edtShippingAddress1.getText().toString().trim());
            billingAndShippingAddress.setShippingAddress2(edtShippingAddress2.getText().toString().trim());
            billingAndShippingAddress.setShippingCity(edtShippingCity.getText().toString().trim());
            billingAndShippingAddress.setShippingPincode(edtShippingPincode.getText().toString().trim());
            billingAndShippingAddress.setShippingState(shippingStateId);

            shippingAddress.setAddress1(edtShippingAddress1.getText().toString().trim());
            shippingAddress.setAddress2(edtShippingAddress2.getText().toString().trim());
            shippingAddress.setCity(edtShippingCity.getText().toString().trim());
            shippingAddress.setState(shippingStateName);
            shippingAddress.setPincode(edtShippingPincode.getText().toString().trim());
            shippingAddress.setShipping("true");
            addressModels.add(shippingAddress);
        }

        // Save in database
        DatabaseHandler handler = new DatabaseHandler(ShippingDetailsActivity.this);
        handler.saveAddressDetails(addressModels);
        handler.close();*/
    }

    private void loadStateSpinner() {
        DatabaseHandler handler = new DatabaseHandler(ShippingDetailsActivity.this);
        states = handler.getStates();
        // StateSpinnerAdapter adapter = new StateSpinnerAdapter(ShippingDetailsActivity.this, states, R.layout.spinner_layout_transperent, R.layout.spinner_dropview_layout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;

        public CustomTextWatcher(EditText e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            switch (mEditText.getId()) {
                case R.id.edtBillingAddress1:
                    billingAndShippingAddress.setBillingAddress1(s.toString());

                    //set in shipping address too if same as billing
                    if (isSameAsBilling.isChecked()) {
                        billingAndShippingAddress.setShippingAddress1(s.toString());
                    }
                    break;

                case R.id.edtBillingAddress2:
                    billingAndShippingAddress.setBillingAddress2(s.toString());
                    //set in shipping address too if same as billing
                    if (isSameAsBilling.isChecked()) {
                        billingAndShippingAddress.setShippingAddress2(s.toString());
                    }
                    break;

                case R.id.edtBillingCity:
                    billingAndShippingAddress.setBillingCity(s.toString());

                    //set in shipping address too if same as billing
                    if (isSameAsBilling.isChecked()) {
                        billingAndShippingAddress.setShippingCity(s.toString());
                    }
                    break;

                case R.id.edtBillingCountry:
                    billingAndShippingAddress.setBillingCountry(s.toString());

                    //set in shipping address too if same as billing
                    if (isSameAsBilling.isChecked()) {
                        billingAndShippingAddress.setShippingCountry(s.toString());
                    }
                    break;

                case R.id.edtBillingPincode:
                    billingAndShippingAddress.setBillingPincode(s.toString());

                    //set in shipping address too if same as billing
                    if (isSameAsBilling.isChecked()) {
                        billingAndShippingAddress.setShippingPincode(s.toString());
                    }
                    break;

                case R.id.edtShippingAddress1:
                    billingAndShippingAddress.setShippingAddress1(s.toString());
                    break;

                case R.id.edtShippingAddress2:
                    billingAndShippingAddress.setShippingAddress2(s.toString());
                    break;

                case R.id.edtShippingCity:
                    billingAndShippingAddress.setShippingCity(s.toString());
                    break;

                case R.id.edtShippingCountry:
                    billingAndShippingAddress.setShippingCountry(s.toString());
                    break;

                case R.id.edtShippingPincode:
                    billingAndShippingAddress.setShippingPincode(s.toString());
                    break;
            }

        }
    }

    private void sendAddressDetails(final ProgressDialog pDialog) {
        JSONObject billingAddress = null;
        JSONObject shippingAddress = null;
        JSONArray addressJsonArray = new JSONArray();
        JSONObject mainAddress = new JSONObject();

        try {
            billingAddress = new JSONObject();
            billingAddress.put("Address1", billingAndShippingAddress.getBillingAddress1());
            billingAddress.put("Address2", billingAndShippingAddress.getBillingAddress2());
            billingAddress.put("AddressID", 0);
            billingAddress.put("City", billingAndShippingAddress.getBillingCity());
            billingAddress.put("Country", billingAndShippingAddress.getBillingCountry());
            billingAddress.put("IsDefault", true);
            billingAddress.put("IsShipping", false);
            billingAddress.put("MobileNo", currentUserObj.MobileNo);
            billingAddress.put("PinCode", billingAndShippingAddress.getBillingPincode());
            billingAddress.put("StateID", billingAndShippingAddress.getBillingState());
            billingAddress.put("UserID", (PrefUtils.getUserId(ShippingDetailsActivity.this)));

            shippingAddress = new JSONObject();
            shippingAddress.put("Address1", billingAndShippingAddress.getShippingAddress1());
            shippingAddress.put("Address2", billingAndShippingAddress.getShippingAddress2());
            shippingAddress.put("AddressID", 0);
            shippingAddress.put("City", billingAndShippingAddress.getShippingCity());
            shippingAddress.put("Country", billingAndShippingAddress.getShippingCountry());
            shippingAddress.put("IsDefault", true);
            shippingAddress.put("IsShipping", true);
            shippingAddress.put("MobileNo", currentUserObj.MobileNo);
            shippingAddress.put("PinCode", billingAndShippingAddress.getShippingPincode());
            shippingAddress.put("StateID", billingAndShippingAddress.getShippingState());
            shippingAddress.put("UserID", (PrefUtils.getUserId(ShippingDetailsActivity.this)));

            addressJsonArray.put(billingAddress);
            addressJsonArray.put(shippingAddress);

            mainAddress.put("Addresses", addressJsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Functions.logE("SAVE_ADDRESS request", mainAddress.toString());

        new CallWebService(Constants.SAVE_ADDRESS_URL, CallWebService.TYPE_POST, mainAddress) {
            @Override
            public void response(String response) {
                pDialog.dismiss();
                Log.e("SAVE_ADDRESS response", response + "");
                try {
                    Intent i = new Intent(ShippingDetailsActivity.this, ConfirmOrderActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String error) {
                pDialog.dismiss();
                Log.e("error", error);
            }
        }.call();

    }
}
