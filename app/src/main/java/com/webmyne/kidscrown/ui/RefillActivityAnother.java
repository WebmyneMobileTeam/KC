package com.webmyne.kidscrown.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.webmyne.kidscrown.helper.GetPriceSlab;
import com.webmyne.kidscrown.model.CartProduct;
import com.webmyne.kidscrown.model.CrownProductItem;
import com.webmyne.kidscrown.model.PriceSlab;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.model.ProductSlab;
import com.webmyne.kidscrown.ui.widgets.CrownQuadrantAnother;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RefillActivityAnother extends AppCompatActivity implements CrownQuadrantAnother.OnCrownClickListner, RefillOrderAdapterAnother.OnDeleteListner, RefillOrderAdapterAnother.onCartSelectListener, RefillOrderAdapterAnother.onTextChange, MyRecyclerAdapter.addQtyListener {

    private android.support.v7.widget.Toolbar toolbar;
    LinearLayout crownSetLayout, numPadButtonLayout;
    private ArrayList<CrownProductItem> orderArray;
    private ListView listRefill;
    RefillOrderAdapterAnother adapter;
    Button btnContinue, btnSave, btnCancel;
    RecyclerView numberPad;
    private LinearLayout linearNumberPad;
    private LinearLayout linearProducts;
    private TextView txtDisplayCrownName;
    private TextView txtDisplayCrownQTY;
    private int selectedSpecificId = 0;

    private StringBuilder sb = new StringBuilder();
    ArrayList<ProductCart> crowns = new ArrayList<>();

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
        fetchCartCrowns();
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

    private void init() {
        priceSlabs = new ArrayList<>();
        priceSlabs.addAll(product.getPriceSlabDCs());
        handler = new DatabaseHandler(this);

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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

            unitPrice = new GetPriceSlab(this).getRelevantPrice(product.getProductID(), totalCrowns).getPrice();

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

        try {
            DatabaseHandler handler = new DatabaseHandler(RefillActivityAnother.this);
            handler.openDataBase();
            handler.deleteCrown(toDelete);
            handler.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (badgeCart != null)
            badgeCart.displayBadge(handler.getTotalProducts());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Functions.fireIntent(RefillActivityAnother.this, CartActivityRevised.class);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
        }
        return super.onOptionsItemSelected(item);
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