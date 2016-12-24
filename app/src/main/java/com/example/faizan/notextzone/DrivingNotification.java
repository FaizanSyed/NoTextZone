package com.example.faizan.notextzone;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class DrivingNotification {
    private static NotificationCompat.Builder drivingNotifBldr;
    private static int drivingNotifId;
    private static NotificationManager drivingNotifMgr;
    private static Intent toMain;
    private static PendingIntent pendingToMain;

    public static void createNotif(Context context){

        drivingNotifId = 5;

        //Create Intent and PendingIntent for Notification Receiver
        Intent toNotificationReceiver = new Intent(context, NotificationReceiver.class);
        PendingIntent pToNitificationReceiver = PendingIntent.getBroadcast(context,1,toNotificationReceiver,PendingIntent.FLAG_UPDATE_CURRENT);


        //Create builder and manager
        drivingNotifBldr = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.steeringwheel)
                .setContentTitle("No Text Zone")
                .setContentText("Driving mode it ON!")
                .addAction(R.drawable.smallmanwalking, "Turn OFF driving mode", pToNitificationReceiver)
                .setOngoing(true);
        drivingNotifMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Create Intent and PendingIntent and setContentIntent
        toMain = new Intent(context, MainActivity.class);
        pendingToMain = PendingIntent.getActivity(context, 0, toMain, PendingIntent.FLAG_UPDATE_CURRENT);
        drivingNotifBldr.setContentIntent(pendingToMain);

        //Issue Notification
        drivingNotifMgr.notify(drivingNotifId, drivingNotifBldr.build());

    }

    public static void cancelNotif(){
        //Unissue Notification
        drivingNotifMgr.cancel(drivingNotifId);
    }
}
