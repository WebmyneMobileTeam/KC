package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgCart;
    private Button viewOrders;
    TextView txtConfirm;
    String strHtml="<p align=\"center\">\n" +
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
            "    YOGI EnterPrices\n" +
            "    <br/>\n" +
            "    A205, Krishna Township,\n" +
            "    <br/>\n" +
            "    Opp. Vuda Flats, Opp. Ambe Temple,\n" +
            "    <br/>\n" +
            "    Gotri, Vadodara\n" +
            "    <br/>\n" +
            "    Gujarat\n" +
            "    <br/>\n" +
            "    E-mail : yogienterprices2@gmail.com\n" +
            "</p>\n" +
            "<div>\n" +
            "    <p>\n" +
            "        Bank Details\n" +
            "    </p>\n" +
            "</div>\n" +
            "<p>\n" +
            "    Beneficiary Name: Yogi Enterprices Beneficiary\n" +
            "    <br/>\n" +
            "    Bank Name: Oriantal Bank Of Commerce\n" +
            "    <br/>\n" +
            "    Branch: R.C Dutt Road ,Alkapuri ,Vadodara 390007\n" +
            "    <br/>\n" +
            "    IFS Code: ORBC01100957\n" +
            "    <br/>\n" +
            "    Beneficiary A/c No. : 51161131000193\n" +
            "</p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        init();

        txtConfirm.setText(Html.fromHtml(strHtml));
    }

    private void init() {
        txtConfirm = (TextView) findViewById(R.id.txtConfirm);
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
            toolbar.setTitle("Order Confirmation");
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
