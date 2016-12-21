package com.example.faizan.notextzone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public abstract class SmsService extends Service {
    private SMSReceiver mSmsReceiver;
    private IntentFilter mIntentFilter;
    private static final String rTag = "onReceive";
    private static final String sInfo = "SenderInfo";
    @Override
    public void onCreate() {
        super.onCreate();

        mSmsReceiver = new SMSReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
    }

    public class SMSReceiver extends BroadcastReceiver {
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
                        }
                    }
                }
            }catch (Exception e){
                Log.d(rTag, e.getMessage());
            }
            mSmsReceiver = new SMSReceiver();
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        }
    }
}
