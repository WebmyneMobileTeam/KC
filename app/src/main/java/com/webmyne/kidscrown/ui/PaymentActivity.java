package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.PlaceOrderResponse;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgCart;
    private Button viewOrders;
    private TextView txtConfirm;
    private TextView txtTitle;

    // Thanks For Your Order. Please Send Bank Transfer / RTGS / NFT On Below Details To Proceed Your Order.
    String strHtml = "<p align=\"center\">\n" +
            "    <strong>Order Confirmation</strong>\n" +
            "</p>\n" +
            "<p align=\"center\">\n" +
            "    <strong>Thanks For Your Order</strong>\n" +
            "</p>\n" +
            "<p>\n" +
            "    Please Send Bank Transfer / RTGS / NFT On Below Details To Proceess Your Order.\n" +
            "    <br/>\n" +
            "    <br/>\n" +
            "</p>\n" +
            "<div>\n" +
            "    <p>\n" +
            "        Address\n" +
            "    </p>\n" +
            "</div>\n" +
            "<p>\n" +
            "    YOGI EnterPrises\n" +
            "    <br/>\n" +
            "    A205, Krishna Township,\n" +
            "    <br/>\n" +
            "    Opp. Vuda Flats, Opp. Ambe Temple,\n" +
            "    <br/>\n" +
            "    Gotri, Vadodara\n" +
            "    <br/>\n" +
            "    Gujarat\n" +
            "    <br/>\n" +
            "    E-mail : yogienterprises2@gmail.com\n" +
            "</p>\n" +
            "<div>\n" +
            "    <p>\n" +
            "        Bank Details\n" +
            "    </p>\n" +
            "</div>\n" +
            "<p>\n" +
            "    Beneficiary Name: YOGI Enterprises\n" +
            "    <br/>\n" +
            "    Bank Name: Oriental Bank Of Commerce\n" +
            "    <br/>\n" +
            "    Branch: R.C.Dutt Road, Alkapuri, Vadodara - 390007\n" +
            "    <br/>\n" +
            "    IFS Code: ORBC0100957\n" +
            "    <br/>\n" +
            "    Beneficiary A/c No. : 51161131000193\n" +
            "</p>";

    ComplexPreferences complexPreferences;
    PlaceOrderResponse.DataBean resBean;
    private TextView txtCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        complexPreferences = ComplexPreferences.getComplexPreferences(PaymentActivity.this, Constants.PREF_NAME, 0);
        resBean = complexPreferences.getObject("placeOrderRes", PlaceOrderResponse.DataBean.class);

        strHtml = resBean.getPlaceOrderSuccessDetailsDC().getBankDetails();

        init();

        if (!TextUtils.isEmpty(strHtml))
            txtConfirm.setText(Html.fromHtml(strHtml));

        if (!TextUtils.isEmpty(resBean.getPlaceOrderSuccessDetailsDC().getInvoiceNumber())) {
            txtTitle.setText("Thanks For Your Order.\nInvoice number for order is : "
                    + resBean.getPlaceOrderSuccessDetailsDC().getInvoiceNumber()
                    + "\nPlease Send Bank Transfer / RTGS / NFT On Below Details To Proceed Your Order.");
        }

    }

    private void init() {
        txtConfirm = (TextView) findViewById(R.id.txtConfirm);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        viewOrders = (Button) findViewById(R.id.viewOrders);

        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(PaymentActivity.this, MyOrdersActivity.class);
            }
        });

        imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText("Order Placed");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(PaymentActivity.this, MyDrawerActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent newIntent = new Intent(PaymentActivity.this, MyDrawerActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);
    }
}
