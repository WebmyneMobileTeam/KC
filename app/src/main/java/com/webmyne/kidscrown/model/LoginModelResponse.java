package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 24-01-2017.
 */

public class LoginModelResponse {

    BaseResponse Response;
    LoginModelData Data;


    public BaseResponse getResponse() {
        return Response;
    }

    public void setResponse(BaseResponse response) {
        Response = response;
    }

    public LoginModelData getData() {
        return Data;
    }

    public void setData(LoginModelData data) {
        Data = data;
    }
}
