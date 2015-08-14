package com.webmyne.kidscrown.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;

public class RefillActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    LinearLayout crownSetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);

        init();

        crownSetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Functions.fireIntent(RefillActivity.this, DemoCrownActivity.class);
            }
        });

    }

    private void init() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        crownSetLayout = (LinearLayout) findViewById(R.id.crownSetLayout);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            toolbar.setTitle("Refill");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
