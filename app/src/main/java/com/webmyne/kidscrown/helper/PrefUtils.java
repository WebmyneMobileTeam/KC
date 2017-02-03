package com.webmyne.kidscrown.helper;

import android.content.Context;

import com.webmyne.kidscrown.model.LoginModelData;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {

    private static final String USER_PROFILE = "USER_PROFILE";
    private static final String USER_ID = "USER_ID";
    private static final String FIRST_TIME = "FIRST_TIME";
    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String FCM_TOKEN = "FCM_TOKEN";

    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        Prefs.with(context).save(IS_LOGGED_IN, isLoggedIn);
    }

    public static boolean getLoggedIn(Context context) {
        return Prefs.with(context).getBoolean(IS_LOGGED_IN, false);
    }

    public static void setFirstTime(Context context, boolean isFirstTimeLogin){
        Prefs.with(context).save(FIRST_TIME, isFirstTimeLogin);
    }

    public static boolean getFirstTime(Context context) {
        return Prefs.with(context).getBoolean(FIRST_TIME, false);
    }

    public static void setUserProfile(Context context, LoginModelData response) {
        String toJson = MyApplication.getGson().toJson(response);

        setUserId(context, response.getUserID());

        Prefs.with(context).save(USER_PROFILE, toJson);
    }

    public static void clearUserProfile(Context context, LoginModelData response) {
        String toJson = MyApplication.getGson().toJson(response);

        setUserId(context, -1);

        Prefs.with(context).save(USER_PROFILE, toJson);

        setLoggedIn(context, false);
    }

    private static void setUserId(Context context, int userid) {
        Prefs.with(context).save(USER_ID, userid);
    }

    public static int getUserId(Context context) {
        return Prefs.with(context).getInt(USER_ID, -1);
    }

    public static LoginModelData getUserProfile(Context context) {
        LoginModelData profile = null;
        String jsonString = Prefs.with(context).getString(USER_PROFILE, "");
        try {
            profile = MyApplication.getGson().fromJson(jsonString, LoginModelData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profile;
    }

    public static void setFCMToken(Context context, String token) {
        Prefs.with(context).save(FCM_TOKEN, token);
    }

    public static String getFCMToken(Context context) {
        return Prefs.with(context).getString(FCM_TOKEN, "");
    }

}
