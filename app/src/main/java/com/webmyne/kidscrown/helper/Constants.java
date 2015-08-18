package com.webmyne.kidscrown.helper;

/**
 * @author jatin
 */
public class Constants {

    // utils
    public static final boolean LOGGING_ENABLED = true;
    public static final String TAG = "KidsCrown";
    public static final String BASE_URL = "http://ws-srv-net.in.webmyne.com/Applications/KidsCrown/KidsCrownWS_V01/Services";
    public static final String REGISTRATION_URL = BASE_URL + "/User.svc/json/UserRegistration";
    public static final String LOGIN_URL = BASE_URL + "/User.svc/json/UserLogin/";
    public static final String SALUTATION_URL = BASE_URL + "/User.svc/json/FetchAllCodeCommonList/Salutation";
    public static final String QUALIFICATION_URL = BASE_URL + "/User.svc/json/FetchAllCodeCommonList/Qualification";

    //Request
    public static final int REQUEST_ERROR_GENERAL = -1;
    public static final int REQUEST_OK = 0;
    public static final int REQUEST_LOGIN = 1;
    public static final int REQUEST_FBLOGIN = 2;
    public static final int REQUEST_GMAILLOGIN = 3;
    public static final int REQUEST_FORGOT_PASSWORD = 4;


    public static String IMAGE_PREFIX = "http://srijanapp.com/files/profile/";

}