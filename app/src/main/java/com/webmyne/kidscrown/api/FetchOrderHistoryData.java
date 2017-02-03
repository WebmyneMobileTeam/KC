package com.webmyne.kidscrown.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.LoginModelResponse;
import com.webmyne.kidscrown.model.OrderHistoryModelResponse;
import com.webmyne.kidscrown.model.UpdateProfileModelRequest;
import com.webmyne.kidscrown.model.UserProfileModelResponse;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vatsaldesai on 20-01-2017.
 */

public class FetchOrderHistoryData {

    private Context context;
    private AppApi appApi;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;
    private SpotsDialog dialog;

    public FetchOrderHistoryData(Context context, CommonRetrofitResponseListener commonRetrofitResponseListener) {
        this.context = context;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        getLoginData();
    }

    private void getLoginData() {

        showProgress();

        Call<OrderHistoryModelResponse> call = appApi.fetchOrderHistoryData(PrefUtils.getUserId(context));

        call.enqueue(new Callback<OrderHistoryModelResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryModelResponse> call, Response<OrderHistoryModelResponse> response) {

                hideProgress();

                if (response.isSuccessful()) {

                    if (response.body().getResponse().getResponseCode() == Constants.SUCCESS) {

                        commonRetrofitResponseListener.onSuccess(response.body());

                    } else {
                        commonRetrofitResponseListener.onFail();

                        Functions.showToast(context, response.body().getResponse().getResponseMsg());
                    }

                } else {
                    commonRetrofitResponseListener.onFail();

                    Functions.showToast(context, context.getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryModelResponse> call, Throwable t) {

                hideProgress();

                commonRetrofitResponseListener.onFail();

                RetrofitErrorHelper.showErrorMsg(t, context);

            }
        });

    }

    private void hideProgress() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void showProgress() {
        if (dialog == null) {
            dialog = new SpotsDialog(context, "Loading..", R.style.Custom);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

}
