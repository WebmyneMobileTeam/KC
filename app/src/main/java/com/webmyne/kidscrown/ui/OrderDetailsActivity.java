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
    private ArrayList<FinalOrders> myOrders = new ArrayList<>();
    OrderListAdapter adapter;
    ArrayList<OrderModel> ordersById;
    private ListView orderListview;
    ComplexPreferences complexPreferences;
    String orderNumber;

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
        //setOrderDetails();
    }

    private void setOrderDetails() {
        myOrders = new ArrayList<>();
        myOrders.clear();

        ordersById = new ArrayList<>();
        ordersById.clear();

        try {
            DatabaseHandler handler = new DatabaseHandler(OrderDetailsActivity.this);
            handler.openDataBase();
            myOrders = handler.getOrders();
            ordersById = handler.getOrdersForOrderId(orderId);
            handler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyOrderItemView itemView = new MyOrderItemView(OrderDetailsActivity.this, orderId, myOrders);
        linearLayout.addView(itemView);

        for (FinalOrders finalOrder : myOrders) {
            if (finalOrder.orderId.equals(orderId)) {
                txtShipping.setText(finalOrder.address);
            }
        }

        adapter = new OrderListAdapter(OrderDetailsActivity.this, ordersById);
        orderListview.setAdapter(adapter);

    }

    private void init() {
        orderListview = (ListView) findViewById(R.id.orderListview);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

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

        complexPreferences = ComplexPreferences.getComplexPreferences(this, "user_pref", 0);
        OrderProduct currentUserObj = new OrderProduct();
        currentUserObj = complexPreferences.getObject("order", OrderProduct.class);
        orderNumber = currentUserObj.OrderNumber;

        Log.e("orderNumber", orderNumber);
        toolbar.setTitle("Order : " + orderNumber);
    }
}
