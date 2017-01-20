package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 19-01-2017.
 */

public class BaseResponse {

    private int ResponseCode;
    private String ResponseMsg;

    public int getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(int ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getResponseMsg() {
        return ResponseMsg;
    }

    public void setResponseMsg(String ResponseMsg) {
        this.ResponseMsg = ResponseMsg;
    }
}
