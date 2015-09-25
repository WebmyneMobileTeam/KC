package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sagartahelyani on 24-09-2015.
 */
public class TotalOrder {

    @SerializedName("Data")
    public List<OrderProduct> data;
}
