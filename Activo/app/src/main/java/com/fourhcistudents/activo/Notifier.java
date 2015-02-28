package com.fourhcistudents.activo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by seren on 2/28/15.
 */
public class Notifier {
//
//    // sends notifications of all types
//    static public void showNotification() {
//
//        // define sound URI, the sound to be played when there's a notification
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        // intent triggered, you can add other intent for other actions
//        Intent intent = new Intent(HomeActivity.this, StartExerciseActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(HomeActivity.this, 0, intent, 0);
//
//        // this is it, we'll build the notification!
//        // in the addAction method, if you don't want any icon, just set the first param to 0
//        Notification mNotification = new Notification.Builder(this)
//
//                .setContentTitle("You've been still for a while!")
//                .setContentText("Here's an awesome exercise for you!")
//                .setSmallIcon(R.drawable.activo_logo)
//                .setContentIntent(pIntent)
//                .setSound(soundUri)
//                .setPriority(Notification.PRIORITY_MAX) // Fix for showing addAction when it's connected to PC
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        // If you want to hide the notification after it was selected, do the code below
//        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        notificationManager.notify(0, mNotification);
//    }
}