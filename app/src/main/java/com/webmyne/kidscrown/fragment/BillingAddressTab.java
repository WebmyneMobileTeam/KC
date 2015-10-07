package com.webmyne.kidscrown.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.AddressAdapter;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.ToolHelper;
import com.webmyne.kidscrown.model.Address;
import com.webmyne.kidscrown.model.AddressesList;
import com.webmyne.kidscrown.model.BillingAndShippingAddress;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class BillingAddressTab extends android.support.v4.app.Fragment {

    String userId;
    private AddressAdapter adapter;
    //private ListView listAddress;
    View parentView;
    private EditText edtAddress1, edtAddress2, edtCity, edtState, edtCountry, edtPincode ;
    private Button saveDetails;
    AddressesList list;
    BillingAndShippingAddress billingAndShippingAddress;


    public static BillingAddressTab newInstance(String param1, String param2) {
        BillingAddressTab fragment = new BillingAddressTab();

        return fragment;
    }

    public BillingAddressTab() {
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
        parentView = inflater.inflate(R.layout.fragment_existing_address, container, false);

        init(parentView);

        return parentView;
    }

    private void init(View parentView) {
       // listAddress = (ListView) parentView.findViewById(R.id.listAddress);
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        userId = currentUserObj.UserID;

        edtAddress1 = (EditText) parentView.findViewById(R.id.edtBillingAddress1);
        edtAddress2 = (EditText) parentView.findViewById(R.id.edtBillingAddress2);
        edtCity = (EditText) parentView.findViewById(R.id.edtBillingCity);
        edtState = (EditText) parentView.findViewById(R.id.edtBillingState);
        edtCountry = (EditText) parentView.findViewById(R.id.edtBillingCountry);
        edtPincode = (EditText) parentView.findViewById(R.id.edtBillingPincode);
        saveDetails = (Button) parentView.findViewById(R.id.saveDetails);


    }
    public void setListener(AddressesList _list){
        this.list = _list;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchAddress();
    }

    private void fetchAddress() {
        String user = "?UserId=" + userId;
        Log.e("Address URL", Constants.GET_EXISTING_ADDRESS + user);
        View view = ((MyDrawerActivity) getActivity()).getToolbar().getRootView();
        final ToolHelper helper = new ToolHelper(getActivity(), view);
        helper.displayProgress();

        new CallWebService(Constants.GET_EXISTING_ADDRESS + user, CallWebService.TYPE_GET) {
            @Override
            public void response(String response) {
                helper.hideProgress();
                Log.e("Response Address", response);
                Type listType = new TypeToken<List<Address>>() {
                }.getType();
                ArrayList<Address> addresses = new GsonBuilder().create().fromJson(response, listType);
                DatabaseHandler handler = new DatabaseHandler(getActivity());
                handler.saveAddress(addresses);
                handler.close();
                displayAddress();

            }

            @Override
            public void error(String error) {
                helper.hideProgress();
                Log.e("Error", error);

            }
        }.call();
    }

    private void displayAddress() {
        String[] columns = new String[]{
                "address_1",
                "address_2",
        };

        int[] to = new int[]{
                R.id.txtAddress1,
                R.id.txtAddress2
        };

        try {
            DatabaseHandler handler = new DatabaseHandler(getActivity());
            handler.openDataBase();
            Cursor cursor = handler.getAddressCursor();
            handler.close();
            /*adapter = new AddressAdapter(getActivity(), R.layout.address_row, cursor, columns, to, 0);
            listAddress.setAdapter(adapter);*/

            if(cursor.getString(cursor.getColumnIndexOrThrow("is_shipping")).equals("true")) {
                edtAddress1.setText(cursor.getString(cursor.getColumnIndexOrThrow("address_1")));
                edtAddress2.setText(cursor.getString(cursor.getColumnIndexOrThrow("address_2")));
                edtCity.setText(cursor.getString(cursor.getColumnIndexOrThrow("city_name")));
                edtCountry.setText(cursor.getString(cursor.getColumnIndexOrThrow("country_name")));
                edtState.setText(cursor.getString(cursor.getColumnIndexOrThrow("state_name")));
                edtPincode.setText(cursor.getString(cursor.getColumnIndexOrThrow("pincode")));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        displayAddress();
    }
}
