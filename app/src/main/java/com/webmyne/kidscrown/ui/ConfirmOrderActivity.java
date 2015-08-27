package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.ToolHelper;
import com.webmyne.kidscrown.model.Address;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ConfirmOrderActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ConfirmOrderActivity.this, "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        userId = currentUserObj.UserID;

        init();

        fetchCartDetails();
        fetchAddress();
    }

    private void fetchCartDetails() {
        try {
            DatabaseHandler handler = new DatabaseHandler(ConfirmOrderActivity.this);
            handler.openDataBase();
            products = handler.getCartProduct();
            handler.close();
            if (products.size() == 0) {
                totalLayout.setVisibility(View.GONE);
            } else {
                totalLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < products.size(); i++) {
            price = price + Integer.parseInt(products.get(i).getProductTotalPrice());
            ItemCartView itemView = new ItemCartView(ConfirmOrderActivity.this, products.get(i));
            itemView.hideControls();
            itemView.setOnValueChangeListener(onValueChangeListener);
            linearParent.addView(itemView);
        }
        totalPrice.setText("Rs. " + price);
    }


    private void fetchAddress() {
        String user = "?UserId=" + userId;
        Log.e("Address URL", Constants.GET_EXISTING_ADDRESS + user);

        pd = ProgressDialog.show(ConfirmOrderActivity.this, "Loading", "Please wait..", true);


        new CallWebService(Constants.GET_EXISTING_ADDRESS + user, CallWebService.TYPE_GET) {
            @Override
            public void response(String response) {
                pd.dismiss();
                Log.e("Response Address", response);
                Type listType = new TypeToken<List<Address>>() {
                }.getType();
                addresses = new GsonBuilder().create().fromJson(response, listType);
                DatabaseHandler handler = new DatabaseHandler(ConfirmOrderActivity.this);
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


    ItemCartView.OnValueChangeListener onValueChangeListener = new ItemCartView.OnValueChangeListener() {
        @Override
        public void onChange() {
            try {
                DatabaseHandler handler = new DatabaseHandler(ConfirmOrderActivity.this);
                handler.openDataBase();
                products = handler.getCartProduct();
                handler.close();
                price = 0;
                for (int k = 0; k < products.size(); k++) {
                    price += Integer.parseInt(products.get(k).getProductTotalPrice());
                }
                totalPrice.setText("Rs. " + price);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void init() {

        linearParent = (LinearLayout) findViewById(R.id.linearParent);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

}
