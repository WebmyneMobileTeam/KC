package com.webmyne.kidscrown.model;

/**
 * Created by priyasindkar on 26-08-2015.
 */
public class BillingAndShippingAddress {
    private String BillingAddress1;
    private String BillingAddress2;
    private String BillingCity;
    private int BillingState;
    private String BillingCountry;
    private String BillingPincode;

    private String ShippingAddress1;
    private String ShippingAddress2;
    private String ShippingCity;
    private int ShippingState;
    private String ShippingCountry;
    private String ShippingPincode;

    public BillingAndShippingAddress() {
    }

    public BillingAndShippingAddress(String billingAddress1, String billingAddress2, String billingCity, int billingState, String billingCountry, String billingPincode, String shippingAddress1, String shippingAddress2, String shippingCity, int shippingState, String shippingCountry, String shippingPincode) {
        BillingAddress1 = billingAddress1;
        BillingAddress2 = billingAddress2;
        BillingCity = billingCity;
        BillingState = billingState;
        BillingCountry = billingCountry;
        BillingPincode = billingPincode;
        ShippingAddress1 = shippingAddress1;
        ShippingAddress2 = shippingAddress2;
        ShippingCity = shippingCity;
        ShippingState = shippingState;
        ShippingCountry = shippingCountry;
        ShippingPincode = shippingPincode;
    }


    public String getBillingAddress1() {
        return BillingAddress1;
    }

    public void setBillingAddress1(String billingAddress1) {
        BillingAddress1 = billingAddress1;
    }

    public String getBillingAddress2() {
        return BillingAddress2;
    }

    public void setBillingAddress2(String billingAddress2) {
        BillingAddress2 = billingAddress2;
    }

    public String getBillingCity() {
        return BillingCity;
    }

    public void setBillingCity(String billingCity) {
        BillingCity = billingCity;
    }

    public int getBillingState() {
        return BillingState;
    }

    public void setBillingState(int billingState) {
        BillingState = billingState;
    }

    public String getBillingCountry() {
        return BillingCountry;
    }

    public void setBillingCountry(String billingCountry) {
        BillingCountry = billingCountry;
    }

    public String getBillingPincode() {
        return BillingPincode;
    }

    public void setBillingPincode(String billingPincode) {
        BillingPincode = billingPincode;
    }

    public String getShippingAddress1() {
        return ShippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        ShippingAddress1 = shippingAddress1;
    }

    public String getShippingAddress2() {
        return ShippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        ShippingAddress2 = shippingAddress2;
    }

    public String getShippingCity() {
        return ShippingCity;
    }

    public void setShippingCity(String shippingCity) {
        ShippingCity = shippingCity;
    }

    public int getShippingState() {
        return ShippingState;
    }

    public void setShippingState(int shippingState) {
        ShippingState = shippingState;
    }

    public String getShippingCountry() {
        return ShippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        ShippingCountry = shippingCountry;
    }

    public String getShippingPincode() {
        return ShippingPincode;
    }

    public void setShippingPincode(String shippingPincode) {
        ShippingPincode = shippingPincode;
    }

    @Override
    public String toString() {
        return "BillingAndShippingAddress{" +
                "BillingAddress1='" + BillingAddress1 + '\'' +
                ", BillingAddress2='" + BillingAddress2 + '\'' +
                ", BillingCity='" + BillingCity + '\'' +
                ", BillingState='" + BillingState + '\'' +
                ", BillingCountry='" + BillingCountry + '\'' +
                ", BillingPincode='" + BillingPincode + '\'' +
                ", ShippingAddress1='" + ShippingAddress1 + '\'' +
                ", ShippingAddress2='" + ShippingAddress2 + '\'' +
                ", ShippingCity='" + ShippingCity + '\'' +
                ", ShippingState='" + ShippingState + '\'' +
                ", ShippingCountry='" + ShippingCountry + '\'' +
                ", ShippingPincode='" + ShippingPincode + '\'' +
                '}';
    }
}
