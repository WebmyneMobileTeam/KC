package com.webmyne.kidscrown.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vatsaldesai on 25-01-2017.
 */

public class OrderHistoryModel implements Serializable{

    /**
     * Dentist : String content
     * FullAddress : String content
     * InvoiceNumber : String content
     * IsPaymentComplete : true
     * IsShipped : true
     * OrderDate : String content
     * OrderID : 9223372036854775807
     * OrderNumber : String content
     * PayableAmount : 1.2678967543233E7
     * PaymentTransactionID : String content
     * Status : String content
     * StatusID : 9223372036854775807
     * TaxAmount : 1.2678967543233E7
     * TotalAmount : 1.2678967543233E7
     * YouSaved : 1.2678967543233E7
     * lstOrderProduct : [{"Price":1.2678967543233E7,"ProductID":9223372036854775807,"ProductName":"String content","ProductNumber":"String content","Quantity":9223372036854775807}]
     */

    private String Dentist;
    private String FullAddress;
    private String InvoiceNumber;
    private boolean IsPaymentComplete;
    private boolean IsShipped;
    private String OrderDate;
    private int OrderID;
    private String OrderNumber;
    private double PayableAmount;
    private String PaymentTransactionID;
    private String Status;
    private long StatusID;
    private double TaxAmount;
    private double TotalAmount;
    private double YouSaved;
    private ArrayList<OrderProductListModel> lstOrderProduct;

    public String getDentist() {
        return Dentist;
    }

    public void setDentist(String dentist) {
        Dentist = dentist;
    }

    public String getFullAddress() {
        return FullAddress;
    }

    public void setFullAddress(String fullAddress) {
        FullAddress = fullAddress;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public boolean isPaymentComplete() {
        return IsPaymentComplete;
    }

    public void setPaymentComplete(boolean paymentComplete) {
        IsPaymentComplete = paymentComplete;
    }

    public boolean isShipped() {
        return IsShipped;
    }

    public void setShipped(boolean shipped) {
        IsShipped = shipped;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public double getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        PayableAmount = payableAmount;
    }

    public String getPaymentTransactionID() {
        return PaymentTransactionID;
    }

    public void setPaymentTransactionID(String paymentTransactionID) {
        PaymentTransactionID = paymentTransactionID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public long getStatusID() {
        return StatusID;
    }

    public void setStatusID(long statusID) {
        StatusID = statusID;
    }

    public double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        TaxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getYouSaved() {
        return YouSaved;
    }

    public void setYouSaved(double youSaved) {
        YouSaved = youSaved;
    }

    public ArrayList<OrderProductListModel> getLstOrderProduct() {
        return lstOrderProduct;
    }

    public void setLstOrderProduct(ArrayList<OrderProductListModel> lstOrderProduct) {
        this.lstOrderProduct = lstOrderProduct;
    }
}