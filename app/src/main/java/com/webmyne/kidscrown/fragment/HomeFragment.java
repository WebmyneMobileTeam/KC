package com.webmyne.kidscrown.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.ProductAdapter;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.GetSortedDiscount;
import com.webmyne.kidscrown.model.DiscountModel;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.ui.MyDrawerActivity;
import com.webmyne.kidscrown.ui.ProductDetailActivity;
import com.webmyne.kidscrown.ui.RefillActivityAnother;
import com.webmyne.kidscrown.ui.RegisterActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listProducts;
    private ProductAdapter adapter;
    private ImageView closeInfo;
    private Dialog dialog;
    private LinearLayout offerLayout;
    private TextView txtOffer, txtDiscount;
    private ImageView offerImage;
    private DatabaseHandler handler;
    private GetSortedDiscount sortedDiscount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);

        return view;
    }

    private void init(View v) {
        offerImage = (ImageView) v.findViewById(R.id.offerImage);
        txtDiscount = (TextView) v.findViewById(R.id.txtDiscount);
        txtOffer = (TextView) v.findViewById(R.id.txtOffer);
        offerLayout = (LinearLayout) v.findViewById(R.id.offerLayout);
        listProducts = (ListView) v.findViewById(R.id.listProducts);
        listProducts.setOnItemClickListener(this);
        listProducts.setEmptyView(v.findViewById(R.id.txtLoading));
        ((MyDrawerActivity) getActivity()).setTitle("Products");

        handler = new DatabaseHandler(getActivity());
        sortedDiscount = new GetSortedDiscount(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        displayProducts();

        SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        if (preferences.getBoolean("isFirstTimeLogin", true)) {
            displayDialogForFirstTime();
            closeInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        fetchProducts();
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

    private void displayProducts() {

        try {
            handler.openDataBase();
            Cursor cursor = handler.getProductsCursor();
            handler.close();
            adapter = new ProductAdapter(getActivity(), cursor, 0);
            listProducts.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fetchProducts() {

        new CallWebService(Constants.FETCH_PRODUCTS, CallWebService.TYPE_GET) {
            @Override
            public void response(String response) {

                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                ArrayList<Product> products = new GsonBuilder().create().fromJson(response, listType);

                for (Product p : products) {
                    if (p.name.equals(Constants.CROWN_PRODUCT_NAME)) {
                        SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("crownProductId", p.productID);
                        editor.apply();
                    }
                }
                handler.saveProducts(products);
                handler.close();
                displayProducts();

            }

            @Override
            public void error(String error) {
                // helper.hideProgress();
                Log.e("Error", error);

            }
        }.call();

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