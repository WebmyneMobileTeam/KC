package com.webmyne.kidscrown.model;

/**
 * Created by sagartahelyani on 26-08-2015.
 */
public class CrownPricing {

    int Price, MinQty, MaxQty, PriceID, ProductID;

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getMinQty() {
        return MinQty;
    }

    public void setMinQty(int minQty) {
        MinQty = minQty;
    }

    public int getMaxQty() {
        return MaxQty;
    }

    public void setMaxQty(int maxQty) {
        MaxQty = maxQty;
    }

    public int getPriceID() {
        return PriceID;
    }

    public void setPriceID(int priceID) {
        PriceID = priceID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }
}
