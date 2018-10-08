package com.marcozz.rob.gamingdays.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.marcozz.rob.gamingdays.NotificationUtils;
import com.marcozz.rob.gamingdays.config.Config;

import org.json.JSONObject;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = NotificationService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onNewToken(String token) {
        Log.d("APP", "Refreshed token: " + token);
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }
}
