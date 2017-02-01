package com.webmyne.kidscrown.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.ProductAdapter;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchProducts;
import com.webmyne.kidscrown.custom.EmptyLayout;
import com.webmyne.kidscrown.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.GetPriceSlab;
import com.webmyne.kidscrown.helper.GetSortedDiscount;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.DiscountModel;
import com.webmyne.kidscrown.model.PriceSlab;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.ProductResponse;
import com.webmyne.kidscrown.ui.MyDrawerActivity;
import com.webmyne.kidscrown.ui.ProductDetailActivity;
import com.webmyne.kidscrown.ui.RefillActivityAnother;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private FamiliarRecyclerView productRV;
    private ProductAdapter adapter;
    private ImageView closeInfo;
    private Dialog dialog;
    private LinearLayout offerLayout;
    private TextView txtOffer, txtDiscount;
    private ImageView offerImage;
    private DatabaseHandler handler;
    private GetSortedDiscount sortedDiscount;
    private View parentView;
    private EmptyLayout emptyLayout;

    private ArrayList<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        return parentView;
    }

    private void init() {

        offerImage = (ImageView) parentView.findViewById(R.id.offerImage);
        txtDiscount = (TextView) parentView.findViewById(R.id.txtDiscount);
        txtOffer = (TextView) parentView.findViewById(R.id.txtOffer);
        offerLayout = (LinearLayout) parentView.findViewById(R.id.offerLayout);

        ((MyDrawerActivity) getActivity()).setTitle("Products");

        handler = new DatabaseHandler(getActivity());
        sortedDiscount = new GetSortedDiscount(getActivity());

        initRecyclerView();
    }

    private void initRecyclerView() {
        productList = new ArrayList<>();

        adapter = new ProductAdapter(getActivity(), productList);
        productRV = (FamiliarRecyclerView) parentView.findViewById(R.id.productRV);
        emptyLayout = (EmptyLayout) parentView.findViewById(R.id.emptyLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        productRV.setLayoutManager(layoutManager);

        productRV.setEmptyView(emptyLayout);
        emptyLayout.setContent("No Product found.");

        productRV.setAdapter(adapter);

        productRV.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Product product = productList.get(position);
                Intent intent = null;
                if (product.getIsSingleInt() == 1) {
                    intent = new Intent(getActivity(), ProductDetailActivity.class);
                } else {
                    intent = new Intent(getActivity(), RefillActivityAnother.class);
                }
                intent.putExtra("product", product);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        if (Functions.isConnected(getActivity()))
            fetchProducts();
        else
            Functions.showToast(getActivity(), "No Internet Connectivity");

        /*PriceSlab priceSlab = new GetPriceSlab(getActivity()).getRelevantPrice(16, 43);
        Log.e("priceSlab", Functions.jsonString(priceSlab));*/

    }

    @Override
    public void onResume() {
        super.onResume();

        if (PrefUtils.getFirstTime(getActivity())) {
            displayDialogForFirstTime();
            closeInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefUtils.setFirstTime(getActivity(), false);
                    dialog.dismiss();
                }
            });
        }
    }

    private void displayDialogForFirstTime() {

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_info, null);
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        closeInfo = (ImageView) dialogView.findViewById(R.id.closeInfo);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();

        SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstTimeLogin", false);
        editor.apply();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getOffers();

    }

    private void getOffers() {

        DiscountModel model = sortedDiscount.getOffer("0");
        if (model == null || model.DiscountPercentage.equals("0.00")) {
            offerLayout.setVisibility(View.GONE);
        } else {
            offerLayout.setVisibility(View.VISIBLE);
            txtOffer.setText(model.DiscountInitial);
            Glide.with(getActivity()).load(model.DiscountImage).into(offerImage);
            txtDiscount.setText("Offer " + model.DiscountPercentage + "%");
        }
    }

    private void fetchProducts() {

        new FetchProducts(getActivity(), new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {
                Log.e("response", Functions.jsonString(responseBody));
                ProductResponse productResponse = (ProductResponse) responseBody;
                productList.addAll(productResponse.getData());
                adapter.setProducts(productResponse.getData());
                handler.savePriceSlab(productResponse.getData());
            }

            @Override
            public void onFail() {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        processProductClick(cursor);

    }

    private void processProductClick(Cursor cursor) {

        String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));

        if (productName.equalsIgnoreCase(Constants.CROWN_PRODUCT_NAME)) {

            Intent iRefill = new Intent(getActivity(), RefillActivityAnother.class);
            iRefill.putExtra("product_id", productId);
            startActivity(iRefill);

        } else {

            Intent iDetail = new Intent(getActivity(), ProductDetailActivity.class);
            iDetail.putExtra("product_id", productId);
            startActivity(iDetail);
            getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }

    }

}