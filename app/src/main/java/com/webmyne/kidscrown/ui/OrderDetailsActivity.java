package com.webmyne.kidscrown.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.OrderSummary;
import com.webmyne.kidscrown.model.OrderHistoryModel;

import java.text.DecimalFormat;

public class OrderDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtShipping;
    private ComplexPreferences complexPreferences;
    private LinearLayout orderSummaryLayout;
    private LinearLayout.LayoutParams params;
    private DecimalFormat formatter;
    private OrderHistoryModel model;
    private TextView txtCustomTitle;

    private TextView txtBilling;
    private TextView subtotalPrice;
    private TextView txtCharge;
    private TextView txtSavedPrice;
    private TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        complexPreferences = ComplexPreferences.getComplexPreferences(this, Constants.PREF_NAME, 0);
        model = new OrderHistoryModel();
        model = complexPreferences.getObject("order", OrderHistoryModel.class);

        init();

    }

    private void init() {
        formatter = new DecimalFormat("#,##,###");

        orderSummaryLayout = (LinearLayout) findViewById(R.id.orderSummaryLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText("Order : " + model.getInvoiceNumber());
        }
        setSupportActionBar(toolbar);

        subtotalPrice = (TextView) findViewById(R.id.subtotalPrice);
        txtCharge = (TextView) findViewById(R.id.txtCharge);
        txtSavedPrice = (TextView) findViewById(R.id.txtSavedPrice);
        totalPrice = (TextView) findViewById(R.id.totalPrice);

        Log.e("orderNumber", model.getInvoiceNumber());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderSummaryLayout = (LinearLayout) findViewById(R.id.orderSummaryLayout);
        txtBilling = (TextView) findViewById(R.id.txtBilling);
        txtShipping = (TextView) findViewById(R.id.txtShipping);

        setDetails();

    }

    private void setDetails() {
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        txtShipping.setText(model.getShippingAddressDC().toString());
        txtBilling.setText(model.getBillingAddressDC().toString());

        orderSummaryLayout.removeAllViews();
        orderSummaryLayout.invalidate();

        for (int i = 0; i < model.getLstOrderProduct().size(); i++) {
            OrderSummary orderSummary = new OrderSummary(OrderDetailsActivity.this);
            orderSummary.setDetails(model.getLstOrderProduct().get(i));
            orderSummaryLayout.addView(orderSummary, params);
        }

        subtotalPrice.setText(getResources().getString(R.string.Rs) + " " + formatter.format(model.getSubTotal()));
        txtSavedPrice.setText(getResources().getString(R.string.Rs) + " " + formatter.format(model.getYouSaved()));
        txtCharge.setText(getResources().getString(R.string.Rs) + " " + formatter.format(model.getShippingCost()));
        totalPrice.setText(getResources().getString(R.string.Rs) + " " + model.getTotalAmount());

    }
}
