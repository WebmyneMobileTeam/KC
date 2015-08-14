package com.webmyne.kidscrown.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.ui.widgets.CrownQuadrant;

import java.util.HashMap;
import java.util.Map;

public class RefillActivity extends AppCompatActivity {

   private android.support.v7.widget.Toolbar toolbar;
    LinearLayout crownSetLayout;

    private CrownQuadrant upperLeft;
    private CrownQuadrant upperRight;
    private CrownQuadrant lowerLeft;
    private CrownQuadrant lowerRight;



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

        upperLeft.setUpperLeft();
        upperRight.setUpperRight();
        lowerLeft.setLowerLeft();
        lowerRight.setLowerRight();


        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> orderMapUL = upperLeft.getValues();
                HashMap<String,String> orderMapUR = upperRight.getValues();
                HashMap<String,String> orderMapLL = lowerLeft.getValues();
                HashMap<String,String> orderMapLR = lowerRight.getValues();
                HashMap<String,String> orderMap = new HashMap<String, String>();

                orderMap.putAll(orderMapUL);
                orderMap.putAll(orderMapUR);
                orderMap.putAll(orderMapLL);
                orderMap.putAll(orderMapLR);

                for(Map.Entry<String,String> map : orderMap.entrySet()){
                    Log.e("Item",String.format("%s * %s",map.getKey(),map.getValue()));
                }



            }
        });




    }
}
