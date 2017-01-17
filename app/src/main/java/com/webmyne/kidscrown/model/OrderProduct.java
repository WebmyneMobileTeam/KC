package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sagartahelyani on 24-09-2015.
 */
public class OrderProduct {

    @SerializedName("AssortDiscount")
    public int AssortDiscount;

    @SerializedName("FinalAssortPrice")
    public int FinalAssortPrice;

    @SerializedName("FinalIntroPrice")
    public int FinalIntroPrice;

    @SerializedName("FinalRefillPrice")
    public int FinalRefillPrice;

    @SerializedName("IntroDiscount")
    public int IntroDiscount;

    @SerializedName("RefillDiscount")
    public int RefillDiscount;

    @SerializedName("DiscountPercent")
    public double DiscountPercent;

    @SerializedName("FullAddress")
    public String FullAddress;

    @SerializedName("IsPaymentComplete")
    public boolean IsPaymentComplete;

    @SerializedName("IsShipped")
    public boolean IsShipped;

    @SerializedName("OrderID")
    public String OrderID;

    @SerializedName("OrderDate")
    public String OrderDate;

    @SerializedName("OrderNumber")
    public String OrderNumber;

    @SerializedName("PayableAmount")
    public int PayableAmount;

    @SerializedName("TaxAmount")
    public int TaxAmount;

    @SerializedName("TotalDiscount")
    public double TotalDiscount;

    @SerializedName("TotalAmount")
    public String TotalAmount;

    @SerializedName("InvoiceNumber")
    public String InvoiceNumber;

    @SerializedName("lstOrderProduct")
    public List<OrderProductModel> orderProducts;
}
