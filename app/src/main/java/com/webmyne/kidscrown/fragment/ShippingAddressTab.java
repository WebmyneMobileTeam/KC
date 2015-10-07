package com.webmyne.kidscrown.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.webmyne.kidscrown.R;


public class ShippingAddressTab extends android.support.v4.app.Fragment {

    private String mParam1;
    private String mParam2;
    private CheckBox isSameAsShipping;
    private EditText edtAddress1, edtAddress2, edtCity, edtState, edtCountry, edtPincode;
    View parentView;


    public static ShippingAddressTab newInstance(String param1, String param2) {
        ShippingAddressTab fragment = new ShippingAddressTab();

        return fragment;
    }

    public ShippingAddressTab() {
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
        parentView = inflater.inflate(R.layout.fragment_new_address_tab, container, false);

        isSameAsShipping = (CheckBox) parentView.findViewById(R.id.checkbox);
        edtAddress1 = (EditText) parentView.findViewById(R.id.edtShippingAddress1);
        edtAddress2 = (EditText) parentView.findViewById(R.id.edtShippingAddress2);
        edtCity = (EditText) parentView.findViewById(R.id.edtShippingCity);
        edtState = (EditText) parentView.findViewById(R.id.edtShippingState);
        edtCountry = (EditText) parentView.findViewById(R.id.edtShippingCountry);
        edtPincode = (EditText) parentView.findViewById(R.id.edtShippingPincode);

        isSameAsShipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    edtAddress1.setEnabled(false);
                    edtAddress2.setEnabled(false);
                    edtCity.setEnabled(false);
                    edtState.setEnabled(false);
                    edtCountry.setEnabled(false);
                    edtPincode.setEnabled(false);
                } else {
                    edtAddress1.setEnabled(true);
                    edtAddress2.setEnabled(true);
                    edtCity.setEnabled(true);
                    edtState.setEnabled(true);
                    edtCountry.setEnabled(true);
                    edtPincode.setEnabled(true);
                }
            }
        });
        return parentView;

    }

}
