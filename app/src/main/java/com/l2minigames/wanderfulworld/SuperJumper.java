package com.l2minigames.wanderfulworld;

import android.content.Context;
import android.content.Intent;

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