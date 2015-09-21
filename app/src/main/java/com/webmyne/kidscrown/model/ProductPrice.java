package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhruvil on 20-08-2015.
 */
public class ProductPrice {

    @SerializedName("ProductID")
    public int product_id;

    @SerializedName("PriceID")
    public int price_id;

    @SerializedName("Price")
    public float price;

    @SerializedName("MinQty")
    public int min;

    @SerializedName("MaxQty")
    public int max;

}
