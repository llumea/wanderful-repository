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
import android.view.View;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference collectedRef;
    TextView mPositionLatitude;
    TextView mPositionLongitude;
    ProgressBar mProgressBar;
    LocationRequest locationRequest;
    ImageView mImageViewBackground;
    double myPositionLatitude;
    double myPositionLongitude;
    String uid;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    ArrayList<MyMarker> mMarkers =new ArrayList<>();
    UserObject object;
    CircleOptions mCircle;
    Timer timer;
    int timerTime;
    Marker tmpMarker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mCircle = new CircleOptions();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            uid = user.getUid();
            Log.d("TAG","UserID: "+uid);

        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(uid);
        collectedRef = myRef.child("collectedItems");
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mImageViewBackground = (ImageView)findViewById(R.id.image_view_background);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); //10 sek mellan requests
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                mMap.clear();
                object = dataSnapshot.getValue(UserObject.class);
                if (object.timer==10){createMarkersFirstTime();}

                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                long checkMarkersTimestamp = date.getTime();
                if (checkMarkersTimestamp>object.timer+60000) {
                    updateMarkers(object.latitude, object.longitude, checkMarkersTimestamp);
                }
                /// textHome.setText(object.username+" "+object.email);

                mPositionLatitude.setText(""+object.latitude);
                mPositionLongitude.setText(""+object.longitude);

                LatLng mPosition = new LatLng(object.latitude, object.longitude);
                mMap.addMarker(new MarkerOptions().position(mPosition).title("MyMarker").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow)));


                for (int i=0;i<object.markerList.size();i++){
                    Log.d("TAG", "markerSize before LatLng: "+object.markerList.size());
                    LatLng tmpPosition = new LatLng(object.markerList.get(i).markerLatitude, object.markerList.get(i).markerLongitude);
                    mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue)));

                    Log.d("TAG", "markerSize: "+object.markerList.size());
                }




                ///Omvandla long till formaterat String-datum
                ///SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy HH:mm:ss");
                /// String formatedDate = sdf.format(tmpToDoNote.date);
                /// date.setText(formatedDate);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Toast.makeText(MapsActivity.this, "Map is loaded",
                        Toast.LENGTH_SHORT).show();
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

        myPositionLatitude = location.getLatitude();
        myPositionLongitude = location.getLongitude();
        myRef.child("latitude").setValue(myPositionLatitude);
        myRef.child("longitude").setValue(myPositionLongitude);
        LatLng cameraPosition = new LatLng(myPositionLatitude+0.0005, myPositionLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(cameraPosition)
                        .tilt(90)
                        .zoom(18)
                        .build()));
        ///mMap.getUiSettings(). setZoomGesturesEnabled(false);
        ///mMap.getUiSettings(). setScrollGesturesEnabled(false);
      /*
       /// mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mMap.clear();
        createMarkers();
        mMap.setOnMarkerClickListener(this);


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
       */
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

        float[] distance = new float[2];
        //users current location


        mCircle.center(new LatLng(myPositionLatitude, myPositionLongitude));
        mCircle.radius(60);





        Location.distanceBetween(marker.getPosition().latitude,
                marker.getPosition().longitude, mCircle.getCenter().latitude,
                mCircle.getCenter().longitude, distance);

        Log.d("TAG", "DISTANCE BETWEEN MARKER AND MY POSITION: "+distance[0]);
        Log.d("TAG", "DISTANCE BETWEEN MARKER AND MY POSITION: "+distance[1]);
        if (distance[0] > mCircle.getRadius()) {
            Log.d("TAG", "DISTANCE IS BIGGER THAN RADIUS");
            Toast.makeText(this, "You are not in range",
                    Toast.LENGTH_SHORT).show();

        }else if (distance[0] < mCircle.getRadius() && !name.equalsIgnoreCase("MyMarker")) {
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            long itemTimestamp = date.getTime();
            Log.d("TAG", "Timestamp when pick up item: "+itemTimestamp);
            Log.d("TAG", "DISTANCE IS SMALLER THAN RADIUS");
            for (int i=0;i<object.markerList.size();i++){
                if (name.equalsIgnoreCase(object.markerList.get(i).markerType)){
                    Log.d("TAG", "This MyMarker is tapped: "+name);
                    String tmpId = Integer.toString(i);
                    if (object.markerList.get(i).markerType.equals("hallon 1")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("strawberry", "berry","earth", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                    }
                    if (object.markerList.get(i).markerType.equals("hallon 2")){

                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("blueberry", "berry","earth", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                    }
                    if (object.markerList.get(i).markerType.equals("hallon 3")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("woodberry", "berry","earth", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                    }
                    if (object.markerList.get(i).markerType.equals("hallon 4")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("nordic berry", "berry","earth", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                    }
                    myRef.child("markerList").child(tmpId).child("markerLatitude").setValue(0);
                    myRef.child("markerList").child(tmpId).child("markerLongitude").setValue(0);
                }

            }
            Toast.makeText(this, "You collected an item",
                    Toast.LENGTH_SHORT).show();
        }

        if (name.equalsIgnoreCase("MyMarker"))
        {
            Log.d("TAG", "STROKE COLOR"+mCircle.getStrokeColor());
            Log.d("TAG", "STROKE COLOR"+mCircle.getFillColor());
            if (mCircle.getStrokeColor()==-16777216 && mCircle.getFillColor()==0x00000000){
                mCircle.strokeColor(R.color.colorAccent);
                mCircle.fillColor(R.color.colorPrimary);
                mMap.addCircle(mCircle);
            }




            Log.d("TAG", "MyMarker is so tapped!");


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
    public void createMarkersFirstTime(){

        ///Ändra här så att det finns rätt markers i början
        Log.d("TAG", "INNE I CREATE MARKERS FOR FIRST TIME");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long markersTimestamp = date.getTime();
        myRef.child("timer").setValue(markersTimestamp);

        ArrayList<MyMarker> tmpMarkerslist = new ArrayList<>();
        MyMarker tmpMarker = new MyMarker(myPositionLatitude+0.0005, myPositionLongitude, "Marker 1");
        tmpMarkerslist.add(tmpMarker);
        MyMarker tmpMarker2 = new MyMarker(myPositionLatitude-0.0005, myPositionLongitude, "Marker 2");
        tmpMarkerslist.add(tmpMarker2);


    }

    public void updateMarkers(double myLatitude, double myLongitude, long timestamp){
        Log.d("TAG", "INNE I UPDATE MARKERS");
        Toast.makeText(this, "Items are updated!",
                Toast.LENGTH_SHORT).show();
        myRef.child("timer").setValue(timestamp);

        ArrayList<MyMarker> tmpMarkerslist = new ArrayList<>();
        MyMarker tmpMarker = new MyMarker(myLatitude+0.0005, myLongitude, "hallon 2");
        tmpMarkerslist.add(tmpMarker);
        MyMarker tmpMarker2 = new MyMarker(myLatitude-0.0005, myLongitude, "hallon 3");
        tmpMarkerslist.add(tmpMarker2);
        MyMarker tmpMarker3 = new MyMarker(myLatitude-0.0015, myLongitude, "hallon 4");
        tmpMarkerslist.add(tmpMarker3);
        MyMarker tmpMarker4 = new MyMarker(myLatitude+0.0015, myLongitude, "hallon 1");
        tmpMarkerslist.add(tmpMarker4);
        myRef.child("markerList").setValue(tmpMarkerslist);

    }





}
