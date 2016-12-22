package com.l2minigames.wanderfulworld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.l2minigames.framework.Game;
import com.l2minigames.framework.Screen;
import com.l2minigames.framework.impl.GLGame;

import java.util.Calendar;
import java.util.Date;

public class SuperJumper extends GLGame {
    FirebaseDatabase database;
    DatabaseReference myRef;
    boolean firstTimeCreate = true;
    int hp;
    int max_hp;
    int cp;
    int max_cp;
    int earth_power;
    int fire_power;
    int air_power;
    int water_power;
    String enemy;
    String world;
    String uid;


    
    public Screen getStartScreen() {
        return new MainMenuScreen(this, SuperJumper.this);
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();

        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        enemy = bundle.getString("ENEMY");
        world = bundle.getString("WORLD");
        hp = bundle.getInt("HP");
        max_hp = bundle.getInt("MAX_HP");
        cp = bundle.getInt("CP");
        max_cp = bundle.getInt("MAX_CP");
        earth_power = bundle.getInt("EARTH_POWER");
        fire_power = bundle.getInt("FIRE_POWER");
        air_power = bundle.getInt("AIR_POWER");
        water_power = bundle.getInt("WATER_POWER");
        Firebase.setAndroidContext(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            uid = user.getUid();
            Log.d("TAG","UserID: "+uid);


        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(uid);

        Log.i("SUPERJUMPER", "BUNDLE VALUES: "+"enemy: "+enemy+" world: "+world+" hp: "+hp+" maxhp: "+max_hp+" cp: "+cp+" maxcp: "+max_cp+" earthpower: "+earth_power +" firepower: "+fire_power+" airpower: "+air_power+" waterpower: "+water_power);
    }     
    
    @Override
    public void onPause() {
        super.onPause();
        if(Settings.soundEnabled)
            Assets.music.pause();
    }
    public void goMapsActivity(){
        Intent intent = new Intent(SuperJumper.this, MapsActivity.class);
        startActivity(intent);
    }

    public void sendResultBeforeExit(int earthpower, int firepower, int airpower, int waterpower, int hp){
        myRef.child("earthpower").setValue(earthpower);
        myRef.child("firepower").setValue(firepower);
        myRef.child("airpower").setValue(airpower);
        myRef.child("waterpower").setValue(waterpower);
        myRef.child("hp").setValue(hp);
    }
    public void sendArtefactBeforeExit(String artefact){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long itemTimestamp = date.getTime();
        ///Sparar key i objektet genom en push/getKey
        String key = myRef.child("collectedItems").push().getKey();
        CollectedItem tmpCollectedArtefact = new CollectedItem(artefact, "Artefact", "imageRef", itemTimestamp, key);
        myRef.child("collectedItems").child(key).setValue(tmpCollectedArtefact);
    }



}