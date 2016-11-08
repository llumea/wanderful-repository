package com.l2minigames.wanderfulworld;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    TextView mPositionLatitude;
    TextView mPositionLongitude;
    ProgressBar mProgressBar;
    LocationRequest locationRequest;
    ImageView mImageViewBackground;
    double myPositionLatitude;
    double myPositionLongitude;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    ArrayList<MyMarker> mMarkers =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mImageViewBackground = (ImageView)findViewById(R.id.image_view_background);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); //10 sek mellan requests
        locationRequest.setFastestInterval(2500);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mPositionLatitude = (TextView)findViewById(R.id.position_latitude);
        mPositionLongitude = (TextView)findViewById(R.id.position_longitude);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mImageViewBackground.setVisibility(ImageView.INVISIBLE);
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });


        ///LatLng sydney = new LatLng(-34, 151);
       /// mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       /// mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("Tag", "In OnConnected");

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        Log.i("Tag", "permissionCheck: " + permissionCheck);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
        else{
            getUpDates();
        }

    }
    public void getUpDates(){
        try{

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,locationRequest, this);
            /// mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch (SecurityException e){

        }
        if (mLastLocation != null) {
            mPositionLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
            mPositionLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getUpDates();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("Tag","not permitted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
       /// mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mMap.clear();
        createMarkers();
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings(). setZoomGesturesEnabled(false);
       /// mMap.getUiSettings(). setScrollGesturesEnabled(false);

        myPositionLatitude = location.getLatitude();
        myPositionLongitude = location.getLongitude();
        mPositionLatitude.setText(String.valueOf(location.getLatitude()));
        mPositionLongitude.setText(String.valueOf(location.getLongitude()));
        Log.d("TAG", "MyLatitude in onLocationChanged: "+location.getLatitude());
        Log.d("TAG", "MyLongitude in onLocationChanged: "+location.getLongitude());
        LatLng mPosition = new LatLng(myPositionLatitude, myPositionLongitude);
        mMap.addMarker(new MarkerOptions().position(mPosition).title("MyMarker"));
        if (mMarkers.size()>0) {
            Log.d("TAG", "INNE I MARKERSUTSKRIFT");
            for (int i=0;i<mMarkers.size();i++) {
                LatLng tmpPosition = new LatLng(mMarkers.get(i).markerLatitude, mMarkers.get(i).markerLongitude);
                mMap.addMarker(new MarkerOptions().position(tmpPosition).title(mMarkers.get(i).markerType));
            }

        }
        LatLng cameraPosition = new LatLng(myPositionLatitude+0.0005, myPositionLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(cameraPosition)
                        .tilt(90)
                        .zoom(18)
                        .build()));

       /// mImageViewBackground.setVisibility(ImageView.INVISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String name= marker.getTitle();


        if (name.equalsIgnoreCase("MyMarker"))
        {
            Log.d("TAG", "MyMarker is so tapped!");

        }
        if (name.equalsIgnoreCase("Marker 1"))
        {
            Log.d("TAG", "Marker 1 is tapped!");
        }
        if (name.equalsIgnoreCase("Marker 2"))
        {
            Log.d("TAG", "Marker 2 is tapped!");
        }

        return true;
    }
    public void createMarkers(){

        Log.d("TAG", "INNE I CREATE MARKERS");
        mMarkers.clear();
        MyMarker tmpMarker = new MyMarker(myPositionLatitude+0.0005, myPositionLongitude, "Marker 1");
        mMarkers.add(tmpMarker);
        MyMarker tmpMarker2 = new MyMarker(myPositionLatitude-0.0005, myPositionLongitude, "Marker 2");
        mMarkers.add(tmpMarker2);
    }
}
