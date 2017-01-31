package com.webmyne.kidscrown.api;

import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.URLConstants;
import com.webmyne.kidscrown.model.AboutUsResponseModel;
import com.webmyne.kidscrown.model.ContactUsResponseModel;
import com.webmyne.kidscrown.model.ForgetPasswordModelResponse;
import com.webmyne.kidscrown.model.LoginModelRequest;
import com.webmyne.kidscrown.model.LoginModelResponse;
import com.webmyne.kidscrown.model.OrderHistoryModelResponse;
import com.webmyne.kidscrown.model.PlaceOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderResponse;
import com.webmyne.kidscrown.model.ProductResponse;
import com.webmyne.kidscrown.model.UpdateProfileModelRequest;
import com.webmyne.kidscrown.model.UserProfileModelResponse;
import com.webmyne.kidscrown.model.VersionModelResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @POST(URLConstants.UPDATE_PROFILE)
    Call<UserProfileModelResponse> fetchUpdateProfileData(@Body UpdateProfileModelRequest updateProfileModelRequest);

    @GET(URLConstants.ORDER_HISTORY)
    Call<OrderHistoryModelResponse> fetchOrderHistoryData(@Path("USERID") int USERID);

    @POST(URLConstants.PLACE_ORDER)
    Call<PlaceOrderResponse> placeOrder(@Body PlaceOrderRequest request);

    @GET(URLConstants.USER_PROFILE)
    Call<UserProfileModelResponse> fetchUserProfileData(@Path("USERID") int USERID);

    @GET(URLConstants.CHECK_VERSION)
    Call<VersionModelResponse> fetchVersionData(@Path("VERSION") String VERSION, @Path("DEVICETYPE") String DEVICETYPE);

    @GET(URLConstants.FORGOT_PASSWORD)
    Call<ForgetPasswordModelResponse> fetchForgotPasswordData(@Path("USERNAME") String USERNAME);

}
