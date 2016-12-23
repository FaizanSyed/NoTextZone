package com.example.faizan.notextzone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsService extends Service {
    private SMSReceiver smsReceiver;
    private static IntentFilter mIntentFilter;
    private static SharedPreferences savedMessages;
    private static String customMessage;
    private static final String R_TAG = "onReceive";
    private static final String S_INFO = "SenderInfo";
    private static final String D_TAG = "DEBUG";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        savedMessages = getSharedPreferences("savedMessages", Context.MODE_PRIVATE);
        customMessage = savedMessages.getString("customMessage", getResources().getString(R.string.default_message));

        Log.i(D_TAG, "SmsService onCreate");
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        smsReceiver = new SMSReceiver();
        registerReceiver(smsReceiver, mIntentFilter);
    }

    public static class SMSReceiver extends BroadcastReceiver {
        private SmsManager smsManager = SmsManager.getDefault();

        @Override
        public void onReceive(Context context, Intent intent) {
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

    @Override
    public void onDestroy() {
        unregisterReceiver(smsReceiver);
        super.onDestroy();
    }
}
