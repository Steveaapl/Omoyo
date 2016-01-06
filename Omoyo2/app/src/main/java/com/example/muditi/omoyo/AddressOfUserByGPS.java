package com.example.muditi.omoyo;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

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
        } catch (IOException ioException) {

            errorMessage = "Service not found";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {

            errorMessage = "Invalide Latitude and Longitude";
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }


        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No address found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Omoyo.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
                   Log.i(TAG,"Address Found");
                   deliverResultToReceiver(Omoyo.SUCCESS_RESULT,
                   TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Omoyo.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
