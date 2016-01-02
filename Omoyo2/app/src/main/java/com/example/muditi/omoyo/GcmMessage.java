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
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by muditi on 25-12-2015.
 */
public class GcmMessage extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        //final Handler mainThread = new Handler(getApplicationContext().getMainLooper());
        String offerDescription = data.getString("offerDescription");
        String offerPosterUri=data.getString("offerPosterUri");
        String offerTitle=data.getString("offerTitle");
        String offerContent=data.getString("offerContent");
     // Omoyo.toast("Message:"+message,getApplicationContext());
        sendNotification(offerDescription,offerPosterUri,offerContent,offerTitle);
     // Omoyo.toast("From:"+from,getApplicationContext());
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

