package com.l2minigames.wanderfulworld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.l2minigames.framework.Game;
import com.l2minigames.framework.Screen;
import com.l2minigames.framework.impl.GLGame;

public class SuperJumper extends GLGame {
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



}