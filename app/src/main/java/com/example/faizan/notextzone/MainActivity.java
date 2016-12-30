package com.example.faizan.notextzone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static boolean active;
    private static TextView message;
    private static Switch drivingSwitch;
    private static Button editButton;
    private static SharedPreferences savedMessages;
    private static String customMessage;
    private static boolean slackAuthenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drivingSwitch = (Switch) findViewById(R.id.drivingSwitch);
        editButton = (Button) findViewById(R.id.editButton);
        message = (TextView) findViewById(R.id.message);

        savedMessages = getSharedPreferences("savedMessages", Context.MODE_PRIVATE);
        customMessage = savedMessages.getString("customMessage", getResources().getString(R.string.default_message));

        message.setText(customMessage);
        drivingSwitch.setChecked(savedMessages.getBoolean("isChecked", false));
        slackAuthenticated = savedMessages.getBoolean("slackAuthenticated", false);

        if(isSlackInstalled(MainActivity.this) && !slackAuthenticated){
            Log.d("Main_OnCreate", "toSlackAuthen");
            Intent toSlackAuthentication = new Intent(MainActivity.this, SlackAuthentication.class);
            startActivity(toSlackAuthentication);
        }

        setListeners(MainActivity.this);
    }

    public static boolean isSlackInstalled(Context context){
        try {
            context.getPackageManager().getApplicationInfo("com.Slack", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e){
            Log.d("isSlackInstalled", "FALSE");
            return false;
        }
    }

    public static void setListeners(final Context context){
        drivingSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            //Start RespondService
                            RespondServiceManager.startRespondService(context);

                        } else {

                            //Stop RespondService
                            RespondServiceManager.stopRespondService(context);

                        }
                    }
                }
        );

        editButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toEdit = new Intent(context, EditActivity.class);
                        context.startActivity(toEdit);
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onDestroy() {
        active = false;
        super.onDestroy();
    }

    public static void setDrivingSwitch(boolean setting){
        drivingSwitch.setChecked(setting);
    }
}
