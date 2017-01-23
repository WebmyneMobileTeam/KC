package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 19-01-2017.
 */

public class AboutUsResponseModel {

    private BaseResponse Response;
    private AboutUsData Data;

    public BaseResponse getResponse() {
        return Response;
    }

    public void setResponse(BaseResponse response) {
        Response = response;
    }

    public AboutUsData getData() {
        return Data;
    }

    public void setData(AboutUsData data) {
        Data = data;
    }
}
