package com.webmyne.kidscrown.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.fragment.AboutUsFragment;
import com.webmyne.kidscrown.fragment.ContactUsFragment;
import com.webmyne.kidscrown.fragment.HomeFragment;
import com.webmyne.kidscrown.fragment.MyOrdersFragment;
import com.webmyne.kidscrown.fragment.ProfileFragment;
import com.webmyne.kidscrown.helper.BadgeHelper;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.LoginModelData;

public class MyDrawerActivity extends AppCompatActivity {

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView view;

    private DatabaseHandler handler;

    private TextView txtCustomTitle;
    private MenuItem cartItem;
    private BadgeHelper badgeCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        init();

        initDrawer();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content, new HomeFragment());
        ft.commit();

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                setDrawerClick(menuItem, menuItem.getItemId());
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        cartItem = menu.findItem(R.id.action_cart);
        badgeCart = new BadgeHelper(this, cartItem, ActionItemBadge.BadgeStyles.YELLOW);
        badgeCart.displayBadge(handler.getTotalProducts());
        return true;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setTitle(String title) {
        txtCustomTitle.setText(title);
    }

    private void setDrawerClick(MenuItem menuItem, int itemId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        switch (itemId) {
            case R.id.drawer_home:
                // Home
                menuItem.setChecked(true);
                ft.replace(R.id.content, new HomeFragment(), "HOME_PAGE");
                ft.commit();
                break;

            case R.id.drawer_profile:
                // Profile
                menuItem.setChecked(true);
                ft.replace(R.id.content, new ProfileFragment(), "PROFILE_PAGE");
                ft.commit();
                break;

            case R.id.drawer_orders:
                // My Orders
                menuItem.setChecked(true);
                ft.replace(R.id.content, new MyOrdersFragment(), "MY_ORDERS_PAGE");
                ft.commit();
                break;

            case R.id.drawer_help:
                // intro page
                menuItem.setCheckable(false);
                Functions.fireIntent(this, IntroActivity.class);
                break;

            case R.id.drawer_log_out:
                // Logout
                menuItem.setCheckable(false);
                final AlertDialog dialog = new AlertDialog.Builder(MyDrawerActivity.this).create();
                dialog.setTitle("Are you sure want to logout?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        PrefUtils.clearUserProfile(MyDrawerActivity.this, new LoginModelData());

                        handler.deleteCart();

                        Intent intent = new Intent(MyDrawerActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;

            case R.id.drawer_feedback:
                // Feedback
                menuItem.setCheckable(false);
                Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
                feedbackIntent.setType("text/email");
                feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kids@crown.com"});
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "KidsCrown Feedback");
                startActivity(Intent.createChooser(feedbackIntent, "Send Feedback:"));
                break;

            case R.id.drawer_about:
                // About Us
                menuItem.setChecked(true);
                ft.replace(R.id.content, new AboutUsFragment(), "ABOUT_US_PAGE");
                ft.commit();
                break;

            case R.id.drawer_contact_us:
                // Contact Us
                menuItem.setChecked(true);
                ft.replace(R.id.content, new ContactUsFragment(), "CONTACT_US_PAGE");
                ft.commit();
                break;
        }
    }

    private void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Functions.hideKeyPad(MyDrawerActivity.this, drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Functions.hideKeyPad(MyDrawerActivity.this, drawerView);
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
        handler = new DatabaseHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        view = (NavigationView) findViewById(R.id.navigation_view);

        if (toolbar != null) {
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText(getString(R.string.app_name));
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (badgeCart != null)
            badgeCart.displayBadge(handler.getTotalProducts());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_cart:
                Functions.fireIntent(this, CartActivityRevised.class);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
