package com.webmyne.kidscrown.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.RefillOrderAdapter;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.CrownPricing;
import com.webmyne.kidscrown.model.CrownProductItem;
import com.webmyne.kidscrown.ui.widgets.CrownQuadrant;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;

public class RefillActivity extends AppCompatActivity implements CrownQuadrant.OnCrownClickListner, RefillOrderAdapter.OnDeleteListner, RefillOrderAdapter.onCartSelectListener, RefillOrderAdapter.onTextChange {

    private android.support.v7.widget.Toolbar toolbar;
    LinearLayout crownSetLayout;
    private CrownQuadrant upperLeft;
    private CrownQuadrant upperRight;
    private CrownQuadrant lowerLeft;
    private CrownQuadrant lowerRight;
    // private ImageView imgAdd;
    private ArrayList<CrownProductItem> orderArray;
    private ListView listRefill;
    RefillOrderAdapter adapter;
    private int productID;
    ImageView imgCart;
    Button btnContinue;
    ArrayList<String> upperLeftArray = new ArrayList<>();
    private RefillOrderAdapter.onCartSelectListener onCartSelectListener;
    ArrayList<CrownProductItem> crownProducts = new ArrayList<>();
    ArrayList<CrownPricing> crownPricing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);

        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        productID = getIntent().getIntExtra("product_id", 0);
        fetchDetails();

        fetchCrownPricing();
    }

    private void fetchCrownPricing() {
        DatabaseHandler handler = new DatabaseHandler(RefillActivity.this);
        crownPricing = new ArrayList<>();
        crownPricing = handler.getCrownPricing(productID);
    }

    private void fetchDetails() {
        DatabaseHandler handler = new DatabaseHandler(RefillActivity.this);
        Cursor cursorProduct = handler.getProductCursor("" + productID);
        int color = cursorProduct.getInt(cursorProduct.getColumnIndexOrThrow("color"));
        btnContinue.setBackgroundColor(color);
        toolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.7f; // value component
            color = Color.HSVToColor(hsv);
            window.setStatusBarColor(color);
        }
        handler.close();
    }

    private void init() {

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        crownSetLayout = (LinearLayout) findViewById(R.id.crownSetLayout);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            toolbar.setTitle("Refill");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(RefillActivity.this, CartActivity.class);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

        crownSetLayout = (LinearLayout) findViewById(R.id.crownSetLayout);
        upperLeft = (CrownQuadrant) findViewById(R.id.quadUpperLeft);
        upperRight = (CrownQuadrant) findViewById(R.id.quadUpperRight);
        lowerLeft = (CrownQuadrant) findViewById(R.id.quadLowerLeft);
        lowerRight = (CrownQuadrant) findViewById(R.id.quadLowerRight);

        upperLeft.setOnCrownClickListner(this);
        upperRight.setOnCrownClickListner(this);
        lowerLeft.setOnCrownClickListner(this);
        lowerRight.setOnCrownClickListner(this);

        upperLeft.setUpperLeft();
        upperRight.setUpperRight();
        lowerLeft.setLowerLeft();
        lowerRight.setLowerRight();

        listRefill = (ListView) findViewById(R.id.listRefill);
        listRefill.setEmptyView(findViewById(android.R.id.empty));
        orderArray = new ArrayList<>();
        adapter = new RefillOrderAdapter(this, orderArray);
        adapter.setOnDeleteListner(this);
        adapter.setOnTextChange(this);
        listRefill.setAdapter(adapter);


        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processContinue();

            }
        });
    }

    private void processContinue() {
        int totalCrowns = 0;
        int unitPrice = 0;
        for (CrownProductItem item : orderArray) {
            totalCrowns += item.itemQty;
        }

        if (totalCrowns != 0) {

            for (int k = 0; k < crownPricing.size(); k++) {

                if (totalCrowns >= crownPricing.get(k).getMin() && totalCrowns <= crownPricing.get(k).getMax()) {
                    unitPrice = crownPricing.get(k).getPrice();
                    break;
                } else if (totalCrowns > crownPricing.get(k).getMax()) {
                    unitPrice = crownPricing.get(k).getPrice();
                }
            }
            Log.e("unit", unitPrice + "--");
        }

        DatabaseHandler handler = new DatabaseHandler(RefillActivity.this);
        for (CrownProductItem item : orderArray) {
            ArrayList<String> productDetails = new ArrayList<String>();
            Log.e("----", String.format("%s - %d", item.itemName, item.itemQty));
            productDetails.add(productID + "");
            productDetails.add(item.itemName);
            productDetails.add(item.itemQty + "");

            productDetails.add(unitPrice + "");
            productDetails.add((unitPrice * item.itemQty) + "");

            Log.e("productDetails", productDetails.toString());

            handler.addCartProduct(productDetails);
        }

    }

    @Override
    public void add(String value) {

        CrownProductItem item = new CrownProductItem();
        item.itemName = value;
        item.itemQty = 100;
        orderArray.add(item);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void delete(String value) {


        for (CrownProductItem item : orderArray) {
            if (item.itemName.equalsIgnoreCase(value)) {
                orderArray.remove(item);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(int position) {

        String toDelete = orderArray.get(position).itemName;//todo
        upperLeft.removeSelected(toDelete);
        upperRight.removeSelected(toDelete);
        lowerLeft.removeSelected(toDelete);
        lowerRight.removeSelected(toDelete);

        orderArray.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addCart() {
        Toast.makeText(RefillActivity.this, "click on ccontinue cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTextChange(String crownName, int qty) {

        for (CrownProductItem item : orderArray) {
            if (item.itemName.equalsIgnoreCase(crownName)) {
                item.itemQty = qty;
                break;
            }
        }
        // adapter.notifyDataSetChanged();


    }
}
