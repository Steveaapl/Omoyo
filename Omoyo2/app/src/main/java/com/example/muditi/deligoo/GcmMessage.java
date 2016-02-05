package com.example.muditi.deligoo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.gcm.GcmListenerService;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
            }
            if(jsonObject.getString("type_of").equals("17")){
                Log.d("TAGFORGCM", jsonObjectForData.toString() + "HEllo");
                JSONObject jsonObject1 = jsonObjectForData.getJSONObject("ads");
                JSONObject jsonObject2 = jsonObjectForData.getJSONObject("item");
                jsonObject1.remove("ads_item");
                jsonObject1.put("ads_item", jsonObject2);
                Omoyo.currentShopId=jsonObject1.getString("shop_id");
                shoploader(jsonObject1);
            }
        }
        catch(JSONException jsonex){
              Log.d("ERROR-GCM",jsonex.getLocalizedMessage());
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
    private void sendNotification(final JSONObject jsonObject) {
        try {
        final SharedPreferences shared=getSharedPreferences("omoyo", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit=shared.edit();
            JSONArray jsonArray = new JSONArray(shared.getString("ads",""));
            jsonArray.put(jsonArray.length(),jsonObject);
            edit.putString("ads", jsonArray.toString());
            edit.commit();
        Intent intent = new Intent(getApplicationContext(), shoppage.class);
        intent.putExtra("type_of",0);
            intent.putExtra("_id", jsonObject.getString("ads_id"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
       final  RemoteViews remoteViews=new RemoteViews("com.example.muditi.omoyo",R.layout.adsnotification);

            remoteViews.setTextViewText(R.id.offerDescriptionTextView, jsonObject.getString("ads_description"));
            remoteViews.setTextViewText(R.id.text_view_for_notification_item_name,jsonObject.getJSONObject("ads_item").getString("item_name"));
            remoteViews.setTextViewText(R.id.text_view_for_notification_item_offer,jsonObject.getJSONObject("ads_item").getString("item_offer"));
            remoteViews.setTextViewText(R.id.text_view_for_notification_item_price,"Price - "+jsonObject.getJSONObject("ads_item").getString("item_price"));

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(jsonObject.getJSONObject("ads_item").getString("item_name"))
                    .setContentText(jsonObject.getString("ads_description"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            android.os.Handler handler = new android.os.Handler(getApplicationContext().getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Glide.with(getApplicationContext()).load(jsonObject.getString("ads_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                //remoteViews.setBitmap(R.id.offerposterlinearlayout, "", resource);
                                remoteViews.setImageViewBitmap(R.id.offerposterlinearlayout, resource);
                                Notification notification = notificationBuilder.build();
                                notification.bigContentView = remoteViews;
                                NotificationManager notificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                try {
                                    notificationManager.notify(Integer.valueOf(jsonObject.getString("ads_id")), notification);
                                }
                                catch(JSONException jx){

                                }
                            }
                        });

                    } catch (JSONException jx) {

                    }
                }
            });
        }
        catch(JSONException jx){

        }
    }


    public  void shoploader(final JSONObject jsonObject){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"shop_id\" : \"%s\"}", Omoyo.currentShopId);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/shop/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    Log.d("TAGSHOP",data);
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        Omoyo.shared = getSharedPreferences("omoyo", Context.MODE_PRIVATE);
                        Omoyo.edit = Omoyo.shared.edit();
                        Omoyo.edit.putString("shop",jsonArray.getJSONObject(0).toString());
                        Omoyo.edit.commit();
                        sendNotification(jsonObject);
                    }
                    catch(JSONException e){

                    }

                }
            }
        });
    }


}

