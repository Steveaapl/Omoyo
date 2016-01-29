package com.example.muditi.omoyo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by muditi on 25-12-2015.
 */
public class GcmMessage extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Omoyo.shared=getSharedPreferences("omoyo", Context.MODE_PRIVATE);
        Omoyo.edit=Omoyo.shared.edit();
        try{
            JSONObject jsonObject = new JSONObject(data.getString("data"));
            JSONObject jsonObjectForData = jsonObject.getJSONObject("data");
            if(jsonObject.getString("type_of").equals("1")){
                Log.d("TAGFORGCM",jsonObjectForData.toString());
                Omoyo.edit.putString("user_id", jsonObjectForData.getString("token_id"));
                Omoyo.edit.commit();
            }
            if(jsonObject.getString("type_of").equals("2")){
                Log.d("TAGFORGCM",jsonObjectForData.toString());
                Omoyo.edit.putString("user_name", jsonObjectForData.getString("user_name"));
                Omoyo.edit.commit();
                Omoyo.edit.putString("user_email", jsonObjectForData.getString("user_email"));
                Omoyo.edit.commit();
            }
            if(jsonObject.getString("type_of").equals("3")){
                Log.d("TAGFORGCM", jsonObjectForData.toString());
                Omoyo.edit.putString("senderId", jsonObjectForData.getString("smswtf")+"-OMOYoo");
                Omoyo.edit.commit();
            }
            if(jsonObject.getString("type_of").equals("4")){
                Log.d("TAGFORGCM", jsonObjectForData.toString());
                Omoyo.edit.putString("OMOYoo_contact_number", jsonObjectForData.getString("contact_number_of_OMOYoo"));
                Omoyo.edit.commit();
            }
            if(jsonObject.getString("type_of").equals("5")){
                Log.d("TAGFORGCM", jsonObjectForData.toString());
                Omoyo.edit.putString("license_description", jsonObjectForData.getString("license_description"));
                Omoyo.edit.commit();
            }
            if(jsonObject.getString("type_of").equals("6")){
                Log.d("TAGFORGCM", jsonObjectForData.toString());
                Omoyo.edit.putString("data_for_faq", jsonObjectForData.getJSONArray("data_for_faq").toString());
                Omoyo.edit.commit();
            }
            if(jsonObject.getString("type_of").equals("7")){
                Log.d("TAGFORGCM", jsonObjectForData.toString());
                Omoyo.edit.putString("system_notification", jsonObjectForData.getJSONArray("system_notification").toString());
                Omoyo.edit.commit();
                //
            }

        }
        catch(JSONException jsonex){

        }
        if (from.startsWith("/topics/")){
            // message received from some topic.
        } else {
            // normal downstream message.
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
    private void sendNotification(String offerDescription , final String offerPosterUri ,String offerContent , String offerTitle) {
        final SharedPreferences shared=getSharedPreferences("omoyo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shared.edit();
        Intent intent = new Intent(this, firstpage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
       final  RemoteViews remoteViews=new RemoteViews("com.example.muditi.omoyo",R.layout.adsnotification);
        remoteViews.setTextViewText(R.id.offerDescriptionTextView, offerDescription);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(offerContent)
                .setContentText(offerTitle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        android.os.Handler handler=new android.os.Handler(getApplicationContext().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext()).load("http://"+getApplicationContext().getResources().getString(R.string.ip)+"/bitmap/ads/ads.jpg").asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                     //remoteViews.setBitmap(R.id.offerposterlinearlayout, "", resource);
                        remoteViews.setImageViewBitmap(R.id.offerposterlinearlayout,resource);
                        Notification notification = notificationBuilder.build();
                        notification.bigContentView = remoteViews;
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notification);
                    }
                });
            }
        });

    }

}

