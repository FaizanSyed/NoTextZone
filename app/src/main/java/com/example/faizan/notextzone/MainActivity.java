package com.example.faizan.notextzone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static boolean active;
    private static TextView message;
    private static Switch drivingSwitch;
    private static Button editButton;
    private static SharedPreferences savedMessages;
    private static String customMessage;

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

        drivingSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            //Start RespondService
                            RespondServiceManager.startRespondService(MainActivity.this);

                        } else {

                            //Stop RespondService
                            RespondServiceManager.stopRespondService(MainActivity.this);

                        }
                    }
                }
        );

        editButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toEdit = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(toEdit);
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
