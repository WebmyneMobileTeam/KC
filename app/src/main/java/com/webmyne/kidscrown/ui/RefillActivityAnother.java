package com.webmyne.kidscrown.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.MyRecyclerAdapter;
import com.webmyne.kidscrown.adapters.RefillOrderAdapterAnother;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.ToolHelper;
import com.webmyne.kidscrown.model.CrownPricing;
import com.webmyne.kidscrown.model.CrownProductItem;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.ui.widgets.CrownQuadrantAnother;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RefillActivityAnother extends AppCompatActivity implements CrownQuadrantAnother.OnCrownClickListner, RefillOrderAdapterAnother.OnDeleteListner, RefillOrderAdapterAnother.onCartSelectListener, RefillOrderAdapterAnother.onTextChange, MyRecyclerAdapter.addQtyListener {

    private android.support.v7.widget.Toolbar toolbar;
    ToolHelper helper;
    LinearLayout crownSetLayout, numPadButtonLayout;
    private CrownQuadrantAnother upperLeft;
    private CrownQuadrantAnother upperRight;
    private CrownQuadrantAnother lowerLeft;
    private CrownQuadrantAnother lowerRight;
    private ArrayList<CrownProductItem> orderArray;
    private ListView listRefill;
    RefillOrderAdapterAnother adapter;
    private int productID;
    ImageView imgCart;
    Button btnContinue, btnOK, btnCancel;
    ArrayList<CrownPricing> crownPricing;
    RecyclerView numberPad;
    private LinearLayout linearNumberPad;
    private LinearLayout linearProducts;
    private TextView txtDisplayCrownName;
    private TextView txtDisplayCrownQTY;
    private String selectedValue = new String();
    private StringBuilder sb = new StringBuilder();
    ArrayList<ProductCart> crowns = new ArrayList<>();
    int crownProductId;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_another);

        init();

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        crownProductId = preferences.getInt("crownProductId", 0);

        productID = getIntent().getIntExtra("product_id", 0);
        fetchDetails();
        fetchCrownPricing();
        helper.displayBadge();
        fetchCartCrowns();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processOK(v);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCancel();
            }
        });
    }

    private void processCancel() {
        sb = new StringBuilder();
        txtDisplayCrownQTY.setText("0");
        String toDelete = txtDisplayCrownName.getText().toString();
       /* upperLeft.removeSelected(toDelete);
        upperRight.removeSelected(toDelete);
        lowerLeft.removeSelected(toDelete);
        lowerRight.removeSelected(toDelete);*/

        upperLeft.clearSelection();
        upperRight.clearSelection();
        lowerLeft.clearSelection();
        lowerRight.clearSelection();


        if (linearNumberPad.isShown()) {
            linearNumberPad.setVisibility(View.GONE);
            linearProducts.setVisibility(View.VISIBLE);
        }
    }

    private void processOK(View v) {
        String crownName = txtDisplayCrownName.getText().toString();
        String crownQty = txtDisplayCrownQTY.getText().toString();

        if (crownQty.length() == 0 || crownQty.equals("0") || crownQty.equals("00")) {
            Functions.snack(v, "Enter Valid Quantity");
        } else {
            boolean shouldAdd = false;
            int position = 0;
            if (orderArray.isEmpty()) {
                Log.e("crown", "added");
                CrownProductItem item = new CrownProductItem();
                item.itemName = crownName;
                item.itemQty = Integer.parseInt(crownQty);
                orderArray.add(item);
            } else {
                for (int i = 0; i < orderArray.size(); i++) {
                    if (orderArray.get(i).itemName.equalsIgnoreCase(crownName)) {
                        position = i;
                        shouldAdd = false;
                        break;
                    } else {
                        shouldAdd = true;
                    }
                }
                if (shouldAdd) {
                    Log.e("crown", "added");
                    CrownProductItem newItem = new CrownProductItem();
                    newItem.itemName = crownName;
                    newItem.itemQty = Integer.parseInt(crownQty);
                    orderArray.add(newItem);
                } else {
                    Log.e("crown", "updated");
                    CrownProductItem updatedItem = orderArray.get(position);
                    updatedItem.itemQty = Integer.parseInt(crownQty);
                }
            }

            adapter = new RefillOrderAdapterAnother(RefillActivityAnother.this, orderArray);
            adapter.setOnDeleteListner(RefillActivityAnother.this);
            adapter.setOnTextChange(RefillActivityAnother.this);
            listRefill.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            upperLeft.setQuanity(crownName, crownQty);
            upperRight.setQuanity(crownName, crownQty);
            lowerLeft.setQuanity(crownName, crownQty);
            lowerRight.setQuanity(crownName, crownQty);

            sb = new StringBuilder();
            txtDisplayCrownQTY.setText("0");
            linearNumberPad.setVisibility(View.GONE);
            linearProducts.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void fetchCartCrowns() {

        orderArray = new ArrayList<>();
        orderArray.clear();

        upperLeft.setDefault();
        upperRight.setDefault();
        lowerRight.setDefault();
        lowerLeft.setDefault();

        try {
            DatabaseHandler handler = new DatabaseHandler(RefillActivityAnother.this);
            handler.openDataBase();
            crowns = handler.getCrownCartProduct(crownProductId);

            for (ProductCart cart : crowns) {
                String crown = cart.getProductName();
                int qty = cart.getProductQty();

                upperLeft.setQuanity(crown, qty + "");
                upperRight.setQuanity(crown, qty + "");
                lowerLeft.setQuanity(crown, qty + "");
                lowerRight.setQuanity(crown, qty + "");

                CrownProductItem item = new CrownProductItem();
                item.itemName = crown;
                item.itemQty = qty;
                orderArray.add(item);

            }

            adapter = new RefillOrderAdapterAnother(RefillActivityAnother.this, orderArray);
            adapter.setOnDeleteListner(RefillActivityAnother.this);
            adapter.setOnTextChange(RefillActivityAnother.this);
            listRefill.setAdapter(adapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void fetchCrownPricing() {
        DatabaseHandler handler = new DatabaseHandler(RefillActivityAnother.this);
        crownPricing = new ArrayList<>();
        crownPricing = handler.getCrownPricing(productID);
    }

    private void fetchDetails() {
        DatabaseHandler handler = new DatabaseHandler(RefillActivityAnother.this);
        Cursor cursorProduct = handler.getProductCursor("" + productID);
        int color = cursorProduct.getInt(cursorProduct.getColumnIndexOrThrow("color"));
        btnContinue.setBackgroundColor(color);
        toolbar.setBackgroundColor(color);
        numPadButtonLayout.setBackgroundColor(color);
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

        btnOK = (Button) findViewById(R.id.btnOK);
        btnCancel = (Button) findViewById(R.id.btnRemove);

        txtDisplayCrownName = (TextView) findViewById(R.id.txtDisplayCrownName);
        txtDisplayCrownQTY = (TextView) findViewById(R.id.txtDisplayCrownQTY);

        linearNumberPad = (LinearLayout) findViewById(R.id.linearNumberPad);
        linearProducts = (LinearLayout) findViewById(R.id.linearProducts);
        numPadButtonLayout = (LinearLayout) findViewById(R.id.numPadButtonLayout);

        linearNumberPad.setVisibility(View.GONE);
        linearProducts.setVisibility(View.VISIBLE);
        numberPad = (RecyclerView) findViewById(R.id.numberPad);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        crownSetLayout = (LinearLayout) findViewById(R.id.crownSetLayout);

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            toolbar.setTitle("Refill");
            setSupportActionBar(toolbar);
        }

        helper = new ToolHelper(RefillActivityAnother.this, toolbar);

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
                Functions.fireIntent(RefillActivityAnother.this, CartActivity.class);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

        crownSetLayout = (LinearLayout) findViewById(R.id.crownSetLayout);
        upperLeft = (CrownQuadrantAnother) findViewById(R.id.quadUpperLeft);
        upperRight = (CrownQuadrantAnother) findViewById(R.id.quadUpperRight);
        lowerLeft = (CrownQuadrantAnother) findViewById(R.id.quadLowerLeft);
        lowerRight = (CrownQuadrantAnother) findViewById(R.id.quadLowerRight);

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
        adapter = new RefillOrderAdapterAnother(this, orderArray);
        adapter.setOnDeleteListner(this);
        adapter.setOnTextChange(this);
        listRefill.setAdapter(adapter);

        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processContinue(v);
            }
        });

        fillNumberPad();
    }

    private void fillNumberPad() {
        List<String> feedsList = new ArrayList<>();
        feedsList.add("1");
        feedsList.add("2");
        feedsList.add("3");
        feedsList.add("4");
        feedsList.add("5");
        feedsList.add("6");
        feedsList.add("7");
        feedsList.add("8");
        feedsList.add("9");
        feedsList.add("");
        feedsList.add("0");
        feedsList.add("Del");

        MyRecyclerAdapter adapter = new MyRecyclerAdapter(RefillActivityAnother.this, feedsList);
        numberPad.setAdapter(adapter);
        adapter.setAddQtyListener(this);
        android.support.v7.widget.GridLayoutManager layoutManager = new android.support.v7.widget.GridLayoutManager(RefillActivityAnother.this, 3);
        numberPad.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();

    }

    private void processContinue(View v) {
        if (orderArray.size() == 0) {
            Functions.snack(v, "No crowns selected");
        } else {
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
            }

            DatabaseHandler handler = new DatabaseHandler(RefillActivityAnother.this);
            handler.deleteCartProduct(crownProductId);

            for (CrownProductItem item : orderArray) {
                ArrayList<String> productDetails = new ArrayList<String>();
                productDetails.add(productID + "");
                productDetails.add(item.itemName);
                productDetails.add(item.itemQty + "");
                productDetails.add(unitPrice + "");
                productDetails.add((unitPrice * item.itemQty) + "");
                handler.addCartProduct(productDetails);
            }
            Snackbar.make(btnContinue, "Added to Cart", Snackbar.LENGTH_SHORT).show();
            helper.displayBadge();
        }

    }

    private void displayCrown(String value) {
        selectedValue = value;
        txtDisplayCrownName.setText(value);
    }


    @Override
    public void displayNumberpad(String value) {
        if (!linearNumberPad.isShown()) {
            linearNumberPad.setVisibility(View.VISIBLE);
            linearProducts.setVisibility(View.GONE);
        }
        displayCrown(value);
    }

    @Override
    public void setSelection(String value) {

        upperLeft.setSelection(value);
        upperRight.setSelection(value);
        lowerLeft.setSelection(value);
        lowerRight.setSelection(value);

    }

    @Override
    public void onDelete(int position) {

        String toDelete = orderArray.get(position).itemName;//todo

        orderArray.remove(position);
        adapter.notifyDataSetChanged();

        upperLeft.removeSelected(toDelete);
        upperRight.removeSelected(toDelete);
        lowerLeft.removeSelected(toDelete);
        lowerRight.removeSelected(toDelete);

        upperLeft.clearSelected(toDelete);
        upperRight.clearSelected(toDelete);
        lowerLeft.clearSelected(toDelete);
        lowerRight.clearSelected(toDelete);

        try {
            DatabaseHandler handler = new DatabaseHandler(RefillActivityAnother.this);
            handler.openDataBase();
            handler.deleteCrownProduct(toDelete);
            handler.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        helper.displayBadge();
    }

    @Override
    public void addCart() {
        Toast.makeText(RefillActivityAnother.this, "click on continue cart", Toast.LENGTH_SHORT).show();
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

    @Override
    public void addQty(String value) {
        if (value.equals("Del")) {
            sb = new StringBuilder();
        } else {
            sb.append(value);
        }
        if (sb.toString().length() > 2) {
            if (sb.toString().equals("100")) {
                txtDisplayCrownQTY.setText(sb.toString());
            } else {
                Functions.snack(txtDisplayCrownQTY, "Quantity is in between 1 to 100");
            }
        } else {
            txtDisplayCrownQTY.setText(sb.toString());
        }
    }

}