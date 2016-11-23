package com.l2minigames.wanderfulworld;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
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
import com.firebase.ui.FirebaseRecyclerAdapter;

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


    AnimationDrawable circleAnimation;
    ImageView circleImageView;
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
    CircleOptions mCircleTotal;
    CircleOptions mCircleTotalMax;
    Timer timer;
    int timerTime;
    Marker tmpMarker;
    int onlyOneTime;
    ArrayList<Double> randomList = new ArrayList<>();

    FragmentManager fragmentManager;

    ImageView personImage;
    TextView personUserName;
    TextView personLevel;
    ImageView personProgress;
    ImageView personProgressTotal;
    TextView personXP;
    TextView personTotalXP;

    RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter<CollectedItem, MapsActivity.ObjectViewHolder> adapter;
    static Firebase myFirebaseRef;
    static ArrayList<CollectedItem> tmpCollectedItems = new ArrayList<CollectedItem>();
    RelativeLayout relativeLayoutRecycle;
    RelativeLayout relativeLayoutPerson;
    private Runnable mAnimation;
    Handler mHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mHandler = new Handler();
        Firebase.setAndroidContext(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_map);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCircle = new CircleOptions();
        mCircleTotal = new CircleOptions();
        mCircleTotalMax = new CircleOptions();
        circleImageView = (ImageView)findViewById(R.id.circleImageView);
        circleImageView.setBackgroundResource(R.drawable.animation);
        circleAnimation = (AnimationDrawable) circleImageView.getBackground();
        personImage = (ImageView)findViewById(R.id.personImage);
        personUserName = (TextView)findViewById(R.id.personUserName);
        personLevel = (TextView)findViewById(R.id.personLevel);
        personProgress = (ImageView)findViewById(R.id.personProgress);
        personProgressTotal = (ImageView)findViewById(R.id.personProgressTotal);
        personXP = (TextView)findViewById(R.id.personXP);
        personTotalXP = (TextView)findViewById(R.id.personTotalXP);
        final ImageButton fab = (ImageButton) findViewById(R.id.fabRecycler);
        final ImageButton personFab = (ImageButton) findViewById(R.id.fabPerson);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (relativeLayoutRecycle.getVisibility()==View.INVISIBLE) {
                    relativeLayoutRecycle.setVisibility(View.VISIBLE);
                    personFab.setVisibility(View.INVISIBLE);
                    fab.setBackgroundResource(R.drawable.close);
                } else {
                    relativeLayoutRecycle.setVisibility(View.INVISIBLE);
                    personFab.setVisibility(View.VISIBLE);
                    fab.setBackgroundResource(R.drawable.backpackbuttonblue);
                }
                /// Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                ///        .setAction("Action", null).show();
            }
        });

        personFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (relativeLayoutPerson.getVisibility()==View.INVISIBLE) {
                    relativeLayoutPerson.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    personFab.setBackgroundResource(R.drawable.close);
                } else {
                    relativeLayoutPerson.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    personFab.setBackgroundResource(R.drawable.girlfaceblue);
                }
                /// Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                ///        .setAction("Action", null).show();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            uid = user.getUid();
            myFirebaseRef = new Firebase("https://wanderful-world.firebaseio.com/"+uid+"/collectedItems");
            Log.d("TAG","UserID: "+uid);

        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(uid);
        relativeLayoutRecycle = (RelativeLayout)findViewById(R.id.relativeLayoutRecycle);
        ///relativeLayoutRecycle.setEnabled(false);
        relativeLayoutRecycle.setVisibility(View.INVISIBLE);
        relativeLayoutPerson = (RelativeLayout)findViewById(R.id.relativeLayoutPerson);
        ///relativeLayoutPerson.setEnabled(false);
        relativeLayoutPerson.setVisibility(View.INVISIBLE);


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
                if (checkMarkersTimestamp>object.timer+60000 &&myPositionLatitude != 0 && myPositionLongitude!=0 &&onlyOneTime==0) {
                    updateMarkers(object.latitude, object.longitude, checkMarkersTimestamp);
                    onlyOneTime=1;
                }
                /// textHome.setText(object.username+" "+object.email);

                mPositionLatitude.setText(""+object.latitude);
                mPositionLongitude.setText(""+object.longitude);
                mCircleTotal.center(new LatLng(myPositionLatitude, myPositionLongitude));
                mCircleTotal.radius(250);
                mCircleTotal.strokeColor(Color.parseColor("#28b6e8"));
                mCircleTotal.strokeWidth(50f);
                mMap.addCircle(mCircleTotal);

                mCircleTotalMax.center(new LatLng(myPositionLatitude, myPositionLongitude));
                mCircleTotalMax.radius(500);
                mCircleTotalMax.strokeColor(Color.parseColor("#28b6e8"));
                mCircleTotalMax.strokeWidth(50f);
                mMap.addCircle(mCircleTotalMax);


                LatLng mPosition = new LatLng(object.latitude, object.longitude);
                mMap.addMarker(new MarkerOptions().position(mPosition).title("MyMarker").icon(BitmapDescriptorFactory.fromResource(R.drawable.girl)));


                for (int i=0;i<object.markerList.size();i++){
                    Log.d("TAG", "markerSize before LatLng: "+object.markerList.size());
                    LatLng tmpPosition = new LatLng(object.markerList.get(i).markerLatitude, object.markerList.get(i).markerLongitude);



                    Log.i("Object", "Object_markerlist_type: "+object.markerList.get(i).markerType);
                    Marker tmpMarker = null;
                    if (object.markerList.get(i).markerType.equals("earth")){
                        tmpMarker = mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.earth_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("fire")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.fire_item)));
                    }

                    else if (object.markerList.get(i).markerType.equals("air")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.air_item)));

                    }
                    else if (object.markerList.get(i).markerType.equals("water")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.water_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("scroll")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.scroll_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("wizardacademy")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.wizardacademy)));
                    }
                    if (tmpMarker!=null){tmpMarker.setAnchor(0.5f,1.0f);}
                    Log.d("TAG", "markerSize: "+object.markerList.size());
                }

                ///Fyller Personobjekt här
                ///personImage = (ImageView)findViewById(R.id.personImage);
                personUserName.setText(object.username);
                checkNewLevel(object.level, object.XP);
                setLevelBar(object.level, object.XP);


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

        if (onlyOneTime==1){onlyOneTime=0;} ///Nollställ uppdatering av markers
        animateCircle();
        mCircle.strokeColor(0x00000000);
        mCircle.fillColor(0x00000000);
        myPositionLatitude = location.getLatitude();
        myPositionLongitude = location.getLongitude();
        myRef.child("latitude").setValue(myPositionLatitude);
        myRef.child("longitude").setValue(myPositionLongitude);
        ///Tidigare var myPositionLatitude+0.0005, men rotationen blev sned
        LatLng cameraPosition = new LatLng(myPositionLatitude, myPositionLongitude);
        CameraPosition currentCameraPosition = mMap.getCameraPosition();
        ///mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .bearing(currentCameraPosition.bearing)
                        .target(cameraPosition)
                        .tilt(90)
                        .zoom(18)
                        .build()));


        mMap.getUiSettings(). setZoomGesturesEnabled(false);
        mMap.getUiSettings(). setScrollGesturesEnabled(false);
        mMap.getUiSettings(). setCompassEnabled(false);

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
        tmpCollectedItems.clear();

        adapter = new FirebaseRecyclerAdapter<CollectedItem, ObjectViewHolder>(CollectedItem.class, R.layout.list_items_map, ObjectViewHolder.class, myFirebaseRef) {
            @Override
            protected void populateViewHolder(ObjectViewHolder objectViewHolder, CollectedItem collectedItem, int i) {

                ///String key = this.getRef(i).getKey();
                ///Fill objects
                CollectedItem tmpCollectedItem = new CollectedItem();
                tmpCollectedItem.cp = collectedItem.cp;
                tmpCollectedItem.elementType = collectedItem.elementType;
                tmpCollectedItem.hp = collectedItem.hp;
                tmpCollectedItem.imageRef = collectedItem.imageRef;
                tmpCollectedItem.itemName = collectedItem.itemName;
                tmpCollectedItem.itemType = collectedItem.itemType;
                tmpCollectedItem.level = collectedItem.level;
                tmpCollectedItem.timestamp = collectedItem.timestamp;
                tmpCollectedItem.uid = collectedItem.uid;

                tmpCollectedItems.add(tmpCollectedItem);

                objectViewHolder.itemName.setText(collectedItem.itemName);
                objectViewHolder.itemType.setText(collectedItem.itemType);
                if (collectedItem.itemType.equals("Earth")) {
                    objectViewHolder.itemIconMap.setBackgroundResource(R.drawable.earth_item);
                }
                else if (collectedItem.itemType.equals("Fire")) {
                    objectViewHolder.itemIconMap.setBackgroundResource(R.drawable.fire_item);
                }
                else if (collectedItem.itemType.equals("Air")) {
                    objectViewHolder.itemIconMap.setBackgroundResource(R.drawable.air_item);
                }
                else if (collectedItem.itemType.equals("Water")) {
                    objectViewHolder.itemIconMap.setBackgroundResource(R.drawable.water_item);
                }
                else if (collectedItem.itemType.equals("Scroll")) {
                    objectViewHolder.itemIconMap.setBackgroundResource(R.drawable.scroll_item);
                }

            }


        };
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        ///Startar bounceAnimationkod
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;

        // Cancels the previous animation

        mHandler.removeCallbacks(mAnimation);

        // Starts the bounce animation
        BounceAnimation mAnimation = new BounceAnimation(start, duration, marker, mHandler);
        mHandler.post(mAnimation);
       //Slut på bounceAnimationkod

        String name= marker.getTitle();

        float[] distance = new float[2];
        //users current location

        ///Lägger till en cirkel så att det går att kolla om markers finns inom radien
        ///Cirkeln ska animeras när man trycker på "MyMarker"
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

            ///Här skapas objekten som tidigare bara var markers med en typ



            for (int i=0;i<object.markerList.size();i++){
                if (name.equalsIgnoreCase(object.markerList.get(i).markerType)){
                    Log.d("TAG", "This MyMarker is tapped: "+name);
                    String tmpId = Integer.toString(i);
                    if (object.markerList.get(i).markerType.equals("earth")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Plant", "Earth","earth", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);

                    }
                    else if (object.markerList.get(i).markerType.equals("fire")){

                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Flames", "Fire","earth", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("air")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Trombulus", "Air","earth", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("water")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Waterdrop", "Water","earth", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("scroll")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Ancient Scrollifix", "Scroll","scroll", "imageRef", itemTimestamp, 1, 10, 10, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }


                }

            }
            for (int i=0;i<object.markerList.size();i++){
                if (name.equalsIgnoreCase(object.markerList.get(i).markerType)){
                    if (object.markerList.get(i).markerType.equals("wizardacademy")){
                        Toast.makeText(this, "You need to collect 5 artefacts to enter Wizard Academy!",
                                Toast.LENGTH_SHORT).show();
                    }

                }


            }


        }

        if (name.equalsIgnoreCase("MyMarker"))
        {
            ///animateCircle();

            Log.d("TAG", "STROKE COLOR"+mCircle.getStrokeColor());
            Log.d("TAG", "STROKE COLOR"+mCircle.getFillColor());
            if (mCircle.getStrokeColor()==0x00000000 && mCircle.getFillColor()==0x00000000){
               mCircle.strokeColor(Color.parseColor("#dedede"));
                mCircle.strokeWidth(50f);
               mCircle.fillColor(Color.parseColor("#28b6e8"));
                mMap.addCircle(mCircle);
                circleAnimation.stop();
                circleAnimation.start();




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

            ///Byter ut listan mer markers på servern

            myRef.child("timer").setValue(timestamp);

        ///Cirkel 1

        int randomNumber = 0;
        double randomPosition = 0.0000;

        randomList.clear();
        for (int i=0;i<40;i++){
            ///Jag vill ha noll också
            randomNumber = (int) (50 * Math.random());
            randomPosition = (double)randomNumber/10000;
            randomList.add(randomPosition);
            Log.i("TAG", "RANDOMLISTAN"+randomPosition);
        }



            ArrayList<MyMarker> tmpMarkerslist = new ArrayList<>();
            MyMarker tmpMarker = new MyMarker(myLatitude + randomList.get(0), myLongitude+randomList.get(1), "earth");
            tmpMarkerslist.add(tmpMarker);
            MyMarker tmpMarker2 = new MyMarker(myLatitude + randomList.get(2), myLongitude+randomList.get(3), "fire");
            tmpMarkerslist.add(tmpMarker2);
            MyMarker tmpMarker3 = new MyMarker(myLatitude + randomList.get(4), myLongitude+randomList.get(5), "air");
            tmpMarkerslist.add(tmpMarker3);
            MyMarker tmpMarker4 = new MyMarker(myLatitude + randomList.get(6), myLongitude+randomList.get(7), "water");
            tmpMarkerslist.add(tmpMarker4);
            MyMarker tmpMarker5 = new MyMarker(myLatitude + randomList.get(8), myLongitude+randomList.get(9), "scroll");
            tmpMarkerslist.add(tmpMarker5);

            MyMarker tmpMarker6 = new MyMarker(myLatitude - randomList.get(10), myLongitude+randomList.get(11), "earth");
            tmpMarkerslist.add(tmpMarker6);
            MyMarker tmpMarker7 = new MyMarker(myLatitude - randomList.get(12), myLongitude+randomList.get(13), "fire");
            tmpMarkerslist.add(tmpMarker7);
            MyMarker tmpMarker8 = new MyMarker(myLatitude - randomList.get(14), myLongitude+randomList.get(15), "air");
            tmpMarkerslist.add(tmpMarker8);
            MyMarker tmpMarker9 = new MyMarker(myLatitude - randomList.get(16), myLongitude+randomList.get(17), "water");
            tmpMarkerslist.add(tmpMarker9);
            MyMarker tmpMarker10 = new MyMarker(myLatitude - randomList.get(18), myLongitude+randomList.get(19), "scroll");
            tmpMarkerslist.add(tmpMarker10);

            MyMarker tmpMarker11 = new MyMarker(myLatitude - randomList.get(20), myLongitude-randomList.get(21), "earth");
            tmpMarkerslist.add(tmpMarker11);
            MyMarker tmpMarker12 = new MyMarker(myLatitude - randomList.get(22), myLongitude-randomList.get(23), "fire");
            tmpMarkerslist.add(tmpMarker12);
            MyMarker tmpMarker13 = new MyMarker(myLatitude - randomList.get(24), myLongitude-randomList.get(25), "air");
            tmpMarkerslist.add(tmpMarker13);
            MyMarker tmpMarker14 = new MyMarker(myLatitude - randomList.get(26), myLongitude-randomList.get(27), "water");
            tmpMarkerslist.add(tmpMarker14);
            MyMarker tmpMarker15 = new MyMarker(myLatitude - randomList.get(28), myLongitude-randomList.get(29), "scroll");
            tmpMarkerslist.add(tmpMarker15);

            MyMarker tmpMarker16 = new MyMarker(myLatitude + randomList.get(30), myLongitude-randomList.get(31), "earth");
            tmpMarkerslist.add(tmpMarker16);
            MyMarker tmpMarker17 = new MyMarker(myLatitude + randomList.get(32), myLongitude-randomList.get(33), "fire");
            tmpMarkerslist.add(tmpMarker17);
            MyMarker tmpMarker18 = new MyMarker(myLatitude + randomList.get(34), myLongitude-randomList.get(35), "air");
            tmpMarkerslist.add(tmpMarker18);
            MyMarker tmpMarker19 = new MyMarker(myLatitude + randomList.get(36), myLongitude-randomList.get(37), "water");
            tmpMarkerslist.add(tmpMarker19);
            MyMarker tmpMarker20 = new MyMarker(myLatitude + randomList.get(38), myLongitude-randomList.get(39), "scroll");
            tmpMarkerslist.add(tmpMarker20);

        MyMarker tmpMarkerHouse = new MyMarker(myLatitude + 0.0004, myLongitude - 0.0004, "wizardacademy");
        tmpMarkerslist.add(tmpMarkerHouse);




            myRef.child("markerList").setValue(tmpMarkerslist);
            Toast.makeText(this, "Items are updated!",
                    Toast.LENGTH_SHORT).show();


    }


    public static class ObjectViewHolder extends RecyclerView.ViewHolder {

        ImageView itemIconMap;
        TextView itemName;
        TextView itemType;
        TextView mDivider;
        ImageButton removeButton;

        public ObjectViewHolder(View v) {
            super(v);
            itemIconMap = (ImageView) v.findViewById(R.id.item_icon_map);
            itemName = (TextView) v.findViewById(R.id.itemName_map);
            itemType = (TextView) v.findViewById(R.id.itemType_map);
            mDivider = (TextView) v.findViewById(R.id.divider_map);
            removeButton = (ImageButton) v.findViewById(R.id.removeButton_map);
            removeButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = getAdapterPosition();

                    Log.i ("TAG", "Remove item clicked; "+position);
                    Log.i ("TAG", "Remove key clicked; "+""+tmpCollectedItems.get(position).uid);
                    myFirebaseRef.child(tmpCollectedItems.get(position).uid).removeValue();
                    tmpCollectedItems.remove(position);
                    Log.i ("TAG", "Storlek på tmpListan: "+tmpCollectedItems.size());


                    return false;
                }
            });
            itemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.i ("TAG", "Item clicked; "+position);
                    String thisItemName = itemName.getText().toString();

                    Log.i ("TAG", "Key clicked; "+""+tmpCollectedItems.get(position).uid);



                }
            });
        }
    }

    public void animateCircle(){


/*
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setSize(500,500);
        d.setColor(0x555751FF);
        d.setStroke(5, 0x555751FF);

        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth()
                , d.getIntrinsicHeight()
                , Bitmap.Config.ARGB_8888);

        // Convert the drawable to bitmap
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);

        // Radius of the circle
        final int mRadius = 100;

        LatLng groundPosition = new LatLng(63.8107993, 20.3420892);
        // Add the circle to the map
        final GroundOverlay circle = mMap.addGroundOverlay(new GroundOverlayOptions()
                .position(groundPosition, 200.0f).image(BitmapDescriptorFactory.fromBitmap(bitmap)));

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setIntValues(0, mRadius);
       /// valueAnimator.setDuration(3000);
        valueAnimator.setEvaluator(new IntEvaluator());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                circle.setDimensions(animatedFraction * mRadius * 2);
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.start();



        LatLng groundPosition = new LatLng(63.8107993, 20.3420892);
        final CircleOptions tCircle = new CircleOptions()
                .center(groundPosition)   //set center
                .radius(100)   //set radius in meters
                .strokeColor(0x555751FF)
                .fillColor(0x555751FF)
                .strokeWidth(5);

        mMap.addCircle(tCircle);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setIntValues(0, 100);
        valueAnimator.setDuration(3000);
        valueAnimator.setEvaluator(new IntEvaluator());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                tCircle.radius(100f);
            }
        });

        valueAnimator.start();
        */
    }
    public void checkNewLevel(int level, int xp){
        if (xp >=1000 && level ==1){setNewLevel(1);}
        else if (xp >=2000 && level ==2){setNewLevel(2);}
        else if (xp >=3000 && level ==3){setNewLevel(3);}
        else if (xp >=5000 && level ==4){setNewLevel(4);}
        else if (xp >=7000 && level ==5){setNewLevel(5);}
        else if (xp >=10000 && level ==6){setNewLevel(6);}
        else if (xp >=15000 && level ==7){setNewLevel(7);}
        else if (xp >=20000 && level ==8){setNewLevel(8);}
        else if (xp >=25000 && level ==9){setNewLevel(9);}
        else if (xp >=30000 && level ==10){setNewLevel(10);}
        else if (xp >=35000 && level ==11){setNewLevel(11);}
        else if (xp >=40000 && level ==12){setNewLevel(12);}
        else if (xp >=45000 && level ==13){setNewLevel(13);}
        else if (xp >=50000 && level ==14){setNewLevel(14);}
        else if (xp >=60000 && level ==15){setNewLevel(15);}
        else if (xp >=70000 && level ==16){setNewLevel(16);}
        else if (xp >=80000 && level ==17){setNewLevel(17);}
        else if (xp >=90000 && level ==18){setNewLevel(18);}
        else if (xp >=100000 && level ==19){setNewLevel(19);}
    }
    public void setLevelBar(int level, int xp){

        ///personProgress = (ImageView)findViewById(R.id.personProgress);
        ///personXP = (TextView)findViewById(R.id.personXP);
        xp = 53710;
        level = 15;
        personLevel.setText("Level "+level);
        personTotalXP.setText(""+xp+" XP");
       ///Testa olika xp och levels här

        ///levelprogressbar
        double tmpLevelDifference = 0.0;
        double diff = 0.0;

        if (level==1){
            diff = xp-0;
            tmpLevelDifference=diff/1000;
        }
        else if (level==2){
            diff = xp-1000;
            tmpLevelDifference=diff/1000;
            }
        else if (level==3){
            diff = xp-2000;
            tmpLevelDifference=diff/1000;
        }
        else if (level==4){
            diff = xp-3000;
            tmpLevelDifference=diff/2000;
        }
        else if (level==5){
            diff = xp-5000;
            tmpLevelDifference=diff/2000;
        }
        else if (level==6){
            diff = xp-7000;
            tmpLevelDifference=diff/3000;
        }
        else if (level==7){
            diff = xp-10000;
            tmpLevelDifference=diff/5000;
        }
        else if (level==8){
            diff = xp-15000;
            tmpLevelDifference=diff/5000;
        }
        else if (level==9){
            diff = xp-20000;
            tmpLevelDifference=diff/5000;
        }
        else if (level==10){
            diff = xp-25000;
            tmpLevelDifference=diff/5000;
        }
        else if (level==11){
            diff = xp-30000;
            tmpLevelDifference=diff/5000;
        }
        else if (level==12){
            diff = xp-35000;
            tmpLevelDifference=diff/5000;
        }
        else if (level==13){
            diff = xp-40000;
            tmpLevelDifference=diff/5000;
        }
        else if (level==14){
            diff = xp-45000;
            tmpLevelDifference=diff/5000;
        }
        else if (level==15){
            diff = xp-50000;
            tmpLevelDifference=diff/10000;
        }
        else if (level==16){
            diff = xp-60000;
            tmpLevelDifference=diff/10000;
        }
        else if (level==17){
            diff = xp-70000;
            tmpLevelDifference=diff/10000;
        }
        else if (level==18){
            diff = xp-80000;
            tmpLevelDifference=diff/10000;
        }
        else if (level==19){
            diff = xp-90000;
            tmpLevelDifference=diff/10000;
        }

        ///tmpXMMinusLevel är värdet minus xp som krävs för aktuell level
        ///tmpLevelDifferens används för att beräkna xp i förhållande till antal xp som krävs för nästa level

        tmpLevelDifference=tmpLevelDifference*100;

        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) personProgressTotal.getLayoutParams();
        params2.width = 100*5;
        personProgressTotal.setLayoutParams(params2);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) personProgress.getLayoutParams();
        params.width = (int) tmpLevelDifference*5;
        personProgress.setLayoutParams(params);


        Log.i("TAG", "width in personProgress"+personProgress.getWidth());
        Log.i("TAG", "width in personProgres tmpLevelDifference"+tmpLevelDifference);

        if (level==1){
            personXP.setText(""+xp+"/"+1000+" XP");
        }
        else if (level==2){
            personXP.setText(""+(int)diff+"/"+"1000"+" XP");
        }
        else if (level==3){
            personXP.setText(""+(int)diff+"/"+"1000"+" XP");
        }
        else if (level==4){
            personXP.setText(""+(int)diff+"/"+"2000"+" XP");
        }
        else if (level==5){
            personXP.setText(""+(int)diff+"/"+"2000"+" XP");
        }
        else if (level==6){
            personXP.setText(""+(int)diff+"/"+"3000"+" XP");
        }
        else if (level==7){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }
        else if (level==8){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }
        else if (level==9){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }
        else if (level==10){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }
        else if (level==11){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }
        else if (level==12){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }
        else if (level==13){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }
        else if (level==14){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }
        else if (level==15){
            personXP.setText(""+(int)diff+"/"+"10000"+" XP");
        }
        else if (level==16){
            personXP.setText(""+(int)diff+"/"+"10000"+" XP");
        }
        else if (level==17){
            personXP.setText(""+(int)diff+"/"+"10000"+" XP");
        }
        else if (level==18){
            personXP.setText(""+(int)diff+"/"+"10000"+" XP");
        }
        else if (level==19){
            personXP.setText(""+(int)diff+"/"+"5000"+" XP");
        }

    }
    public void setNewLevel (int oldLevel){
        ///change different values
        ///show message about new level
        ///setlevelbar i slutet
    }
    public void showPickedUpItem (String name, String tmpId){
        myRef.child("markerList").child(tmpId).child("markerLatitude").setValue(0);
        myRef.child("markerList").child(tmpId).child("markerLongitude").setValue(0);

        fragmentManager = getSupportFragmentManager();
        PickupFragment pickupFragment = new PickupFragment();
        pickupFragment.show(fragmentManager, "pickupFragment");
        Bundle bundle = new Bundle();
        bundle.putString("itemtype", name);
        pickupFragment.setArguments(bundle);

        Toast.makeText(this, "You collected an item",
                Toast.LENGTH_SHORT).show();

    }

}
