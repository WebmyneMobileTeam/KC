package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgCart;
    private Button viewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        init();
    }

    private void init() {
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
            toolbar.setTitle("Secure Payment");
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
