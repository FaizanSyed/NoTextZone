package com.example.faizan.notextzone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    SmsManager smsManager;
    EditText message;
    Button sendButton;
    private static final String TAG = "SEND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = (EditText) findViewById(R.id.message);
        sendButton = (Button) findViewById(R.id.sendButton);
        smsManager = SmsManager.getDefault();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsManager.sendTextMessage("5556", null, "HEY THERE BUD", null, null);
                try {
                    smsManager.sendTextMessage("5556", null, "HEY THERE BUD", null, null);
                } catch (SecurityException se){
                    Log.i(TAG, "Don't have permission!");
                    Toast.makeText(MainActivity.this, "DON'T HAVE PERMISSION", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
