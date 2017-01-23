package com.webmyne.kidscrown.model;

import java.util.ArrayList;

/**
 * Created by vatsaldesai on 19-01-2017.
 */

public class ContactUsResponseModel {

    private BaseResponse Response;
    private ArrayList<ContactUsDataModel> Data;

    public BaseResponse getResponse() {
        return Response;
    }

    public void setResponse(BaseResponse response) {
        Response = response;
    }

    public ArrayList<ContactUsDataModel> getData() {
        return Data;
    }

    public void setData(ArrayList<ContactUsDataModel> data) {
        Data = data;
    }
}
