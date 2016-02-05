package com.google.muditi.deligoo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.example.muditi.deligoo.*;
import com.example.muditi.deligoo.Google_analytics_class;
import com.rey.material.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;

import com.rey.material.widget.Spinner;
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

import butterknife.Bind;
import butterknife.ButterKnife;


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
    ProgressView progressbarnetworkcheck;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ObjectAnimator areaselectionanimation;
    ArrayList<String> listforcity = new ArrayList<>();
    ArrayList<String> listforarea = new ArrayList<>();
    private static String city_id, area_id;
    @Bind(R.id.doneselectingarea)
    Button done;
    HashMap<String, String> hash = new HashMap<>();
    @Bind(R.id.gpsLocation)
    FloatingActionButton gpsLocation;
    @Bind(R.id.process_bar_for_button)
    ProgressView progressView;
    @Bind(R.id.button_for_existing_user)
    android.widget.Button button_for_existing_user;
    @Bind(R.id.button_for_term_and_condition)
    android.widget.Button button_for_term_and_condition;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 10;
    private static final String TAG = "MainActivity";
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private Boolean clickForLocation = false;
    private static Context context;
    private String locationOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        ButterKnife.bind(this);
        statusBarBackColor();
        Log.d("DATE:",Omoyo.Date());
        Omoyo.errorReportByMint(getApplicationContext());
        Omoyo.shared = getSharedPreferences("omoyo", Context.MODE_PRIVATE);
        Omoyo.edit = Omoyo.shared.edit();
        context = getApplicationContext();
        mResultReceiver = new AddressResultReceiver(new android.os.Handler());
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                Log.d("TAG", "GoogleApiClient Connected");
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (mLastLocation != null) {
                    Log.d("TAG", "Lati:" + mLastLocation.getLatitude() + "Longi:" + mLastLocation.getLongitude());
                    Omoyo.edit.putFloat("latitude", (float) mLastLocation.getLatitude());
                    Omoyo.edit.commit();
                    Omoyo.edit.putFloat("longitude", (float) mLastLocation.getLongitude());
                    Omoyo.edit.commit();
                    if (!Geocoder.isPresent()) {
                        Omoyo.toast("No GeoCoder Avilabe", getApplicationContext());
                        return;
                    }
                    if (clickForLocation) {
                        progressbarnetworkcheck.setVisibility(View.VISIBLE);
                        startServiceLocationAddress();
                    }

                    //Omoyo.toast(mLastLocation.getLatitude()+"-"+mLastLocation.getLongitude(),getApplicationContext());

                } else {
                    Log.d("TAG", "Location is null");
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("TAG", "GoogleApiClient connection suspended! " + i);
            }
        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                Log.d("TAG", "GoogleApiClient connection failed" + connectionResult.getErrorMessage());
            }
        })
                .addApi(LocationServices.API).build();

        //open();

        spinnerforarea.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                if (listforarea.size() > 0) {
                    area_id = hash.get(listforarea.get(position));
                    Omoyo.edit.putString("area", listforarea.get(position));
                    Omoyo.edit.commit();
                    Omoyo.check = 1;
                    progressView.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInLeft).duration(500).playOn(done);
                }
          //      Omoyo.toast("Area:"+position,getApplicationContext());
            }
        });


        spinnerforcity.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                //   Omoyo.toast("City:"+position,getApplicationContext());
                if (listforcity.size() > 0) {
                    Omoyo.flagForSpinner = false;
                    city_id = hash.get(listforcity.get(position));
                    Omoyo.edit.putString("city", listforcity.get(position));
                    Omoyo.edit.commit();
                    arealoader(city_id);
                    progressbarnetworkcheck.setVisibility(View.VISIBLE);
                } else {
                    //  view.setVisibility(View.GONE);
                }
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
                    progressbarnetworkcheck.setVisibility(View.VISIBLE);
                    com.example.muditi.deligoo.Google_analytics_class.getInstance().trackEvent("Bo","Bo","Bo");
                    locationloader();


                } else {
                    Omoyo.toast("Area need to be Selected", getApplicationContext());
                    YoYo.with(Techniques.Shake).duration(100).playOn(findViewById(R.id.linearlayoutforlocation));
                }
            }
        });
        areaselectionanimation = ObjectAnimator.ofFloat(linearlayoutforlocation, "alpha", 0, 1);
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

        // progressbarnetworkcheck.setProgress(20);

        cityloader();

     //   TourGuide tour_of_location = TourGuide.init(this).with(TourGuide.Technique.Click)
      //          .setPointer(new Pointer()).setToolTip(new ToolTip().setTitle("W").setDescription("WW")).setOverlay(new Overlay()).playOn(gpsLocation);


        gpsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleApiClient.isConnected()) {
                    if (statusCheck()) {

                    } else {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        startServiceLocationAddress();
                        progressbarnetworkcheck.setVisibility(View.VISIBLE);
                    }

                    Log.d("TAG", "GoogleApiClient");
                } else {
                    clickForLocation = true;
                    //  statusCheck();
                    Log.d("TAG", "GoogleApiClient!");
                }
            }
        });

        button_for_existing_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverStatusCheck();
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


        if (id == R.id.gps) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void locationloader() {
        progressbarnetworkcheck.setVisibility(View.VISIBLE);
        OkHttpClient okhttp = new OkHttpClient();
        String json = String.format("{\"city_id\" : \"%s\",\"area_id\" : \"%s\"}", city_id, area_id);
        // Omoyo.toast(json,getApplicationContext());
        final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/location/").post(requestbody).build();
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                        snackBar(3);
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
                            //    progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                            gcmRegistration();
                        }
                    });
                }
            }
        });
    }

    private void gcmRegistration() {
        if (checkPlayServices()) {
            if (Omoyo.shared.getBoolean("gcm_token_flag", true)) {
                Intent intent = new Intent(getApplicationContext(), Registrationid.class);
                intent.putExtra(Omoyo.RECEIVER, mResultReceiver);
                startService(intent);
                progressbarnetworkcheck.setVisibility(View.VISIBLE);
                linearlayoutforlocation.setVisibility(View.VISIBLE);
            } else {
                linearlayoutforlocation.setVisibility(View.VISIBLE);
                progressbarnetworkcheck.setVisibility(View.GONE);
                startActivity(new Intent(context, MainActivity.class));
                overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
            }
        } else {
            //Google Play Service need to be updated;
        }
    }

    public void arealoader(String city_id) {
        OkHttpClient okhttp = new OkHttpClient();
        String json = String.format("{\"city_id\" : \"%s\"}", city_id);
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestbody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/getarea/").post(requestbody).build();
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                        snackBar(2);
                        //    Omoyo.toast("Error - In the Network ",getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    listforarea.clear();
                 //   listforarea.add("Select Area");
                    final String data = response.body().string();

                    //data=[{"area":[{"area":"Green Park","_id":"56675528f3ca917c166cca9c"},{"area":"Quatub Minar","_id":"56675528f3ca917c166cca9b"}]}]
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                            try {
                                jsonarray = new JSONArray(data);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    jsonobject = jsonarray.getJSONObject(i);
                                    String value = jsonobject.getString("area_name").substring(0, 1).toUpperCase() + jsonobject.getString("area_name").substring(1, jsonobject.getString("area_name").length());
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

        progressbarnetworkcheck.setVisibility(View.VISIBLE);


        OkHttpClient okhttp = new OkHttpClient();

        Request request = new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/getcity/").build();

        Call call = okhttp.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                        snackBar(1);
                        Omoyo.InternetCheck = false;
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    listforarea.clear();
                  //  listforcity.add("Select City");
                    Omoyo.InternetCheck = true;
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                            try {
                                jsonarray = new JSONArray(data);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    jsonobject = jsonarray.getJSONObject(i);
                                    String value = jsonobject.getString("city_name").substring(0, 1).toUpperCase() + jsonobject.getString("city_name").substring(1, jsonobject.getString("city_name").length());
                                    hash.put(value, jsonobject.getString("city_id"));
                                    listforcity.add(value);
                                }
                            } catch (JSONException jsonex) {

                            }

                            //   areaselectionanimation.start();
                            linearlayoutforlocation.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.ZoomIn).duration(500).playOn(linearlayoutforlocation);
                            ArrayAdapter adapterforcity = new firstpagespinneradapter("City", getApplicationContext(), R.layout.firstpagespinnerlayout, listforcity);
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
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();

            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        AlertDialog alertDialog = alertDialogBuilder.create();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupcard, null, false);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.setMyLocationEnabled(true);
                map.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("HeHo"));
            }
        });

        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawable(null);
        WindowManager.LayoutParams linear = alertDialog.getWindow().getAttributes();
        linear.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        linear.width = 200;
        linear.height = 200;
        alertDialog.show();

    }


    public Boolean statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return true;
        } else
            return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled,  enable it to get your location?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
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

    private void startServiceLocationAddress() {
        linearlayoutforlocation.setVisibility(View.INVISIBLE);
        progressbarnetworkcheck.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AddressOfUserByGPS.class);

        //  String mResultReceiverObject=new Gson().toJson(mResultReceiver);
        intent.putExtra(Omoyo.RECEIVER, mResultReceiver);


        intent.putExtra(Omoyo.LOCATION_DATA_EXTRA, mLastLocation);

        startService(intent);
    }

    @Override
    protected void onPause() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        googleApiClient.connect();
        Google_analytics_class.getInstance().trackScreenView("First View");
        super.onResume();
    }

    class AddressResultReceiver extends ResultReceiver implements Parcelable {

        Creator CREATOR;

        public AddressResultReceiver(android.os.Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == Omoyo.SUCCESS_RESULT) {
                //  Omoyo.toast(resultData.getString("fromWhereCode"),getApplicationContext());
               // progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                if (resultData.getString("fromWhereCode").equals("0")) {
                    String mAddressOutput = resultData.getString(Omoyo.RESULT_DATA_KEY);
                    //  Omoyo.toast(mAddressOutput, getApplicationContext());
                    try {
                        JSONObject jsonObject = new JSONObject(mAddressOutput);
                        if (jsonObject.getString("success").equals("1")) {
                            city_id = jsonObject.getString("city_id");
                            area_id = jsonObject.getString(("area_id"));
                            Omoyo.edit.putString("GpsLocation", jsonObject.getString("location"));
                            Omoyo.edit.commit();
                            locationloader();
                        }
                        if (jsonObject.getString("success").equals("0")) {
                            locationOfUser = jsonObject.getString("location");
                            snackBar(404);
                        }
                    } catch (JSONException ex) {

                    }
                }
                if (resultData.getString("fromWhereCode").equals("1")) {
                    //  Omoyo.toast("Done",getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    progressbarnetworkcheck.setVisibility(View.GONE);
                }
            } else {
                //Omoyo.toast("Not Done",getApplicationContext());
                Log.d("TAG", "FAILURE");
                if (resultData.getString("fromWhereCode").equals("0")) {
                    Log.d("TAG", "FAILURE in side");
                    snackBar(4);
                }
                if (resultData.getString("fromWhereCode").equals("1")) {
                    snackBar(5);
                }
            }
        }
    }

    private void snackBar(final int i) {
        progressbarnetworkcheck.setVisibility(View.INVISIBLE);
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.relativelayout_parent_first_page), getResources().getString(R.string.internet_not_available), Snackbar.LENGTH_INDEFINITE);
        final View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snackbar_back));
        final TextView textView = ButterKnife.findById(snackbarView, android.support.design.R.id.snackbar_text);
        final TextView textViewAction = ButterKnife.findById(snackbarView, android.support.design.R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        snackbar.setText(R.string.internet_not_available);
        snackbar.setAction(getResources().getString(R.string.try_again), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (i) {
                    case 1:
                        cityloader();
                        break;
                    case 2:
                        arealoader(city_id);
                        break;
                    case 3:
                        locationloader();
                        break;
                    case 4:
                        startServiceLocationAddress();
                        break;
                    case 5:
                        gcmRegistration();
                        break;
                    case 404:
                        Log.d("Nothing", "No");
                        break;
                    case 505:
                        finish();
                        break;
                    default:
                        Log.d("Nothing", "No");
                }
            }
        });
        if (i == 505) {
            textView.setText(getResources().getString(R.string.exitfromtheapp));
            textView.setTextSize(getResources().getInteger(R.integer.app_exit_text_view_size));
            textViewAction.setText(getResources().getString(R.string.exit));
            textViewAction.setTextSize(getResources().getInteger(R.integer.exit));
            textViewAction.setTextColor(getResources().getColor(R.color.monza));
            snackbarView.setBackgroundColor(getResources().getColor(R.color.background_material_dark));
        }
        if (i == 404) {
            snackbar.setText(locationOfUser);
            textViewAction.setText(R.string.location_not_supported);
            snackbar.setDuration(Snackbar.LENGTH_LONG);
            linearlayoutforlocation.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.ZoomIn).duration(500).playOn(linearlayoutforlocation);
        }
        if (i == 4 && Omoyo.InternetCheck) {
            linearlayoutforlocation.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.ZoomIn).duration(500).playOn(linearlayoutforlocation);
        }
        if (i == 6 && Omoyo.InternetCheck) {
            textView.setText(getResources().getString(R.string.please_login_again));
            textViewAction.setText(getResources().getString(R.string.welcome));
            snackbar.setDuration(Snackbar.LENGTH_SHORT);
        }
        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        if (Omoyo.InternetCheck || i == 1)
            snackbar.show();
    }

    @TargetApi(21)
    private void statusBarBackColor() {
        Log.d("TAG", Build.VERSION.SDK_INT + "");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            getWindow().setStatusBarColor(getResources().getColor(R.color.appcolor));
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("fromMain")) {
            snackBar(505);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_transition_backword_in, R.anim.activity_transition_backword_out);
        }
    }

    private void serverStatusCheck() {
        OkHttpClient okhttp = new OkHttpClient();
        Request request = new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/servercheck/").get().build();
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackBar(6);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Omoyo.shared.getBoolean("user_status", false)) {
                                startActivity(new Intent(context, MainActivity.class));
                                overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                            } else {
                                snackBar(6);
                            }
                        }
                    });
                }
            }
        });
    }
}
