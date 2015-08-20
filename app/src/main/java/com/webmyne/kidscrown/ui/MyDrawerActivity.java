package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.fragment.AboutUsFragment;
import com.webmyne.kidscrown.fragment.HelpFragment;
import com.webmyne.kidscrown.fragment.HomeFragment;
import com.webmyne.kidscrown.fragment.MyAddressFragment;
import com.webmyne.kidscrown.fragment.MyOrdersFragment;
import com.webmyne.kidscrown.fragment.ProfileFragment;
import com.webmyne.kidscrown.fragment.SettingsFragment;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.model.UserProfile;

public class MyDrawerActivity extends AppCompatActivity {
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        initDrawer();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content, new HomeFragment());
        // ft.addToBackStack(null);
        ft.commit();

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                setDrawerClick(menuItem.getItemId());

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    public void setTitle(String title){
        toolbar.setTitle(title);
    }

    private void setDrawerClick(int itemId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        switch (itemId) {
            case R.id.drawer_home:
                // Home
                ft.replace(R.id.content, new HomeFragment(), "HOME_PAGE");
                ft.commit();
                break;

            case R.id.drawer_profile:
                // Profile
                ft.replace(R.id.content, new ProfileFragment(), "PROFILE_PAGE");
                ft.commit();
                break;

            case R.id.drawer_orders:
                // My Orders
                ft.replace(R.id.content, new MyOrdersFragment(), "MY_ORDERS_PAGE");
                ft.commit();
                break;

            case R.id.drawer_address:
                // My Address
                ft.replace(R.id.content, new MyAddressFragment(), "MY_ADDRESS_PAGE");
                ft.commit();
                break;

            case R.id.drawer_log_out:
                // Logout
                ComplexPreferences preferences = ComplexPreferences.getComplexPreferences(MyDrawerActivity.this, "user_pref", 0);
                UserProfile currentUserObj = new UserProfile();
                preferences.putObject("current-user", currentUserObj);
                preferences.commit();

                SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent i = new Intent(MyDrawerActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

            case R.id.drawer_feedback:
                // Feedback
                Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
                feedbackIntent.setType("text/email");
                feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kids@crown.com"});
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "KidsCrown Feedback");
                startActivity(Intent.createChooser(feedbackIntent, "Send Feedback:"));
                break;

            case R.id.drawer_settings:
                // Settings
                ft.replace(R.id.content, new SettingsFragment(), "SETTINGS_PAGE");
                //ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.drawer_about:
                // About Us
                ft.replace(R.id.content, new AboutUsFragment(), "ABOUT_US_PAGE");
                //ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.drawer_help:
                // Help and FAQ
                ft.replace(R.id.content, new HelpFragment(), "HELP_PAGE");
                //ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

    private void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        view = (NavigationView) findViewById(R.id.navigation_view);

        if (toolbar != null) {
            toolbar.setTitle("Kids Crown");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(MyDrawerActivity.this, "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
