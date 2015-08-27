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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters


    ViewPager pager;
    View parentView;
    Button btnUpdate;
    String userId;
    BillingAndShippingAddress addresses;

    public static MyAddressFragment newInstance(String param1, String param2) {
        MyAddressFragment fragment = new MyAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
