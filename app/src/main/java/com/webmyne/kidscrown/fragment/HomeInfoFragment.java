package com.webmyne.kidscrown.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.ui.MyDrawerActivity;


public class HomeInfoFragment extends Fragment {

    public static HomeInfoFragment newInstance(String param1, String param2) {
        HomeInfoFragment fragment = new HomeInfoFragment();

        return fragment;
    }

    public HomeInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home_info, container, false);
        ((MyDrawerActivity) getActivity()).setTitle("About Us");
        return view;
    }

}
