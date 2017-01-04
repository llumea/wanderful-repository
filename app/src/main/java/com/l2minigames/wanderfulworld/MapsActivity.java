package com.l2minigames.wanderfulworld;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
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
import java.util.Random;
import java.util.Timer;

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
    int localTravelMode;
    Marker tmpMarker;
    int onlyOneTime;
    int showWizard;
    ArrayList<Double> randomList = new ArrayList<>();
    ArrayList<Double> randomList2 = new ArrayList<>();
    ArrayList<MyMarker> tmpMarkersList = new ArrayList<>();

    ArrayList<String> flamesList = new ArrayList<>();
    ArrayList<String> plantsList = new ArrayList<>();
    ArrayList<String> trombulusList = new ArrayList<>();
    ArrayList<String> waterdropsList = new ArrayList<>();

    String currentItemKeySelected;
    String currentItemNameSelected;
    String currentTravelwindSelected;

    FragmentManager fragmentManager;

    ImageView personImage;
    TextView personUserName;
    TextView personLevel;
    ImageView personProgress;
    ImageView personProgressTotal;
    TextView personXP;
    TextView personTotalXP;
    TextView personHP;
    TextView personCP;

    TextView elementWaterValue;
    TextView elementEarthValue;
    TextView elementFireValue;
    TextView elementAirValue;


    ImageView pickImage;
    TextView itemType;
    TextView itemTitle;
    TextView itemDescription;

    ImageButton closeLoadingScreen;

    RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter<CollectedItem, MapsActivity.ObjectViewHolder> adapter;
    static Firebase myFirebaseRef;
    static ArrayList<CollectedItem> tmpCollectedItems = new ArrayList<CollectedItem>();
    RelativeLayout relativeLayoutRecycle;
    RelativeLayout relativeLayoutPerson;
    RelativeLayout relativeLayoutPicked;
    private Runnable mAnimation;
    Handler mHandler;

    ImageButton fab;
    ImageButton personFab;
    ImageButton gameButton;
    ImageButton useScrollButton;
    ImageButton closePickedButton;
    Button travelAway;
    Button travelHome;
    Button increaseElementPowers;

    double lastTravelLatitude;
    double lastTravelLongitude;
    double lastNormalLatitude;
    double lastNormalLongitude;
    boolean travelStarted;
    boolean isLeveledUp;
    String increaseThisElement;
    int artefactParis;
    int artefactLondon;
    int artefactIndia;

    private static MapsActivity mMapsActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        showWizard = 0;
        mMapsActivity = this;
        travelStarted =false;
        artefactParis = 0;
        artefactLondon = 0;
        artefactIndia = 0;
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
        personHP = (TextView)findViewById(R.id.personHP);
        personCP = (TextView)findViewById(R.id.personCP);
        elementEarthValue = (TextView)findViewById(R.id.elementEarthValue);
        elementFireValue = (TextView)findViewById(R.id.elementFireValue);
        elementAirValue = (TextView)findViewById(R.id.elementAirValue);
        elementWaterValue = (TextView)findViewById(R.id.elementWaterValue);
        pickImage = (ImageView)findViewById(R.id.pickImage);
        itemType = (TextView)findViewById(R.id.itemType);
        itemTitle = (TextView)findViewById(R.id.itemTitle);
        itemDescription = (TextView)findViewById(R.id.itemDescription);
        fab = (ImageButton) findViewById(R.id.fabRecycler);
        personFab = (ImageButton) findViewById(R.id.fabPerson);
        gameButton = (ImageButton) findViewById(R.id.gameButton);
        closeLoadingScreen = (ImageButton)findViewById(R.id.closeLoadingScreen);
        useScrollButton = (ImageButton) findViewById(R.id.useScrollButton);
        travelAway = (Button) findViewById(R.id.travelAway);
        travelHome = (Button) findViewById(R.id.travelHome);
        increaseThisElement ="nothing";
        increaseElementPowers = (Button) findViewById(R.id.increaseElementPowers);
        isLeveledUp =false;
        increaseElementPowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (increaseThisElement.equals("earth")){
                    if (mMapsActivity.getInstance().object.earthpower>6){
                        Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.you_can_only_have_7_elements_of_the_same_type),
                                Toast.LENGTH_LONG).show();
                    } else {
                        int tmpEarthPower = mMapsActivity.getInstance().object.earthpower+1;
                        myRef.child("earthpower").setValue(tmpEarthPower);
                        myFirebaseRef.child(currentItemKeySelected).removeValue();
                        Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.earthpower_increased),
                                Toast.LENGTH_LONG).show();
                        closePicked();
                    }
                }
                else if(increaseThisElement.equals("fire")){
                    if (mMapsActivity.getInstance().object.firepower>6){
                        Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.you_can_only_have_7_elements_of_the_same_type),
                                Toast.LENGTH_LONG).show();
                    }else {
                        int tmpFirePower = mMapsActivity.getInstance().object.firepower+1;
                        myRef.child("firepower").setValue(tmpFirePower);
                        myFirebaseRef.child(currentItemKeySelected).removeValue();
                        Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.firepower_increased),
                                Toast.LENGTH_LONG).show();
                        closePicked();
                    }

                }else if(increaseThisElement.equals("air")){
                    if (mMapsActivity.getInstance().object.airpower>6){
                        Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.you_can_only_have_7_elements_of_the_same_type),
                                Toast.LENGTH_LONG).show();
                    }else {
                        int tmpAirPower = mMapsActivity.getInstance().object.airpower+1;
                        myRef.child("airpower").setValue(tmpAirPower);
                        myFirebaseRef.child(currentItemKeySelected).removeValue();
                        Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.airpower_increased),
                                Toast.LENGTH_LONG).show();
                        closePicked();
                    }

                }
                else if(increaseThisElement.equals("water")){
                    if (mMapsActivity.getInstance().object.waterpower>6){
                        Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.you_can_only_have_7_elements_of_the_same_type),
                                Toast.LENGTH_LONG).show();
                    }else {
                        int tmpWaterPower = mMapsActivity.getInstance().object.waterpower+1;
                        myRef.child("waterpower").setValue(tmpWaterPower);
                        myFirebaseRef.child(currentItemKeySelected).removeValue();
                        Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.waterpower_increased),
                                Toast.LENGTH_LONG).show();
                        closePicked();
                    }

                }else if(increaseThisElement.equals("hp")){
                    myRef.child("hp").setValue(object.maxhp);
                    myFirebaseRef.child(currentItemKeySelected).removeValue();
                    Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.hp_increased_max),
                            Toast.LENGTH_LONG).show();
                    closePicked();

                }else if(increaseThisElement.equals("elements")){
                    myRef.child("earthpower").setValue(7);
                    myRef.child("firepower").setValue(7);
                    myRef.child("airpower").setValue(7);
                    myRef.child("waterpower").setValue(7);
                    myFirebaseRef.child(currentItemKeySelected).removeValue();
                    Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.elements_increased_max),
                            Toast.LENGTH_LONG).show();
                    closePicked();

                }
            }
        });
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    ///ToDo fungerar bara om det finns koppling till servern OBS! Den här knappen ska tas bort!
                    Bundle bundle = new Bundle();
                    bundle.putString("ENEMY", "wizgirl");
                    bundle.putString("WORLD", "wanderful world");
                    bundle.putInt("HP", object.hp);
                    bundle.putInt("MAX_HP", object.maxhp);
                    bundle.putInt("CP", object.cp);
                    bundle.putInt("MAX_CP", object.maxcp);
                    bundle.putInt("EARTH_POWER", object.earthpower);
                    bundle.putInt("FIRE_POWER", object.firepower);
                    bundle.putInt("AIR_POWER", object.airpower);
                    bundle.putInt("WATER_POWER", object.waterpower);
                    bundle.putInt("XP", object.XP);
                    Intent intent = new Intent(MapsActivity.this, SuperJumper.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                catch(Exception e){
                    Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.no_connection),
                            Toast.LENGTH_LONG).show();
                }


            }
        });
        travelHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap!=null) {
                    ///Sätter lat/long så att inte markers uppdateras i fel värld!
                    myRef.child("latitude").setValue(lastNormalLatitude);
                    myRef.child("longitude").setValue(lastNormalLongitude);
                    myRef.child("travelMode").setValue(0);
                    myPositionLatitude = lastNormalLatitude;
                    myPositionLongitude = lastNormalLongitude;
                    String magic = "Travel Home";
                    LatLng cameraPosition = new LatLng(myPositionLatitude, myPositionLongitude);
                    CameraPosition currentCameraPosition = mMap.getCameraPosition();
                    Log.i("TAG", "CURRENT CAMERA POSITION" + currentCameraPosition);

                    ///mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition));

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .bearing(currentCameraPosition.bearing)
                                    .target(cameraPosition)
                                    .tilt(90)
                                    .zoom(19)
                                    .build()));

                    closePicked();
                    closeBackpack();
                    vibrate();
                    doMagicAnimation(magic);
                }
            }
        });
        travelAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///ToDo check level

                if (mMap!=null) {
                    String magic="";
                    if (currentTravelwindSelected.equals("Paris")){
                        myRef.child("travelMode").setValue(1);
                        myPositionLatitude = 48.853320;
                        myPositionLongitude = 2.348600;
                        magic ="Paris";
                    }
                    else if (currentTravelwindSelected.equals("London")){
                        myRef.child("travelMode").setValue(2);
                        myPositionLatitude = 51.508530;
                        myPositionLongitude = -0.076132;
                        magic ="London";
                    }
                    else if (currentTravelwindSelected.equals("India")){
                        myRef.child("travelMode").setValue(3);

                        myPositionLatitude = 27.173891;
                        myPositionLongitude = 78.042068;

                        ///myPositionLatitude = 29.976480;
                       /// myPositionLongitude = 31.131302;
                        magic ="India";
                    }

                    LatLng cameraPosition = new LatLng(myPositionLatitude, myPositionLongitude);
                    CameraPosition currentCameraPosition = mMap.getCameraPosition();
                    Log.i("TAG", "CURRENT CAMERA POSITION" + currentCameraPosition);

                    ///mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition));

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .bearing(currentCameraPosition.bearing)
                                    .target(cameraPosition)
                                    .tilt(90)
                                    .zoom(19)
                                    .build()));

                    closePicked();
                    closeBackpack();
                    vibrate();
                    doMagicAnimation(magic);
                }

            }
        });
        useScrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///Kontrollerar om det finns tillräckligt med elements och genomför eventuellt en "förvandling"
                Log.i("TAGGY", "Key selected: "+currentItemKeySelected);
                Log.i("TAGGY", "Name selected: "+currentItemNameSelected);
                if (currentItemNameSelected.equals("Ancient Scrollidge")){
                    Log.i("TAGGY", "PersonTotalXP: "+mMapsActivity.getInstance().personTotalXP.getText().toString());

                    if (plantsList.size()<4){Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.you_need_more_plants),
                            Toast.LENGTH_LONG).show();}
                    else if (plantsList.size()>3){


                        ///Ta bort fyra plants från collectibleItems på servern
                        myFirebaseRef.child(plantsList.get(0)).removeValue();
                        myFirebaseRef.child(plantsList.get(1)).removeValue();
                        myFirebaseRef.child(plantsList.get(2)).removeValue();
                        myFirebaseRef.child(plantsList.get(3)).removeValue();
                        ///Ta bort aktuell scroll från servern
                        myFirebaseRef.child(currentItemKeySelected).removeValue();
                        ///Lägg till ökad xp
                        int tmpXP = mMapsActivity.getInstance().object.XP;
                        int changeXP = tmpXP+150;
                        myRef.child("XP").setValue(changeXP);
                        String magic = "XP Increased";
                        closePicked();
                        closeBackpack();
                        vibrate();
                        doMagicAnimation(magic);

                    }
                }
                else if (currentItemNameSelected.equals("Healing Scrollifix")){
                    if (waterdropsList.size()<4){Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.you_need_more_waterdrops),
                            Toast.LENGTH_LONG).show();}
                    else if (waterdropsList.size()>3){

                        ///Ta bort fyra waterdrops från collectibleItems på servern
                        myFirebaseRef.child(waterdropsList.get(0)).removeValue();
                        myFirebaseRef.child(waterdropsList.get(1)).removeValue();
                        myFirebaseRef.child(waterdropsList.get(2)).removeValue();
                        myFirebaseRef.child(waterdropsList.get(3)).removeValue();
                        ///Ta bort aktuell scroll från servern
                        myFirebaseRef.child(currentItemKeySelected).removeValue();
                        ///Lägg till Healing Portion
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        long itemTimestamp = date.getTime();
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Healing Potion", "Potion", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        String magic = "Healing Potion";
                        closePicked();
                        closeBackpack();
                        vibrate();
                        doMagicAnimation(magic);
                    }
                }
                else if (currentItemNameSelected.equals("Swift Scrollifly")){
                    if (trombulusList.size()<4){Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.you_need_more_tromuluses),
                            Toast.LENGTH_LONG).show();}
                    if (trombulusList.size()>3){

                        myFirebaseRef.child(trombulusList.get(0)).removeValue();
                        myFirebaseRef.child(trombulusList.get(1)).removeValue();
                        myFirebaseRef.child(trombulusList.get(2)).removeValue();
                        myFirebaseRef.child(trombulusList.get(3)).removeValue();
                        ///Ta bort aktuell scroll från servern
                        myFirebaseRef.child(currentItemKeySelected).removeValue();
                        ///Lägg till Healing Portion
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        long itemTimestamp = date.getTime();
                        String key = myRef.child("collectedItems").push().getKey();
                        Random randomTravelwind = new Random();
                        int slumpTravelwind = randomTravelwind.nextInt(3)+1;
                        if (slumpTravelwind==1) {
                            CollectedItem tmpCollectedItem2 = new CollectedItem("Travelwind Paris", "Travel", "imageRef", itemTimestamp, key);
                            myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        }else if (slumpTravelwind==2) {
                            CollectedItem tmpCollectedItem2 = new CollectedItem("Travelwind London", "Travel", "imageRef", itemTimestamp, key);
                            myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        }else if (slumpTravelwind==3) {
                            CollectedItem tmpCollectedItem2 = new CollectedItem("Travelwind India", "Travel", "imageRef", itemTimestamp, key);
                            myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        }

                        String magic = "Travelwind Added";
                        closePicked();
                        closeBackpack();
                        vibrate();
                        doMagicAnimation(magic);

                    }
                }
                else if (currentItemNameSelected.equals("Mighty Scrollipow")){
                    if (flamesList.size()<4){Toast.makeText(mMapsActivity.getInstance(), getResources().getString(R.string.you_need_more_flames),
                            Toast.LENGTH_LONG).show();}
                    else if (flamesList.size()>3){


                        myFirebaseRef.child(flamesList.get(0)).removeValue();
                        myFirebaseRef.child(flamesList.get(1)).removeValue();
                        myFirebaseRef.child(flamesList.get(2)).removeValue();
                        myFirebaseRef.child(flamesList.get(3)).removeValue();
                        ///Ta bort aktuell scroll från servern
                        myFirebaseRef.child(currentItemKeySelected).removeValue();
                        ///Lägg till Healing Portion
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        long itemTimestamp = date.getTime();
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Combat Potion", "Potion", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        String magic = "Combat Potion";
                        closePicked();
                        closeBackpack();
                        vibrate();
                        doMagicAnimation(magic);

                    }
                }

            }
        });

        closePickedButton = (ImageButton) findViewById(R.id.closePickedButton);
        closeLoadingScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeLoadingScreen();
            }
        });
        closePickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closePicked();
                ///isLeveledUp
                isLeveledUp=false;

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (relativeLayoutRecycle.getVisibility()==View.INVISIBLE) {
                    relativeLayoutRecycle.setVisibility(View.VISIBLE);
                    relativeLayoutPerson.setVisibility(View.INVISIBLE);
                   /// personFab.setVisibility(View.INVISIBLE);
                    personFab.setBackgroundResource(R.drawable.girlfaceblue);
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
                    relativeLayoutRecycle.setVisibility(View.INVISIBLE);
                   /// fab.setVisibility(View.INVISIBLE);
                    fab.setBackgroundResource(R.drawable.backpackbuttonblue);
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
        relativeLayoutPicked = (RelativeLayout)findViewById(R.id.relativeLayoutPicked);
        ///relativeLayoutPerson.setEnabled(false);
        relativeLayoutPicked.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
        personFab.setVisibility(View.INVISIBLE);
        closeLoadingScreen.setVisibility(View.INVISIBLE);


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
        mMap.setMinZoomPreference(17.0f);
        mMap.setMaxZoomPreference(19.0f);

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int mResource = R.raw.style_json;
        if (currentHour<18 && currentHour>7){mResource = R.raw.style_json;
        } else {mResource = R.raw.night;}


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, mResource));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                mMap.clear();
                ///Ta en snapshot av databasen och lägg in den i UserObject object
                object = dataSnapshot.getValue(UserObject.class);

                ///Hämta reseläget från servern och sätt det lokalt. Är detta ok???
                localTravelMode=object.travelMode;

                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                long checkMarkersTimestamp = date.getTime();
                ///ToDo Ändra värdet för timer till 30 minuter före test
                if (checkMarkersTimestamp>object.timer+60000 &&myPositionLatitude != 0 && myPositionLongitude!=0 &&onlyOneTime==0 &&object.travelMode==0) {
                    ///ToDo object.latitude och object.longitude kan vara från andra delar av världen
                    updateMarkers(object.latitude, object.longitude, checkMarkersTimestamp);
                    onlyOneTime=1;
                }
                /// textHome.setText(object.username+" "+object.email);

                ///Ringar eller "cirklar" som märker ut 250 meter respektive 500 meter

               /// mPositionLatitude.setText(""+object.latitude);
               /// mPositionLongitude.setText(""+object.longitude);
                mCircleTotal.center(new LatLng(myPositionLatitude, myPositionLongitude));
                mCircleTotal.radius(250);
                mCircleTotal.strokeColor(Color.parseColor("#dedede"));
                mCircleTotal.strokeWidth(50f);
                mMap.addCircle(mCircleTotal);

                mCircleTotalMax.center(new LatLng(myPositionLatitude, myPositionLongitude));
                mCircleTotalMax.radius(500);
                mCircleTotalMax.strokeColor(Color.parseColor("#dedede"));
                mCircleTotalMax.strokeWidth(50f);
                mMap.addCircle(mCircleTotalMax);


                LatLng mPosition = new LatLng(object.latitude, object.longitude);
                if (showWizard==0) {
                    mMap.addMarker(new MarkerOptions().position(mPosition).title("MyMarker").icon(BitmapDescriptorFactory.fromResource(R.drawable.girl)));
                } else if (showWizard==1) {
                    mMap.addMarker(new MarkerOptions().position(mPosition).title("MyMarker").icon(BitmapDescriptorFactory.fromResource(R.drawable.girl2)));
                }

                for (int i=0;i<object.markerList.size();i++){
                    Log.d("TAG", "markerSize before LatLng: "+object.markerList.size());
                    LatLng tmpPosition = new LatLng(object.markerList.get(i).markerLatitude, object.markerList.get(i).markerLongitude);



                    Log.i("Object", "Object_markerlist_type: "+object.markerList.get(i).markerType);
                    Marker tmpMarker = null;
                    if (object.markerList.get(i).markerType.equals("earth")){
                        tmpMarker = mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.earth_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("earth2")){
                        tmpMarker = mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.earth_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("earth3")){
                        tmpMarker = mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.earth_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("earth4")){
                        tmpMarker = mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.earth_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("fire")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.fire_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("fire2")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.fire_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("fire3")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.fire_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("fire4")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.fire_item)));
                    }

                    else if (object.markerList.get(i).markerType.equals("air")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.air_item)));

                    }
                    else if (object.markerList.get(i).markerType.equals("ai2")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.air_item)));

                    }
                    else if (object.markerList.get(i).markerType.equals("air3")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.air_item)));

                    }
                    else if (object.markerList.get(i).markerType.equals("air4")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.air_item)));

                    }
                    else if (object.markerList.get(i).markerType.equals("water")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.water_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("water2")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.water_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("water3")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.water_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("water4")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.water_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("scroll")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.scroll_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("scroll2")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.scroll_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("scroll3")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.scroll_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("scroll4")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.scroll_item)));
                    }
                    else if (object.markerList.get(i).markerType.equals("wizardacademy")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.wizardacademy)));
                    }
                    else if (object.markerList.get(i).markerType.equals("towerair")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.towerair)));
                    }
                    else if (object.markerList.get(i).markerType.equals("towerearth")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.towerearth)));
                    }
                    else if (object.markerList.get(i).markerType.equals("towerwater")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.towerwater)));
                    }
                    else if (object.markerList.get(i).markerType.equals("towerfire")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.towerfire)));
                    }
                    else if (object.markerList.get(i).markerType.equals("hunchback")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_hunchback)));
                    }
                    else if (object.markerList.get(i).markerType.equals("gent")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gent)));
                    }
                    else if (object.markerList.get(i).markerType.equals("bull")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bull)));
                    }
                    else if (object.markerList.get(i).markerType.equals("captain")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_captain)));
                    }
                    else if (object.markerList.get(i).markerType.equals("wizgirl")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_wizgirl)));
                    }
                    else if (object.markerList.get(i).markerType.equals("wizboy")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_wizboy)));
                    }
                    else if (object.markerList.get(i).markerType.equals("darkwiz")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_darkwiz)));
                    }
                    else if (object.markerList.get(i).markerType.equals("birdman")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_birdman)));
                    }
                    else if (object.markerList.get(i).markerType.equals("speargirl")){
                        tmpMarker =mMap.addMarker(new MarkerOptions().position(tmpPosition).title(object.markerList.get(i).markerType).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_speargirl)));
                    }
                    if (tmpMarker!=null){tmpMarker.setAnchor(0.5f,1.0f);}
                    Log.d("TAG", "markerSize: "+object.markerList.size());
                }

                ///Fyller Personobjekt här
                ///personImage = (ImageView)findViewById(R.id.personImage);
                personUserName.setText(object.username);

                personCP.setText("CP: "+object.cp+"/"+object.maxcp);
                personHP.setText("HP: "+object.hp+"/"+object.maxhp);
                elementEarthValue.setText(" "+object.earthpower);
                elementFireValue.setText(" "+object.firepower);
                elementAirValue.setText(" "+object.airpower);
                elementWaterValue.setText(" "+object.waterpower);

                if (isLeveledUp==false){checkNewLevel(object.level, object.XP);}
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

                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                closeLoadingScreen.setVisibility(View.VISIBLE);


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
            ///mPositionLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
           /// mPositionLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
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

    if (showWizard==0){showWizard=1;} else if (showWizard==1){showWizard=0;}

        if (onlyOneTime==1){onlyOneTime=0;} ///Nollställ uppdatering av markers
        animateCircle();
        mCircle.strokeColor(0x00000000);
        mCircle.fillColor(0x00000000);

        if (localTravelMode>0){
            Log.i("TAGGY", "TravelStarted: "+travelStarted);
           /// myPositionLatitude = 48.852966;
            if (travelStarted==false){
                if (localTravelMode==1) {
                    myPositionLatitude = 48.853320;
                    myPositionLongitude = 2.348600;
                    lastTravelLatitude = 48.853320;
                    lastTravelLongitude = 2.348600;
                }else if (localTravelMode==2) {
                    myPositionLatitude = 51.508530;
                    myPositionLongitude = -0.076132;
                    lastTravelLatitude = 51.508530;
                    lastTravelLongitude = -0.076132;
                }else if (localTravelMode==3) {
                    myPositionLatitude = 27.173891;
                    myPositionLongitude = 78.042068;
                    lastTravelLatitude = 27.173891;
                    lastTravelLongitude = 78.042068;

                }
                travelStarted=true;
                lastNormalLatitude = location.getLatitude();
                lastNormalLongitude = location.getLongitude();

            } else if(travelStarted==true) {

                Location lastLocation = new Location("");
                ///Sätt värdet innan det sätts till ett nytt
                lastLocation.setLatitude(lastNormalLatitude);
                lastLocation.setLongitude(lastNormalLongitude);
                ///Sätt det nya värdet
                lastNormalLatitude = location.getLatitude();
                lastNormalLongitude = location.getLongitude();
                float distance = lastLocation.distanceTo(location);
                float bearing = lastLocation.bearingTo(location);
                LatLng tmpPosition = getLatLng(distance, bearing, lastTravelLatitude, lastTravelLongitude);
                lastTravelLatitude = tmpPosition.latitude;
                lastTravelLongitude = tmpPosition.longitude;
                myPositionLatitude = lastTravelLatitude;
                myPositionLongitude = lastTravelLongitude;

            }

        }else {
            myPositionLatitude = location.getLatitude();
            myPositionLongitude = location.getLongitude();
        }
        myRef.child("latitude").setValue(myPositionLatitude);
        myRef.child("longitude").setValue(myPositionLongitude);
        ///Tidigare var myPositionLatitude+0.0005, men rotationen blev sned
        LatLng cameraPosition = new LatLng(myPositionLatitude, myPositionLongitude);
        CameraPosition currentCameraPosition = mMap.getCameraPosition();
        Log.i("TAG", "CURRENT CAMERA POSITION" +currentCameraPosition);
        ///mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .bearing(currentCameraPosition.bearing)
                        .target(cameraPosition)
                        .tilt(90)
                        .zoom(19)
                        .build()));


        ///mMap.getUiSettings(). setZoomGesturesEnabled(false);

        mMap.getUiSettings(). setScrollGesturesEnabled(false);
        mMap.getUiSettings(). setCompassEnabled(false);



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

                tmpCollectedItem.imageRef = collectedItem.imageRef;
                tmpCollectedItem.itemName = collectedItem.itemName;
                tmpCollectedItem.itemType = collectedItem.itemType;
                tmpCollectedItem.timestamp = collectedItem.timestamp;
                tmpCollectedItem.uid = collectedItem.uid;
                tmpCollectedItems.add(tmpCollectedItem);

                String key = collectedItem.uid;
                objectViewHolder.itemKeyList.setText(key);
                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy HH:mm:ss");
                String formatedDate = sdf.format(collectedItem.timestamp);
                objectViewHolder.itemDateList.setText(formatedDate);

                ///objectViewHolder.itemName.setText(collectedItem.itemName);
               /// objectViewHolder.itemType.setText(collectedItem.itemType);
                Log.i("TAGGY", "POPULATE ITEMNAME: "+collectedItem.itemName);

                if (collectedItem.itemName.equals("Plant")) {
                    objectViewHolder.itemNoLocale.setText("Plant");
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.earth_item);
                    objectViewHolder.itemNameList.setText(R.string.plant);
                    objectViewHolder.itemTypeList.setText(R.string.earth);
                }
                else if (collectedItem.itemName.equals("Flames")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.fire_item);
                    objectViewHolder.itemNoLocale.setText("Flames");
                    objectViewHolder.itemNameList.setText(R.string.flame);
                    objectViewHolder.itemTypeList.setText(R.string.fire);
                }
                else if (collectedItem.itemName.equals("Trombulus")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.air_item);
                    objectViewHolder.itemNoLocale.setText("Trombulus");
                    objectViewHolder.itemNameList.setText(R.string.trombulus);
                    objectViewHolder.itemTypeList.setText(R.string.air);
                }
                else if (collectedItem.itemName.equals("Waterdrop")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.water_item);
                    objectViewHolder.itemNoLocale.setText("Waterdrop");
                    objectViewHolder.itemNameList.setText(R.string.waterdrop);
                    objectViewHolder.itemTypeList.setText(R.string.water);
                }
                else if (collectedItem.itemName.equals("Healing Scrollifix")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.scroll_item);
                    objectViewHolder.itemNoLocale.setText("Healing Scrollifix");
                    objectViewHolder.itemNameList.setText(R.string.healing_scrollifix);
                    objectViewHolder.itemTypeList.setText(R.string.scroll);
                }
                else if (collectedItem.itemName.equals("Mighty Scrollipow")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.scroll_item);
                    objectViewHolder.itemNoLocale.setText("Mighty Scrollipow");
                    objectViewHolder.itemNameList.setText(R.string.mighty_scrollipow);
                    objectViewHolder.itemTypeList.setText(R.string.scroll);
                }
                else if (collectedItem.itemName.equals("Swift Scrollifly")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.scroll_item);
                    objectViewHolder.itemNoLocale.setText("Swift Scrollifly");
                    objectViewHolder.itemNameList.setText(R.string.swift_scrollifly);
                    objectViewHolder.itemTypeList.setText(R.string.scroll);
                }
                else if (collectedItem.itemName.equals("Ancient Scrollidge")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.scroll_item);
                    objectViewHolder.itemNoLocale.setText("Ancient Scrollidge");
                    objectViewHolder.itemNameList.setText(R.string.ancient_scrollidge);
                    objectViewHolder.itemTypeList.setText(R.string.scroll);
                }
                else if (collectedItem.itemName.equals("Healing Potion")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.healingpotion);
                    objectViewHolder.itemNoLocale.setText("Healing Potion");
                    objectViewHolder.itemNameList.setText(R.string.healing_potion);
                    objectViewHolder.itemTypeList.setText(R.string.potion);
                }
                else if (collectedItem.itemName.equals("Combat Potion")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.cppotion);
                    objectViewHolder.itemNoLocale.setText("Combat Potion");
                    objectViewHolder.itemNameList.setText(R.string.combat_potion);
                    objectViewHolder.itemTypeList.setText(R.string.potion);
                }
                else if (collectedItem.itemName.equals("Travelwind Paris")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.travelwind);
                    objectViewHolder.itemNoLocale.setText("Travelwind Paris");
                    objectViewHolder.itemNameList.setText(R.string.travelwind_paris);
                    objectViewHolder.itemTypeList.setText(R.string.travel);
                }
                else if (collectedItem.itemName.equals("Travelwind London")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.travelwind);
                    objectViewHolder.itemNoLocale.setText("Travelwind London");
                    objectViewHolder.itemNameList.setText(R.string.travelwind_london);
                    objectViewHolder.itemTypeList.setText(R.string.travel);
                }
                else if (collectedItem.itemName.equals("Travelwind India")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.travelwind);
                    objectViewHolder.itemNoLocale.setText("Travelwind India");
                    objectViewHolder.itemNameList.setText(R.string.travelwind_india);
                    objectViewHolder.itemTypeList.setText(R.string.travel);
                }
                else if (collectedItem.itemName.equals("Artefact Paris")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.artefact_painter);
                    objectViewHolder.itemNoLocale.setText("Artefact Paris");
                    objectViewHolder.itemNameList.setText(R.string.artefact_paris);
                    objectViewHolder.itemTypeList.setText(R.string.artefact);
                    artefactParis = artefactParis+1;
                }
                else if (collectedItem.itemName.equals("Artefact London")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.artefact_phone);
                    objectViewHolder.itemNoLocale.setText("Artefact London");
                    objectViewHolder.itemNameList.setText(R.string.artefact_london);
                    objectViewHolder.itemTypeList.setText(R.string.artefact);
                    artefactLondon = artefactLondon+1;
                }
                else if (collectedItem.itemName.equals("Artefact India")) {
                    objectViewHolder.itemIconList.setBackgroundResource(R.drawable.artefact_bull);
                    objectViewHolder.itemNoLocale.setText("Artefact India");
                    objectViewHolder.itemNameList.setText(R.string.artefact_india);
                    objectViewHolder.itemTypeList.setText(R.string.artefact);
                    artefactIndia = artefactIndia+1;
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


        // Starts the bounce animation
        ///mHandler.removeCallbacks(mAnimation);
       /// BounceAnimation mAnimation = new BounceAnimation(start, duration, marker, mHandler);
       /// mHandler.post(mAnimation);
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
            mHandler.removeCallbacks(mAnimation);
            BounceAnimation mAnimation = new BounceAnimation(start, duration, marker, mHandler);
            mHandler.post(mAnimation);
            Log.d("TAG", "DISTANCE IS BIGGER THAN RADIUS");
            Toast.makeText(this, R.string.not_in_range,
                    Toast.LENGTH_LONG).show();

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
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Plant", "Earth", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("earth2")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Plant", "Earth", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("earth3")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Plant", "Earth", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("earth4")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Plant", "Earth", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("fire")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Flames", "Fire", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("fire2")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Flames", "Fire", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("fire3")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Flames", "Fire", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("fire4")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Flames", "Fire", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("air")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Trombulus", "Air", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("air2")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Trombulus", "Air", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("air3")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Trombulus", "Air", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("air4")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Trombulus", "Air", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                    }
                    else if (object.markerList.get(i).markerType.equals("water")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Waterdrop", "Water", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                        break;
                    }
                    else if (object.markerList.get(i).markerType.equals("water2")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Waterdrop", "Water", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                        break;
                    }
                    else if (object.markerList.get(i).markerType.equals("water3")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Waterdrop", "Water", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                        break;
                    }
                    else if (object.markerList.get(i).markerType.equals("water4")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Waterdrop", "Water", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);
                        break;
                    }
                    else if (object.markerList.get(i).markerType.equals("scroll")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Healing Scrollifix", "Scroll", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);

                    }
                    else if (object.markerList.get(i).markerType.equals("scroll2")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Mighty Scrollipow", "Scroll", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);

                    }
                    else if (object.markerList.get(i).markerType.equals("scroll3")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Swift Scrollifly", "Scroll", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);

                    }
                    else if (object.markerList.get(i).markerType.equals("scroll4")){
                        String key = myRef.child("collectedItems").push().getKey();
                        CollectedItem tmpCollectedItem2 = new CollectedItem("Ancient Scrollidge", "Scroll", "imageRef", itemTimestamp, key);
                        myRef.child("collectedItems").child(key).setValue(tmpCollectedItem2);
                        showPickedUpItem(name, tmpId);

                    }

                }

            }
            for (int i=0;i<object.markerList.size();i++){
                String tmpId = Integer.toString(i);
                if (name.equalsIgnoreCase(object.markerList.get(i).markerType)){
                    if (object.markerList.get(i).markerType.equals("wizardacademy")){
                        showWizardAcademy();

                    } else if (object.markerList.get(i).markerType.equals("birdman")
                            ||object.markerList.get(i).markerType.equals("speargirl")
                            ||object.markerList.get(i).markerType.equals("wizboy")
                            ||object.markerList.get(i).markerType.equals("wizgirl")
                            ||object.markerList.get(i).markerType.equals("captain")
                            ||object.markerList.get(i).markerType.equals("darkwiz")
                            ||object.markerList.get(i).markerType.equals("hunchback")
                            ||object.markerList.get(i).markerType.equals("gent")
                            ||object.markerList.get(i).markerType.equals("bull")
                            ){
                        try {
                            myRef.child("markerList").child(tmpId).child("markerLatitude").setValue(0);
                            myRef.child("markerList").child(tmpId).child("markerLongitude").setValue(0);
                            if (object.markerList.get(i).markerType.equals("birdman")){
                            fight("birdman", "wanderful world");
                            } else if (object.markerList.get(i).markerType.equals("speargirl")){
                                fight("speargirl", "wanderful world");
                            }else if (object.markerList.get(i).markerType.equals("wizboy")){
                                fight("wizboy", "wanderful world");
                            }else if (object.markerList.get(i).markerType.equals("wizgirl")){
                                fight("wizgirl", "wanderful world");
                            }else if (object.markerList.get(i).markerType.equals("captain")){
                                fight("captain", "wanderful world");
                            }else if (object.markerList.get(i).markerType.equals("darkwiz")){
                                fight("darkwiz", "wanderful world");
                            }else if (object.markerList.get(i).markerType.equals("hunchback")){
                                fight("hunchback", "paris");
                            }else if (object.markerList.get(i).markerType.equals("gent")){
                                fight("gent", "london");
                            }else if (object.markerList.get(i).markerType.equals("bull")){
                                fight("bull", "india");
                            }
                        } catch (Exception e){

                        }

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
                ///De första 4 värdena anger transparent 55 procent
               mCircle.fillColor(0x5528b6e8);

                mMap.addCircle(mCircle);
               /// circleAnimation.stop();
               /// circleAnimation.start();
                ///ToDo move to right methods
               /// circleImageView.setVisibility(View.VISIBLE);

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

        int randomDistance = 0;
        float randomBearing = 0.0f;

        randomList.clear();
        randomList2.clear();
        tmpMarkersList.clear();
        for (int i=0;i<20;i++){
            ///Jag vill ha noll också
            if (i<6) {
                randomBearing = (float) (90 * Math.random());
            } else if (i>5&&i<11){
                randomBearing = (float) ((90 * Math.random())+90);
            }
            else if (i>10&&i<16){
                randomBearing = (float) ((90 * Math.random())+180);
            }
            else if (i>15&&i<21){
                randomBearing = (float) ((90 * Math.random())+270);
            }
            randomDistance = (int) ((500 * Math.random()) + 80);
            LatLng tmpLatLng = getLatLng(randomDistance, randomBearing, myLatitude, myLongitude);
            double theLatitude = tmpLatLng.latitude;
            double theLongitude = tmpLatLng.longitude;
            randomList.add(theLatitude);
            randomList2.add(theLongitude);
        }

///ToDo fix two first markers

        ///Här vill jag kunna placera ut 20 olika markers plus standardmarkers (wizhouse och torn)

            MyMarker tmpMarker = new MyMarker(randomList.get(0), randomList2.get(0), "earth");
            tmpMarkersList.add(tmpMarker);
            MyMarker tmpMarker2 = new MyMarker(randomList.get(1), randomList2.get(1), "fire");
            tmpMarkersList.add(tmpMarker2);
            MyMarker tmpMarker3 = new MyMarker(randomList.get(2), randomList2.get(2), "air");
            tmpMarkersList.add(tmpMarker3);
            MyMarker tmpMarker4 = new MyMarker(randomList.get(3), randomList2.get(3), "water");
            tmpMarkersList.add(tmpMarker4);
            MyMarker tmpMarker5 = new MyMarker(randomList.get(4), randomList2.get(4), "scroll");
            tmpMarkersList.add(tmpMarker5);

            MyMarker tmpMarker6 = new MyMarker(randomList.get(5), randomList2.get(5), "earth2");
            tmpMarkersList.add(tmpMarker6);
            MyMarker tmpMarker7 = new MyMarker(randomList.get(6), randomList2.get(6), "fire2");
            tmpMarkersList.add(tmpMarker7);
            MyMarker tmpMarker8 = new MyMarker(randomList.get(7), randomList2.get(7), "air2");
            tmpMarkersList.add(tmpMarker8);
            MyMarker tmpMarker9 = new MyMarker(randomList.get(8), randomList2.get(8), "water2");
            tmpMarkersList.add(tmpMarker9);
            MyMarker tmpMarker10 = new MyMarker(randomList.get(9), randomList2.get(9), "scroll2");
            tmpMarkersList.add(tmpMarker10);

            MyMarker tmpMarker11 = new MyMarker(randomList.get(10), randomList2.get(10), "earth3");
            tmpMarkersList.add(tmpMarker11);
            MyMarker tmpMarker12 = new MyMarker(randomList.get(11), randomList2.get(11), "fire3");
            tmpMarkersList.add(tmpMarker12);
            MyMarker tmpMarker13 = new MyMarker(randomList.get(12), randomList2.get(12), "air3");
            tmpMarkersList.add(tmpMarker13);
            MyMarker tmpMarker14 = new MyMarker(randomList.get(13), randomList2.get(13), "water3");
            tmpMarkersList.add(tmpMarker14);
            MyMarker tmpMarker15 = new MyMarker(randomList.get(14), randomList2.get(14), "scroll3");
            tmpMarkersList.add(tmpMarker15);

            MyMarker tmpMarker16 = new MyMarker(randomList.get(15), randomList2.get(15), "earth4");
            tmpMarkersList.add(tmpMarker16);
            MyMarker tmpMarker17 = new MyMarker(randomList.get(16), randomList2.get(16), "fire4");
            tmpMarkersList.add(tmpMarker17);
            MyMarker tmpMarker18 = new MyMarker(randomList.get(17), randomList2.get(17), "air4");
            tmpMarkersList.add(tmpMarker18);
            MyMarker tmpMarker19 = new MyMarker(randomList.get(18), randomList2.get(18), "water4");
            tmpMarkersList.add(tmpMarker19);
            MyMarker tmpMarker20 = new MyMarker(randomList.get(19), randomList2.get(19), "scroll4");
            tmpMarkersList.add(tmpMarker20);

        ///ToDo fixa till lat/long för de här också
        MyMarker tmpMarkerHouse = new MyMarker(myLatitude + 0.0004, myLongitude - 0.0004, "wizardacademy");
        tmpMarkersList.add(tmpMarkerHouse);

        MyMarker towerAir = new MyMarker(myLatitude + 0.00215, myLongitude, "towerair");
        tmpMarkersList.add(towerAir);
        MyMarker towerEarth = new MyMarker(myLatitude - 0.00215, myLongitude, "towerearth");
        tmpMarkersList.add(towerEarth);
        MyMarker towerFire = new MyMarker(myLatitude, myLongitude + 0.0049, "towerfire");
        tmpMarkersList.add(towerFire);
        MyMarker towerWater = new MyMarker(myLatitude, myLongitude - 0.0049, "towerwater");
        tmpMarkersList.add(towerWater);

        ///I London
        MyMarker gent = new MyMarker(myLatitude+ 0.0002, myLongitude + 0.0002, "gent");
        tmpMarkersList.add(gent);
        ///I Paris
        MyMarker hunchback = new MyMarker(myLatitude+ 0.0002, myLongitude + 0.0002, "hunchback");
        tmpMarkersList.add(hunchback);
        ///I Indien
        MyMarker bull = new MyMarker(myLatitude - 0.0002, myLongitude- 0.0002, "bull");
        tmpMarkersList.add(bull);

        MyMarker birdman = new MyMarker(myLatitude + 0.00012, myLongitude+ 0.00012, "birdman");
        tmpMarkersList.add(birdman);

        MyMarker captain = new MyMarker(myLatitude + 0.0002, myLongitude+ 0.0002, "captain");
        tmpMarkersList.add(captain);

        MyMarker wizgirl = new MyMarker(myLatitude + 0.0002, myLongitude+ 0.0002, "wizgirl");
        tmpMarkersList.add(wizgirl);

        MyMarker wizboy = new MyMarker(myLatitude + 0.0002, myLongitude+ 0.0002, "wizboy");
        tmpMarkersList.add(wizboy);

        MyMarker speargirl = new MyMarker(myLatitude + 0.0001, myLongitude+ 0.0001, "speargirl");
        tmpMarkersList.add(speargirl);

        MyMarker darkwiz = new MyMarker(myLatitude + 0.0002, myLongitude+ 0.0002, "darkwiz");
        tmpMarkersList.add(darkwiz);

            myRef.child("markerList").setValue(tmpMarkersList);
            Toast.makeText(this, R.string.world_has_changed,
                    Toast.LENGTH_LONG).show();


    }


    public static class ObjectViewHolder extends RecyclerView.ViewHolder {

        ImageView itemIconList;
        TextView itemNameList;
        TextView itemTypeList;
        TextView mDivider;
        TextView itemKeyList;
        TextView itemNoLocale;
        TextView itemDateList;
        ImageButton removeButton;

        public ObjectViewHolder(View v) {
            super(v);
            itemIconList = (ImageView) v.findViewById(R.id.itemIconList);
            itemNameList = (TextView) v.findViewById(R.id.itemNameList);
            itemTypeList = (TextView) v.findViewById(R.id.itemTypeList);
            itemKeyList = (TextView) v.findViewById(R.id.itemKeyList);
            itemNoLocale = (TextView) v.findViewById(R.id.itemNoLocale);
            itemDateList = (TextView) v.findViewById(R.id.itemDateList);
            mDivider = (TextView) v.findViewById(R.id.divider_map);
            removeButton = (ImageButton) v.findViewById(R.id.removeButtonList);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    String name = itemNoLocale.getText().toString();
                    String type = itemNoLocale.getText().toString();
                    mMapsActivity.getInstance().showItem(name,type);
                    ///Hämta key för valt objekt
                    mMapsActivity.getInstance().currentItemKeySelected = itemKeyList.getText().toString();
                    mMapsActivity.getInstance().currentItemNameSelected = name;

                    ///Hämta antal elementobjekt och deras key
                    int listSize = mMapsActivity.getInstance().adapter.getItemCount();
                    if (name.equals("Healing Scrollifix")){
                        mMapsActivity.getInstance().useScrollButton.setVisibility(View.VISIBLE);
                        mMapsActivity.getInstance().waterdropsList.clear();
                        for (int i = 0;i<listSize;i++){
                            if (mMapsActivity.getInstance().adapter.getItem(i).itemName.equals("Waterdrop")){
                                mMapsActivity.getInstance().waterdropsList.add(mMapsActivity.getInstance().adapter.getItem(i).uid);
                                Log.i("TAGGY", "UI för Waterdrop: "+mMapsActivity.getInstance().adapter.getItem(i).uid);

                            }
                        }
                        Log.i("TAGGY", "NAME ÄR LIKA MED HEALING SCROLLIFIX OCH ANTALET WATERDROPS ÄR: "+mMapsActivity.getInstance().plantsList.size());

                    }
                    else if (name.equals("Mighty Scrollipow")){
                        mMapsActivity.getInstance().useScrollButton.setVisibility(View.VISIBLE);
                        mMapsActivity.getInstance().flamesList.clear();
                        for (int i = 0;i<listSize;i++){
                            if (mMapsActivity.getInstance().adapter.getItem(i).itemName.equals("Flames")){
                                mMapsActivity.getInstance().flamesList.add(mMapsActivity.getInstance().adapter.getItem(i).uid);
                                Log.i("TAGGY", "UI för Flames: "+mMapsActivity.getInstance().adapter.getItem(i).uid);

                            }
                        }
                        Log.i("TAGGY", "NAME ÄR LIKA MED HEALING SCROLLIFIX OCH ANTALET FLAMES ÄR: "+mMapsActivity.getInstance().flamesList.size());

                    }
                    else if (name.equals("Swift Scrollifly")){
                        mMapsActivity.getInstance().useScrollButton.setVisibility(View.VISIBLE);
                        mMapsActivity.getInstance().trombulusList.clear();
                        for (int i = 0;i<listSize;i++){
                            if (mMapsActivity.getInstance().adapter.getItem(i).itemName.equals("Trombulus")){
                                mMapsActivity.getInstance().trombulusList.add(mMapsActivity.getInstance().adapter.getItem(i).uid);
                                Log.i("TAGGY", "UI för Trombulus: "+mMapsActivity.getInstance().adapter.getItem(i).uid);

                            }
                        }
                        Log.i("TAGGY", "NAME ÄR LIKA MED HEALING SCROLLIFIX OCH ANTALET TROMBULUS ÄR: "+mMapsActivity.getInstance().trombulusList.size());

                    }
                    else if (name.equals("Ancient Scrollidge")){
                        mMapsActivity.getInstance().useScrollButton.setVisibility(View.VISIBLE);
                        mMapsActivity.getInstance().plantsList.clear();
                        for (int i = 0;i<listSize;i++){
                            if (mMapsActivity.getInstance().adapter.getItem(i).itemName.equals("Plant")){
                                mMapsActivity.getInstance().plantsList.add(mMapsActivity.getInstance().adapter.getItem(i).uid);
                                Log.i("TAGGY", "UI för Plant: "+mMapsActivity.getInstance().adapter.getItem(i).uid);

                            }
                        }
                        Log.i("TAGGY", "NAME ÄR LIKA MED HEALING SCROLLIFIX OCH ANTALET PLANTS ÄR: "+mMapsActivity.getInstance().plantsList.size());

                    }
                    else if (name.equals("Travelwind Paris")||name.equals("Travelwind London")||name.equals("Travelwind India")){
                        mMapsActivity.getInstance().useScrollButton.setVisibility(View.INVISIBLE);
                        mMapsActivity.getInstance().travelAway.setVisibility(View.VISIBLE);
                        mMapsActivity.getInstance().travelHome.setVisibility(View.VISIBLE);

                        if (name.equals("Travelwind Paris")){
                            mMapsActivity.getInstance().currentTravelwindSelected = "Paris";
                            mMapsActivity.getInstance().travelAway.setText(R.string.paris_button);
                        }
                        else if (name.equals("Travelwind London")){
                            mMapsActivity.getInstance().currentTravelwindSelected = "London";
                            mMapsActivity.getInstance().travelAway.setText(R.string.london_button);
                        }
                        else if (name.equals("Travelwind India")){
                            mMapsActivity.getInstance().currentTravelwindSelected = "India";
                            mMapsActivity.getInstance().travelAway.setText(R.string.india_button);
                        }

                    } else {mMapsActivity.getInstance().useScrollButton.setVisibility(View.INVISIBLE);}





                    Log.i("TAG", "Item clicked JUST ON VIEW: "+position+" Name: " +name+" Type: "+type);
                    Log.i("TAG", "I WILL REMOVE THIS ONE: "+itemKeyList.getText().toString());
                }
            });
            removeButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = getAdapterPosition();
                    myFirebaseRef.child(itemKeyList.getText().toString()).removeValue();
                    return false;
                }
            });
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mMapsActivity.getInstance(), ""+mMapsActivity.getInstance().getResources().getString(R.string.hold_to_remove), Toast.LENGTH_LONG).show();
                }
            });
            itemNameList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.i ("TAG", "Item clicked; "+position);
                    String thisItemName = itemNameList.getText().toString();

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
        isLeveledUp = true;
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

        ///ToDo Inte snyggt om detta körs innan koppling mot servern, dvs. vid start
        relativeLayoutPicked.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        personFab.setVisibility(View.INVISIBLE);
        int changeToLevel = oldLevel+1;
        Random rnd1 = new Random();
        int slumphp = rnd1.nextInt(3)+1;
        int changeToHp = object.maxhp+slumphp;
        Log.i("TAGGY", "slumphp: "+slumphp);
        myRef.child("level").setValue(changeToLevel);
        myRef.child("maxhp").setValue(changeToHp);
        itemTitle.setText(getResources().getString(R.string.you_leveled_up));
        itemType.setText("Level " +changeToLevel);
        itemDescription.setText(getResources().getString(R.string.hp_increased)+" + "+slumphp);
        pickImage.setBackgroundResource(R.drawable.girlfaceblue);



        ///change different values
        ///show message about new level
        ///setlevelbar i slutet
    }
    public void showPickedUpItem (String name, String tmpId){

        ///Flytta itemMarkers ut i havet utanför Afrika!
        myRef.child("markerList").child(tmpId).child("markerLatitude").setValue(0);
        myRef.child("markerList").child(tmpId).child("markerLongitude").setValue(0);

        itemTitle.setText(getResources().getString(R.string.you_picked_up));
        itemDescription.setText(getResources().getString(R.string.item_added_in_backpack));
        relativeLayoutPicked.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        personFab.setVisibility(View.INVISIBLE);

        if (name.equals("earth")){
            itemType.setText(getResources().getString(R.string.a_plant));
            pickImage.setBackgroundResource(R.drawable.earth_item);

        }
        else if (name.equals("fire")){
            itemType.setText(getResources().getString(R.string.a_flame));
            pickImage.setBackgroundResource(R.drawable.fire_item);
        }
        else if (name.equals("air")){
            itemType.setText(getResources().getString(R.string.a_trombulus));
            pickImage.setBackgroundResource(R.drawable.air_item);
        }
        else if (name.equals("water")){
            itemType.setText(getResources().getString(R.string.a_waterdrop));
            pickImage.setBackgroundResource(R.drawable.water_item);
        }
        else if (name.equals("scroll")){
            itemType.setText(getResources().getString(R.string.a_healing_scrollifix));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }
        else if (name.equals("scroll2")){
            itemType.setText(getResources().getString(R.string.a_mighty_scrollipow));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }
        else if (name.equals("scroll3")){
            itemType.setText(getResources().getString(R.string.a_swift_scrollifly));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }
        else if (name.equals("scroll4")){
            itemType.setText(getResources().getString(R.string.an_ancient_scrollidge));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }

        ///itemType = (TextView)findViewById(R.id.itemType);



    }
    public void showItem(String name, String type){


        Log.i("TAGGY", "showITEMNAAME: "+name);
        increaseElementPowers.setVisibility(View.INVISIBLE);

        if (name.equals("Plant")){
            itemTitle.setText(getResources().getString(R.string.plant));
            itemType.setText(getResources().getString(R.string.earth));
            increaseElementPowers.setText(R.string.increase_earthpower);
            increaseElementPowers.setVisibility(View.VISIBLE);
            itemDescription.setText(getResources().getString(R.string.plant_description));
            pickImage.setBackgroundResource(R.drawable.earth_item);
            increaseThisElement="earth";

        }
        else if (name.equals("Flames")){
            itemTitle.setText(getResources().getString(R.string.flame));
            itemType.setText(getResources().getString(R.string.fire));
            increaseElementPowers.setText(R.string.increase_firepower);
            increaseElementPowers.setVisibility(View.VISIBLE);
            itemDescription.setText(getResources().getString(R.string.flame_description));
            pickImage.setBackgroundResource(R.drawable.fire_item);
            increaseThisElement="fire";
        }
        else if (name.equals("Trombulus")){
            itemTitle.setText(getResources().getString(R.string.trombulus));
            itemType.setText(getResources().getString(R.string.air));
            increaseElementPowers.setText(R.string.increase_airpower);
            increaseElementPowers.setVisibility(View.VISIBLE);
            itemDescription.setText(getResources().getString(R.string.trombulus_description));
            pickImage.setBackgroundResource(R.drawable.air_item);
            increaseThisElement="air";
        }
        else if (name.equals("Waterdrop")){
            itemTitle.setText(getResources().getString(R.string.waterdrop));
            itemType.setText(getResources().getString(R.string.water));
            increaseElementPowers.setText(R.string.increase_waterpower);
            increaseElementPowers.setVisibility(View.VISIBLE);
            itemDescription.setText(getResources().getString(R.string.waterdrop_description));
            pickImage.setBackgroundResource(R.drawable.water_item);
            increaseThisElement="water";
        }

        else if (name.equals("Healing Scrollifix")){
          itemTitle.setText(getResources().getString(R.string.healing_scrollifix));
            itemType.setText(getResources().getString(R.string.scroll));
            itemDescription.setText(getResources().getString(R.string.scrollifix_description));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }
        else if (name.equals("Mighty Scrollipow")){
            itemTitle.setText(getResources().getString(R.string.mighty_scrollipow));
            itemType.setText(getResources().getString(R.string.scroll));
            itemDescription.setText(getResources().getString(R.string.scrollipow_description));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }
        else if (name.equals("Swift Scrollifly")){
            itemTitle.setText(getResources().getString(R.string.swift_scrollifly));
            itemType.setText(getResources().getString(R.string.scroll));
            itemDescription.setText(getResources().getString(R.string.scrollifly_description));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }
        else if (name.equals("Ancient Scrollidge")){
            itemTitle.setText(getResources().getString(R.string.ancient_scrollidge));
            itemType.setText(getResources().getString(R.string.scroll));
            itemDescription.setText(getResources().getString(R.string.scrollidge_description));
            pickImage.setBackgroundResource(R.drawable.scroll_item);
        }
        else if (name.equals("Healing Potion")){
            increaseElementPowers.setText(R.string.increase_hp);
            increaseElementPowers.setVisibility(View.VISIBLE);
            itemTitle.setText(getResources().getString(R.string.healing_potion));
            itemType.setText(getResources().getString(R.string.potion));
            itemDescription.setText(getResources().getString(R.string.healing_potion_description));
            pickImage.setBackgroundResource(R.drawable.healingpotion);
            increaseThisElement="hp";
        }
        else if (name.equals("Combat Potion")){
            increaseElementPowers.setText(R.string.increase_elements);
            increaseElementPowers.setVisibility(View.VISIBLE);
            itemTitle.setText(getResources().getString(R.string.combat_potion));
            itemType.setText(getResources().getString(R.string.potion));
            itemDescription.setText(getResources().getString(R.string.combat_potion_description));
            pickImage.setBackgroundResource(R.drawable.cppotion);
            increaseThisElement="elements";
        }
        else if (name.equals("Travelwind Paris")){
            itemTitle.setText(getResources().getString(R.string.travelwind_paris));
            itemType.setText(getResources().getString(R.string.travel));
            itemDescription.setText(getResources().getString(R.string.travelwind_description));
            pickImage.setBackgroundResource(R.drawable.travelwind);
        }
        else if (name.equals("Travelwind London")){
            itemTitle.setText(getResources().getString(R.string.travelwind_london));
            itemType.setText(getResources().getString(R.string.travel));
            itemDescription.setText(getResources().getString(R.string.travelwind_description));
            pickImage.setBackgroundResource(R.drawable.travelwind);
        }
        else if (name.equals("Travelwind India")){
            itemTitle.setText(getResources().getString(R.string.travelwind_india));
            itemType.setText(getResources().getString(R.string.travel));
            itemDescription.setText(getResources().getString(R.string.travelwind_description));
            pickImage.setBackgroundResource(R.drawable.travelwind);
        }
        else if (name.equals("Artefact Paris")){
            itemTitle.setText(getResources().getString(R.string.artefact_paris));
            itemType.setText(getResources().getString(R.string.artefact));
            itemDescription.setText(getResources().getString(R.string.artefact_paris_description));
            pickImage.setBackgroundResource(R.drawable.artefact_painter);
        }
        else if (name.equals("Artefact London")){
            itemTitle.setText(getResources().getString(R.string.artefact_london));
            itemType.setText(getResources().getString(R.string.artefact));
            itemDescription.setText(getResources().getString(R.string.artefact_london_description));
            pickImage.setBackgroundResource(R.drawable.artefact_phone);
        }
        else if (name.equals("Artefact India")){
            itemTitle.setText(getResources().getString(R.string.artefact_india));
            itemType.setText(getResources().getString(R.string.artefact));
            itemDescription.setText(getResources().getString(R.string.artefact_india_description));
            pickImage.setBackgroundResource(R.drawable.artefact_bull);
        }

        relativeLayoutPicked.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        personFab.setVisibility(View.INVISIBLE);


    }
    public void closeLoadingScreen(){

        mImageViewBackground.setVisibility(ImageView.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        personFab.setVisibility(View.VISIBLE);
        closeLoadingScreen.setVisibility(View.INVISIBLE);
    }
    public void showWizardAcademy(){

        itemTitle.setText(getResources().getString(R.string.wizardacademy_title));
        itemType.setText(getResources().getString(R.string.wizardacademy_type));

        if (artefactParis>0 && artefactLondon>0 && artefactIndia>0){
            itemDescription.setText(getResources().getString(R.string.wizardacademy_accepted));
            pickImage.setBackgroundResource(R.drawable.arnrothwizhouse);
        } else {
            itemDescription.setText(getResources().getString(R.string.wizardacademy_description));
            pickImage.setBackgroundResource(R.drawable.arnrothwizhouse);}


        relativeLayoutPicked.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        personFab.setVisibility(View.INVISIBLE);

    }
    public static MapsActivity getInstance() {
        return mMapsActivity;
    }

    public void closePicked(){

        relativeLayoutPicked.setVisibility(View.INVISIBLE);
        useScrollButton.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        personFab.setVisibility(View.VISIBLE);
        travelAway.setVisibility(View.INVISIBLE);
        travelHome.setVisibility(View.INVISIBLE);
        increaseElementPowers.setVisibility(View.INVISIBLE);

    }
    public void closeBackpack(){

        relativeLayoutRecycle.setVisibility(View.INVISIBLE);
        fab.setBackgroundResource(R.drawable.backpackbuttonblue);

    }

    public LatLng getLatLng(float distance, float bearing, double latitude, double longitude){

        double dist = (double)distance/6371e3;
        double brng = Math.toRadians((double)bearing);
        double lat1 = Math.toRadians(latitude);
        double lon1 = Math.toRadians(longitude);

        double lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist) + Math.cos(lat1)*Math.sin(dist)*Math.cos(brng) );
        double a = Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(lat1), Math.cos(dist)-Math.sin(lat1)*Math.sin(lat2));
        double lon2 = lon1 + a;
        lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;
        double sendBackLatitude = Math.toDegrees(lat2);
        double sendBackLongitude = Math.toDegrees(lon2);

        LatLng tmpLatLng = new LatLng(sendBackLatitude, sendBackLongitude);
        return tmpLatLng;
    }

    public void vibrate(){

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }
    public void doMagicAnimation(String magic){

        circleAnimation.stop();
        circleAnimation.start();
        circleImageView.setVisibility(View.VISIBLE);
        itemTitle.setText(getResources().getString(R.string.abrakadabra));
        if (magic.equals("Paris")){
            itemDescription.setText(getResources().getString(R.string.you_traveled_to_paris)+" "+getResources().getString(R.string.you_landed));
            pickImage.setBackgroundResource(R.drawable.travelwind);
        }
        else if (magic.equals("London")){
            itemDescription.setText(getResources().getString(R.string.you_traveled_to_london)+" "+getResources().getString(R.string.you_landed));
            pickImage.setBackgroundResource(R.drawable.travelwind);
        }
        else if (magic.equals("India")){
            itemDescription.setText(getResources().getString(R.string.you_traveled_to_india)+" "+getResources().getString(R.string.you_landed));
            pickImage.setBackgroundResource(R.drawable.travelwind);
        }
        else if (magic.equals("Combat Potion")){
            itemDescription.setText(getResources().getString(R.string.combat_potion_added));
            pickImage.setBackgroundResource(R.drawable.cppotion);
        }
        else if (magic.equals("Travelwind Added")){
            itemDescription.setText(getResources().getString(R.string.travelwind_added));
            pickImage.setBackgroundResource(R.drawable.travelwind);
        }
        else if (magic.equals("Healing Potion")){
            itemDescription.setText(getResources().getString(R.string.healing_potion_added));
            pickImage.setBackgroundResource(R.drawable.healingpotion);
        }
        else if (magic.equals("XP Increased")){
            itemDescription.setText(getResources().getString(R.string.your_xp_increased_with));
            ///ToDo Ändra bild för XP
            pickImage.setBackgroundResource(R.drawable.girl);
        }
        else if (magic.equals("Travel Home")){
            itemDescription.setText(getResources().getString(R.string.you_traveled_home));
            pickImage.setBackgroundResource(R.drawable.travelwind);
        }

        relativeLayoutPicked.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        personFab.setVisibility(View.INVISIBLE);


    }
    public void fight(String enemy, String world){

        Bundle bundle = new Bundle();
        bundle.putString("ENEMY", enemy);
        bundle.putString("WORLD", world);
        bundle.putInt("HP", object.hp);
        bundle.putInt("MAX_HP", object.maxhp);
        bundle.putInt("CP", object.cp);
        bundle.putInt("MAX_CP", object.maxcp);
        bundle.putInt("EARTH_POWER", object.earthpower);
        bundle.putInt("FIRE_POWER", object.firepower);
        bundle.putInt("AIR_POWER", object.airpower);
        bundle.putInt("WATER_POWER", object.waterpower);
        bundle.putInt("XP", object.XP);
        Intent intent = new Intent(MapsActivity.this, SuperJumper.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

}
