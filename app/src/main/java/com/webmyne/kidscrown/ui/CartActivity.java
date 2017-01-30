package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.GetSortedDiscount;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.ui.widgets.CrownCartView;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private FamiliarRecyclerView cartRV;
    Toolbar toolbar;
    GridLayout gridLayout;
    ArrayList<ProductCart> products = new ArrayList<>();
    ArrayList<ProductCart> crowns = new ArrayList<>();
    TextView totalPrice, emptyCart, subtotalPrice, txtSaved, txtSavedPrice;
    LinearLayout linearParent, totalLayout, offerLayout;
    int price, crownProductId;
    SharedPreferences preferences;
    RelativeLayout rLayoutCheckout;
    boolean isOffer;
    float percentage;
    GetSortedDiscount getSortedDiscount;
    DecimalFormat formatter;

    private TextView txtCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        crownProductId = preferences.getInt("crownProductId", 0);
        isOffer = preferences.getBoolean("offer", false);

        init();

        if (getSortedDiscount.getOffer(Constants.INVOICE) != null || !getSortedDiscount.getOffer(Constants.INVOICE).DiscountPercentage.equals("0.00")) {
            percentage = preferences.getFloat("percentage", 0);
            offerLayout.setVisibility(View.GONE);
            txtSaved.setText("You saved as per " + getSortedDiscount.getOffer(Constants.INVOICE).DiscountPercentage + "%");
        } else {
            offerLayout.setVisibility(View.GONE);
        }

        fetchCartDetails();

        rLayoutCheckout = (RelativeLayout) findViewById(R.id.rLayoutCheckout);
        rLayoutCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (products.size() == 0 && crowns.size() == 0) {
                    Functions.snack(v, "Please add products before place order.");
                    return;
                }

                if (!Functions.isConnected(CartActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    return;
                }

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
            /*ItemCartView itemCartView = new ItemCartView(CartActivity.this, products.get(i));
            itemCartView.setOnValueChangeListener(onValueChangeListener);
            itemCartView.setOnRemoveProductListener(onRemoveProductListener);
            linearParent.addView(itemCartView);*/

            // pricing
            int productPrice = Integer.parseInt(products.get(i).getProductUnitPrice()) * products.get(i).getProductQty();
            price += productPrice;
        }

        for (int i = 0; i < crowns.size(); i++) {
            int productPrice = Integer.parseInt(crowns.get(i).getProductUnitPrice()) * crowns.get(i).getProductQty();
            price += productPrice;
            CrownCartView crownView = new CrownCartView(CartActivity.this, crowns.get(i));
            crownView.setOnRemoveCrownListener(onRemoveCrownListener);
            gridLayout.addView(crownView);
        }

        String myString = formatter.format(price);
        totalPrice.setText(getString(R.string.Rs) + " " + myString);
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

                if (getSortedDiscount.getOffer(Constants.INVOICE) != null || !getSortedDiscount.getOffer(Constants.INVOICE).DiscountPercentage.equals("0.00")) {
                    subtotalPrice.setText(getString(R.string.Rs) + " " + price);
                    float savedPrice = ((price * percentage) / 100);
                    txtSavedPrice.setText(getString(R.string.Rs) + " " + savedPrice);
                    String myString = formatter.format((price - (int) savedPrice));
                    totalPrice.setText(getString(R.string.Rs) + " " + myString);

                } else {
                    String myString = formatter.format(price);
                    totalPrice.setText(getString(R.string.Rs) + " " + myString);
                }

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
        public void removeCrown(final String productName) {

            new AsyncTask<Void, Void, Void>() {

                ProgressDialog pd;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd = ProgressDialog.show(CartActivity.this, "Please wait", "Updating Cart", false);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        DatabaseHandler handler = new DatabaseHandler(CartActivity.this);
                        handler.openDataBase();
                        handler.deleteCrownProduct(productName);
                        handler.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.dismiss();
                    refreshActivity();
                }
            }.execute();
        }
    };

    private void init() {
        formatter = new DecimalFormat("#,##,###");
        getSortedDiscount = new GetSortedDiscount(this);

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(2);

        offerLayout = (LinearLayout) findViewById(R.id.offerLayout);
        txtSaved = (TextView) findViewById(R.id.txtSaved);
        txtSavedPrice = (TextView) findViewById(R.id.txtSavedPrice);
        subtotalPrice = (TextView) findViewById(R.id.subtotalPrice);
        linearParent = (LinearLayout) findViewById(R.id.linearParent);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        emptyCart = (TextView) findViewById(R.id.emptyCart);
        imgCart.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText("My Cart");
            toolbar.setTitle("");
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
