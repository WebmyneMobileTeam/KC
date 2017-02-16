package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.PlaceOrderApi;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.OrderSummary;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.FinalOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderResponse;

import java.text.DecimalFormat;

public class ConfirmOrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView totalPrice, txtBilling, txtShipping, subtotalPrice, txtSaved, txtSavedPrice;
    LinearLayout totalLayout, offerLayout;
    private LinearLayout orderSummaryLayout;
    DatabaseHandler handler;
    private TextView txtCharge;
    private LinearLayout.LayoutParams params;
    DecimalFormat formatter;

    PlaceOrderResponse.DataBean resBean;
    PlaceOrderRequest reqBean;
    private TextView txtCustomTitle;
    private Button btnContinue;
    ComplexPreferences complexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        init();

        complexPreferences = ComplexPreferences.getComplexPreferences(ConfirmOrderActivity.this, Constants.PREF_NAME, 0);
        resBean = complexPreferences.getObject("placeOrderRes", PlaceOrderResponse.DataBean.class);
        reqBean = complexPreferences.getObject("placeOrderReq", PlaceOrderRequest.class);

        txtBilling.setText(reqBean.getBillingAddressDC().toString());
        txtShipping.setText(reqBean.getShippingAddressDC().toString());

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        displayProducts();

    }

    private void displayProducts() {

        orderSummaryLayout.removeAllViews();
        orderSummaryLayout.invalidate();

        for (int i = 0; i < resBean.getPlaceOrderCalculationDC().getProductCalculationDCs().size(); i++) {
            String productName = "";
            PlaceOrderResponse.DataBean.PlaceOrderCalculationDCBean.ProductCalculationDCsBean bean = resBean.getPlaceOrderCalculationDC().getProductCalculationDCs().get(i);
            OrderSummary summary = new OrderSummary(ConfirmOrderActivity.this);

            if (bean.getPlaceOrderRiffileDC() == null || bean.getPlaceOrderRiffileDC().isEmpty()) {
                productName = bean.getProductName();

            } else {
                StringBuilder sb = new StringBuilder();
                for (int i1 = 0; i1 < bean.getPlaceOrderRiffileDC().size(); i1++) {
                    sb.append(bean.getPlaceOrderRiffileDC().get(i1).getRiffleName());
                    if (i1 != bean.getPlaceOrderRiffileDC().size() - 1)
                        sb.append(", ");
                }
                productName = bean.getProductName() + "\n" + sb.toString();
            }

            summary.setDetails(productName, bean.getQuantity(), bean.getTotalPrice());
            orderSummaryLayout.addView(summary, params);
        }

        subtotalPrice.setText(getString(R.string.Rs) + "" + resBean.getPlaceOrderCalculationDC().getTotalAmount());
        txtSavedPrice.setText(getString(R.string.Rs) + "" + resBean.getPlaceOrderCalculationDC().getTotalDiscount());
        txtCharge.setText(getString(R.string.Rs) + "" + resBean.getPlaceOrderCalculationDC().getDeliveryCharge());
        totalPrice.setText(getString(R.string.Rs) + "" + resBean.getPlaceOrderCalculationDC().getPayableAmount());

    }

    private void init() {
        handler = new DatabaseHandler(ConfirmOrderActivity.this);
        formatter = new DecimalFormat("#,##,###");

        offerLayout = (LinearLayout) findViewById(R.id.offerLayout);
        txtSaved = (TextView) findViewById(R.id.txtSaved);
        txtSavedPrice = (TextView) findViewById(R.id.txtSavedPrice);
        subtotalPrice = (TextView) findViewById(R.id.subtotalPrice);
        txtCharge = (TextView) findViewById(R.id.txtCharge);
        btnContinue = (Button) findViewById(R.id.btnContinue);

        orderSummaryLayout = (LinearLayout) findViewById(R.id.orderSummaryLayout);

        txtBilling = (TextView) findViewById(R.id.txtBilling);
        txtShipping = (TextView) findViewById(R.id.txtShipping);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        imgCart.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            toolbar.setTitle("");
            txtCustomTitle.setText("Order Confirmation");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        clickListener();
    }

    private void clickListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalOrderRequest orderRequest = new FinalOrderRequest();
                orderRequest.setMobileOS("A");
                orderRequest.setUserID(PrefUtils.getUserId(ConfirmOrderActivity.this));
                orderRequest.setBillingAddressDC(reqBean.getBillingAddressDC());
                orderRequest.setPlaceOrderCalculationDC(resBean.getPlaceOrderCalculationDC());
                orderRequest.setPlaceOrderProductDC(reqBean.getPlaceOrderProductDC());
                orderRequest.setShippingAddressDC(reqBean.getShippingAddressDC());

                Log.e("final_req", Functions.jsonString(orderRequest));

                new PlaceOrderApi(ConfirmOrderActivity.this, new CommonRetrofitResponseListener() {
                    @Override
                    public void onSuccess(Object responseBody) {
                        //   hideProgress();

                        PlaceOrderResponse placeOrderResponse = (PlaceOrderResponse) responseBody;
                        Log.e("final_res", Functions.jsonString(placeOrderResponse));

                        complexPreferences.putObject("placeOrderRes", placeOrderResponse.getData());
                        complexPreferences.commit();

                        handler.deleteCart();

                        Intent i = new Intent(ConfirmOrderActivity.this, PaymentActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    }

                    @Override
                    public void onFail() {
                    }
                }, orderRequest);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}