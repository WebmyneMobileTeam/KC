package com.webmyne.kidscrown.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

public class MyAddressFragment2 extends android.support.v4.app.Fragment {

    private String mParam1;
    private String mParam2;
    View parentView;

    public static MyAddressFragment2 newInstance(String param1, String param2) {
        MyAddressFragment2 fragment = new MyAddressFragment2();

        return fragment;
    }

    public MyAddressFragment2() {
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
        parentView = inflater.inflate(R.layout.fragment_my_address_fragment2, container, false);

        ((MyDrawerActivity) getActivity()).setTitle("My Address");

        return parentView;
    }

}
