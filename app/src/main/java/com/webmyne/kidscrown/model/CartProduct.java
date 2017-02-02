package com.webmyne.kidscrown.model;

import java.io.Serializable;

/**
 * Created by sagartahelyani on 25-01-2017.
 */

public class CartProduct implements Serializable{

    private int productId;

    private String productName;

    private int qty;

    private int unitPrice;

    private int totalPrice;

    private int isSingleInt;

    public int getSpecificId() {
        return specificId;
    }

    public void setSpecificId(int specificId) {
        this.specificId = specificId;
    }

    private int specificId;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private int max;

    public int isSingle() {
        return isSingleInt;
    }

    public void setSingle(int single) {
        isSingleInt = single;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
