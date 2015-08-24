package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.ui.widgets.ComboSeekBar;
import com.webmyne.kidscrown.ui.widgets.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    private ImageView imageProduct;
    private int productID;
    private FloatingActionButton fabShop;
    private ComboSeekBar combo;
    Cursor cursorProduct;
    Cursor cursorProductPrice;
    Cursor cursorProductImage;
    private TextView txtInfo;
    private TextView txtPriceIndividual;
    private TextView txtPriceQTY;
    private TextView txtPriceTotal;
    private int price = 0;
    ArrayList<String> values;
    private FlowLayout flowImages;

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
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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

        combo = (ComboSeekBar) findViewById(R.id.combo);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtPriceIndividual = (TextView) findViewById(R.id.txtPriceIndividual);
        txtPriceQTY = (TextView) findViewById(R.id.txtPriceQTY);
        txtPriceTotal = (TextView) findViewById(R.id.txtPriceTotal);
        flowImages = (FlowLayout) findViewById(R.id.flowImages);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDetails();
    }

    private void fetchDetails() {

        DatabaseHandler handler = new DatabaseHandler(ProductDetailActivity.this);
        cursorProduct = handler.getProductCursor("" + productID);
        cursorProductImage = handler.getProductImageCursor("" + productID);
        cursorProductPrice = handler.getProductPriceCursor("" + productID);
        cursorProductPrice.moveToFirst();
        handler.close();

        String imagePath = handler.getImagePath("" + productID);
        if (imagePath != null && imagePath.isEmpty() == false) {
            Glide.with(ProductDetailActivity.this).load(imagePath).into(imageProduct);
        }
        handler.close();

        imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processDisplayGallery(0);
            }
        });


        displayDetails(cursorProduct, cursorProductPrice, cursorProductImage);


    }

    private void processDisplayGallery(int position) {

        Intent iGallery = new Intent(ProductDetailActivity.this, GalleryActivity.class);
        iGallery.putExtra("pos",position);
        iGallery.putExtra("id", productID);
        startActivity(iGallery);
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom);

    }

    private void displayDetails(Cursor cursorProduct, Cursor cursorProductPrice, Cursor cursorProductImage) {

        collapsingToolbar.setTitle(cursorProduct.getString(cursorProduct.getColumnIndexOrThrow("name")));
        imageProduct.setBackgroundColor(cursorProduct.getInt(cursorProduct.getColumnIndexOrThrow("color")));
        collapsingToolbar.setContentScrimColor(cursorProduct.getInt(cursorProduct.getColumnIndexOrThrow("color")));
        combo.setColor(cursorProduct.getInt(cursorProduct.getColumnIndexOrThrow("color")));
        txtInfo.setText(Html.fromHtml(cursorProduct.getString(cursorProduct.getColumnIndexOrThrow("description"))));
        price = cursorProductPrice.getInt(cursorProductPrice.getColumnIndexOrThrow("price"));


        Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_action_action_add_shopping_cart);
        //copy it in a new one
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        //set the color filter, you can use also Mode.SRC_ATOP
        willBeWhite.mutate().setColorFilter(cursorProduct.getInt(cursorProduct.getColumnIndexOrThrow("color")), PorterDuff.Mode.MULTIPLY);
        //set it to your fab button initialized before
        fabShop.setImageDrawable(willBeWhite);

        int max = cursorProductPrice.getInt(cursorProductPrice.getColumnIndexOrThrow("max"));
        values = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            values.add("" + i);
        }
        combo.setAdapter(values);
        combo.setSelection(0);
        displayQTYandTotal(0);
        combo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                displayQTYandTotal(position);
            }
        });

        txtPriceIndividual.setText(String.format("Rs. %d", price));

        displayImages();

    }

    private void displayImages() {

        flowImages.removeAllViews();
        flowImages.invalidate();

        int width = (getResources().getDisplayMetrics().widthPixels) / 6;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.leftMargin = 8;
        params.rightMargin = 8;
        params.bottomMargin = 4;
        params.topMargin = 4;

        cursorProductImage.moveToFirst();
        do {
            //Log.e("ImagePath ", cursorProductImage.getString(cursorProductImage.getColumnIndexOrThrow("path")));
            String imagepath = cursorProductImage.getString(cursorProductImage.getColumnIndexOrThrow("path"));
            ImageView img = new ImageView(ProductDetailActivity.this);
            Glide.with(ProductDetailActivity.this).load(imagepath).centerCrop().thumbnail(1).into(img);
            img.setOnClickListener(flowImageClickListner);

            flowImages.addView(img, params);
            flowImages.invalidate();

        } while (cursorProductImage.moveToNext());

    }

    public View.OnClickListener flowImageClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            processDisplayGallery(flowImages.indexOfChild(v));
        }
    };

    private void displayQTYandTotal(int position) {
        txtPriceQTY.setText(String.format("x %s QTY", values.get(position)));
        int qty = Integer.parseInt(values.get(position));
        int total = price * qty;
        txtPriceTotal.setText(String.format("= Rs. %d", total));
    }
}
