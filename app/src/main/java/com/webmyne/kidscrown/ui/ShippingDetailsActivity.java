package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
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

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.StateSpinnerAdapter;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.CountryResponse;
import com.webmyne.kidscrown.model.PlaceOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderResponse;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ShippingDetailsActivity extends AppCompatActivity {

    LinearLayout linearParent;
    Toolbar toolbar;

    SegmentedGroup segmented2;
    int shippingStateId = 0, billingStateId = 0;
    private EditText edtBillingAddress1, edtBillingAddress2, edtBillingCity, edtBillingPincode, edtShippingAddress1, edtShippingAddress2, edtShippingCity, edtShippingPincode;
    private AppCompatCheckBox isSameAsBilling;
    boolean sameAsBilling = false;

    PlaceOrderResponse.DataBean resBean;
    PlaceOrderRequest reqBean;
    private Button btnConfirm;
    private TextView txtCustomTitle;
    private RelativeLayout shippingLayout;
    private ComplexPreferences complexPreferences;
    private AppCompatSpinner stateShippingSpinner, stateBillingSpinner;
    private CountryResponse countryResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);

        complexPreferences = ComplexPreferences.getComplexPreferences(ShippingDetailsActivity.this, Constants.PREF_NAME, 0);
        resBean = complexPreferences.getObject("placeOrderRes", PlaceOrderResponse.DataBean.class);
        reqBean = complexPreferences.getObject("placeOrderReq", PlaceOrderRequest.class);
        countryResponse = complexPreferences.getObject("state", CountryResponse.class);

        init();

        displayAddress();

    }

    private void displayAddress() {

        edtShippingAddress1.setText(resBean.getShippingAddressDC().getAddress1());
        edtShippingAddress2.setText(resBean.getShippingAddressDC().getAddress2());
        edtShippingCity.setText(resBean.getShippingAddressDC().getCity());
        edtShippingPincode.setText(resBean.getShippingAddressDC().getPinCode());

        if (resBean.getShippingAddressDC().getStateID() != 0) {
            stateShippingSpinner.setSelection(getIndex(countryResponse.getData(), resBean.getShippingAddressDC().getStateID()));
            shippingStateId = resBean.getShippingAddressDC().getStateID();
        }

        if (resBean.getBillingAddressDC().getStateID() != 0) {
            stateBillingSpinner.setSelection(getIndex(countryResponse.getData(), resBean.getBillingAddressDC().getStateID()));
            billingStateId = resBean.getBillingAddressDC().getStateID();
        }

        edtBillingAddress1.setText(resBean.getBillingAddressDC().getAddress1());
        edtBillingAddress2.setText(resBean.getBillingAddressDC().getAddress2());
        edtBillingCity.setText(resBean.getBillingAddressDC().getCity());
        edtBillingPincode.setText(resBean.getBillingAddressDC().getPinCode());

    }

    private void init() {

        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

        shippingLayout = (RelativeLayout) findViewById(R.id.shippingLayout);
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

        edtShippingAddress1 = (EditText) findViewById(R.id.edtShippingAddress1);
        edtShippingAddress2 = (EditText) findViewById(R.id.edtShippingAddress2);
        edtShippingCity = (EditText) findViewById(R.id.edtShippingCity);
        edtShippingPincode = (EditText) findViewById(R.id.edtShippingPincode);

        isSameAsBilling = (AppCompatCheckBox) findViewById(R.id.checkbox);

        segmented2 = (SegmentedGroup) findViewById(R.id.segmented2);
        segmented2.setTintColor(ContextCompat.getColor(this, R.color.primaryColor));

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
                Functions.hideKeyPad(ShippingDetailsActivity.this, v);

                if (!Functions.isConnected(ShippingDetailsActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }

                if (validationDone()) {
                    saveAddressDetails();
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

    private boolean validationDone() {
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

        } else if (edtBillingPincode.getText().toString().length() != getResources().getInteger(R.integer.pincode)) {
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

            } else if (edtShippingPincode.getText().toString().length() != getResources().getInteger(R.integer.pincode)) {
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

        billingStateId = ((CountryResponse.DataBean) stateBillingSpinner.getSelectedItem()).getStateID();
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

        shippingStateId = ((CountryResponse.DataBean) stateShippingSpinner.getSelectedItem()).getStateID();
        shippingAddressDCBean.setStateID(shippingStateId);
        Log.e("shippingAddressDCBean", Functions.jsonString(shippingAddressDCBean));

        reqBean.setBillingAddressDC(billingAddressDCBean);
        reqBean.setShippingAddressDC(shippingAddressDCBean);
        complexPreferences.putObject("placeOrderReq", reqBean);
        complexPreferences.commit();

        Intent i = new Intent(ShippingDetailsActivity.this, ConfirmOrderActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
