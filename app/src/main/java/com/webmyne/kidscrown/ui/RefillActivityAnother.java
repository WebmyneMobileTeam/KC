package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.MyRecyclerAdapter;
import com.webmyne.kidscrown.adapters.RefillOrderAdapterAnother;
import com.webmyne.kidscrown.helper.BadgeHelper;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.GetSortedDiscount;
import com.webmyne.kidscrown.helper.ToolHelper;
import com.webmyne.kidscrown.model.CartProduct;
import com.webmyne.kidscrown.model.CrownProductItem;
import com.webmyne.kidscrown.model.PriceSlab;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.model.ProductSlab;
import com.webmyne.kidscrown.ui.widgets.CrownQuadrantAnother;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    Button btnContinue, btnSave, btnCancel;
    RecyclerView numberPad;
    private LinearLayout linearNumberPad;
    private LinearLayout linearProducts;
    private TextView txtDisplayCrownName;
    private TextView txtDisplayCrownQTY;
    private String selectedValue = new String();
    private int selectedSpecificId = 0;

    private StringBuilder sb = new StringBuilder();
    ArrayList<ProductCart> crowns = new ArrayList<>();
    int crownProductId;
    private GetSortedDiscount getSortedDiscount;

    private Product product;
    private TextView txtCustomTitle;
    private ArrayList<PriceSlab> priceSlabs;
    private MenuItem cartItem;
    private BadgeHelper badgeCart;
    private DatabaseHandler handler;
    private int unitTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_another);

        product = (Product) getIntent().getSerializableExtra("product");

        init();

        productID = getIntent().getIntExtra("product_id", 0);

        // fetchDetails();
        // fetchCrownPricing();
        // helper.displayBadge();
        //  fetchCartCrowns();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSave();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCancel();
            }
        });

        adapter = new RefillOrderAdapterAnother(RefillActivityAnother.this, orderArray);
        adapter.setOnDeleteListner(RefillActivityAnother.this);
        adapter.setOnTextChange(RefillActivityAnother.this);
        listRefill.setAdapter(adapter);
    }

    private void processCancel() {
        sb = new StringBuilder();
        txtDisplayCrownQTY.setText("0");
        String toDelete = txtDisplayCrownName.getText().toString();

       /* upperLeft.removeSelected(toDelete);
        upperRight.removeSelected(toDelete);
        lowerLeft.removeSelected(toDelete);
        lowerRight.removeSelected(toDelete);*/

        for (int i = 0; i < crownSetLayout.getChildCount(); i++) {
            CrownQuadrantAnother crownQuadrantAnother = (CrownQuadrantAnother) crownSetLayout.getChildAt(i);
            crownQuadrantAnother.clearSelection();
        }

        if (linearNumberPad.isShown()) {
            linearNumberPad.setVisibility(View.GONE);
            linearProducts.setVisibility(View.VISIBLE);
        }
    }

    private void processSave() {
        String crownName = txtDisplayCrownName.getText().toString();
        String crownQty = txtDisplayCrownQTY.getText().toString();

        if (crownQty.length() == 0 || crownQty.equals("0") || crownQty.equals("00")) {
            Functions.showToast(RefillActivityAnother.this, "Enter valid quantity");

        } else {
            boolean shouldAdd = false;
            int position = 0;
            if (orderArray.isEmpty()) {
                CrownProductItem item = new CrownProductItem(crownName, Integer.parseInt(crownQty), selectedSpecificId);
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
                    CrownProductItem newItem = new CrownProductItem(crownName, Integer.parseInt(crownQty), selectedSpecificId);
                    orderArray.add(newItem);

                } else {
                    CrownProductItem updatedItem = orderArray.get(position);
                    updatedItem.itemQty = Integer.parseInt(crownQty);
                }
            }

            adapter.setCrowns(orderArray);

//            upperLeft.setQuanity(crownName, crownQty);
//            upperRight.setQuanity(crownName, crownQty);
//            lowerLeft.setQuanity(crownName, crownQty);
//            lowerRight.setQuanity(crownName, crownQty);

            for (int i = 0; i < crownSetLayout.getChildCount(); i++) {
                CrownQuadrantAnother crownQuadrantAnother = (CrownQuadrantAnother) crownSetLayout.getChildAt(i);
                crownQuadrantAnother.setQuanity(crownName, crownQty);
            }

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

        for (int i = 0; i < crownSetLayout.getChildCount(); i++) {
            CrownQuadrantAnother crownQuadrantAnother = (CrownQuadrantAnother) crownSetLayout.getChildAt(i);
            crownQuadrantAnother.setDefault();
        }

        crowns = handler.getCrownCartProduct(product.getProductID());
        Log.e("crowns", crowns.size() + "##");

        for (ProductCart cart : crowns) {
            String crown = cart.getProductName();
            int qty = cart.getProductQty();
            int specificId = cart.getSpecificId();

            for (int i = 0; i < crownSetLayout.getChildCount(); i++) {
                CrownQuadrantAnother crownQuadrantAnother = (CrownQuadrantAnother) crownSetLayout.getChildAt(i);
                crownQuadrantAnother.setQuanity(crown, qty + "");
            }
            CrownProductItem item = new CrownProductItem(crown, qty, specificId);
            orderArray.add(item);
        }
        adapter.setCrowns(orderArray);
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
        priceSlabs = new ArrayList<>();
        priceSlabs.addAll(product.getPriceSlabDCs());
        handler = new DatabaseHandler(this);

        getSortedDiscount = new GetSortedDiscount(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

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
            toolbar.setTitle("");
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText(product.getProductName());
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // helper = new ToolHelper(RefillActivityAnother.this, toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        upperLeft = (CrownQuadrantAnother) findViewById(R.id.quadUpperLeft);
//        upperRight = (CrownQuadrantAnother) findViewById(R.id.quadUpperRight);
//        lowerLeft = (CrownQuadrantAnother) findViewById(R.id.quadLowerLeft);
//        lowerRight = (CrownQuadrantAnother) findViewById(R.id.quadLowerRight);

//        upperLeft.setOnCrownClickListner(this);
//        upperRight.setOnCrownClickListner(this);
//        lowerLeft.setOnCrownClickListner(this);
//        lowerRight.setOnCrownClickListner(this);

        // TODO: 30-01-2017
      /*  upperLeft.setUpperLeft();
        upperRight.setUpperRight();
        lowerLeft.setLowerLeft();
        lowerRight.setLowerRight();*/

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

        crownSetLayout.removeAllViews();
        crownSetLayout.invalidate();

        fetchCrowns();

        fetchCartCrowns();
    }

    private void fetchCrowns() {
        LinkedHashMap<String, ArrayList<ProductSlab>> productsMap = product.getProducts();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (Map.Entry<String, ArrayList<ProductSlab>> entry : productsMap.entrySet()) {

            String header = entry.getKey();
            ArrayList<ProductSlab> values = entry.getValue();

            int res = Functions.getResources(header);
            int color = Functions.getColor(RefillActivityAnother.this, header);
            String strHeader = Functions.getHeaderValue(header);

            CrownQuadrantAnother crownQuadrantAnother = new CrownQuadrantAnother(RefillActivityAnother.this, strHeader, res, color);
            crownQuadrantAnother.setOnCrownClickListner(this);
            crownSetLayout.addView(crownQuadrantAnother, params);
            crownQuadrantAnother.setupCrowns(values);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fetchDetails();
        //fetchCrownPricing();
        helper.displayBadge();
        fetchCartCrowns();
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

            boolean isPass = false;
            List<Integer> maxies = new ArrayList<>();

            if (totalCrowns != 0) {

                for (int k = 0; k < priceSlabs.size(); k++) {

                    maxies.add(priceSlabs.get(k).getMaxQty());
                    if (totalCrowns >= priceSlabs.get(k).getMinQty() && totalCrowns <= priceSlabs.get(k).getMaxQty()) {
                        unitPrice = priceSlabs.get(k).getPrice();
                        isPass = true;
                        break;
                    } else {
                        isPass = false;
                        continue;
                    }
                }
            }

            if (!isPass) {
                int tempPos = 0;
                int max = Collections.max(maxies);
                tempPos = maxies.indexOf(Integer.valueOf(max));
                unitPrice = priceSlabs.get(tempPos).getPrice();
            }

            Log.e("unit_price", unitPrice + " ---");

            handler.deleteCartProduct(product.getProductID());

            for (CrownProductItem item : orderArray) {
                CartProduct cartProduct = new CartProduct();
                cartProduct.setProductName(item.itemName);
                cartProduct.setProductId(product.getProductID());
                cartProduct.setQty(item.itemQty);
                cartProduct.setUnitPrice(unitPrice);
                unitTotalPrice = unitPrice * item.itemQty;
                cartProduct.setTotalPrice(unitTotalPrice);
                cartProduct.setSingle(product.getIsSingleInt());
                cartProduct.setMax(product.getOrderLimit());
                cartProduct.setSpecificId(item.specificId);

                Log.e("cart_insert", Functions.jsonString(cartProduct));

                handler.addToCart(cartProduct);
            }

            Functions.showToast(RefillActivityAnother.this, "Added to Cart");
            badgeCart.displayBadge(handler.getTotalProducts());
        }

    }

    private void displayCrown(String value, int specificId) {
        selectedValue = value;
        selectedSpecificId = specificId;
        txtDisplayCrownName.setText(value);
    }

    @Override
    public void displayNumberpad(String value, int specificId) {
        if (!linearNumberPad.isShown()) {
            linearNumberPad.setVisibility(View.VISIBLE);
            linearProducts.setVisibility(View.GONE);
        }
        displayCrown(value, specificId);
    }

    @Override
    public void setSelection(String value) {

        for (int i = 0; i < crownSetLayout.getChildCount(); i++) {
            CrownQuadrantAnother crownQuadrantAnother = (CrownQuadrantAnother) crownSetLayout.getChildAt(i);
            crownQuadrantAnother.setSelection(value);
        }
//        upperLeft.setSelection(value);
//        upperRight.setSelection(value);
//        lowerLeft.setSelection(value);
//        lowerRight.setSelection(value);

    }

    @Override
    public void onDelete(int position) {

        String toDelete = orderArray.get(position).itemName;

        orderArray.remove(position);
        adapter.notifyDataSetChanged();

        for (int i = 0; i < crownSetLayout.getChildCount(); i++) {
            CrownQuadrantAnother crownQuadrantAnother = (CrownQuadrantAnother) crownSetLayout.getChildAt(i);
            crownQuadrantAnother.removeSelected(toDelete);
            crownQuadrantAnother.clearSelected(toDelete);
        }

//        upperLeft.removeSelected(toDelete);
//        upperRight.removeSelected(toDelete);
//        lowerLeft.removeSelected(toDelete);
//        lowerRight.removeSelected(toDelete);
//
//
//
//        upperLeft.clearSelected(toDelete);
//        upperRight.clearSelected(toDelete);
//        lowerLeft.clearSelected(toDelete);
//        lowerRight.clearSelected(toDelete);


        try {
            DatabaseHandler handler = new DatabaseHandler(RefillActivityAnother.this);
            handler.openDataBase();
            handler.deleteCrown(toDelete);
            handler.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //helper.displayBadge();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        cartItem = menu.findItem(R.id.action_cart);
        badgeCart = new BadgeHelper(this, cartItem, ActionItemBadge.BadgeStyles.YELLOW);
        badgeCart.displayBadge(handler.getTotalProducts());
        return true;
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
                Functions.showToast(RefillActivityAnother.this, "Quantity is in between 1 to 100");
            }
        } else {
            txtDisplayCrownQTY.setText(sb.toString());
        }
    }

}