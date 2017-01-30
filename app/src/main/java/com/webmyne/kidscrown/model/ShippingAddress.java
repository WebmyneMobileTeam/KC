package com.webmyne.kidscrown.model;

/**
 * Created by sagartahelyani on 27-01-2017.
 */

public class ShippingAddress {

    /**
     * Address1 : String content
     * Address2 : String content
     * City : String content
     * Country : String content
     * Email : String content
     * IsUpdated : true
     * MobileNo : String content
     * PinCode : String content
     * ShippingAddressID : 9223372036854775807
     */

    private String Address1;
    private String Address2;
    private String City;
    private String Country;
    private String Email;
    private boolean IsUpdated;
    private String MobileNo;
    private String PinCode;
    private long ShippingAddressID;

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public boolean isIsUpdated() {
        return IsUpdated;
    }

    public void setIsUpdated(boolean IsUpdated) {
        this.IsUpdated = IsUpdated;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo) {
        this.MobileNo = MobileNo;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String PinCode) {
        this.PinCode = PinCode;
    }

    public long getShippingAddressID() {
        return ShippingAddressID;
    }

    public void setShippingAddressID(long ShippingAddressID) {
        this.ShippingAddressID = ShippingAddressID;
    }
}
