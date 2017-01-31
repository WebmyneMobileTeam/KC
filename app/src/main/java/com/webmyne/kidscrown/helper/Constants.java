package com.webmyne.kidscrown.helper;


public class Constants {

    public static final int SINGLE = 1;
    public static final int NOT_SINGLE = 0;

    public static final String PREF_NAME = "user_pref";

    // Login with
    public static final String NORMAL = "1";
    public static final String FB = "2";
    public static final String GPLUS = "3";

    // product ids
    public static final String INTRO = "14";
    public static final String ASSORTED = "15";
    public static final String CROWN = "16";
    public static final String INVOICE = "0";

    public static final int shipping_cost = 100;

    public static final int SUCCESS = 1;

    public static final String UL = "UL";
    public static final String UR = "UR";
    public static final String LL = "LL";
    public static final String LR = "LR";

    public static final String UL_STR = "Upper Left";
    public static final String UR_STR = "Upper Right";
    public static final String LL_STR = "Lower Left";
    public static final String LR_STR = "Lower Right";

    // utils
    public static final boolean LOGGING_ENABLED = true;
    public static final String TAG = "KidsCrown";

    //    public static final String BASE_URL = "http://ws.kidscrown.in/Services";
    private static final String BASE_URL = "http://ws-srv-net.in.webmyne.com/Applications/KidsCrown/KidsCrownWS_V01/Services";
    public static final String BASE_URL_V03 = "http://ws-srv-net.in.webmyne.com/Applications/KidsCrown_V03/WCF/Services/";
//    public static final String BASE_URL = "http://ws.kidscrown.in/Services";

    public static final String REGISTRATION_URL = BASE_URL + "/User.svc/json/UserRegistration";
    public static final String LOGIN_URL = BASE_URL + "/User.svc/json/UserLogin/";
    public static final String SOCIAL_MEDIA_LOGIN_URL = BASE_URL + "/User.svc/json/LoginWithSocialMedia/";
    //    public static final String SOCIAL_MEDIA_SIGN_UP = BASE_URL + "/User.svc/json/SignUpWithSocialMedia/";
    public static final String SOCIAL_MEDIA_SIGN_UP = BASE_URL + "/User.svc/json/SignUpWithSocialMediaWithMobileOS/";

    public static final String SALUTATION_URL = BASE_URL + "/User.svc/json/FetchAllCodeCommonList/Salutation";
    public static final String QUALIFICATION_URL = BASE_URL + "/User.svc/json/FetchAllCodeCommonList/Qualification";
    public static final String FETCH_PRODUCTS = BASE_URL + "/Master.svc/json/FetchCurrentPricingForMobile";
    public static final String UPDATE_URL = BASE_URL + "/User.svc/json/UpdateUserProfile";
    public static final String GET_EXISTING_ADDRESS = BASE_URL + "/Master.svc/json/FetchShippingAddresses";

    public static final String GET_OFFERS = BASE_URL + "/Order.svc/json/GetRecentDiscount";

    public static final String SAVE_ADDRESS_URL = BASE_URL + "/Master.svc/json/AddNewShippingAddress";

    public static final String PLACE_ORDER = BASE_URL + "/Order.svc/json/PlaceOrder";

    public static final String FORGOT_PASSWORD_URL = BASE_URL + "/User.svc/json/ForgotPassword/";

    public static final String CROWN_PRODUCT_NAME = "Refill";

    public static final String FETCH_ORDER = BASE_URL + "/Order.svc/json/FetchOrdersApp?UserId=";

    public static final String ABOUT_US_URL = "Home.svc/json/GetAboutUsDetail";

    public static final String CONTACT_US_URL = "Home.svc/json/GetPageDetails";

    public static String regNoPattern = "^[A-Z][0-9]{6}$";


}
