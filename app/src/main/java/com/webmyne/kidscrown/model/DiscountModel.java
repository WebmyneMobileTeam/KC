package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sagartahelyani on 18-12-2015.
 */
public class DiscountModel {

    @SerializedName("DiscountInitial")
    public String DiscountInitial;

    @SerializedName("DiscountPercentage")
    public float DiscountPercentage;

    @SerializedName("DiscountImage")
    public String DiscountImage;

}
