package com.google.muditi.deligoo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.muditi.deligoo.Omoyo;

public class OTPSMSReceiver  extends BroadcastReceiver{
private static String SMS_BUNDLE="pdus",address,messageBody;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        com.google.muditi.deligoo.Omoyo.shared=context.getSharedPreferences("omoyo", Context.MODE_PRIVATE);
        com.google.muditi.deligoo.Omoyo.edit= com.google.muditi.deligoo.Omoyo.shared.edit();
        Bundle smsBundle = intent.getExtras();
        if(smsBundle != null){
            Object[] sms = (Object[])smsBundle.get(SMS_BUNDLE);

            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                messageBody= smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();
            //  Omoyo.toast("Message:"+messageBody+"Length:"+messageBody.length(),context);
            }
             if(varifivationOfSenderId(address))
             {
                   // Omoyo.toast("1",context);
                     String  OTPCodeReceived = messageBody.substring(13,18);
                  //   Omoyo.toast("OTP:"+OTPCodeReceived + "Length:"+OTPCodeReceived.length(),context);
                 if(loginCheck(OTPCodeReceived))
                 {
                         //    Omoyo.toast("Login Successful",context);


                 }
                 else
                 {
                        //     Omoyo.toast("Login Unsuccessful :" + OTPCodeReceived ,context);
                 }

                 }
            else
             {
          //Some other Source Sms Received
                 }
        }
    }

    private Boolean varifivationOfSenderId(String senderId){
        if(com.google.muditi.deligoo.Omoyo.shared.getString("senderId","MM-OMOYoO").equals(senderId))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private Boolean loginCheck(String OTPCodeReceived){
               if(OTPCodeReceived.equals(com.google.muditi.deligoo.Omoyo.shared.getString("OTPGenerated","OTPGenerated")))
                   return true;

        else
               return  false;

    }

}
