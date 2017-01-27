package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 24-01-2017.
 */

public class UserProfileModelResponse {

    BaseResponse Response;
    UserProfileModel Data;

    public BaseResponse getResponse() {
        return Response;
    }

    public void setResponse(BaseResponse response) {
        Response = response;
    }

    public UserProfileModel getData() {
        return Data;
    }

    public void setData(UserProfileModel data) {
        Data = data;
    }
}
