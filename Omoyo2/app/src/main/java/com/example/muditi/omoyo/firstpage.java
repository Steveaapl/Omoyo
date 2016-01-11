package com.example.muditi.omoyo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import butterknife.Bind;
import butterknife.ButterKnife;
import okio.BufferedSink;


public class firstpage extends Activity {
    GoogleApiClient googleApiClient;
    @Bind(R.id.linearlayoutfooter)
    LinearLayout linearlayoutforfooter;
    @Bind(R.id.linearlayoutforlocation)
    LinearLayout linearlayoutforlocation;
    @Bind(R.id.spinnerforarea)
    Spinner spinnerforarea;
    @Bind(R.id.spinnerforcity)
    Spinner spinnerforcity;
    @Bind(R.id.processbarfirstpagenetwork)
    ProgressBar progressbarnetworkcheck;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ObjectAnimator areaselectionanimation;
    ArrayList<String> listforcity =new ArrayList<>();
    ArrayList<String> listforarea =new ArrayList<>();
    String city_id,area_id;
    @Bind(R.id.doneselectingarea)
    Button done;
    HashMap<String,String> hash=new HashMap<>();
    @Bind(R.id.gpsLocation)
    FloatingActionButton gpsLocation;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private Boolean clickForLocation = false;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        ButterKnife.bind(this);
        Omoyo.shared=getSharedPreferences("omoyo", Context.MODE_PRIVATE);
        Omoyo.edit=Omoyo.shared.edit();
        context=getApplicationContext();
        mResultReceiver=new AddressResultReceiver(new android.os.Handler());
        googleApiClient=new GoogleApiClient.Builder(getApplicationContext()).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (mLastLocation != null) {
                    // Determine whether a Geocoder is available.
                    if (!Geocoder.isPresent()) {
                        Omoyo.toast("No GeoCoder Avilabe",getApplicationContext());
                        return;
                    }

                    if (clickForLocation) {
                        startServiceLocationAddress();
                    }

                    //Omoyo.toast(mLastLocation.getLatitude()+"-"+mLastLocation.getLongitude(),getApplicationContext());

                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {

            }
        })
       .addApi(LocationServices.API).build();

       //open();
        Omoyo.edit.putString("senderId", "MM-OMOYoO");
        Omoyo.edit.commit();
        spinnerforarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    area_id = hash.get(listforarea.get(position));
                    Omoyo.edit.putString("area", listforarea.get(position));
                    Omoyo.edit.commit();
                    Omoyo.check = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Omoyo.toast("Nothing", getApplicationContext());
            }
        });

        spinnerforcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    city_id = hash.get(listforcity.get(position));
                    Omoyo.edit.putString("city", listforcity.get(position));
                    Omoyo.edit.commit();
                    arealoader(city_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
done.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (Omoyo.check == 1) {
            Omoyo.shared = getSharedPreferences("omoyo", Context.MODE_PRIVATE);
            Omoyo.edit = Omoyo.shared.edit();
            Omoyo.edit.putString("city_id", city_id);
            Omoyo.edit.putString("area_id", area_id);
            Omoyo.edit.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    locationloader();
                }
            }).start();


        } else {
            Omoyo.toast("Area need to be Selected", getApplicationContext());
            YoYo.with(Techniques.Shake).duration(100).playOn(findViewById(R.id.linearlayoutforlocation));
        }
    }
});
        areaselectionanimation=ObjectAnimator.ofFloat(linearlayoutforlocation,"alpha",0,1);
        areaselectionanimation.setStartDelay(500);
        areaselectionanimation.setDuration(500);
        areaselectionanimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

             progressbarnetworkcheck.setProgress(20);

             cityloader();

        gpsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleApiClient.isConnected() && mLastLocation != null) {
                    startServiceLocationAddress();
                    //     open();
                } else {
                    clickForLocation = true;
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_firstpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        if(id == R.id.gps){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void locationloader(){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"city_id\" : \"%s\",\"area_id\" : \"%s\"}", city_id, area_id);
       // Omoyo.toast(json,getApplicationContext());
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/location/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Omoyo.toast("Error in the network", getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    Omoyo.edit.putString("location", data);
                    Omoyo.edit.commit();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Omoyo.toast(Omoyo.shared.getString("ads","f"),getApplicationContext());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            if (checkPlayServices()) {
                                if (Omoyo.shared.getBoolean("gcm_token_registered", true)) {
                                    Intent intent = new Intent(getApplicationContext(), Registrationid.class);
                                    startService(intent);
                                } else {
                                    Omoyo.toast("Registered Already", getApplicationContext());
                                }
                            } else {
                                Omoyo.toast("Service not supported for GCM", getApplicationContext());
                            }
                            //  Omoyo.toast("Response:"+data,getApplicationContext());
                        }
                    });
                }
            }
        });
    }

    public void arealoader(String city_id){
        OkHttpClient okhttp=new OkHttpClient();
        String json = String.format("{\"city_id\" : \"%s\"}", city_id);
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/getarea/").post(requestbody).build();
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                        Omoyo.toast("Error - In the Network ",getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()) {
                    listforarea.clear();
                    listforarea.add("area");
                    final String data = response.body().string();

                    //data=[{"area":[{"area":"Green Park","_id":"56675528f3ca917c166cca9c"},{"area":"Quatub Minar","_id":"56675528f3ca917c166cca9b"}]}]
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                            try {
                                jsonarray = new JSONArray(data);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    jsonobject = jsonarray.getJSONObject(i);
                                    String value=jsonobject.getString("area_name").substring(0,1).toUpperCase()+jsonobject.getString("area_name").substring(1, jsonobject.getString("area_name").length());
                                    hash.put(value, jsonobject.getString("area_id"));
                                    listforarea.add(value);
                                }
                            } catch (JSONException jsonex) {

                            }

                            //  areaselectionanimation.start();
                            ArrayAdapter adapterforarea = new firstpagespinneradapter("Area", getApplicationContext(), R.layout.firstpagespinnerlayout, listforarea);
                            spinnerforarea.setAdapter(adapterforarea);
                        }
                    });
                }

            }
        });

    }




    public void cityloader() {
        OkHttpClient okhttp = new OkHttpClient();

        Request request=new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/getcity/").build();

        Call call = okhttp.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                        Omoyo.toast("Error - In the Network ",getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                listforcity.add("city");
                if(response.isSuccessful()) {
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                            try {
                                jsonarray = new JSONArray(data);
                                for(int i=0 ; i<jsonarray.length() ; i++){
                                    jsonobject=jsonarray.getJSONObject(i);
                                    String value=jsonobject.getString("city_name").substring(0,1).toUpperCase()+jsonobject.getString("city_name").substring(1, jsonobject.getString("city_name").length());
                                    hash.put(value,jsonobject.getString("city_id"));
                                    listforcity.add(value);
                                }
                            }
                            catch(JSONException jsonex){

                            }

                         //   areaselectionanimation.start();
                            linearlayoutforlocation.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.ZoomIn).duration(500).playOn(linearlayoutforlocation);
                            ArrayAdapter adapterforcity=new firstpagespinneradapter("City",getApplicationContext(),R.layout.firstpagespinnerlayout,listforcity);
                            spinnerforcity.setAdapter(adapterforcity);
                        }
                    });
                }

            }
        });

    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode,PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();

            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        AlertDialog alertDialog = alertDialogBuilder.create();

        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.popupcard, null, false);

        MapFragment mapFragment= (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.setMyLocationEnabled(true);
                map.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())).title("HeHo"));
            }
        });

        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawable(null);
        WindowManager.LayoutParams linear = alertDialog.getWindow().getAttributes();
        linear.gravity= Gravity.TOP|Gravity.CENTER_HORIZONTAL;
        linear.width=200;
        linear.height=200;
        alertDialog.show();

    }


    public void statusCheck()
    {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            buildAlertMessageNoGps();

        }


    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled,  enable it to get your location?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
 private  void startServiceLocationAddress(){

     Intent intent = new Intent(this, AddressOfUserByGPS.class);

   //  String mResultReceiverObject=new Gson().toJson(mResultReceiver);
     intent.putExtra(Omoyo.RECEIVER, mResultReceiver);


     intent.putExtra(Omoyo.LOCATION_DATA_EXTRA, mLastLocation);

     startService(intent);
}

    @Override
    protected void onPause() {
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        googleApiClient.connect();
        super.onResume();
    }

    static class AddressResultReceiver extends ResultReceiver implements Parcelable {

         static   Parcelable.Creator CREATOR;

        public AddressResultReceiver(android.os.Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

        String     mAddressOutput = resultData.getString(Omoyo.RESULT_DATA_KEY);

            if (resultCode == Omoyo.SUCCESS_RESULT) {
               Omoyo.edit.putString("GpsLocation",mAddressOutput);
                Omoyo.edit.commit();
            }
        }
    }
}
