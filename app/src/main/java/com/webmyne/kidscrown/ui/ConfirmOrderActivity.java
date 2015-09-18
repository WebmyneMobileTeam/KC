package com.webmyne.kidscrown.ui;

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
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.AddressModel;
import com.webmyne.kidscrown.model.OrderModel;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ConfirmOrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<OrderModel> orders = new ArrayList<>();
    TextView totalPrice, txtBilling, txtShipping;
    LinearLayout totalLayout;
    int price;
    ListView orderListview;
    OrderListAdapter adapter;
    ArrayList<AddressModel> addressModels;
    RelativeLayout continueLayout;
    String randomOrderId;
    String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        init();

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
            price += Integer.parseInt(orders.get(i).getProductTotalPrice());
        }
        adapter = new OrderListAdapter(ConfirmOrderActivity.this, orders);
        orderListview.setAdapter(adapter);

        totalPrice.setText(getResources().getString(R.string.Rs) + " " + price);
    }

    private void init() {
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
                    DatabaseHandler handler = new DatabaseHandler(ConfirmOrderActivity.this);
                    handler.openDataBase();
                    handler.addOrderItem(orders, randomOrderId, dateTime);

                    handler.deleteCart();

                    handler.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Functions.fireIntent(ConfirmOrderActivity.this, PaymentActivity.class);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
