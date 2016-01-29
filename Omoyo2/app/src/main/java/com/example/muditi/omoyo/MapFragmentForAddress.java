package com.example.muditi.omoyo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
 * Created by muditi on 24-01-2016.
 */
public class MapFragmentForAddress extends Fragment implements OnMapReadyCallback {
    LayoutInflater layoutInflater;
    View view;
    public MapFragmentForAddress() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAGATTACH","Hipo");
        if(!Omoyo.shared.contains("gpsposition")){
            downloadCoordinateOfShop();
        }
        view = inflater.inflate(R.layout.map_show_of_address_layout, container, false);
        GoogleMapOptions options = new GoogleMapOptions();
        options.zoomControlsEnabled(true);
        options.compassEnabled(true);
        MapFragment mMapFragment = MapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_of_shop, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("TAGSWQ", "NULL");
        double longitude=2.0,latitude=2.0;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        PolylineOptions polylineOptions = new PolylineOptions();
        try {
            JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("gpsposition",""));
            for(int i =0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("shop_id").equals(Omoyo.currentShopId)){
                    longitude = jsonObject.getDouble("longitude");
                    latitude = jsonObject.getDouble("latitude");
                    polylineOptions.add(new LatLng(latitude,longitude))
                            .add(new LatLng((double) Omoyo.shared.getFloat("latitude", 41), (double) Omoyo.shared.getFloat("longitude", 14)));
                    polylineOptions.color(getResources().getColor(R.color.appcolor));
                    googleMap.addPolyline(polylineOptions);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).
                            title("Shop").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }
            }
        } catch (JSONException jx) {

        }
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

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String data = response.body().string();
                    Omoyo.edit.putString("gpsposition",data);
                    Omoyo.edit.commit();
                }
            });
        }
        catch(JSONException ex) {

        }
    }
}
