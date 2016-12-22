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
    //private static SMSReceiver mSmsReceiver;
    private static IntentFilter mIntentFilter;
    SharedPreferences savedMessages = getSharedPreferences("savedMessages", Context.MODE_PRIVATE);
    String customMessage = savedMessages.getString("customMessage", getResources().getString(R.string.default_message));
    private static final String rTag = "onReceive";
    private static final String sInfo = "SenderInfo";
    private static final String dTag = "DEBUG";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(dTag, "SmsService onCreate");
        //mSmsReceiver = new SMSReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
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
                            Log.i(sInfo, senderNumber + " " + senderMessage);

                            try{
                                smsManager.sendTextMessage(senderNumber, null, senderMessage, null, null);
                            }catch(Exception e){
                                Log.d(dTag, e.getMessage());
                            }
                        }
                    }
                }
            }catch (Exception e){
                Log.d(rTag, e.getMessage());
            }
            //mSmsReceiver = new SMSReceiver();
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        }
    }
}
