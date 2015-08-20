package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sagartahelyani on 20-08-2015.
 */
public class Address {

    @SerializedName("Address1")
    public String Address1;

    @SerializedName("Address2")
    public String Address2;

    @SerializedName("AddressID")
    public int AddressID;

    @SerializedName("CityID")
    public int CityID;

    @SerializedName("CountryID")
    public int CountryID;

    @SerializedName("MobileNo")
    public String MobileNo;

    @SerializedName("PinCode")
    public int PinCode;

    @SerializedName("StateID")
    public int StateID;

    @SerializedName("UserID")
    public int UserID;


}
