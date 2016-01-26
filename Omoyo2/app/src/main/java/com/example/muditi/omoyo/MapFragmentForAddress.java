package com.example.muditi.omoyo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
        Log.d("TAGSWQ","NULL");
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            PolylineOptions polylineOptions = new PolylineOptions();
            LatLng User = new LatLng((double)Omoyo.shared.getFloat("latitude",41),(double)Omoyo.shared.getFloat("longitude",14));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(User)
                .zoom(25)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
           googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
           googleMap.addMarker(new MarkerOptions().position(new LatLng((double)Omoyo.shared.getFloat("latitude",41),(double)Omoyo.shared.getFloat("longitude",14))));

    }
}
