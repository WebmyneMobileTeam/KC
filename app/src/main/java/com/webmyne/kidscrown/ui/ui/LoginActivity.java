package com.webmyne.kidscrown.ui.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.ui.helper.Functions;

public class LoginActivity extends ActionBarActivity {

    Button btnLogin;
    TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(LoginActivity.this, HomeActivity.class);
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(LoginActivity.this, RegisterActivity.class);
            }
        });
    }

    private void init() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);

    }

}
