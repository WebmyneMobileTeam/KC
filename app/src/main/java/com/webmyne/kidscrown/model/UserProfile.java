package com.webmyne.kidscrown.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sagartahelyani on 17-08-2015.
 */
public class UserProfile {
    @SerializedName("EmailID")
    public String EmailID;

    @SerializedName("FirstName")
    public String FirstName;

    @SerializedName("LastName")
    public String LastName;

    @SerializedName("MobileNo")
    public String MobileNo;

    @SerializedName("Password")
    public String Password;

    @SerializedName("Qualification")
    public String Qualification;

    @SerializedName("RegistrationNumber")
    public String RegistrationNumber;

    @SerializedName("Salutation")
    public String Salutation;

    @SerializedName("UserName")
    public String UserName;

    @SerializedName("UserID")
    public String UserID;
}
