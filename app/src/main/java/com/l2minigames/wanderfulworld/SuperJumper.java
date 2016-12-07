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
        String enemy = bundle.getString("ENEMY");
        String world = bundle.getString("WORLD");
        int hp = bundle.getInt("HP");
        int max_hp = bundle.getInt("MAX_HP");
        int cp = bundle.getInt("CP");
        int max_cp = bundle.getInt("MAX_CP");
        int earth_power = bundle.getInt("EARTH_POWER");
        int fire_power = bundle.getInt("FIRE_POWER");
        int air_power = bundle.getInt("AIR_POWER");
        int water_power = bundle.getInt("WATER_POWER");

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