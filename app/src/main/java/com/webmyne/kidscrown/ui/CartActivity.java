package com.webmyne.kidscrown.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.CustomGridAdapter;
import com.webmyne.kidscrown.adapters.RefillOrderAdapterAnother;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.ui.widgets.CrownCartView;
import com.webmyne.kidscrown.ui.widgets.CrownQuadrantAnother;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import java.sql.SQLException;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    GridLayout gridLayout;
    ArrayList<ProductCart> products = new ArrayList<>();
    ArrayList<ProductCart> crowns = new ArrayList<>();
    TextView totalPrice, emptyCart;
    LinearLayout linearParent, totalLayout;
    int price, crownProductId;
    SharedPreferences preferences;
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
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

    }

    private void fetchCartDetails() {
        price = 0;
        gridLayout.removeAllViews();
        gridLayout.invalidate();
        linearParent.removeAllViews();
        linearParent.invalidate();
        try {
            DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
            handler.openDataBase();
            products = handler.getCartProduct(crownProductId);
            crowns = handler.getCrownCartProduct(crownProductId);
            handler.close();
            if (products.size() == 0 && crowns.size() == 0) {
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
            ItemCartView itemView = new ItemCartView(CartActivity.this, products.get(i));
            itemView.setOnValueChangeListener(onValueChangeListener);
            itemView.setOnRemoveProductListener(onRemoveProductListener);
            linearParent.addView(itemView);
        }

        for (int i = 0; i < crowns.size(); i++) {
            price = price + Integer.parseInt(crowns.get(i).getProductTotalPrice());
            CrownCartView crownView = new CrownCartView(CartActivity.this, crowns.get(i));
            crownView.setOnRemoveCrownListener(onRemoveCrownListener);
            gridLayout.addView(crownView);

        }

        totalPrice.setText("Rs. " + price);
    }

    ItemCartView.OnValueChangeListener onValueChangeListener = new ItemCartView.OnValueChangeListener() {
        @Override
        public void onChange() {
            try {
                DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
                handler.openDataBase();
                products = handler.getCartProduct(crownProductId);
                crowns = handler.getCrownCartProduct(crownProductId);
                handler.close();
                price = 0;
                for (int k = 0; k < products.size(); k++) {
                    price += Integer.parseInt(products.get(k).getProductTotalPrice());
                }
                for (int k = 0; k < crowns.size(); k++) {
                    price += Integer.parseInt(crowns.get(k).getProductTotalPrice());
                }
                totalPrice.setText("Rs. " + price);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    ItemCartView.onRemoveProductListener onRemoveProductListener = new ItemCartView.onRemoveProductListener() {
        @Override
        public void removeProduct(int productId) {
            try {
                DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
                handler.openDataBase();
                handler.deleteCartProduct(productId);
                handler.close();
                refreshActivity();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    CrownCartView.onRemoveCrownListener onRemoveCrownListener = new CrownCartView.onRemoveCrownListener() {
        @Override
        public void removeCrown(String productName) {
            try {
                DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
                handler.openDataBase();
                handler.deleteCrownProduct(productName);
                handler.close();

                refreshActivity();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    private void init() {

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(2);

       /* String screenType = getResources().getString(R.string.screen_type);
        if (screenType.equals("phone")) {
            gridLayout.setColumnCount(2);
        } else if (screenType.equals("7-inch")) {
            gridLayout.setColumnCount(2);
        } else {
            gridLayout.setColumnCount(3);
        }*/

        linearParent = (LinearLayout) findViewById(R.id.linearParent);
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
                setResult(100, getIntent());
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        emptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Clicked", Snackbar.LENGTH_SHORT).show();
                Intent a = new Intent(CartActivity.this, MyDrawerActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        setResult(100, getIntent());
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    private void refreshActivity() {
        fetchCartDetails();
    }
}
