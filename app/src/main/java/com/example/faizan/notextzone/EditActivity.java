package com.example.faizan.notextzone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editMessage;
    Button confirmButton;
    Button defaultButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final Intent toMain = new Intent(EditActivity.this, MainActivity.class);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        defaultButton = (Button) findViewById(R.id.defaultButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        editMessage = (EditText) findViewById(R.id.editMessage);

        confirmButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(toMain);
                    }
                }
        );

        defaultButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(toMain);
                    }
                }
        );

        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(toMain);
                    }
                }
        );
    }
}
