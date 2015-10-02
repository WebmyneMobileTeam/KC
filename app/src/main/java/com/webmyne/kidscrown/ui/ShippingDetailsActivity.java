package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.StateSpinnerAdapter;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.Address;
import com.webmyne.kidscrown.model.AddressModel;
import com.webmyne.kidscrown.model.BillingAndShippingAddress;
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
    LinearLayout linearParent, totalLayout;
    UserProfile currentUserObj = new UserProfile();
    String userId;
    ProgressDialog pd, pd1;
    Toolbar toolbar;

    SegmentedGroup segmented2;
    RelativeLayout rLContPayment;
    int shippingStateId = 0, billingStateId = 0;
    String shippingStateName, billingStateName;
    BillingAndShippingAddress billingAndShippingAddress = new BillingAndShippingAddress();
    private EditText edtBillingAddress1, edtBillingAddress2, edtBillingCity, edtBillingCountry, edtBillingPincode, edtShippingAddress1, edtShippingAddress2, edtShippingCity, edtShippingCountry, edtShippingPincode;
    private CheckBox isSameAsBilling;
    Spinner edtBillingStateSpinner, edtShipingStateSpinner;
    ArrayList<StateModel> states = new ArrayList<>();
    boolean sameAsBilling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ShippingDetailsActivity.this, "user_pref", 0);
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        userId = currentUserObj.UserID;

        init();

        pd = ProgressDialog.show(ShippingDetailsActivity.this, "Loading", "Please wait..", true);
        fetchAddress(pd);

        edtShipingStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shippingStateId = states.get(position).state_id;
                shippingStateName = states.get(position).state_name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtBillingStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billingStateId = states.get(position).state_id;
                billingStateName = states.get(position).state_name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void fetchAddress(final ProgressDialog progressDialog) {
        String user = "?UserId=" + userId;
        Log.e("Address URL", Constants.GET_EXISTING_ADDRESS + user);

        new CallWebService(Constants.GET_EXISTING_ADDRESS + user, CallWebService.TYPE_GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("Response Address", response);
                Type listType = new TypeToken<List<Address>>() {
                }.getType();
                addresses = new GsonBuilder().create().fromJson(response, listType);

                displayAddress(addresses);

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Log.e("Error", error);

            }
        }.call();
    }

    private void displayAddress(ArrayList<Address> addresses) {
        for (int k = 0; k < addresses.size(); k++) {
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
        }

    }

    private int getIndex(ArrayList<StateModel> states, int stateId) {
        int index = 0;
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).state_id == stateId) {
                index = i;
            }
        }
        return index;
    }


    private void init() {

        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

        edtBillingStateSpinner = (Spinner) findViewById(R.id.edtBillingStateSpinner);
        edtShipingStateSpinner = (Spinner) findViewById(R.id.edtShipingStateSpinner);

        loadStateSpinner();

        linearParent = (LinearLayout) findViewById(R.id.linearParent);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        //totalPrice = (TextView) findViewById(R.id.totalPrice);
        rLContPayment = (RelativeLayout) findViewById(R.id.rLContPayment);

        edtBillingAddress1 = (EditText) findViewById(R.id.edtBillingAddress1);
        edtBillingAddress2 = (EditText) findViewById(R.id.edtBillingAddress2);
        edtBillingCity = (EditText) findViewById(R.id.edtBillingCity);
        edtBillingCountry = (EditText) findViewById(R.id.edtBillingCountry);
        edtBillingPincode = (EditText) findViewById(R.id.edtBillingPincode);

        edtBillingAddress1.addTextChangedListener(new CustomTextWatcher(edtBillingAddress1));
        edtBillingAddress2.addTextChangedListener(new CustomTextWatcher(edtBillingAddress2));
        edtBillingCity.addTextChangedListener(new CustomTextWatcher(edtBillingCity));
        edtBillingCountry.addTextChangedListener(new CustomTextWatcher(edtBillingCountry));
        edtBillingPincode.addTextChangedListener(new CustomTextWatcher(edtBillingPincode));

        edtShippingAddress1 = (EditText) findViewById(R.id.edtShippingAddress1);
        edtShippingAddress2 = (EditText) findViewById(R.id.edtShippingAddress2);
        edtShippingCity = (EditText) findViewById(R.id.edtShippingCity);
        edtShippingCountry = (EditText) findViewById(R.id.edtShippingCountry);
        edtShippingPincode = (EditText) findViewById(R.id.edtShippingPincode);

        edtShippingAddress1.addTextChangedListener(new CustomTextWatcher(edtShippingAddress1));
        edtShippingAddress2.addTextChangedListener(new CustomTextWatcher(edtShippingAddress2));
        edtShippingCity.addTextChangedListener(new CustomTextWatcher(edtShippingCity));
        edtShippingCountry.addTextChangedListener(new CustomTextWatcher(edtShippingCountry));
        edtShippingPincode.addTextChangedListener(new CustomTextWatcher(edtShippingPincode));

        isSameAsBilling = (CheckBox) findViewById(R.id.checkbox);

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
            toolbar.setTitle("Shipping Details");
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

        rLContPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationDone(v)) {
                    saveAddressDetails();

                    pd1 = ProgressDialog.show(ShippingDetailsActivity.this, "Loading", "Please wait..", true);
                    sendAddressDetails(pd1);
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

                    edtShipingStateSpinner.setEnabled(false);
                    if (billingStateId != 0) {
                        edtShipingStateSpinner.setSelection(getIndex(states, billingStateId));
                    } else {
                        edtShipingStateSpinner.setSelection(0);
                    }

                    edtShippingCountry.setEnabled(false);
                    edtShippingCountry.setText(edtBillingCountry.getText().toString().trim());

                    edtShippingPincode.setEnabled(false);
                    edtShippingPincode.setText(edtBillingPincode.getText().toString().trim());

                } else {
                    sameAsBilling = false;
                    edtShippingAddress1.setEnabled(true);
                    edtShippingAddress2.setEnabled(true);
                    edtShippingCity.setEnabled(true);
                    edtShipingStateSpinner.setEnabled(true);
                    edtShippingCountry.setEnabled(true);
                    edtShippingPincode.setEnabled(true);
                }
            }
        });

    }

    private boolean validationDone(View v) {
        boolean isValid = true;

        if (edtBillingAddress1.getText().toString().length() == 0) {
            Functions.snack(v, "Enter Billing Address 1");
            isValid = false;
        } else if (edtBillingAddress2.getText().toString().length() == 0) {
            Functions.snack(v, "Enter Billing Address 2");
            isValid = false;
        } else if (edtBillingCity.getText().toString().length() == 0) {
            Functions.snack(v, "Enter Billing City");
            isValid = false;
        } else if (edtBillingCountry.getText().toString().length() == 0) {
            Functions.snack(v, "Enter Billing Address Country");
            isValid = false;
        } else if (edtBillingPincode.getText().toString().length() != 6) {
            Functions.snack(v, "Enter Billing Address Pincode with exactly 6 digits");
            isValid = false;
        } else if (billingStateId == 0) {
            Functions.snack(v, "Select Billing Address State");
            isValid = false;
        } else if (sameAsBilling) {
            isValid = true;
        } else {
            if (edtShippingAddress1.getText().toString().length() == 0) {
                Functions.snack(v, "Enter Shipping Address 1");
                isValid = false;
            } else if (edtShippingAddress2.getText().toString().length() == 0) {
                Functions.snack(v, "Enter Shipping Address 2");
                isValid = false;
            } else if (edtShippingCity.getText().toString().length() == 0) {
                Functions.snack(v, "Enter Shipping Address City");
                isValid = false;
            } else if (edtShippingCountry.getText().toString().length() == 0) {
                Functions.snack(v, "Enter Shipping Address Country");
                isValid = false;
            } else if (edtShippingPincode.getText().toString().length() != 6) {
                Functions.snack(v, "Enter Shipping Address Pincode with exactly 6 digits");
                isValid = false;
            } else if (shippingStateId == 0) {
                Functions.snack(v, "Select Shipping Address State");
                isValid = false;
            }
        }

       /* if (!isValid) {
            Functions.snack(v, "All shipping and biling details are mandatory and valid.");
        }*/
        return isValid;
    }

    private void saveAddressDetails() {
        addresses.clear();
        addressModels = new ArrayList<>();
        addressModels.clear();
        AddressModel billingAddress = new AddressModel();
        AddressModel shippingAddress = new AddressModel();

        // Set Billing Address Details
        billingAndShippingAddress.setBillingAddress1(edtBillingAddress1.getText().toString().trim());
        billingAndShippingAddress.setBillingAddress2(edtBillingAddress2.getText().toString().trim());
        billingAndShippingAddress.setBillingCity(edtBillingCity.getText().toString().trim());
        billingAndShippingAddress.setBillingCountry(edtBillingCountry.getText().toString().trim());
        billingAndShippingAddress.setBillingPincode(edtBillingPincode.getText().toString().trim());
        billingAndShippingAddress.setBillingState(billingStateId);

        // Save Billing Address object
        billingAddress.setAddress1(edtBillingAddress1.getText().toString().trim());
        billingAddress.setAddress2(edtBillingAddress2.getText().toString().trim());
        billingAddress.setCity(edtBillingCity.getText().toString().trim());
        billingAddress.setCountry(edtBillingCountry.getText().toString().trim());
        billingAddress.setState(billingStateName);
        billingAddress.setPincode(edtBillingPincode.getText().toString().trim());
        billingAddress.setShipping("false");
        addressModels.add(billingAddress);

        // Set Shipping Address Details and save object
        if (sameAsBilling) {
            billingAndShippingAddress.setShippingAddress1(edtBillingAddress1.getText().toString().trim());
            billingAndShippingAddress.setShippingAddress2(edtBillingAddress2.getText().toString().trim());
            billingAndShippingAddress.setShippingCity(edtBillingCity.getText().toString().trim());
            billingAndShippingAddress.setShippingCountry(edtBillingCountry.getText().toString().trim());
            billingAndShippingAddress.setShippingPincode(edtBillingPincode.getText().toString().trim());
            billingAndShippingAddress.setShippingState(billingStateId);

            shippingAddress = billingAddress;
            shippingAddress.setShipping("true");
            addressModels.add(shippingAddress);
        } else {
            billingAndShippingAddress.setShippingAddress1(edtShippingAddress1.getText().toString().trim());
            billingAndShippingAddress.setShippingAddress2(edtShippingAddress2.getText().toString().trim());
            billingAndShippingAddress.setShippingCity(edtShippingCity.getText().toString().trim());
            billingAndShippingAddress.setShippingCountry(edtShippingCountry.getText().toString().trim());
            billingAndShippingAddress.setShippingPincode(edtShippingPincode.getText().toString().trim());
            billingAndShippingAddress.setShippingState(shippingStateId);

            shippingAddress.setAddress1(edtShippingAddress1.getText().toString().trim());
            shippingAddress.setAddress2(edtShippingAddress2.getText().toString().trim());
            shippingAddress.setCity(edtShippingCity.getText().toString().trim());
            shippingAddress.setCountry(edtShippingCountry.getText().toString().trim());
            shippingAddress.setState(shippingStateName);
            shippingAddress.setPincode(edtShippingPincode.getText().toString().trim());
            shippingAddress.setShipping("true");
            addressModels.add(shippingAddress);
        }

        // Save in database
        DatabaseHandler handler = new DatabaseHandler(ShippingDetailsActivity.this);
        handler.saveAddressDetails(addressModels);
        handler.close();
        displayAddress();
    }

    private void loadStateSpinner() {
        DatabaseHandler handler = new DatabaseHandler(ShippingDetailsActivity.this);
        states = handler.getStates();
        StateSpinnerAdapter adapter = new StateSpinnerAdapter(ShippingDetailsActivity.this, states, R.layout.spinner_layout_transperent, R.layout.spinner_dropview_layout);
        edtShipingStateSpinner.setAdapter(adapter);
        edtBillingStateSpinner.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    private void displayAddress() {
        try {
            DatabaseHandler handler = new DatabaseHandler(ShippingDetailsActivity.this);
            handler.openDataBase();
            Cursor cursor = handler.getAddressCursor();
            handler.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

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
            billingAddress.put("UserID", Integer.parseInt(userId));

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
            shippingAddress.put("UserID", Integer.parseInt(userId));

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
