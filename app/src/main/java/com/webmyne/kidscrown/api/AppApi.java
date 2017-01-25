package com.webmyne.kidscrown.api;

import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.URLConstants;
import com.webmyne.kidscrown.model.AboutUsResponseModel;
import com.webmyne.kidscrown.model.ContactUsResponseModel;
import com.webmyne.kidscrown.model.LoginModelRequest;
import com.webmyne.kidscrown.model.LoginModelResponse;
import com.webmyne.kidscrown.model.ProductResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by sagartahelyani on 26-08-2016.
 */
public interface AppApi {

    @GET(Constants.ABOUT_US_URL)
    Call<AboutUsResponseModel> fetchAboutUsData();

    @GET(Constants.CONTACT_US_URL)
    Call<ContactUsResponseModel> fetchContactUsData();

    @GET(URLConstants.GET_PRODUCTS)
    Call<ProductResponse> fetchProducts();

    @POST(URLConstants.LOGIN)
    Call<LoginModelResponse> fetchLoginData(@Body LoginModelRequest loginModelRequest);

}
