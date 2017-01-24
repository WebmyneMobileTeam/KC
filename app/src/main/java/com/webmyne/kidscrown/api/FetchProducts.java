package com.webmyne.kidscrown.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.MyApplication;
import com.webmyne.kidscrown.helper.RetrofitErrorHelper;
import com.webmyne.kidscrown.model.ProductResponse;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagartahelyani on 23-01-2017.
 */

public class FetchProducts {

    private Context context;
    private AppApi appApi;
    private CommonRetrofitResponseListener commonRetrofitResponseListener;
    private SpotsDialog dialog;

    public FetchProducts(Context context, CommonRetrofitResponseListener commonRetrofitResponseListener) {
        this.context = context;
        this.commonRetrofitResponseListener = commonRetrofitResponseListener;

        appApi = MyApplication.getRetrofit().create(AppApi.class);

        getProducts();
    }

    private void getProducts() {

        showProgress();

        Call<ProductResponse> call = appApi.fetchProducts();

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                hideProgress();

                if (response.isSuccessful()) {

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
            public void onFailure(Call<ProductResponse> call, Throwable t) {

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
            dialog = new SpotsDialog(context, "", R.style.Custom);
        }
    }

}
