package com.webmyne.kidscrown.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.LoginModelRequest;
import com.webmyne.kidscrown.model.LoginModelResponse;
import com.webmyne.kidscrown.model.UpdateProfileModelRequest;
import com.webmyne.kidscrown.model.UserProfileModelResponse;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vatsaldesai on 20-01-2017.
 */

public class FetchUpdateProfileData {

    private Context context;
    private AppApi appApi;
    private UpdateProfileModelRequest model;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;
    private SpotsDialog dialog;

    public FetchUpdateProfileData(Context context, UpdateProfileModelRequest model, CommonRetrofitResponseListener commonRetrofitResponseListener) {
        this.context = context;
        this.model = model;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        getLoginData();
    }

    private void getLoginData() {

        showProgress();

        Log.e("request", "" + Functions.jsonString(model));

        Call<UserProfileModelResponse> call = appApi.fetchUpdateProfileData(model);

        call.enqueue(new Callback<UserProfileModelResponse>() {
            @Override
            public void onResponse(Call<UserProfileModelResponse> call, Response<UserProfileModelResponse> response) {

                hideProgress();

                if (response.isSuccessful()) {

                    Log.e("response", Functions.jsonString(response));

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
            public void onFailure(Call<UserProfileModelResponse> call, Throwable t) {

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
