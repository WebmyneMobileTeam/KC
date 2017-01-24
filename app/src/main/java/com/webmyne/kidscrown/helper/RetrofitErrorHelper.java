package com.webmyne.kidscrown.helper;

import android.content.Context;
import android.widget.Toast;

import com.webmyne.kidscrown.R;

import java.util.concurrent.TimeoutException;

/**
 * Created by sagartahelyani on 13-09-2016.
 */
public class RetrofitErrorHelper {

    public static void showErrorMsg(Throwable t, Context context) {

        if (t instanceof TimeoutException) {
            Functions.showToast(context, context.getString(R.string.time_out));
        } else {
            Functions.showToast(context, context.getString(R.string.try_again));
        }
    }
}
