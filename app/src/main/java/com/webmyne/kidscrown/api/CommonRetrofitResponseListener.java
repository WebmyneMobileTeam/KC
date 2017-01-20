package com.webmyne.kidscrown.api;

/**
 * Created by vatsaldesai on 20-01-2017.
 */

public interface CommonRetrofitResponseListener {

    void onSuccess(Object responseBody);

    void onFail();
}
