package com.webmyne.kidscrown.model;

import java.io.Serializable;

/**
 * Created by sagartahelyani on 23-01-2017.
 */

public class PriceSlab implements Serializable {

    public int MaxQty;
    public int MinQty;
    public int Price;

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
