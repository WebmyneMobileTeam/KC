package com.webmyne.kidscrown.helper;

/**
 * @author jatin
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.droidbyme.toastlib.ToastEnum;
import com.droidbyme.toastlib.ToastLib;
import com.webmyne.kidscrown.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Functions {

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static void logE(final String tag, final String logMsg) {
        if (Constants.LOGGING_ENABLED)
            if (logMsg.length() > 4000) {
                Log.e(tag, logMsg.substring(0, 4000));
                logE(tag, logMsg.substring(4000));
            } else
                Log.e(tag, logMsg);
    }

    public static void displayMessage(Context ctx, String msg) {

        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

    }

    public static void fireIntent(Activity activity, Class cls) {

        Intent i = new Intent(activity, cls);
        activity.startActivity(i);

    }

    public static boolean emailValidation(String email) {
        boolean validEmailAddress = true;
        if (email.length() == 0) {
            validEmailAddress = false;
        } else {
            if (!email.contains(".") || !email.contains("@")) {
                validEmailAddress = false;
            } else {
                int index1 = email.indexOf("@");
                String subStringType = email.substring(index1);
                int index2 = index1 + subStringType.indexOf(".");
                if (index1 == 0 || index2 == 0) {
                    validEmailAddress = false;
                } else {
                    String typeOf = email.substring(index1, index2);
                    if (typeOf.length() < 1) {
                        validEmailAddress = false;
                    }
                    String typeOf2 = email.substring(index2);
                    if (typeOf2.length() < 2) {
                        validEmailAddress = false;
                    }
                }

            }
        }

        return validEmailAddress;
    }

    public static String parseDate(String inputDate, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void snack(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }

    public static String jsonString(Object obj) {
        return "" + MyApplication.getGson().toJson(obj);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static int getBgColor(Context context, int position) {
        ArrayList<Integer> colors;
        colors = new ArrayList();
        int pos = position;
        colors.add(ContextCompat.getColor(context, R.color.quad_green));
        colors.add(ContextCompat.getColor(context, R.color.quad_violate));
        colors.add(ContextCompat.getColor(context, R.color.quad_orange));
        colors.add(ContextCompat.getColor(context, R.color.quad_blue));

        if (pos >= colors.size() - 1) {
            pos = 0;
        } else {
            pos = pos + 1;
        }

        return colors.get(pos);
    }

    public static void showToast(Context context, String message) {
        new ToastLib.Builder(context, message)
                .duration(ToastEnum.SHORT)
                .backgroundColor(ContextCompat.getColor(context, R.color.color_button_darkred))
                .textColor(ContextCompat.getColor(context, R.color.white))
                .textSize(16)
                .corner(12)
                .margin(128)
                .padding(22)
                .spacing(1)
                .gravity(Gravity.BOTTOM)
                .show();
    }

    public static String getStr(EditText editText){
        return editText.getText().toString().trim();
    }

    public static String priceFormat(int amount){
        DecimalFormat formatter;
        formatter = new DecimalFormat("#,##,###");
        return formatter.format(amount);
    }

    public static String getBuildVersion(Context context) {
        String version = null;

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static void showSimplePrompt(Context context, String title, String msg, String positiveText, String negativeText, final SuccessFailListener successFailListener) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setCancelable(false);

        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }

        dialog.setMessage(msg);

        if (!TextUtils.isEmpty(positiveText)) {
            dialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                    successFailListener.Success();
                }
            });
        }

        if (!TextUtils.isEmpty(negativeText)) {
            dialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                    successFailListener.Fail();
                }
            });
        }

//        dialog.setIcon(android.R.drawable.ic_dialog_alert);

        dialog.show();

    }

}
