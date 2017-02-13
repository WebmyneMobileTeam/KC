package com.webmyne.kidscrown.api;

import android.content.Context;
import android.util.Log;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.UpdateProfileModelRequest;
import com.webmyne.kidscrown.model.UserProfileModelResponse;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vatsaldesai on 20-01-2017.
 */

public class FetchUsereProfileData {

    private Context context;
    private AppApi appApi;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;
    private SpotsDialog dialog;

    public FetchUsereProfileData(Context context, CommonRetrofitResponseListener commonRetrofitResponseListener) {
        this.context = context;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        getData();
    }

    private void getData() {

        showProgress();

        Log.e("request", "userId: " + PrefUtils.getUserId(context));

        Call<UserProfileModelResponse> call = appApi.fetchUserProfileData(PrefUtils.getUserId(context));

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
            dialog = new SpotsDialog(context, "Loading..", R.style.Custom);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
