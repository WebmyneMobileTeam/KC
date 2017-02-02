package com.webmyne.kidscrown.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.OrderAdapter;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchOrderHistoryData;
import com.webmyne.kidscrown.custom.EmptyLayout;
import com.webmyne.kidscrown.custom.LineDividerItemDecoration;
import com.webmyne.kidscrown.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.model.OrderHistoryModel;
import com.webmyne.kidscrown.model.OrderHistoryModelResponse;
import com.webmyne.kidscrown.ui.MyDrawerActivity;
import com.webmyne.kidscrown.ui.OrderDetailsActivity;

import java.util.ArrayList;


public class MyOrdersFragment extends Fragment {

    private Activity activity;
    private FamiliarRecyclerView orderListview;
    private OrderAdapter adapter;
    private ArrayList<OrderHistoryModel> data;
    private ComplexPreferences complexPreferences;
    private EmptyLayout emptyLayout;

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

        complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), Constants.PREF_NAME, 0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        orderListview.setLayoutManager(layoutManager);
        orderListview.addItemDecoration(new LineDividerItemDecoration(getActivity()));
        data = new ArrayList<>();
        adapter = new OrderAdapter(getActivity(), data);

        orderListview.setEmptyView(emptyLayout);
        emptyLayout.setContent("No Order found.");

        orderListview.setAdapter(adapter);

        orderListview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                OrderHistoryModel object = data.get(position);
                complexPreferences.putObject("order", object);
                complexPreferences.commit();

                Intent orderIntent = new Intent(getActivity(), OrderDetailsActivity.class);
                startActivity(orderIntent);

            }
        });

        return view;
    }

    private void init(View view) {
        emptyLayout = (EmptyLayout) view.findViewById(R.id.emptyLayout);
        orderListview = (FamiliarRecyclerView) view.findViewById(R.id.orderListview);
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

        new FetchOrderHistoryData(getActivity(), new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                OrderHistoryModelResponse modelResponse = (OrderHistoryModelResponse) responseBody;
                ArrayList<OrderHistoryModel> responseModel = modelResponse.getData();

                data.addAll(responseModel);

                adapter.setOrders(data);

            }

            @Override
            public void onFail() {

            }
        });

    }

}
