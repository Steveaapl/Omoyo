package com.example.muditi.deligoo;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by muditi on 01-01-2016.
 */
public class AddressOfUserByGPS extends IntentService {
    private static final String TAG = "LocationAddress";

    protected ResultReceiver mReceiver;

    public AddressOfUserByGPS() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(Omoyo.RECEIVER);


        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }


        Location location = intent.getParcelableExtra(Omoyo.LOCATION_DATA_EXTRA);

       // Omoyo.toast(location.getLatitude()+":"+location.getLongitude(),getApplicationContext());

        if (location == null) {
            errorMessage = "No Location Found";
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(Omoyo.FAILURE_RESULT, errorMessage);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {

            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);


            if (addresses == null || addresses.size()  == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "No address found";
                    Log.e(TAG, errorMessage);
                }
                deliverResultToReceiver(Omoyo.FAILURE_RESULT, errorMessage);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();
                StringBuffer stringBuffer =new StringBuffer();
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++)
                {
                    stringBuffer.append(address.getAddressLine(i) + " ");

                }
                //  String locationOfUser=TextUtils.join(System.getProperty("line.separator"), addressFragments);
                gettingUserLocation(stringBuffer.toString());

            }

        } catch (IOException ioException) {
            errorMessage = "Service not found";
            deliverResultToReceiver(Omoyo.FAILURE_RESULT,errorMessage );
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {

            errorMessage = "Invalide Latitude and Longitude";
            deliverResultToReceiver(Omoyo.FAILURE_RESULT,errorMessage);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }


    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Omoyo.RESULT_DATA_KEY, message);
        Omoyo.fromWhereCode=0;
        bundle.putString("fromWhereCode",String.valueOf(Omoyo.fromWhereCode));
        mReceiver.send(resultCode, bundle);
    }

    private void gettingUserLocation(final String locationThroughGPS){
        String json = String.format("{\"location\" : \"%s\"}",locationThroughGPS);
        OkHttpClient okhttp=new OkHttpClient();
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON,json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/locationthroughgps/").post(requestbody).build();
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                deliverResultToReceiver(Omoyo.FAILURE_RESULT,"Error occured"
                );
                Log.d("TAG","Error:"+e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                           if(response.isSuccessful()){
                               String data = response.body().string();
                               deliverResultToReceiver(Omoyo.SUCCESS_RESULT,data
                               );
                           }
            }
        });
    }

}
