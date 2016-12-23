package com.example.faizan.notextzone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private EditText editMessage;
    private Button confirmButton;
    private Button defaultButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final Intent toMain = new Intent(EditActivity.this, MainActivity.class);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        defaultButton = (Button) findViewById(R.id.defaultButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        editMessage = (EditText) findViewById(R.id.editMessage);
        final SharedPreferences savedMessages = getSharedPreferences("savedMessages", Context.MODE_PRIVATE);
        String customMessage = savedMessages.getString("customMessage", getResources().getString(R.string.default_message));
        editMessage.setText(customMessage, TextView.BufferType.EDITABLE);

        confirmButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor smEditor = savedMessages.edit();
                        smEditor.putString("customMessage", editMessage.getText().toString());
                        smEditor.apply();
                        Toast.makeText(EditActivity.this, "Custom Message Saved!", Toast.LENGTH_LONG).show();
                        startActivity(toMain);
                    }
                }
        );

        defaultButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editMessage.setText(R.string.default_message, TextView.BufferType.EDITABLE);
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
