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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.fragment.AboutUsFragment;
import com.webmyne.kidscrown.fragment.ContactUsFragment;
import com.webmyne.kidscrown.fragment.HomeFragment;
import com.webmyne.kidscrown.fragment.MyOrdersFragment;
import com.webmyne.kidscrown.fragment.ProfileFragment;
import com.webmyne.kidscrown.helper.BadgeHelper;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.helper.ToolHelper;
import com.webmyne.kidscrown.model.DiscountModel;
import com.webmyne.kidscrown.model.LoginModelData;
import com.webmyne.kidscrown.model.UserProfile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        cartItem = menu.findItem(R.id.action_cart);
        badgeCart = new BadgeHelper(this, cartItem, ActionItemBadge.BadgeStyles.YELLOW);
        badgeCart.displayBadge(handler.getTotalProducts());
        return true;
    }

    private void fetchDiscount() {

        new CallWebService(Constants.GET_OFFERS, CallWebService.TYPE_GET) {
            @Override
            public void response(String response) {

                Log.e("Response Products", response);
                Type listType = new TypeToken<List<DiscountModel>>() {
                }.getType();
                ArrayList<DiscountModel> discountModels = new GsonBuilder().create().fromJson(response, listType);

                handler.saveOffers(discountModels);

            }

            @Override
            public void error(String error) {
                Log.e("Error", error);

            }
        }.call();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setTitle(String title) {
        txtCustomTitle.setText(title);
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

            case R.id.drawer_help:
                // intro page
                Functions.fireIntent(this, IntroActivity.class);
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

                PrefUtils.clearUserProfile(this, new LoginModelData());

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

/*
            case R.id.drawer_settings:
                // Settings
                ft.replace(R.id.content, new SettingsFragment(), "SETTINGS_PAGE");
                //ft.addToBackStack(null);
                ft.commit();
                break;
*/
            case R.id.drawer_about:
                // About Us
                ft.replace(R.id.content, new AboutUsFragment(), "ABOUT_US_PAGE");
                //ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.drawer_contact_us:
                // Contact Us
                ft.replace(R.id.content, new ContactUsFragment(), "CONTACT_US_PAGE");
                //ft.addToBackStack(null);
                ft.commit();
                break;
/*
            case R.id.drawer_help:
                // Help and FAQ
                ft.replace(R.id.content, new HelpFragment(), "HELP_PAGE");
                //ft.addToBackStack(null);
                ft.commit();
                break;
*/
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
        fetchDiscount();
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
