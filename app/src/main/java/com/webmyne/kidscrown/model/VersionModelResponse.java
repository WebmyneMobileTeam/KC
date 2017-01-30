package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 24-01-2017.
 */

public class VersionModelResponse {

    BaseResponse Response;
    VersionModel Data;

    public BaseResponse getResponse() {
        return Response;
    }

    public void setResponse(BaseResponse response) {
        Response = response;
    }

    public VersionModel getData() {
        return Data;
    }

    public void setData(VersionModel data) {
        Data = data;
    }
}
