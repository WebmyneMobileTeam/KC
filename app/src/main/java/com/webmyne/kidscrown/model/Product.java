package com.webmyne.kidscrown.model;

import android.content.res.Resources;

import com.google.gson.annotations.SerializedName;
import com.webmyne.kidscrown.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dhruvil on 19-08-2015.
 */
public class Product implements Serializable {

    // new Data
    private String Description;
    private int DiscountPercentage;

    @SerializedName("IsSingle")
    private boolean IsSingle;

    public int getIsSingleInt() {
        return IsSingleInt;
    }

    public void setIsSingleInt(int isSingleInt) {
        IsSingleInt = isSingleInt;
    }

    @SerializedName("IsSingleInt")
    private int IsSingleInt;

    private int OrderLimit;
    private int ProductID;
    private String ProductName;
    private String RootImage;
    private ArrayList<PriceSlab> priceSlabDCs;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public boolean isSingle() {
        return IsSingle;
    }

    public void setSingle(boolean single) {
        IsSingle = single;
    }

    public int getOrderLimit() {
        return OrderLimit;
    }

    public void setOrderLimit(int orderLimit) {
        OrderLimit = orderLimit;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getRootImage() {
        return RootImage;
    }

    public void setRootImage(String rootImage) {
        RootImage = rootImage;
    }

    public ArrayList<PriceSlab> getPriceSlabDCs() {
        return priceSlabDCs;
    }

    public void setPriceSlabDCs(ArrayList<PriceSlab> priceSlabDCs) {
        this.priceSlabDCs = priceSlabDCs;
    }

}
