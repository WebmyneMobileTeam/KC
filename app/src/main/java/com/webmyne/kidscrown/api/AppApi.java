package com.webmyne.kidscrown.api;

import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.model.AboutUsResponseModel;
import com.webmyne.kidscrown.model.ContactUsResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sagartahelyani on 26-08-2016.
 */
public interface AppApi {

    @GET(Constants.ABOUT_US_URL)
    Call<AboutUsResponseModel> fetchAboutUsData();

    @GET(Constants.CONTACT_US_URL)
    Call<ContactUsResponseModel> fetchContactUsData();


}
