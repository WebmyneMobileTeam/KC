package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 24-01-2017.
 */

public class LoginModelData {

    /**
     * ClinicName : empty
     * EmailID : sagar@webmyne.com
     * FirstName : Sagar
     * IsActive : true
     * IsDelete : false
     * IsNewUser : false
     * LastName : Tahelyani
     * LoginVia : 0
     * MobileNo : 9494949494
     * MobileOS : A
     * Password : 123456
     * PriorityID : 0
     * RegistrationNumber : JJDE868243
     * Salutation : 0
     * SocialID : null
     * UserID : 50073
     * UserName : sagar
     * UserRoleID : 2
     */

    private String ClinicName;
    private String EmailID;
    private String FirstName;
    private boolean IsNewUser;
    private String LastName;
    private int LoginVia;
    private String MobileNo;
    private String MobileOS;
    private String Password;
    private String RegistrationNumber;
    private Object SocialID;
    private int UserID;
    private String UserName;

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public boolean isNewUser() {
        return IsNewUser;
    }

    public void setNewUser(boolean newUser) {
        IsNewUser = newUser;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getLoginVia() {
        return LoginVia;
    }

    public void setLoginVia(int loginVia) {
        LoginVia = loginVia;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getMobileOS() {
        return MobileOS;
    }

    public void setMobileOS(String mobileOS) {
        MobileOS = mobileOS;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRegistrationNumber() {
        return RegistrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        RegistrationNumber = registrationNumber;
    }

    public Object getSocialID() {
        return SocialID;
    }

    public void setSocialID(Object socialID) {
        SocialID = socialID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}