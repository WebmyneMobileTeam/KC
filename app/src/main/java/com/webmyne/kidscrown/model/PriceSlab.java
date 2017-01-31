package com.webmyne.kidscrown.model;

import java.io.Serializable;

/**
 * Created by sagartahelyani on 23-01-2017.
 */

public class PriceSlab implements Serializable {

    private int MaxQty;
    private int MinQty;
    private int Price;
    private int PriceID;

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    private int ProductID;

    public int getPriceID() {
        return PriceID;
    }

    public void setPriceID(int priceID) {
        PriceID = priceID;
    }

    public int getMaxQty() {
        return MaxQty;
    }

    public void setMaxQty(int maxQty) {
        MaxQty = maxQty;
    }

    public int getMinQty() {
        return MinQty;
    }

    public void setMinQty(int minQty) {
        MinQty = minQty;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
