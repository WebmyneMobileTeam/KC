package com.webmyne.kidscrown.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.OrderAdapter;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchOrderHistoryData;
import com.webmyne.kidscrown.custom.EmptyLayout;
import com.webmyne.kidscrown.custom.LineDividerItemDecoration;
import com.webmyne.kidscrown.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.OrderHistoryModel;
import com.webmyne.kidscrown.model.UserProfile;
import com.webmyne.kidscrown.ui.MyDrawerActivity;
import com.webmyne.kidscrown.ui.OrderDetailsActivity;

import java.util.ArrayList;


public class MyOrdersFragment extends Fragment {

    private Activity activity;
    private FamiliarRecyclerView orderListview;
    private LinearLayout linearParent;
    private TextView emptyOrder;
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
                // Log.e("order object", object.toString());
//                complexPreferences.putObject("order", object);
//                complexPreferences.commit();
                complexPreferences.putObject("order", object);
                complexPreferences.commit();

                Intent orderIntent = new Intent(getActivity(), OrderDetailsActivity.class);
               // orderIntent.putExtra("order", object);
                startActivity(orderIntent);

            }
        });

        return view;
    }

    private void init(View view) {
        linearParent = (LinearLayout) view.findViewById(R.id.linearParent);
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

                ArrayList<OrderHistoryModel> responseModel = (ArrayList<OrderHistoryModel>) responseBody;

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                data.addAll(responseModel);

                adapter.setOrders(data);

                Log.e("tag", "from here");
            }

            @Override
            public void onFail() {

            }
        });


//        Log.e("API", Constants.FETCH_ORDER + userId);
//        new CallWebService(Constants.FETCH_ORDER + userId, CallWebService.TYPE_GET, null) {
//
//            @Override
//            public void response(String response) {
//                pd.dismiss();
//             //   Log.e("my_order_response", response);
//
//                Type listType = new TypeToken<List<OrderProduct>>() {
//                }.getType();
//
//                try {
//                    data = new GsonBuilder().create().fromJson(response, listType);
//                } catch (Exception e) {
//                    Log.e("error", e.getMessage());
//                }
//
//                for (int i = 0; i < data.size(); i++) {
//                    adapter = new OrderAdapter(getActivity(), data);
//                    orderListview.setAdapter(adapter);
//
//                }
//            }
//
//            @Override
//            public void error(String error) {
//                pd.dismiss();
//                if (data.size() == 0) {
//                    emptyOrder.setVisibility(View.VISIBLE);
//                } else {
//                    emptyOrder.setVisibility(View.GONE);
//                }
//            }
//        }.call();

    }

}
