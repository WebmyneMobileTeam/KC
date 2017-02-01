package com.webmyne.kidscrown.ui;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.custom.QuantityView;
import com.webmyne.kidscrown.helper.BadgeHelper;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.CartProduct;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.ui.widgets.ComboSeekBar;
import com.webmyne.kidscrown.ui.widgets.FlowLayout;

import java.util.ArrayList;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    Toolbar toolbar;

    CollapsingToolbarLayout collapsingToolbar;
    private ImageView imageProduct;
    private FloatingActionButton fabShop;
    private TextView txtInfo;
    private TextView txtPriceIndividual;
    private TextView txtPriceQTY;
    private TextView txtPriceTotal;
    private int price = 0;
    ArrayList<String> values;
    private FlowLayout flowImages;
    int qty;

    private Product product;
    private int position;
    private MenuItem cartItem;
    private BadgeHelper badgeCart;
    private QuantityView quantityView;

    private int unitPrice;
    private int unitTotalPrice;
    private int userQty;
    private DatabaseHandler handler;
    private Drawable myFabSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        cartItem = menu.findItem(R.id.action_cart);
        badgeCart = new BadgeHelper(this, cartItem, ActionItemBadge.BadgeStyles.YELLOW);
        badgeCart.displayBadge(handler.getTotalProducts());
        return true;
    }

    private void init() {
        // intent data
        handler = new DatabaseHandler(this);
        position = getIntent().getIntExtra("position", 0);
        product = (Product) getIntent().getSerializableExtra("product");

        Log.e("json", Functions.jsonString(product));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageProduct = (ImageView) findViewById(R.id.backdrop);
        fabShop = (FloatingActionButton) findViewById(R.id.fabShop);
        fabShop.setRippleColor(ContextCompat.getColor(this, R.color.quad_green));

        quantityView = (QuantityView) findViewById(R.id.quantityView);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtPriceIndividual = (TextView) findViewById(R.id.txtPriceIndividual);
        txtPriceQTY = (TextView) findViewById(R.id.txtPriceQTY);
        txtPriceTotal = (TextView) findViewById(R.id.txtPriceTotal);
        flowImages = (FlowLayout) findViewById(R.id.flowImages);

        clickListener();

    }

    private void loadProductDetails() {
        collapsingToolbar.setTitle(product.getProductName());
        imageProduct.setBackgroundColor(Functions.getBgColor(ProductDetailActivity.this, position));
        collapsingToolbar.setContentScrimColor(Functions.getBgColor(ProductDetailActivity.this, position));

        values = new ArrayList<>();
        values.add("" + product.getOrderLimit());

        txtInfo.setText(product.getDescription());

        if (checkCart(product.getProductID())) {
            myFabSrc = ContextCompat.getDrawable(ProductDetailActivity.this, R.drawable.ic_action_order);
            userQty = handler.getQty(product.getProductID());
        } else {
            userQty = product.getPriceSlabDCs().get(0).getMinQty();
            myFabSrc = ContextCompat.getDrawable(ProductDetailActivity.this, R.drawable.ic_action_action_add_shopping_cart);
        }

        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        willBeWhite.mutate().setColorFilter(Functions.getBgColor(ProductDetailActivity.this, position), PorterDuff.Mode.MULTIPLY);
        fabShop.setImageDrawable(willBeWhite);

        fabShop.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ProductDetailActivity.this, R.color.white)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Functions.getBgColor(ProductDetailActivity.this, position));
        }

        if (!TextUtils.isEmpty(product.getRootImage())) {
            Glide.with(ProductDetailActivity.this).load(product.getRootImage()).into(imageProduct);
        }

        unitPrice = product.getPriceSlabDCs().get(0).getPrice();

        setPrice(userQty);

        quantityView.setMaxLimit(product.getOrderLimit());
        quantityView.setOnQtyChangeListener(new QuantityView.OnQtyChangeListener() {
            @Override
            public void onChange(int qty) {
                userQty = qty;
                setPrice(userQty);
            }
        });

        displayImages();
    }

    private void setPrice(int userQty) {
        quantityView.setQty(userQty);
        txtPriceIndividual.setText(String.format(Locale.US, "%s %s", getString(R.string.Rs), Functions.priceFormat(unitPrice)));
        unitTotalPrice = unitPrice * userQty;
        txtPriceQTY.setText(String.format(Locale.US, " x %d", userQty));
        txtPriceTotal.setText(String.format(Locale.US, " = %s %s", getString(R.string.Rs), Functions.priceFormat(unitTotalPrice)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Functions.fireIntent(ProductDetailActivity.this, CartActivityRevised.class);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clickListener() {

        fabShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCart(product.getProductID())) {
                    Functions.showToast(ProductDetailActivity.this, "Product already added to Cart");

                } else {
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setProductName(product.getProductName());
                    cartProduct.setProductId(product.getProductID());
                    cartProduct.setSpecificId(0);
                    cartProduct.setQty(userQty);
                    cartProduct.setUnitPrice(unitPrice);
                    cartProduct.setTotalPrice(unitTotalPrice);
                    cartProduct.setSingle(product.getIsSingleInt());
                    cartProduct.setMax(product.getOrderLimit());

                    Log.e("cart_insert", Functions.jsonString(cartProduct));

                    handler.addToCart(cartProduct);
                    Functions.showToast(ProductDetailActivity.this, "Added to Cart");

                    Drawable myFabSrc = ContextCompat.getDrawable(ProductDetailActivity.this, R.drawable.ic_action_order);
                    Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
                    willBeWhite.mutate().setColorFilter(Functions.getBgColor(ProductDetailActivity.this, position), PorterDuff.Mode.MULTIPLY);
                    fabShop.setImageDrawable(willBeWhite);
                }

                displayBadge();
            }
        });
    }

    private void displayBadge() {
        if (badgeCart != null) {
            badgeCart.displayBadge(handler.getTotalProducts());
        }
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
        loadProductDetails();
        if (badgeCart != null)
            badgeCart.displayBadge(handler.getTotalProducts());
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

        String imagePath = product.getRootImage();
        ImageView img = new ImageView(ProductDetailActivity.this);
        Glide.with(ProductDetailActivity.this).load(imagePath).centerCrop().into(img);
        img.setOnClickListener(flowImageClickListner);

        flowImages.addView(img, params);
        flowImages.invalidate();
    }

    public View.OnClickListener flowImageClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //processDisplayGallery(flowImages.indexOfChild(v));
        }
    };

    private void displayQTYandTotal(int position) {
        txtPriceQTY.setText(String.format("x %s QTY", values.get(position)));
        qty = Integer.parseInt(values.get(position));
        int total = price * qty;
        txtPriceTotal.setText(String.format("= Rs.%s", Functions.priceFormat(total)));
    }

    private boolean checkCart(int productID) {
        DatabaseHandler handler = new DatabaseHandler(ProductDetailActivity.this);
        boolean available = handler.ifExists(productID);

        /*Log.e("available in cart", available + "");

        if (available) {
            added = true;
            Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_action_order);
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            willBeWhite.mutate().setColorFilter(cursorProduct.getInt(cursorProduct.getColumnIndexOrThrow("color")), PorterDuff.Mode.MULTIPLY);
            fabShop.setImageDrawable(willBeWhite);

            displayQTYandTotal(handler.getQty(productID) - 1);
            combo.setSelection(handler.getQty(productID) - 1);

        } else {
            added = false;

            displayQTYandTotal(0);
            combo.setSelection(handler.getQty(productID));

            Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_action_action_add_shopping_cart);
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            willBeWhite.mutate().setColorFilter(cursorProduct.getInt(cursorProduct.getColumnIndexOrThrow("color")), PorterDuff.Mode.MULTIPLY);
            fabShop.setImageDrawable(willBeWhite);
        }*/

        return available;
    }
}
