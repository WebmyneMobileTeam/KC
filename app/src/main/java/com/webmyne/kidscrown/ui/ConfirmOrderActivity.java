package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.OrderListAdapter;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.AddressModel;
import com.webmyne.kidscrown.model.OrderModel;
import com.webmyne.kidscrown.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ConfirmOrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<OrderModel> orders = new ArrayList<>();
    TextView totalPrice, txtBilling, txtShipping, subtotalPrice, txtSaved, txtSavedPrice;
    LinearLayout totalLayout, offerLayout;
    int price, finalPrice;
    ListView orderListview;
    OrderListAdapter adapter;
    ArrayList<AddressModel> addressModels;
    RelativeLayout continueLayout;
    String randomOrderId, userId;
    String dateTime;
    String shippingAddress;
    DatabaseHandler handler;
    int crownProductId;
    ProgressDialog pd1;
    SharedPreferences preferences;
    boolean isOffer;
    float percentage;
    float savedPrice;

    private RelativeLayout chargeLayout;
    private TextView txtCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        init();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ConfirmOrderActivity.this, "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        userId = currentUserObj.UserID;

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        crownProductId = preferences.getInt("crownProductId", 0);
        isOffer = preferences.getBoolean("offer", false);

        if (isOffer) {
            percentage = preferences.getFloat("percentage", 0);
            offerLayout.setVisibility(View.VISIBLE);
            txtSaved.setText("You saved as per " + percentage + "%");
        } else {
            offerLayout.setVisibility(View.GONE);
        }

        // Generate Random Order Id
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        randomOrderId = sb.toString();

        fetchCartDetails();
    }

    private void fetchCartDetails() {

        // Fetch Address Details
        addressModels = new ArrayList<>();
        addressModels.clear();
        try {
            DatabaseHandler handler = new DatabaseHandler(ConfirmOrderActivity.this);
            handler.openDataBase();
            addressModels = handler.getAddressDetails();
            handler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (addressModels.get(0).getShipping().equals("true")) {
            txtShipping.setText(addressModels.get(0).getAddress1() + ", " + addressModels.get(0).getAddress2() + ",\n" + addressModels.get(0).getCity() + " - " + addressModels.get(0).getPincode() + "\n" + addressModels.get(0).getState() + ", " + addressModels.get(0).getCountry());
            txtBilling.setText(addressModels.get(1).getAddress1() + ", " + addressModels.get(1).getAddress2() + ",\n" + addressModels.get(1).getCity() + " - " + addressModels.get(1).getPincode() + "\n" + addressModels.get(1).getState() + ", " + addressModels.get(1).getCountry());
        } else {
            txtBilling.setText(addressModels.get(0).getAddress1() + ", " + addressModels.get(0).getAddress2() + ",\n" + addressModels.get(0).getCity() + " - " + addressModels.get(0).getPincode() + "\n" + addressModels.get(0).getState() + ", " + addressModels.get(0).getCountry());
            txtShipping.setText(addressModels.get(1).getAddress1() + ", " + addressModels.get(1).getAddress2() + ",\n" + addressModels.get(1).getCity() + " - " + addressModels.get(1).getPincode() + "\n" + addressModels.get(1).getState() + ", " + addressModels.get(1).getCountry());
        }

        // Total Products from Cart
        try {
            DatabaseHandler handler = new DatabaseHandler(ConfirmOrderActivity.this);
            handler.openDataBase();
            orders = handler.getProducts();
            handler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < orders.size(); i++) {
            int p = orders.get(i).getProductQty() * Integer.parseInt(orders.get(i).getProductUnitPrice());
            price += p;
        }
        adapter = new OrderListAdapter(ConfirmOrderActivity.this, orders);
        orderListview.setAdapter(adapter);

        if (isOffer) {
            subtotalPrice.setText(getString(R.string.Rs) + " " + price);
            savedPrice = ((price * percentage) / 100);
            txtSavedPrice.setText(getString(R.string.Rs) + " " + savedPrice);
            finalPrice = price - (int) savedPrice;

            //  totalPrice.setText(getString(R.string.Rs) + " " + (price - (int) savedPrice));
        } else {
            finalPrice = price;

            // totalPrice.setText(getString(R.string.Rs) + " " + price);
        }

        int shippingCost = 0;

        if (finalPrice < 3000) {
            shippingCost = 100;
            chargeLayout.setVisibility(View.VISIBLE);
        } else {
            shippingCost = 0;
            chargeLayout.setVisibility(View.GONE);
        }

        totalPrice.setText(getString(R.string.Rs) + " " + (finalPrice + shippingCost));
    }

    private void init() {
        offerLayout = (LinearLayout) findViewById(R.id.offerLayout);
        txtSaved = (TextView) findViewById(R.id.txtSaved);
        txtSavedPrice = (TextView) findViewById(R.id.txtSavedPrice);
        subtotalPrice = (TextView) findViewById(R.id.subtotalPrice);
        chargeLayout = (RelativeLayout) findViewById(R.id.chargeLayout);
        txtCharge = (TextView) findViewById(R.id.txtCharge);

        txtCharge.setText(getString(R.string.Rs) + " 100");

        continueLayout = (RelativeLayout) findViewById(R.id.continueLayout);
        orderListview = (ListView) findViewById(R.id.orderListview);
        txtBilling = (TextView) findViewById(R.id.txtBilling);
        txtShipping = (TextView) findViewById(R.id.txtShipping);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        imgCart.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Confirm Order");
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

        continueLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Current date-time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy, EEE");
                dateTime = df.format(c.getTime());

                try {
                    handler = new DatabaseHandler(ConfirmOrderActivity.this);
                    handler.openDataBase();
                    shippingAddress = handler.getShippingAddress();
                    handler.addOrderItem(orders, randomOrderId, dateTime);

                    handler.deleteCart();

                    handler.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pd1 = ProgressDialog.show(ConfirmOrderActivity.this, "Loading", "Please wait..", true);
                createAnotherOrder(orders);
            }
        });
    }

    private void createAnotherOrder(final ArrayList<OrderModel> orders) {

        new AsyncTask<Void, Void, Void>() {

            JSONObject mainObect = new JSONObject();
            JSONObject kitObject = new JSONObject();
            JSONObject crownMainObject = new JSONObject();
            JSONObject crownSubObject = new JSONObject();

            int grandTotal = 0, crownTotal = 0, crownQty = 0, crownSpecificId = 0;
            final JSONArray ordersArray = new JSONArray();
            final JSONArray crownArray = new JSONArray();

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    handler = new DatabaseHandler(ConfirmOrderActivity.this);
                    handler.openDataBase();

                    for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getProductId() == crownProductId) {
                            crownQty += orders.get(i).getProductQty();
                            crownMainObject = new JSONObject();
                            crownMainObject.put("product_id", orders.get(i).getProductId());
                            crownMainObject.put("price_id", orders.get(i).getPriceId());

                            crownSubObject = new JSONObject();
                            crownSubObject.put("product_name", orders.get(i).getProductName());
                            crownSubObject.put("qty", orders.get(i).getProductQty());

                            try {
                                crownSpecificId = handler.getSpecificId(orders.get(i).getProductName());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            crownSubObject.put("crown_specific_id", crownSpecificId);
                            crownSubObject.put("unit_price", orders.get(i).getProductUnitPrice());
                            crownSubObject.put("total_price", orders.get(i).getProductTotalPrice());

                            int crownTotalPrice = orders.get(i).getProductQty() * Integer.parseInt(orders.get(i).getProductUnitPrice());

                            grandTotal = grandTotal + crownTotalPrice;
                            crownTotal = crownTotal + crownTotalPrice;
                            crownArray.put(crownSubObject);

                        } else {
                            kitObject = new JSONObject();
                            kitObject.put("product_id", orders.get(i).getProductId());
                            kitObject.put("product_name", orders.get(i).getProductName());
                            kitObject.put("qty", orders.get(i).getProductQty());
                            kitObject.put("price_id", orders.get(i).getPriceId());
                            kitObject.put("unit_price", orders.get(i).getProductUnitPrice());
                            kitObject.put("total_price", orders.get(i).getProductTotalPrice());
                            grandTotal = grandTotal + Integer.parseInt(orders.get(i).getProductTotalPrice());
                            ordersArray.put(kitObject);
                        }
                    }

                    handler.close();
                    if (crownQty != 0) {
                        crownMainObject.put("crownQty", crownQty);
                        crownMainObject.put("crownTotal", crownTotal);
                        crownMainObject.put("Crowns", crownArray);
                        ordersArray.put(crownMainObject);
                    }

                    // Main JSON Object
                    mainObect = new JSONObject();
                    mainObect.put("date", dateTime);
                    mainObect.put("user_id", userId);

                    if (grandTotal < 3000) {
                        grandTotal += 100;
                        mainObect.put("shipping_cost", 100);
                        mainObect.put("DiscountPercent", savedPrice);
                        mainObect.put("grand_total", grandTotal - savedPrice);

                    } else {
                        mainObect.put("shipping_cost", 0);
                        mainObect.put("DiscountPercent", savedPrice);
                        mainObect.put("grand_total", grandTotal - savedPrice);
                    }

                    mainObect.put("shipping_address", shippingAddress);
                    mainObect.put("OrdersArray", ordersArray);
                    mainObect.put("MobileOS", "A");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Log.e("API", Constants.PLACE_ORDER);
                Log.e("order_request", mainObect.toString());

                new CallWebService(Constants.PLACE_ORDER, CallWebService.TYPE_POST, mainObect) {
                    @Override
                    public void response(String response) {
                        pd1.dismiss();
                        Log.e("order_response", response);
                        try {
                            Functions.fireIntent(ConfirmOrderActivity.this, PaymentActivity.class);
                            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                        } catch (Exception e) {
                            pd1.dismiss();
                            Log.e("error", e.getMessage());
                        }
                    }

                    @Override
                    public void error(String error) {
                        pd1.dismiss();
                        Log.e("error", error);
                    }
                }.call();
            }
        }.execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
