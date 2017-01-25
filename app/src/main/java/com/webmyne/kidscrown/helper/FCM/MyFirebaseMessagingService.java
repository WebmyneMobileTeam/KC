package com.webmyne.kidscrown.helper.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webmyne.kidscrown.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by vatsaldesai on 20-12-2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            JSONObject obj = new JSONObject("" + remoteMessage.getData().get("body"));
            Log.e("FCM MSG", "" + obj.toString());
            sendNotification(remoteMessage.getData().get("body"));
        } catch (JSONException e) {
            Log.e("FCM exc", "" + e.toString());
        }

        // Check if message contains a data payload.
       /* if (remoteMessage.getData().size() > 0) {
            Log.e("tag", "Message data payload: " + remoteMessage.getData());
            Log.e("tag", "Message data body: " + remoteMessage.getData().get("body"));
//
        }
*/
    }

    private void sendNotification(String body) {

        try {

            JSONObject jsonObject = new JSONObject(body);

            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("desc");

            Intent intent = null;

//            if (PrefUtils.getLoggedIn(this)) {
//                intent = new Intent(this, DashboardActivity.class);
//            } else {
//                intent = new Intent(this, LoginActivity.class);
//            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification;
            notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(title)
//                .setWhen(0)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentText(desc)
                    .build();

//        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))/*Notification icon image*/
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(body)
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)))/*Notification with Image*/
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);

//        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Notification n = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(body)
//                .setAutoCancel(true)
//                .setSound(notificationSoundURI)
//                .setContentIntent(resultIntent);

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(m, notification);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
