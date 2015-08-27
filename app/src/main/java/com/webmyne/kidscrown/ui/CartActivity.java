package com.webmyne.kidscrown.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.CustomGridAdapter;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    GridLayout gridLayout;
    ArrayList<ProductCart> products = new ArrayList<>();
    TextView totalPrice, emptyCart;
    LinearLayout linearParent, totalLayout;
    ArrayList<String> values;
    int price, crownProductId;
    SharedPreferences preferences;
    GridView gridView;
    TextView[] text;
    RelativeLayout rLayoutCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        crownProductId = preferences.getInt("crownProductId", 0);

        init();

        fetchCartDetails();
        rLayoutCheckout = (RelativeLayout) findViewById(R.id.rLayoutCheckout);
        rLayoutCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, ShippingDetailsActivity.class);

                startActivity(i);
            }
        });

    }

    private void fetchCartDetails() {
        try {
            DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
            handler.openDataBase();
            products = handler.getCartProduct();
            handler.close();
            if (products.size() == 0) {
                emptyCart.setVisibility(View.VISIBLE);
                totalLayout.setVisibility(View.GONE);
            } else {
                emptyCart.setVisibility(View.GONE);
                totalLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < products.size(); i++) {
            price = price + Integer.parseInt(products.get(i).getProductTotalPrice());

            if (products.get(i).getProductId() == crownProductId) {
                Log.e("View", "GridView");

                text = new TextView[9];
                for(int z=0;z<text.length;z++){
                    text[i]=new TextView(CartActivity.this);
                    text[i].setLayoutParams(new ViewGroup.LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    text[i].setText(String.valueOf(i));
                    text[i].setTextSize(25);
                    text[i].setPadding(50, 25, 10, 25);
                    gridLayout.addView(text[i]);
                }

                CustomGridAdapter adapter = new CustomGridAdapter(CartActivity.this, products);
                gridView.setAdapter(adapter);
            } else {
                Log.e("View", "ItemView");
                ItemCartView itemView = new ItemCartView(CartActivity.this, products.get(i));
                itemView.setOnValueChangeListener(onValueChangeListener);
                linearParent.addView(itemView);
            }
        }
        totalPrice.setText("Rs. " + price);
    }

    ItemCartView.OnValueChangeListener onValueChangeListener = new ItemCartView.OnValueChangeListener() {
        @Override
        public void onChange() {
            try {
                DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
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

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setOrientation(0);
        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(3);

        linearParent = (LinearLayout) findViewById(R.id.linearParent);
        gridView = (GridView) findViewById(R.id.gridView);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        emptyCart = (TextView) findViewById(R.id.emptyCart);
        imgCart.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Your Cart");
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
