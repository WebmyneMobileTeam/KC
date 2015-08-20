package com.webmyne.kidscrown.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.webmyne.kidscrown.R;

public class CrownActivity extends AppCompatActivity {

    LinearLayout kitLayout1, kitLayout2;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crown);
        init();
    }

    private void init() {
        kitLayout1 = (LinearLayout) findViewById(R.id.kitLayout1);
        kitLayout2 = (LinearLayout) findViewById(R.id.kitLayout2);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            toolbar.setTitle("Crowns");
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
