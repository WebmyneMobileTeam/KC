package com.webmyne.kidscrown.ui;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.ProductAdapter;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.ui.widgets.ComboSeekBar;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<ProductCart> products = new ArrayList<>();
    TextView totalPrice, emptyCart;
    LinearLayout linearParent, totalLayout;
    ArrayList<String> values;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();

        fetchCartDetails();
    }

    private void fetchCartDetails() {
        try {
            DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
            handler.openDataBase();
            Cursor cursor = handler.getCartProduct();
            handler.close();

            if (cursor.getCount() == 0) {
                emptyCart.setVisibility(View.VISIBLE);
                totalLayout.setVisibility(View.GONE);
            } else {
                emptyCart.setVisibility(View.GONE);
                totalLayout.setVisibility(View.VISIBLE);
                do {
                    ProductCart cart = new ProductCart();
                    cart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                    cart.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                    cart.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow("qty")) - 1);
                    cart.setProductUnitPrice(cursor.getString(cursor.getColumnIndexOrThrow("unit_price")));
                    cart.setProductTotalPrice(cursor.getString(cursor.getColumnIndexOrThrow("total_price")));
                    cart.setMaxQty(cursor.getInt(cursor.getColumnIndexOrThrow("max")));
                    products.add(cart);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < products.size(); i++) {
            price = price + Integer.parseInt(products.get(i).getProductTotalPrice());
            ItemCartView itemView = new ItemCartView(CartActivity.this, products.get(i));
            itemView.setOnValueChangeListener(onValueChangeListener);
            linearParent.addView(itemView);
        }
        totalPrice.setText("Rs. " + price);
    }

    ItemCartView.OnValueChangeListener onValueChangeListener = new ItemCartView.OnValueChangeListener() {
        @Override
        public void onChange() {
            try {
                DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
                handler.openDataBase();
                Cursor cursor = handler.getCartProduct();
                handler.close();
                price = 0;
                do {
                    price = price + Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("total_price")));
                } while (cursor.moveToNext());
                totalPrice.setText("Rs. " + price);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void init() {

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
