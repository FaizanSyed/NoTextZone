package com.example.faizan.notextzone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //Stop Respond Service
        if(MainActivity.active){
            MainActivity.setDrivingSwitch(false);
        } else {
            RespondServiceManager.stopRespondService(context);
        }

        //Close Notifications Tray
        Intent closeTray = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeTray);
    }
}
