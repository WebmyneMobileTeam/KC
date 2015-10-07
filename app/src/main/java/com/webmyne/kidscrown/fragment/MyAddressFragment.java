package com.webmyne.kidscrown.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.PagerAdapterClass;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.model.AddressesList;
import com.webmyne.kidscrown.model.BillingAndShippingAddress;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.ConfirmOrderActivity;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

import java.sql.SQLException;


public class MyAddressFragment extends Fragment{

    ViewPager pager;
    View parentView;
    Button btnUpdate;
    String userId;
    BillingAndShippingAddress addresses;

    public static MyAddressFragment newInstance(String param1, String param2) {
        MyAddressFragment fragment = new MyAddressFragment();

        return fragment;
    }

    public MyAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_address, container, false);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        userId = currentUserObj.UserID;
        init(parentView);

        return parentView;
    }

    private void init(View view) {
        ((MyDrawerActivity) getActivity()).setTitle("My Address");
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Billing Address"));
        tabLayout.addTab(tabLayout.newTab().setText("Shipping Address"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        pager = (ViewPager) view.findViewById(R.id.pager);
        final PagerAdapterClass adapter = new PagerAdapterClass(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BillingAddressTab billingAddressTab = new BillingAddressTab();
                billingAddressTab.setListener(new AddressesList() {
                    @Override
                    public void setAddress(BillingAndShippingAddress address) {
                        Log.e("Billing Address", address.getBillingAddress1());
                    }

                    @Override
                    public BillingAndShippingAddress getAddress() {
                        return null;
                    }
                });

            }
        });

    }




}
