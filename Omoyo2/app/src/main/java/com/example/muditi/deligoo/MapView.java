package com.example.muditi.deligoo;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapView extends AppCompatActivity implements OnMapReadyCallback{
@Bind(R.id.grid_for_category_list_for_map_view)
    GridView gridView;
    @Bind(R.id.relative_layout_for_map_view)
    RelativeLayout relativeLayout;
    GoogleMap map;
    boolean flag=false;
    JSONArray jsonArray = new JSONArray() , jsonArray2 = new JSONArray() ,jsonArray3 = new JSONArray();
    JSONArray jsonArray1= new JSONArray() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        ButterKnife.bind(this);
        Omoyo.errorReportByMint(getApplicationContext());
        if(statusCheck()){

        }
        if(!Omoyo.shared.contains("gpsposition")){
            downloadCoordinateOfShop();
        }
        try{
            jsonArray = new JSONArray(Omoyo.shared.getString("gpsposition",""));
            jsonArray2 = new JSONArray(Omoyo.shared.getString("category", ""));
        }
        catch(JSONException jx){

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_36dp);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        GoogleMapOptions options = new GoogleMapOptions();
        options.zoomControlsEnabled(true);
        options.compassEnabled(true);
        MapFragment mMapFragment = MapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.relative_layout_for_map_view, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        gridView.setAdapter(new gridViewAdapter(jsonArray2));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
                gridView.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                TextView textView = ButterKnife.findById(view,R.id.text_view_for_quick_search);
                String category_name = textView.getText().toString();
                for(int k=0;k<jsonArray2.length();k++){
                    try {
                        JSONObject jsonObject = jsonArray2.getJSONObject(k);
                        if (category_name.equals(jsonObject.getString("category_name"))) {
                                  for(int h =0;h<jsonArray.length();h++){
                                      JSONObject jsonObject1 = jsonArray.getJSONObject(h);
                                      if(jsonObject.getString("category_id").equals(jsonObject1.getString("category_id"))){
                                          jsonArray3.put(jsonObject1);
                                      }
                                  }
                            gridView.setVisibility(View.GONE);
                            if(flag){
                                mapRender(map,jsonArray3);
                                jsonArray3 = new JSONArray();
                            }
                            k=jsonArray2.length();
                        }
                    }
                    catch(JSONException ex){

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shoplist, menu);

        MenuItem item = menu.findItem(R.id.searchItem);
        SearchManager searchManager =(SearchManager)getSystemService(Context.SEARCH_SERVICE);
       SearchView searchView = (SearchView) item.getActionView();

        if(searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    try{
                        searchCategory(query.trim());
                    }
                    catch(Exception ex){
                       searchCategory(query);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.length()==0){
gridView.setAdapter(new gridViewAdapter(jsonArray2));
                    }
                    else{
                        gridView.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.INVISIBLE);
                     try{
                         searchCategory(newText.trim());
                     }
                     catch(Exception ex){
                         searchCategory(newText);
                     }
                    }
                    return true;
                }
            });
            searchView.setIconified(true);
            searchView.setIconifiedByDefault(true);

            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                      gridView.setVisibility(View.VISIBLE);
                      relativeLayout.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                     gridView.setVisibility(View.GONE);
                     relativeLayout.setVisibility(View.VISIBLE);
                     if(flag){
                         mapRender(map,jsonArray);
                     }
                    return true;
                }
            });

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        else{

        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map =googleMap;
        this.flag = true;
        mapRender(googleMap, jsonArray);
        LatLng User = new LatLng((double) Omoyo.shared.getFloat("latitude", 41), (double) Omoyo.shared.getFloat("longitude", 14));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(User)
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.addMarker(new MarkerOptions().position(new LatLng((double) Omoyo.shared.getFloat("latitude", 41), (double) Omoyo.shared.getFloat("longitude", 14))).
                title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
    }

    private void mapRender(GoogleMap map ,JSONArray jsonArray){
        try {

            for(int i =0;i<jsonArray.length();i++){
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                  double   longitude = jsonObject.getDouble("longitude");
                  double  latitude = jsonObject.getDouble("latitude");
                    map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).
                            title("Shop").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        } catch (JSONException jx) {

        }
    }


    private void downloadCoordinateOfShop(){
        try{
            String defaultlocation=String.format("[{\"location_id\" : \"%s\"}]","1008");
            JSONObject jsonObject = new JSONObject(Omoyo.shared.getString("location", defaultlocation));
            final  String location_id=jsonObject.getString("location_id");
            OkHttpClient okhttp=new OkHttpClient();
            String json=String.format("{\"location_id\" : \"%s\"}",location_id);
            final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
            RequestBody requestbody=RequestBody.create(JSON, json);
            Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/coordinateOfShop/").post(requestbody).build();
            Call call=okhttp.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  snackBar(7);
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Omoyo.edit.putString("gpsposition", data);
                            Omoyo.edit.commit();
                            try {
                                jsonArray = new JSONArray(data);
                                if(flag){
                                    mapRender(map,jsonArray);
                                }
                            }
                            catch(JSONException jx){

                            }
                        }
                    });
                }
            });
        }
        catch(JSONException ex) {

        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void searchCategory(String chr){
        jsonArray1 = new JSONArray();
        try {
            for(int i =0; i<jsonArray2.length();i++){
                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                if(jsonObject.getString("category_name").toLowerCase().contains(chr.toLowerCase())){
                    jsonArray1.put(jsonArray1.length(),jsonObject);
                }
            }
       gridView.setAdapter(new gridViewAdapter(jsonArray1));
        }
        catch(JSONException xj){

        }
    }

    private class gridViewAdapter extends BaseAdapter {
        JSONArray jsonArray;
        View view;
        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public gridViewAdapter(JSONArray jsonArray) {
            super();

            this.jsonArray = jsonArray;
        }

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public View getView(int i, View viewP, ViewGroup viewGroup) {
            try{
                final JSONObject jsonObject=jsonArray.getJSONObject(i);
                LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflate.inflate(R.layout.search_suggestion_layout, null);

                    TextView textView = ButterKnife.findById(view,R.id.text_view_for_quick_search);
                    textView.setText(jsonObject.getString("category_name"));

                return view;
            }
            catch(JSONException e){
                Log.d("OfferXXX:",e.getLocalizedMessage());
                return null;
            }

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_transition_backword_in, R.anim.activity_transition_backword_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Google_analytics_class.getInstance().trackScreenView("MapView View");
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
}
