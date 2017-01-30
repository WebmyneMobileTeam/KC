package com.webmyne.kidscrown.api;

import android.content.Context;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.PlaceOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderResponse;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagartahelyani on 27-01-2017.
 */

public class PlaceOrderApi {

    private Context context;
    private AppApi appApi;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;
    private SpotsDialog dialog;
    private PlaceOrderRequest placeOrderRequest;

    public PlaceOrderApi(Context context, CommonRetrofitResponseListener commonRetrofitResponseListener, PlaceOrderRequest placeOrderRequest) {
        this.context = context;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;
        this.placeOrderRequest = placeOrderRequest;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        doCallPlaceOrder();
    }

    private void doCallPlaceOrder() {
        showProgress();

        Call<PlaceOrderResponse> call = appApi.placeOrder(placeOrderRequest);
        call.enqueue(new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {

                    if (response.body().getResponse().getResponseCode() == Constants.SUCCESS) {

                        commonRetrofitResponseListener.onSuccess(response.body());

                    } else {
                        Functions.showToast(context, response.body().getResponse().getResponseMsg());
                    }

                } else {
                    Functions.showToast(context, context.getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {
                hideProgress();

                commonRetrofitResponseListener.onFail();

                RetrofitErrorHelper.showErrorMsg(t, context);
            }
        });

    }

    private void showProgress() {
        if (dialog == null) {
            dialog = new SpotsDialog(context, "Loading..", R.style.Custom);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideProgress() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
