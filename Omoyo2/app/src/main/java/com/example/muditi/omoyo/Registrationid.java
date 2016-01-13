package com.example.muditi.omoyo;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by muditi on 24-12-2015.
 */
public class Registrationid extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    protected ResultReceiver mReceiver;
    public Registrationid() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            mReceiver = intent.getParcelableExtra(Omoyo.RECEIVER);


            if (mReceiver == null) {
                Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
                return;
            }

            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_appid),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);



          //  Omoyo.toast("Token is :" + token, getApplicationContext());

            Omoyo.edit.putString("gcm_token", token);
            Omoyo.edit.commit();
            Omoyo.edit.putBoolean("gcm_token_registered",false);
            Omoyo.edit.commit();

            sendRegistrationToServer(token);


            subscribeTopics(token);

            sharedPreferences.edit().putBoolean(Omoyo.SENT_TOKEN_TO_SERVER, true).apply();

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean(Omoyo.SENT_TOKEN_TO_SERVER, false).apply();
        }
        Intent registrationComplete = new Intent(Omoyo.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        try {
            String defaultlocation=String.format("[{\"location_id\" : \"%s\"}]","1008");
            JSONObject jsonObject = new JSONObject(Omoyo.shared.getString("location", defaultlocation));
            final String location_id=jsonObject.getString("location_id");
        OkHttpClient okhttp=new OkHttpClient();
        String json = String.format("{\"token_number\" : \"%s\",\"location_id\" : \"%s\"}", token,location_id);
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/gcmtoken/").post(requestbody).build();
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                deliverResultToReceiver(Omoyo.FAILURE_RESULT);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                     if(response.isSuccessful()){
                         deliverResultToReceiver(Omoyo.SUCCESS_RESULT);
                                              Log.d("RESPONCE",response.body().string());
                                      }
            }
        });
        }
        catch(JSONException jsone){

        }
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

    private void deliverResultToReceiver(int resultCode) {
        Bundle bundle = new Bundle();
        Omoyo.fromWhereCode=1;
        bundle.putString("fromWhereCode",""+String.valueOf(Omoyo.fromWhereCode));
        mReceiver.send(resultCode, bundle);
    }

}
