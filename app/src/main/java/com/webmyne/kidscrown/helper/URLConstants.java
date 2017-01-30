package com.webmyne.kidscrown.helper;

/**
 * Created by sagartahelyani on 23-01-2017.
 */

public class URLConstants {

    public static final String BASE_URL_V03 = "http://ws-srv-net.in.webmyne.com/Applications/KidsCrown_V03/WCF/Services/";

    public static final String GET_PRODUCTS = "Product.svc/json/GetProducts";

    public static final String PLACE_ORDER = "Product.svc/json/PlaceOrder";

    public static final String LOGIN = "User.svc/json/LoginSignup";

    public static final String UPDATE_PROFILE = "User.svc/json/UpdateUserProfile";

    public static final String ORDER_HISTORY = "Product.svc/json/OrderHistory/{USERID}";

    public static final String USER_PROFILE = "User.svc/json/UserProfile/{USERID}";

    public static final String CHECK_VERSION = "User.svc/json/GetDeviceInfo/{VERSION}/{DEVICETYPE}";

}
