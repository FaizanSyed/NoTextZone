package com.example.faizan.notextzone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    SmsManager smsManager;
    Button sendButton;
    Switch drivingSwitch;
    private static final String TAG = "SEND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsManager = SmsManager.getDefault();
        sendButton = (Button) findViewById(R.id.sendButton);
        drivingSwitch = (Switch) findViewById(R.id.drivingSwitch);

        drivingSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            Toast.makeText(MainActivity.this, "Driving mode is ON!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Driving mode is OFF!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    smsManager.sendTextMessage("16477021419", null, "HEY THERE BUD", null, null);
                } catch (SecurityException se){
                    Log.i(TAG, "Don't have permission!");
                    Toast.makeText(MainActivity.this, "DON'T HAVE PERMISSION", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
