package com.webmyne.kidscrown.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.ProductAdapter;
import com.webmyne.kidscrown.api.AppApi;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchProducts;
import com.webmyne.kidscrown.custom.EmptyLayout;
import com.webmyne.kidscrown.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.CountryResponse;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.ProductResponse;
import com.webmyne.kidscrown.ui.MyDrawerActivity;
import com.webmyne.kidscrown.ui.ProductDetailActivity;
import com.webmyne.kidscrown.ui.RefillActivityAnother;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FamiliarRecyclerView productRV;
    private ProductAdapter adapter;
    private ImageView closeInfo;
    private Dialog dialog;
    private ImageView offerImage;
    private DatabaseHandler handler;
    private View parentView;
    private EmptyLayout emptyLayout;
    private SpotsDialog progressDialog;

    private ArrayList<Product> productList;

    // state
    private AppApi appApi;
    private ComplexPreferences complexPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        return parentView;
    }

    private void init() {
        appApi = MyApplication.getRetrofit().create(AppApi.class);
        complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), Constants.PREF_NAME, 0);

        offerImage = (ImageView) parentView.findViewById(R.id.offerImage);

        ((MyDrawerActivity) getActivity()).setTitle("Products");

        handler = new DatabaseHandler(getActivity());

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
                if (product.getIsSingleInt() == Constants.SINGLE) {
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
            Functions.showToast(getActivity(), getActivity().getString(R.string.no_internet));

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

        PrefUtils.setFirstTime(getActivity(), false);

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

                if (!TextUtils.isEmpty(productResponse.getRootImage().getImage())) {
                    offerImage.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(productResponse.getRootImage().getImage()).into(offerImage);
                } else {
                    offerImage.setVisibility(View.GONE);
                }

                fetchState();
            }

            @Override
            public void onFail() {

            }
        });

    }

    private void fetchState() {
        showProgress();

        Call<CountryResponse> call = appApi.fetchState();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {

                    if (response.body().getResponse().getResponseCode() == Constants.SUCCESS) {

                        CountryResponse countryResponse = response.body();
                        complexPreferences.putObject("state", countryResponse);
                        complexPreferences.commit();

                    } else {
                        Functions.showToast(getActivity(), response.body().getResponse().getResponseMsg());
                    }

                } else {
                    Functions.showToast(getActivity(), getActivity().getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                hideProgress();
                RetrofitErrorHelper.showErrorMsg(t, getActivity());
            }
        });
    }

    private void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new SpotsDialog(getActivity(), "Loading..", R.style.Custom);
        }
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
}