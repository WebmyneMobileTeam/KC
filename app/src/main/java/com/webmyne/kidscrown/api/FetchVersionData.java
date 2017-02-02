package com.webmyne.kidscrown.api;

import android.content.Context;
import android.util.Log;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.UserProfileModelResponse;
import com.webmyne.kidscrown.model.VersionModelResponse;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vatsaldesai on 20-01-2017.
 */

public class FetchVersionData {

    private Context context;
    private AppApi appApi;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;
    private SpotsDialog dialog;

    public FetchVersionData(Context context, CommonRetrofitResponseListener commonRetrofitResponseListener) {
        this.context = context;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        getData();
    }

    private void getData() {

        showProgress();

        Log.e("tag", "version: " + Functions.getBuildVersion(context));

        Call<VersionModelResponse> call = appApi.fetchVersionData(Functions.getBuildVersion(context), "Android");

        call.enqueue(new Callback<VersionModelResponse>() {
            @Override
            public void onResponse(Call<VersionModelResponse> call, Response<VersionModelResponse> response) {

                hideProgress();

                if (response.isSuccessful()) {

                    Log.e("response", Functions.jsonString(response));

                    commonRetrofitResponseListener.onSuccess(response.body());

                } else {
                    commonRetrofitResponseListener.onFail();

                    Functions.showToast(context, context.getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<VersionModelResponse> call, Throwable t) {

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
