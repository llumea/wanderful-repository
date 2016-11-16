package com.l2minigames.wanderfulworld;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    Timer timer;
    int timerTime;
    Marker tmpMarker;
    int onlyOneTime;

    RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter<CollectedItem, MapsActivity.ObjectViewHolder> adapter;
    static Firebase myFirebaseRef;
    static ArrayList<CollectedItem> tmpCollectedItems = new ArrayList<CollectedItem>();
    RelativeLayout relativeLayoutRecycle;

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
        circleImageView = (ImageView)findViewById(R.id.circleImageView);
        circleImageView.setBackgroundResource(R.drawable.animation);
        circleAnimation = (AnimationDrawable) circleImageView.getBackground();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabRecycler);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (relativeLayoutRecycle.getVisibility()==View.INVISIBLE) {
                    relativeLayoutRecycle.setVisibility(View.VISIBLE);
                } else {relativeLayoutRecycle.setVisibility(View.INVISIBLE);}
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
            ///animateCircle();

            Log.d("TAG", "STROKE COLOR"+mCircle.getStrokeColor());
            Log.d("TAG", "STROKE COLOR"+mCircle.getFillColor());
            if (mCircle.getStrokeColor()==0x00000000 && mCircle.getFillColor()==0x00000000){
               mCircle.strokeColor(R.color.colorAccent);
               mCircle.fillColor(R.color.colorPrimary);
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



            myRef.child("timer").setValue(timestamp);

            ArrayList<MyMarker> tmpMarkerslist = new ArrayList<>();
            MyMarker tmpMarker = new MyMarker(myLatitude + 0.0005, myLongitude, "hallon 2");
            tmpMarkerslist.add(tmpMarker);
            MyMarker tmpMarker2 = new MyMarker(myLatitude - 0.0005, myLongitude, "hallon 3");
            tmpMarkerslist.add(tmpMarker2);
            MyMarker tmpMarker3 = new MyMarker(myLatitude - 0.0015, myLongitude, "hallon 4");
            tmpMarkerslist.add(tmpMarker3);
            MyMarker tmpMarker4 = new MyMarker(myLatitude + 0.0015, myLongitude, "hallon 1");
            tmpMarkerslist.add(tmpMarker4);
            myRef.child("markerList").setValue(tmpMarkerslist);
            Toast.makeText(this, "Items are updated!",
                    Toast.LENGTH_SHORT).show();


    }


    public static class ObjectViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemType;
        TextView mDivider;
        ImageButton removeButton;

        public ObjectViewHolder(View v) {
            super(v);
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
}
