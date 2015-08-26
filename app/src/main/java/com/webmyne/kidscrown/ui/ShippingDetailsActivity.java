package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.model.Address;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ShippingDetailsActivity extends AppCompatActivity {

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

        linearParent = (LinearLayout) findViewById(R.id.linearParent);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        //totalPrice = (TextView) findViewById(R.id.totalPrice);
        rLContPayment = (RelativeLayout) findViewById(R.id.rLContPayment);

        segmented2 = (SegmentedGroup) findViewById(R.id.segmented2);
        segmented2.setTintColor( Color.parseColor("#F2BB1A"));

        segmented2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                FrameLayout frame = (FrameLayout)findViewById(R.id.frame);

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

}
