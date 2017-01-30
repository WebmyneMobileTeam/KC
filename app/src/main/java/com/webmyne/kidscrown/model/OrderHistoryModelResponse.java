package com.webmyne.kidscrown.model;

import java.util.ArrayList;

/**
 * Created by vatsaldesai on 25-01-2017.
 */

public class OrderHistoryModelResponse {

    private BaseResponse Response;

    private ArrayList<OrderHistoryModel> Data;

    public BaseResponse getResponse() {
        return Response;
    }

    public void setResponse(BaseResponse response) {
        Response = response;
    }

    public ArrayList<OrderHistoryModel> getData() {
        return Data;
    }

    public void setData(ArrayList<OrderHistoryModel> data) {
        Data = data;
    }
}
