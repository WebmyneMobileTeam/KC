package com.webmyne.kidscrown.ui;

import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.RefillOrderAdapter;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.ui.widgets.CrownQuadrant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RefillActivity extends AppCompatActivity implements CrownQuadrant.OnCrownClickListner, RefillOrderAdapter.OnDeleteListner {

    private android.support.v7.widget.Toolbar toolbar;
    LinearLayout crownSetLayout;
    private CrownQuadrant upperLeft;
    private CrownQuadrant upperRight;
    private CrownQuadrant lowerLeft;
    private CrownQuadrant lowerRight;
    // private ImageView imgAdd;
    private ArrayList<String> orderArray;
    private ListView listRefill;
    RefillOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);

        init();

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

        crownSetLayout = (LinearLayout) findViewById(R.id.crownSetLayout);
        upperLeft = (CrownQuadrant) findViewById(R.id.quadUpperLeft);
        upperRight = (CrownQuadrant) findViewById(R.id.quadUpperRight);
        lowerLeft = (CrownQuadrant) findViewById(R.id.quadLowerLeft);
        lowerRight = (CrownQuadrant) findViewById(R.id.quadLowerRight);

        upperLeft.setOnCrownClickListner(this);
        upperRight.setOnCrownClickListner(this);
        lowerLeft.setOnCrownClickListner(this);
        lowerRight.setOnCrownClickListner(this);

        upperLeft.setUpperLeft();
        upperRight.setUpperRight();
        lowerLeft.setLowerLeft();
        lowerRight.setLowerRight();

        listRefill = (ListView) findViewById(R.id.listRefill);
        listRefill.setEmptyView(findViewById(android.R.id.empty));
        orderArray = new ArrayList<>();
        adapter = new RefillOrderAdapter(this, orderArray);
        adapter.setOnDeleteListner(this);
        listRefill.setAdapter(adapter);

    }


    @Override
    public void add(String value) {
        orderArray.add(value);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void delete(String value) {
        orderArray.remove(value);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(int position) {

        String toDelete = orderArray.get(position);
        upperLeft.removeSelected(toDelete);
        upperRight.removeSelected(toDelete);
        lowerLeft.removeSelected(toDelete);
        lowerRight.removeSelected(toDelete);

        orderArray.remove(position);
        adapter.notifyDataSetChanged();
    }
}
