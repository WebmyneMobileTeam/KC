package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sagartahelyani on 24-09-2015.
 */
public class OrderProductModel {

    @SerializedName("Price")
    public String Price;

    @SerializedName("ProductName")
    public String ProductName;

    @SerializedName("ProductNumber")
    public String ProductNumber;

    @SerializedName("Quantity")
    public String Quantity;
}
