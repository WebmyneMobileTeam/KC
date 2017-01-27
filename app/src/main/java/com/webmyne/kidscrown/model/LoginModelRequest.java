package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 24-01-2017.
 */

public class LoginModelRequest {

    /**
     * ClinicName :
     * DeviceID : abcd
     * EmailID :
     * FirstName :
     * GCMToken : abcd
     * LastName :
     * LoginVia : 1
     * MobileNo :
     * MobileOS : A
     * Password : 123456
     * RegistrationNumber :
     * SocialID :
     * UserName : sagar
     */

    private String ClinicName;
    private String DeviceID;
    private String EmailID;
    private String FirstName;
    private String GCMToken;
    private String LastName;
    private String LoginVia;
    private String MobileNo;
    private String MobileOS;
    private String Password;
    private String RegistrationNumber;
    private String SocialID;
    private String UserName;
    private int UserID;

//    public LoginModelRequest() {
//        ClinicName = "";
//        DeviceID = "A342E9D0-FFFF-416E-9B55-0B4FD4E1C36A";
//        EmailID = "jj@gmail.com";
//        FirstName = "";
//        GCMToken = "0";
//        LastName = "";
//        LoginVia = "1";
//        MobileNo = "";
//        MobileOS = "A";
//        Password = "123";
//        RegistrationNumber = "";
//        SocialID = "0";
//        UserName = "";
//    }

    public LoginModelRequest() {
        ClinicName = "";
        DeviceID = "";
        EmailID = "";
        FirstName = "";
        GCMToken = "";
        LastName = "";
        LoginVia = "0";
        MobileNo = "";
        MobileOS = "";
        Password = "";
        RegistrationNumber = "";
        SocialID = "";
        UserName = "";
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String ClinicName) {
        this.ClinicName = ClinicName;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String DeviceID) {
        this.DeviceID = DeviceID;
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

    public String getGCMToken() {
        return GCMToken;
    }

    public void setGCMToken(String GCMToken) {
        this.GCMToken = GCMToken;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getLoginVia() {
        return LoginVia;
    }

    public void setLoginVia(String LoginVia) {
        this.LoginVia = LoginVia;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo) {
        this.MobileNo = MobileNo;
    }

    public String getMobileOS() {
        return MobileOS;
    }

    public void setMobileOS(String MobileOS) {
        this.MobileOS = MobileOS;
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

    public String getSocialID() {
        return SocialID;
    }

    public void setSocialID(String SocialID) {
        this.SocialID = SocialID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
}
