package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dhruvil on 19-08-2015.
 */
public class Product {

    @SerializedName("ProductID")
    public int productID;

    @SerializedName("Description")
    public String description;

    @SerializedName("Price")
    public int price;

    @SerializedName("ProductName")
    public String name;

    @SerializedName("ProductNumber")
    public String product_number;

    @SerializedName("lstProductImg")
    public ArrayList<ProductImage> images;

    @SerializedName("lstProductPrice")
    public ArrayList<ProductPrice> prices;


}
