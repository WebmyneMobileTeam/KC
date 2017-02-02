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
import com.webmyne.kidscrown.model.OrderHistoryModel;
import com.webmyne.kidscrown.model.OrderProduct;
import com.webmyne.kidscrown.model.OrderProductModel;
import com.webmyne.kidscrown.ui.widgets.MyOrderItemView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgCart;
    private TextView txtShipping;
    private String orderId;
    private LinearLayout linearLayout;
    private OrderDetailsAdapter adapter;
    private ListView orderListview;
    private ComplexPreferences complexPreferences;
    private String orderNumber;
    private OrderProduct orderObject;
    private LinearLayout orderSummaryLayout;
    private LinearLayout.LayoutParams params;
    private TextView txtPayable;
    private DecimalFormat formatter;
    private OrderHistoryModel model;
    private TextView txtCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        complexPreferences = ComplexPreferences.getComplexPreferences(this, Constants.PREF_NAME, 0);
        model = new OrderHistoryModel();
        model = complexPreferences.getObject("order", OrderHistoryModel.class);

        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
       // setOrderDetails();
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

        int shipping = orderObject.TaxAmount;
        if (orderObject.TaxAmount == 0) {
            txtPayable.setText(getResources().getString(R.string.Rs) + " " + formatter.format(orderObject.PayableAmount));
        } else {
            String text = " (" + (orderObject.PayableAmount - shipping) + " + " + shipping + " Shipping Cost)";
            txtPayable.setText(getResources().getString(R.string.Rs) + " " + formatter.format(orderObject.PayableAmount) + " " + text);
        }
    }

    private void init() {
        formatter = new DecimalFormat("#,##,###");

        orderListview = (ListView) findViewById(R.id.orderListview);
        orderSummaryLayout = (LinearLayout) findViewById(R.id.orderSummaryLayout);
        txtPayable = (TextView) findViewById(R.id.txtPayable);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("");
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText("Order : " + model.getInvoiceNumber());
        }
        setSupportActionBar(toolbar);

        imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

        complexPreferences = ComplexPreferences.getComplexPreferences(this, "user_pref", 0);
        orderObject = new OrderProduct();
        orderObject = complexPreferences.getObject("order", OrderProduct.class);
        orderNumber = orderObject.OrderNumber;

        Log.e("orderNumber", model.getInvoiceNumber());
        //toolbar.setTitle("Order : " + model.getInvoiceNumber());

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
