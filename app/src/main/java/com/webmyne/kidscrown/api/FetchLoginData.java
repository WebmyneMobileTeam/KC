package com.webmyne.kidscrown.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.AboutUsResponseModel;
import com.webmyne.kidscrown.model.LoginModelRequest;
import com.webmyne.kidscrown.model.LoginModelResponse;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vatsaldesai on 20-01-2017.
 */

public class FetchLoginData {

    private Context context;
    private AppApi appApi;
    private LoginModelRequest model;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;
    private SpotsDialog dialog;

    public FetchLoginData(Context context, LoginModelRequest model, CommonRetrofitResponseListener commonRetrofitResponseListener) {
        this.context = context;
        this.model = model;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        getLoginData();
    }

    private void getLoginData() {

        showProgress();

        Log.e("request", "" + Functions.jsonString(model));

        Call<LoginModelResponse> call = appApi.fetchLoginData(model);

        call.enqueue(new Callback<LoginModelResponse>() {
            @Override
            public void onResponse(Call<LoginModelResponse> call, Response<LoginModelResponse> response) {

                hideProgress();

                if (response.isSuccessful()) {

                    Log.e("response", MyApplication.getGson().toJson(response.body(), LoginModelResponse.class));

                    if (response.body().getResponse().getResponseCode() == Constants.SUCCESS) {

                        commonRetrofitResponseListener.onSuccess(response.body().getData());

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
            public void onFailure(Call<LoginModelResponse> call, Throwable t) {

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
            dialog = new SpotsDialog(context, "Loading products..", R.style.Custom);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

}
