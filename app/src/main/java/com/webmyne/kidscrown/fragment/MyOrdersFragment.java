package com.webmyne.kidscrown.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.OrderAdapter;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.FinalOrders;
import com.webmyne.kidscrown.model.OrderProduct;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.TotalOrder;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.MyDrawerActivity;
import com.webmyne.kidscrown.ui.OrderDetailsActivity;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;
import com.webmyne.kidscrown.ui.widgets.MyOrderItemView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MyOrdersFragment extends Fragment {

    private Activity activity;
    ListView orderListview;
    private LinearLayout linearParent;
    private ArrayList<FinalOrders> myOrders = new ArrayList<>();
    ArrayList<String> orderIds;
    private TextView emptyOrder;
    private String userId, redirectOrderId;
    ProgressDialog pd;
    OrderAdapter adapter;
    ArrayList<OrderProduct> data;
    ComplexPreferences complexPreferences;

    public static MyOrdersFragment newInstance() {
        MyOrdersFragment fragment = new MyOrdersFragment();

        return fragment;
    }

    public MyOrdersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        init(view);

        complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
        UserProfile currentUserObj = new UserProfile();
        currentUserObj = complexPreferences.getObject("current-user", UserProfile.class);
        userId = currentUserObj.UserID;

        orderListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                redirectOrderId = data.get(position).OrderNumber;

                complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "user_pref", 0);
                OrderProduct object = data.get(position);
                complexPreferences.putObject("order", object);
                complexPreferences.commit();

                Intent orderIntent = new Intent(getActivity(), OrderDetailsActivity.class);
                startActivity(orderIntent);

            }
        });

        return view;
    }

    private void init(View view) {
        linearParent = (LinearLayout) view.findViewById(R.id.linearParent);
        emptyOrder = (TextView) view.findViewById(R.id.emptyOrder);
        orderListview = (ListView) view.findViewById(R.id.orderListview);
    }

    @Override
    public void onResume() {
        super.onResume();

        activity = getActivity();
        if (activity instanceof MyDrawerActivity) {
            MyDrawerActivity myDrawerActivity = (MyDrawerActivity) activity;
            myDrawerActivity.setTitle("My Orders");
        }

        getOrders();

    }

    private void getOrders() {
        data = new ArrayList<>();
        pd = ProgressDialog.show(getActivity(), "Loading", "Please wait..", true);

        Log.e("API", Constants.FETCH_ORDER + userId);
        new CallWebService(Constants.FETCH_ORDER + userId, CallWebService.TYPE_GET, null) {

            @Override
            public void response(String response) {
                pd.dismiss();
//                Log.e("order_response", response);

                Type listType = new TypeToken<List<OrderProduct>>() {
                }.getType();

                try {
                    data = new GsonBuilder().create().fromJson(response, listType);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }

                for (int i = 0; i < data.size(); i++) {

                    adapter = new OrderAdapter(getActivity(), data);
                    orderListview.setAdapter(adapter);

                }
            }

            @Override
            public void error(String error) {
                pd.dismiss();
            }
        }.call();
    }

}
