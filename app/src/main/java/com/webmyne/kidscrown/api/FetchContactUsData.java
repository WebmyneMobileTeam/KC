package com.webmyne.kidscrown.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.ContactUsResponseModel;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vatsaldesai on 20-01-2017.
 */

public class FetchContactUsData {

    private Context context;
    private AppApi appApi;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;
    private SpotsDialog dialog;

    public FetchContactUsData(Context context, CommonRetrofitResponseListener commonRetrofitResponseListener) {
        this.context = context;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        getContactUsData();
    }

    private void getContactUsData() {

        showProgress();

        Call<ContactUsResponseModel> call = appApi.fetchContactUsData();

        call.enqueue(new Callback<ContactUsResponseModel>() {
            @Override
            public void onResponse(Call<ContactUsResponseModel> call, Response<ContactUsResponseModel> response) {

                hideProgress();

                if (response.isSuccessful()) {

                    Log.e("response", MyApplication.getGson().toJson(response.body(), ContactUsResponseModel.class));

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
            public void onFailure(Call<ContactUsResponseModel> call, Throwable t) {

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
