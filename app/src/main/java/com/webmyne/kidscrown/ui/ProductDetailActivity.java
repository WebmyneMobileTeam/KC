package com.webmyne.kidscrown.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.model.Product;

public class ProductDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    private ImageView imageProduct;
    private int productID;
    private FloatingActionButton fabShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageProduct = (ImageView) findViewById(R.id.backdrop);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        productID = getIntent().getIntExtra("product_id", 0);

        fabShop = (FloatingActionButton) findViewById(R.id.fabShop);
        fabShop.setRippleColor(getResources().getColor(R.color.quad_green));

        fabShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchDetails();
    }

    private void fetchDetails() {

        DatabaseHandler handler = new DatabaseHandler(ProductDetailActivity.this);
        Cursor cursor = handler.getProductCursor("" + productID);
        handler.close();
        collapsingToolbar.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("name")));

        String imagePath = handler.getImagePath("" + productID);
        if (imagePath != null && imagePath.isEmpty() == false) {
            Glide.with(ProductDetailActivity.this).load(imagePath).into(imageProduct);
        }

        //toolbar.setBackgroundColor(cursor.getInt(cursor.getColumnIndexOrThrow("color")));
        imageProduct.setBackgroundColor(cursor.getInt(cursor.getColumnIndexOrThrow("color")));
        collapsingToolbar.setContentScrimColor(cursor.getInt(cursor.getColumnIndexOrThrow("color")));
        // fabShop.setBackgroundColor(cursor.getInt(cursor.getColumnIndexOrThrow("color")));


    }
}
