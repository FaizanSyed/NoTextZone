package com.example.faizan.notextzone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    TextView message;
    Switch drivingSwitch;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drivingSwitch = (Switch) findViewById(R.id.drivingSwitch);
        editButton = (Button) findViewById(R.id.editButton);
        message = (TextView) findViewById(R.id.message);

        final SharedPreferences savedMessages = getSharedPreferences("savedMessages", Context.MODE_PRIVATE);
        String customMessage = savedMessages.getString("customMessage", getResources().getString(R.string.default_message));

        message.setText(customMessage);
        drivingSwitch.setChecked(savedMessages.getBoolean("isChecked", false));

        drivingSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            Toast.makeText(MainActivity.this, "Driving mode is ON!", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor smEditor = savedMessages.edit();
                            smEditor.putBoolean("isChecked", true);
                            smEditor.apply();

                            Intent toSmsService = new Intent(MainActivity.this, SmsService.class);
                            toSmsService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startService(toSmsService);
                        } else {
                            Toast.makeText(MainActivity.this, "Driving mode is OFF!", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor smEditor = savedMessages.edit();
                            smEditor.putBoolean("isChecked", false);
                            smEditor.apply();

                            Intent toSmsService = new Intent(MainActivity.this, SmsService.class);
                            toSmsService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            stopService(toSmsService);
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
}
