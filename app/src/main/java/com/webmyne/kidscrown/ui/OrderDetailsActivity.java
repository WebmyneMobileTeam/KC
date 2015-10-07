package com.webmyne.kidscrown.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.OrderDetailsAdapter;
import com.webmyne.kidscrown.adapters.OrderListAdapter;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.model.FinalOrders;
import com.webmyne.kidscrown.model.OrderModel;
import com.webmyne.kidscrown.model.OrderProduct;
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

        linearLayout.removeAllViews();
        linearLayout.invalidate();

        MyOrderItemView itemView = new MyOrderItemView(OrderDetailsActivity.this, orderObject);
        linearLayout.addView(itemView);

        adapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderObject);
        orderListview.setAdapter(adapter);

    }

    private void init() {
        orderListview = (ListView) findViewById(R.id.orderListview);

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
