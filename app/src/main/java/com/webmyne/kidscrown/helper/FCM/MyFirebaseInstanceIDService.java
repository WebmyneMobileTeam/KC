package com.webmyne.kidscrown.helper.FCM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.webmyne.kidscrown.helper.PrefUtils;

/**
 * Created by vatsaldesai on 20-12-2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("tag", "refreshedToken:-" + refreshedToken);
        
        PrefUtils.setFCMToken(this, refreshedToken);
    }

}
