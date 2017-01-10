package com.webmyne.kidscrown.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.OrderDetailsAdapter;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.OrderSummary;
import com.webmyne.kidscrown.model.OrderProduct;
import com.webmyne.kidscrown.model.OrderProductModel;
import com.webmyne.kidscrown.ui.widgets.MyOrderItemView;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ImageView imgCart;
    TextView txtShipping;
    String orderId;
    LinearLayout linearLayout;
    OrderDetailsAdapter adapter;
    private ListView orderListview;
    ComplexPreferences complexPreferences;
    String orderNumber;
    OrderProduct orderObject;
    LinearLayout orderSummaryLayout;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderId = getIntent().getStringExtra("order_id");

        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setOrderDetails();
    }

    private void setOrderDetails() {

        ArrayList<OrderProductModel> products = new ArrayList<>();
        ArrayList<OrderProductModel> crowns = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Refill\n");
        int crownQty = 0;

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        linearLayout.removeAllViews();
        linearLayout.invalidate();

        orderSummaryLayout.removeAllViews();
        orderSummaryLayout.invalidate();

        MyOrderItemView itemView = new MyOrderItemView(OrderDetailsActivity.this, orderObject);
        linearLayout.addView(itemView);

        for (int i = 0; i < orderObject.orderProducts.size(); i++) {

            if (orderObject.orderProducts.get(i).ProductID == Integer.parseInt(Constants.INTRO)) {
                OrderSummary orderSummary = new OrderSummary(OrderDetailsActivity.this);
                orderSummary.setDetails(orderObject.orderProducts.get(i).ProductName, orderObject.orderProducts.get(i).Quantity, orderObject.FinalIntroPrice);
                orderSummaryLayout.addView(orderSummary, params);
            } else if (orderObject.orderProducts.get(i).ProductID == Integer.parseInt(Constants.ASSORTED)) {
                OrderSummary orderSummary = new OrderSummary(OrderDetailsActivity.this);
                orderSummary.setDetails(orderObject.orderProducts.get(i).ProductName, orderObject.orderProducts.get(i).Quantity, orderObject.FinalAssortPrice);
                orderSummaryLayout.addView(orderSummary, params);
            } else {
                crowns.add(orderObject.orderProducts.get(i));
            }
        }

        if (crowns.size() > 0) {
            for (int i = 0; i < crowns.size(); i++) {
                stringBuilder.append(crowns.get(i).ProductNumber + ", ");
                crownQty += crowns.get(i).Quantity;
            }

            OrderSummary orderSummary = new OrderSummary(OrderDetailsActivity.this);
            orderSummary.setDetails(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 2), crownQty, orderObject.FinalRefillPrice);
            orderSummaryLayout.addView(orderSummary, params);
        }

        adapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderObject);
        orderListview.setAdapter(adapter);

    }

    private void init() {
        orderListview = (ListView) findViewById(R.id.orderListview);
        orderSummaryLayout = (LinearLayout) findViewById(R.id.orderSummaryLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

        complexPreferences = ComplexPreferences.getComplexPreferences(this, "user_pref", 0);
        orderObject = new OrderProduct();
        orderObject = complexPreferences.getObject("order", OrderProduct.class);
        orderNumber = orderObject.OrderNumber;

        Log.e("orderNumber", orderNumber);
        toolbar.setTitle("Order : " + orderNumber);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        txtShipping = (TextView) findViewById(R.id.txtShipping);

        txtShipping.setText(orderObject.FullAddress);

    }
}
