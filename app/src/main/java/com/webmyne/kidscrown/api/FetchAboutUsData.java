package com.webmyne.kidscrown.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.AboutUsResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vatsaldesai on 20-01-2017.
 */

public class FetchAboutUsData {

    private Context context;
    private AppApi appApi;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;

    public FetchAboutUsData(Context context, CommonRetrofitResponseListener commonRetrofitResponseListener) {
        this.context = context;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        getAboutUsData();
    }

    private void getAboutUsData() {

        Call<AboutUsResponseModel> call = appApi.fetchAboutUsData();

        call.enqueue(new Callback<AboutUsResponseModel>() {
            @Override
            public void onResponse(Call<AboutUsResponseModel> call, Response<AboutUsResponseModel> response) {

                if (response.isSuccessful()) {

                    Log.e("response", MyApplication.getGson().toJson(response.body(), AboutUsResponseModel.class));

                    if (response.body().getResponse().getResponseCode() == Constants.SUCCESS) {

                        commonRetrofitResponseListener.onSuccess(response.body());

                    } else {
                        Toast.makeText(context, response.body().getResponse().getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, context.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AboutUsResponseModel> call, Throwable t) {

                commonRetrofitResponseListener.onFail();

                RetrofitErrorHelper.showErrorMsg(t, context);

            }
        });

    }


}
