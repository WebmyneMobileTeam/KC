package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sagartahelyani on 24-09-2015.
 */
public class OrderProduct {

    @SerializedName("DiscountPercent")
    public int DiscountPercent;

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
    public String PayableAmount;

    @SerializedName("TaxAmount")
    public String TaxAmount;

    @SerializedName("TotalAmount")
    public String TotalAmount;

    @SerializedName("InvoiceNumber")
    public String InvoiceNumber;

    @SerializedName("lstOrderProduct")
    public List<OrderProductModel> orderProducts;
}
