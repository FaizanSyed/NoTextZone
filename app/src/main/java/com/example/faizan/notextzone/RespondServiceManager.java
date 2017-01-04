package com.example.faizan.notextzone;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class RespondServiceManager {

    private static SharedPreferences savedMessages;
    private static SharedPreferences.Editor smEditor;
    private static Intent toRespondService;

    public static void startRespondService(Context context){

        savedMessages = context.getSharedPreferences("savedMessages", Context.MODE_PRIVATE);

        Toast.makeText(context, "Driving mode is ON!", Toast.LENGTH_SHORT).show();

        // Save setting of switch
        smEditor = savedMessages.edit();
        smEditor.putBoolean("isChecked", true);
        smEditor.apply();

        // Start service
        toRespondService = new Intent(context, RespondService.class);
        toRespondService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(toRespondService);

        // Create Notification
        DrivingNotification.createNotif(context);

        // Set Snooze on Slack
        SlackSnooze.set();
    }

    public static void stopRespondService(Context context){

        savedMessages = context.getSharedPreferences("savedMessages", Context.MODE_PRIVATE);

        Toast.makeText(context, "Driving mode is OFF!", Toast.LENGTH_SHORT).show();

        //Save setting of switch
        smEditor = savedMessages.edit();
        smEditor.putBoolean("isChecked", false);
        smEditor.apply();

        //Stop service
        toRespondService = new Intent(context, RespondService.class);
        toRespondService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.stopService(toRespondService);

        //Cancel Notification
        Log.d("stopRespondService", "Before cancel Notif");
        DrivingNotification.cancelNotif(context);

        // End Snooze on Slack
        SlackSnooze.end();
    }
}
