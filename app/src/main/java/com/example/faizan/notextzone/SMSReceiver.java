package com.example.faizan.notextzone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    private SmsManager smsManager = SmsManager.getDefault();
    private static SharedPreferences savedMessages;
    private static String customMessage;
    private static final String R_TAG = "onReceive";
    private static final String S_INFO = "SenderInfo";
    private static final String D_TAG = "DEBUG";

    @Override
    public void onReceive(Context context, Intent intent) {
        savedMessages = context.getSharedPreferences("savedMessages", Context.MODE_PRIVATE);
        customMessage = savedMessages.getString("customMessage", context.getResources().getString(R.string.default_message));

        try{
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                Bundle smsBundle = intent.getExtras();
                if(smsBundle != null){
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    int pdusLength = pdus.length;

                    for(int i = 0; i < pdusLength; i++){
                        SmsMessage unreadSms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String senderNumber = unreadSms.getOriginatingAddress();
                        String senderMessage = unreadSms.getMessageBody();
                        Log.i(S_INFO, senderNumber + " " + senderMessage);

                        try{
                            smsManager.sendTextMessage(senderNumber, null, customMessage, null, null);
                        }catch(Exception e){
                            Log.d(D_TAG, e.getMessage());
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.d(R_TAG, e.getMessage());
        }
    }
}
