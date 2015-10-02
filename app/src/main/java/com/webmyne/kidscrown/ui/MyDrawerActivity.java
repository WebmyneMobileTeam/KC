package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.fragment.AboutUsFragment;
import com.webmyne.kidscrown.fragment.HelpFragment;
import com.webmyne.kidscrown.fragment.HomeFragment;
import com.webmyne.kidscrown.fragment.HomeInfoFragment;
import com.webmyne.kidscrown.fragment.MyAddressFragment;
import com.webmyne.kidscrown.fragment.MyAddressFragment2;
import com.webmyne.kidscrown.fragment.MyOrdersFragment;
import com.webmyne.kidscrown.fragment.ProfileFragment;
import com.webmyne.kidscrown.fragment.SettingsFragment;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.ToolHelper;
import com.webmyne.kidscrown.model.UserProfile;

public class MyDrawerActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {
    Toolbar toolbar;
    ToolHelper helper;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView view;
    private CallbackManager callbackManager;

    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        init();

        callbackManager = CallbackManager.Factory.create();
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


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setTitle(String title) {
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

          /*  case R.id.drawer_address:
                // My Address
                ft.replace(R.id.content, new MyAddressFragment(), "MY_ADDRESS_PAGE");
                ft.commit();
                break;*/

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

                try {
                    // Facebook logout
                    LoginManager.getInstance().logOut();
                    //GooglePlus Logout
                    if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient.connect();
                    }
                } catch (Exception e) {
                    Log.e("EXP LOGOUT", e.toString());
                }

                DatabaseHandler handler = new DatabaseHandler(MyDrawerActivity.this);
                handler.deleteCart();

                Intent i = new Intent(MyDrawerActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

            /*case R.id.drawer_settings:
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
                break;*/
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
        helper = new ToolHelper(MyDrawerActivity.this, toolbar);
        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyDrawerActivity.this, CartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
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

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.displayBadge();

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

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
                this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {

    }
}
