package com.webmyne.kidscrown.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.FinalOrders;
import com.webmyne.kidscrown.ui.MyDrawerActivity;
import com.webmyne.kidscrown.ui.OrderDetailsActivity;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;
import com.webmyne.kidscrown.ui.widgets.MyOrderItemView;

import java.util.ArrayList;
import java.util.HashSet;


public class MyOrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Activity activity;
    private LinearLayout linearParent;
    private ArrayList<FinalOrders> myOrders = new ArrayList<>();
    ArrayList<String> orderIds;
    private TextView emptyOrder;

    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyOrdersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        linearParent = (LinearLayout) view.findViewById(R.id.linearParent);
        emptyOrder = (TextView) view.findViewById(R.id.emptyOrder);
    }

    @Override
    public void onResume() {
        super.onResume();

        activity = getActivity();
        if (activity instanceof MyDrawerActivity) {
            MyDrawerActivity myDrawerActivity = (MyDrawerActivity) activity;
            myDrawerActivity.setTitle("My Orders");
        }

        fetchMyOrders();

    }

    private void fetchMyOrders() {
        // Total Products from Cart
        myOrders = new ArrayList<>();
        myOrders.clear();

        orderIds = new ArrayList<>();
        orderIds.clear();

        linearParent.removeAllViews();
        linearParent.invalidate();

        try {
            DatabaseHandler handler = new DatabaseHandler(getActivity());
            handler.openDataBase();
            myOrders = handler.getOrders();
            handler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (myOrders.size() == 0) {
            emptyOrder.setVisibility(View.VISIBLE);
        } else {
            emptyOrder.setVisibility(View.GONE);
        }

        for (int i = 0; i < myOrders.size(); i++) {
            orderIds.add(myOrders.get(i).orderId);
        }

        HashSet hs = new HashSet();
        hs.addAll(orderIds);
        orderIds.clear();
        orderIds.addAll(hs);

        for (int k = 0; k < orderIds.size(); k++) {
            MyOrderItemView itemView = new MyOrderItemView(getActivity(), orderIds.get(k), myOrders);
            linearParent.addView(itemView);
            final int finalK = k;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderIntent = new Intent(getActivity(), OrderDetailsActivity.class);
                    orderIntent.putExtra("order_id", orderIds.get(finalK));
                    startActivity(orderIntent);
                }
            });
        }

    }
}
