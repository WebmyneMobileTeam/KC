package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 25-01-2017.
 */

public class UserProfileModel {

    /**
     * ClinicName : String content
     * EmailID : String content
     * FirstName : String content
     * LastName : String content
     * MobileNo : String content
     * Password : String content
     * RegistrationNumber : String content
     * UserID : 9223372036854775807
     * UserName : String content
     */

    private String ClinicName;
    private String EmailID;
    private String FirstName;
    private String LastName;
    private String MobileNo;
    private String Password;
    private String RegistrationNumber;
    private int UserID;
    private String UserName;

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String ClinicName) {
        this.ClinicName = ClinicName;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String EmailID) {
        this.EmailID = EmailID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo) {
        this.MobileNo = MobileNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getRegistrationNumber() {
        return RegistrationNumber;
    }

    public void setRegistrationNumber(String RegistrationNumber) {
        this.RegistrationNumber = RegistrationNumber;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
}
