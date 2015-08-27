package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.model.Address;
import com.webmyne.kidscrown.model.BillingAndShippingAddress;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ShippingDetailsActivity extends AppCompatActivity  {

    //Toolbar toolbar;
    ArrayList<ProductCart> products = new ArrayList<>();
    ArrayList<Address> addresses = new ArrayList<>();
    TextView totalPrice, emptyCart;
    LinearLayout linearParent, totalLayout;
    ArrayList<String> values;
    int price;
    String userId;
    ProgressDialog pd;
    Toolbar toolbar;

    SegmentedGroup segmented2;
    RelativeLayout rLContPayment;
    BillingAndShippingAddress billingAndShippingAddress = new BillingAndShippingAddress();
    private EditText edtBillingAddress1, edtBillingAddress2, edtBillingCity, edtBillingState, edtBillingCountry, edtBillingPincode, edtShippingAddress1, edtShippingAddress2, edtShippingCity, edtShippingState, edtShippingCountry, edtShippingPincode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ShippingDetailsActivity.this, "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        userId = currentUserObj.UserID;

        init();

        fetchAddress();
        displayAddress();
    }


    private void fetchAddress() {
        String user = "?UserId=" + userId;
        Log.e("Address URL", Constants.GET_EXISTING_ADDRESS + user);

        pd = ProgressDialog.show(ShippingDetailsActivity.this, "Loading", "Please wait..", true);


        new CallWebService(Constants.GET_EXISTING_ADDRESS + user, CallWebService.TYPE_GET) {
            @Override
            public void response(String response) {
                pd.dismiss();
                Log.e("Response Address", response);
                Type listType = new TypeToken<List<Address>>() {
                }.getType();
                addresses = new GsonBuilder().create().fromJson(response, listType);
                DatabaseHandler handler = new DatabaseHandler(ShippingDetailsActivity.this);
                handler.saveAddress(addresses);
                handler.close();

            }

            @Override
            public void error(String error) {
                pd.dismiss();
                Log.e("Error", error);

            }
        }.call();
    }


    private void init() {

        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

        linearParent = (LinearLayout) findViewById(R.id.linearParent);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        //totalPrice = (TextView) findViewById(R.id.totalPrice);
        rLContPayment = (RelativeLayout) findViewById(R.id.rLContPayment);

        edtBillingAddress1 = (EditText) findViewById(R.id.edtBillingAddress1);
        edtBillingAddress2 = (EditText) findViewById(R.id.edtBillingAddress2);
        edtBillingCity = (EditText) findViewById(R.id.edtBillingCity);
        edtBillingState = (EditText) findViewById(R.id.edtBillingState);
        edtBillingCountry = (EditText) findViewById(R.id.edtBillingCountry);
        edtBillingPincode = (EditText) findViewById(R.id.edtBillingPincode);

        edtBillingAddress1.addTextChangedListener(new CustomTextWatcher(edtBillingAddress1));
        edtBillingAddress2.addTextChangedListener(new CustomTextWatcher(edtBillingAddress2));
        edtBillingCity.addTextChangedListener(new CustomTextWatcher(edtBillingCity));
        edtBillingState.addTextChangedListener(new CustomTextWatcher(edtBillingState));
        edtBillingCountry.addTextChangedListener(new CustomTextWatcher(edtBillingCountry));
        edtBillingPincode.addTextChangedListener(new CustomTextWatcher(edtBillingPincode));


        edtShippingAddress1 = (EditText) findViewById(R.id.edtShippingAddress1);
        edtShippingAddress2 = (EditText) findViewById(R.id.edtShippingAddress2);
        edtShippingCity = (EditText) findViewById(R.id.edtShippingCity);
        edtShippingState = (EditText) findViewById(R.id.edtShippingState);
        edtShippingCountry = (EditText) findViewById(R.id.edtShippingCountry);
        edtShippingPincode = (EditText) findViewById(R.id.edtShippingPincode);

        edtShippingAddress1.addTextChangedListener(new CustomTextWatcher(edtShippingAddress1));
        edtShippingAddress2.addTextChangedListener(new CustomTextWatcher(edtShippingAddress2));
        edtShippingCity.addTextChangedListener(new CustomTextWatcher(edtShippingCity));
        edtShippingState.addTextChangedListener(new CustomTextWatcher(edtShippingState));
        edtShippingCountry.addTextChangedListener(new CustomTextWatcher(edtShippingCountry));
        edtShippingPincode.addTextChangedListener(new CustomTextWatcher(edtShippingPincode));


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
                Log.e("Addressses", billingAndShippingAddress.toString());

                saveAddresses();


                Intent i = new Intent(ShippingDetailsActivity.this, ConfirmOrderActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    private void displayAddress() {
        String[] columns = new String[]{
                "address_1",
                "address_2",
        };

        int[] to = new int[]{
                R.id.txtAddress1,
                R.id.txtAddress2
        };

        try {
            DatabaseHandler handler = new DatabaseHandler(ShippingDetailsActivity.this);
            handler.openDataBase();
            Cursor cursor = handler.getAddressCursor();
            handler.close();
            /*adapter = new AddressAdapter(getActivity(), R.layout.address_row, cursor, columns, to, 0);
            listAddress.setAdapter(adapter);*/

            if (cursor != null) {

                do {
                    // check if address is billing or not
                    if (cursor.getString(cursor.getColumnIndexOrThrow("is_shipping")).equals("false")) {
                        billingAndShippingAddress.setBillingAddress1(cursor.getString(cursor.getColumnIndexOrThrow("address_1")));
                        billingAndShippingAddress.setBillingAddress2(cursor.getString(cursor.getColumnIndexOrThrow("address_2")));
                        billingAndShippingAddress.setBillingCity(cursor.getString(cursor.getColumnIndexOrThrow("city_name")));
                        billingAndShippingAddress.setBillingState(cursor.getString(cursor.getColumnIndexOrThrow("state_name")));
                        billingAndShippingAddress.setBillingCountry(cursor.getString(cursor.getColumnIndexOrThrow("country_name")));
                        billingAndShippingAddress.setBillingPincode(cursor.getString(cursor.getColumnIndexOrThrow("pincode")));

                        edtBillingAddress1.setText(cursor.getString(cursor.getColumnIndexOrThrow("address_1")));
                        edtBillingAddress2.setText(cursor.getString(cursor.getColumnIndexOrThrow("address_2")));
                        edtBillingCity.setText(cursor.getString(cursor.getColumnIndexOrThrow("city_name")));
                        edtBillingCountry.setText(cursor.getString(cursor.getColumnIndexOrThrow("country_name")));
                        edtBillingState.setText(cursor.getString(cursor.getColumnIndexOrThrow("state_name")));
                        edtBillingPincode.setText(cursor.getString(cursor.getColumnIndexOrThrow("pincode")));

                    } else{
                        billingAndShippingAddress.setShippingAddress1(cursor.getString(cursor.getColumnIndexOrThrow("address_1")));
                        billingAndShippingAddress.setShippingAddress2(cursor.getString(cursor.getColumnIndexOrThrow("address_2")));
                        billingAndShippingAddress.setShippingCity(cursor.getString(cursor.getColumnIndexOrThrow("city_name")));
                        billingAndShippingAddress.setShippingState(cursor.getString(cursor.getColumnIndexOrThrow("state_name")));
                        billingAndShippingAddress.setShippingCountry(cursor.getString(cursor.getColumnIndexOrThrow("country_name")));
                        billingAndShippingAddress.setShippingPincode(cursor.getString(cursor.getColumnIndexOrThrow("pincode")));

                        edtShippingAddress1.setText(cursor.getString(cursor.getColumnIndexOrThrow("address_1")));
                        edtShippingAddress2.setText(cursor.getString(cursor.getColumnIndexOrThrow("address_2")));
                        edtShippingCity.setText(cursor.getString(cursor.getColumnIndexOrThrow("city_name")));
                        edtShippingCountry.setText(cursor.getString(cursor.getColumnIndexOrThrow("country_name")));
                        edtShippingState.setText(cursor.getString(cursor.getColumnIndexOrThrow("state_name")));
                        edtShippingPincode.setText(cursor.getString(cursor.getColumnIndexOrThrow("pincode")));
                    }
                } while (cursor.moveToNext());
            } else {

            }


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
                    break;
                case R.id.edtBillingAddress2:
                    billingAndShippingAddress.setBillingAddress2(s.toString());
                    break;
                case R.id.edtBillingCity:
                    billingAndShippingAddress.setBillingCity(s.toString());
                    break;
                case R.id.edtBillingCountry:
                    billingAndShippingAddress.setBillingCountry(s.toString());
                    break;
                case R.id.edtBillingState:
                    billingAndShippingAddress.setBillingState(s.toString());
                    break;
                case R.id.edtBillingPincode:
                    billingAndShippingAddress.setBillingPincode(s.toString());
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
                case R.id.edtShippingState:
                    billingAndShippingAddress.setShippingState(s.toString());
                    break;
                case R.id.edtShippingPincode:
                    billingAndShippingAddress.setShippingPincode(s.toString());
                    break;
            }

        }
    }

    private void saveAddresses(){
        List<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> billingAddress = null;
        JSONObject shippingAddress = null;
        JSONArray addressArray = new JSONArray();

        try {
            billingAddress = new HashMap<>();
            billingAddress.put("Address1", billingAndShippingAddress.getBillingAddress1());
            billingAddress.put("Address2", billingAndShippingAddress.getBillingAddress2());
            billingAddress.put("AddressID", "");
            billingAddress.put("CityID", "");
            billingAddress.put("CountryID", "");
            billingAddress.put("IsDefault", "true");
            billingAddress.put("IsShipping", "false");
            billingAddress.put("MobileNo", "");
            billingAddress.put("PinCode", billingAndShippingAddress.getBillingPincode());
            billingAddress.put("StateID", "");
            billingAddress.put("UserID", userId);

            list.add(billingAddress);

           /* shippingAddress = new JSONObject();
            shippingAddress.put("Address1", billingAndShippingAddress.getShippingAddress1());
            shippingAddress.put("Address2", billingAndShippingAddress.getShippingAddress2());
            shippingAddress.put("AddressID", "");
            shippingAddress.put("CityID", "");
            shippingAddress.put("CountryID", "");
            shippingAddress.put("IsDefault", true);
            shippingAddress.put("IsShipping", false);
            shippingAddress.put("MobileNo", "");
            shippingAddress.put("PinCode", billingAndShippingAddress.getShippingPincode());
            shippingAddress.put("StateID", "");
            shippingAddress.put("UserID", userId);*/

            addressArray.put(billingAddress);
            addressArray.put(shippingAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }

        pd = ProgressDialog.show(ShippingDetailsActivity.this, "Loading", "Please wait..", true);
        Functions.logE("SAVE_ADDRESS request", addressArray.toString());

        JsonObjectRequest req = new JsonObjectRequest(Constants.SAVE_ADDRESS_URL, new JSONObject(billingAddress),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                pd.dismiss();
            }
        });

        MyApplication.getInstance().getRequestQueue().add(req);


       /* new CallWebService(Constants.SAVE_ADDRESS_URL, CallWebService.TYPE_POST, billingAddress) {
            @Override
            public void response(String response) {
                pd.dismiss();
                JSONArray data;
                Log.e("register response", response + "");
                try {
                    data = new JSONArray(response);
                    JSONObject description = data.getJSONObject(0);

                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String error) {
                pd.dismiss();
                Log.e("error", error);
            }
        }.call();*/

    }

    public class PostJsonArrayRequest extends JsonRequest<JSONArray> {

        /**
         * Creates a new request.
         * @param url URL to fetch the JSON from
         * @param listener Listener to receive the JSON response
         * @param errorListener Error listener, or null to ignore errors.
         */
        public PostJsonArrayRequest(int method, String url, JSONObject jsonRequest,
                                Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(),
                    listener, errorListener);

        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("name", "value");
            return params;
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString =
                        new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success(new JSONArray(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

}
