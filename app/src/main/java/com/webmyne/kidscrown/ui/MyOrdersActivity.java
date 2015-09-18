package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.fragment.HomeFragment;
import com.webmyne.kidscrown.fragment.MyOrdersFragment;
import com.webmyne.kidscrown.helper.Functions;

public class MyOrdersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        init();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_content, new MyOrdersFragment());
        // ft.addToBackStack(null);
        ft.commit();
    }

    private void init() {
        imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("My Orders");
            setSupportActionBar(toolbar);
        }

        toolbar.setNavigationIcon(R.drawable.ic_home);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MyOrdersActivity.this, MyDrawerActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newIntent);
            }
        });
    }

}