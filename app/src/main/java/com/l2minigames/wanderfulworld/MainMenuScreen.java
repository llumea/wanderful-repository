package com.l2minigames.wanderfulworld;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.l2minigames.framework.Game;
import com.l2minigames.framework.Input.TouchEvent;
import com.l2minigames.framework.gl.Camera2D;
import com.l2minigames.framework.gl.SpriteBatcher;
import com.l2minigames.framework.impl.GLScreen;
import com.l2minigames.framework.math.OverlapTester;
import com.l2minigames.framework.math.Rectangle;
import com.l2minigames.framework.math.Vector2;

public class MainMenuScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle soundBounds;
    Rectangle playBounds;
    Rectangle helpBounds;
    Vector2 touchPoint;
    SuperJumper mContext;


    public MainMenuScreen(Game game, SuperJumper context) {
        super(game);
        this.mContext = context;
        guiCam = new Camera2D(glGraphics, 640, 960);
        batcher = new SpriteBatcher(glGraphics, 100);
        soundBounds = new Rectangle(0, 0, 128, 128);
        playBounds = new Rectangle(0, 200, 640, 960);
        helpBounds = new Rectangle(640-128, 0, 128, 128);
        touchPoint = new Vector2();               
    }       

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);                        
            if(event.type == TouchEvent.TOUCH_UP) {
                touchPoint.set(event.x, event.y);
                guiCam.touchToWorld(touchPoint);
                
                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new GameScreen(game, mContext));
                    return;
                }

                if(OverlapTester.pointInRectangle(helpBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new HelpScreen(game, mContext));
                    return;
                }
                if(OverlapTester.pointInRectangle(soundBounds, touchPoint)) {

                    Assets.playSound(Assets.clickSound);
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled) 
                        Assets.music.play();
                    else
                        Assets.music.pause();

                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(320, 480, 640, 960, Assets.backgroundRegion); ///tidigare 160, 240, 320, 480
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.background_vektor);
    if (mContext.world.equals("wanderful world")) {
        batcher.drawSprite(320, 736, 640, 448, Assets.backgroundRegionVektorWorld); ///tidigare 160, 240, 320, 480
        batcher.drawSprite(320, 300, 640, 600, Assets.backgroundRegionVektorWorld2); ///tidigare 160, 240, 320, 480
        }else if (mContext.world.equals("paris")) {
            batcher.drawSprite(320, 736, 640, 448, Assets.backgroundRegionVektorParis); ///tidigare 160, 240, 320, 480
            batcher.drawSprite(320, 300, 640, 600, Assets.backgroundRegionVektorParis2); ///tidigare 160, 240, 320, 480
        }else if (mContext.world.equals("london")) {
        batcher.drawSprite(320, 736, 640, 448, Assets.backgroundRegionVektorLondon); ///tidigare 160, 240, 320, 480
        batcher.drawSprite(320, 300, 640, 600, Assets.backgroundRegionVektorLondon2); ///tidigare 160, 240, 320, 480
        }else if (mContext.world.equals("india")) {
        batcher.drawSprite(320, 736, 640, 448, Assets.backgroundRegionVektorIndia); ///tidigare 160, 240, 320, 480
        batcher.drawSprite(320, 300, 640, 600, Assets.backgroundRegionVektorIndia2); ///tidigare 160, 240, 320, 480
    }

        batcher.endBatch();
        
        batcher.beginBatch(Assets.items);
        batcher.drawSprite(320, 480, 192+48, 192, Assets.logo);
        ///batcher.drawSprite(160, 200, 300, 110, Assets.mainMenu);
        batcher.drawSprite(640-96, 96-24, 96+24, 96, Assets.itemsHelp);
        batcher.drawSprite(96, 96-24, 96+24, 96, Settings.soundEnabled?Assets.soundOn:Assets.soundOff);
        ///Assets.font.drawText(batcher, mContext.enemy, 64, 896);
        batcher.endBatch();

        batcher.endBatch();

        batcher.beginBatch(Assets.enemyatlas);
        if (mContext.enemy.equals("birdman")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.birdman1);
        } else if (mContext.enemy.equals("speargirl")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.speargirl1);
        } else if (mContext.enemy.equals("wizboy")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.wizboy1);
        } else if (mContext.enemy.equals("wizgirl")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.wizgirl1);
        } else if (mContext.enemy.equals("captain")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.captain1);
        } else if (mContext.enemy.equals("darkwiz")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.darkwiz1);
        } else if (mContext.enemy.equals("hunchback")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.hunchback1);
        } else if (mContext.enemy.equals("gent")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.gent1);
        } else if (mContext.enemy.equals("bull")) {
            batcher.drawSprite(320, 640, 256, 256, Assets.bull1);
        }
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);


    }

    @Override
    public void pause() {        
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {        
    }       

    @Override
    public void dispose() {        
    }
}
