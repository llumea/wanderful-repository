package com.l2minigames.wanderfulworld;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.l2minigames.framework.Game;
import com.l2minigames.framework.Input.TouchEvent;
import com.l2minigames.framework.gl.Camera2D;
import com.l2minigames.framework.gl.FPSCounter;
import com.l2minigames.framework.gl.SpriteBatcher;
import com.l2minigames.framework.impl.GLScreen;
import com.l2minigames.framework.math.OverlapTester;
import com.l2minigames.framework.math.Rectangle;
import com.l2minigames.framework.math.Vector2;
import com.l2minigames.wanderfulworld.World.WorldListener;

public class GameScreen extends GLScreen {
    static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    ///static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
  
    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;    
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;    
   /// Rectangle pauseBounds;
   /// Rectangle resumeBounds;
    Rectangle quitBounds;
    Rectangle jumpBounds;
    int lastScore;
    String scoreString;
    SuperJumper mContext;
    int touchDownX;
    int touchDownY;
    int startTouch = 0;



    public GameScreen(Game game, SuperJumper context) {
        super(game);

        this.mContext = context;
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 640, 960);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        worldListener = new WorldListener() {
            public void jump() {            
                Assets.playSound(Assets.jumpSound);
            }

            public void highJump() {
                Assets.playSound(Assets.highJumpSound);
            }

            public void hit() {
                Assets.playSound(Assets.hitSound);
            }

            public void coin() {
                Assets.playSound(Assets.coinSound);
            }
            public void victory() {
                Assets.playSound(Assets.victorySound);
            }
            public void defeat() {
                Assets.playSound(Assets.defeatSound);
            }
            public void earthSound() {
                Assets.playSound(Assets.earthSound);
            }
            public void fireSound() {
                Assets.playSound(Assets.fireSound);
            }
            public void airSound() {
                Assets.playSound(Assets.airSound);
            }
            public void waterSound() {
                Assets.playSound(Assets.waterSound);
            }
        };
        world = new World(worldListener, mContext);
        renderer = new WorldRenderer(glGraphics, batcher, world, mContext);
       /// pauseBounds = new Rectangle(320- 64, 480- 64, 64, 64);
       /// resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
        quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
        jumpBounds = new Rectangle(0, 0, 640, 960);
        lastScore = 0;
        scoreString = "";
        touchDownX = 0;
        touchDownY = 0;
    }

    @Override
    public void update(float deltaTime) {
        if(deltaTime > 0.1f)
            deltaTime = 0.1f;
        
        switch(state) {
        case GAME_READY:
            updateReady();
            break;
        case GAME_RUNNING:
            updateRunning(deltaTime);
            break;
        case GAME_PAUSED:
            updatePaused();
            break;
        /*
        case GAME_LEVEL_END:
            updateLevelEnd();
            break;
            */
        case GAME_OVER:
            updateGameOver();
            break;
        }
    }

    private void updateReady() {
        if(game.getInput().getTouchEvents().size() > 0) {
            state = GAME_RUNNING;
        }
    }

    private void updateRunning(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();


        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN){
                touchDownX = event.x;
                touchDownY = event.y;
                Log.i("TOUCH", "IS TOUCH DOWN"+""+touchDownX+" "+touchDownY);
            }

            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            
            touchPoint.set(event.x, event.y);


            guiCam.touchToWorld(touchPoint);
            /*
            if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_PAUSED;
                return;
            }
            */
            if(OverlapTester.pointInRectangle(jumpBounds, touchPoint)) {


                ///Det är är punkten där man lyfter upp
                Log.i("TOUCH", "TOUCH POSITION X: "+""+event.x);
                Log.i("TOUCH", "TOUCH POSITION Y: "+""+event.y);
                Log.i("TOUCH", "TOUCH DOWN POSITION X: "+""+touchDownX);
                Log.i("TOUCH", "TOUCH DOWN POSITION Y: "+""+touchDownY);

                //ToDo Här körs doearth och createearth när spelet inleds

                if (touchDownY<event.y-200 && world.molly.state!=world.molly.MOLLY_STATE_JUMP &&startTouch!=0){
                    if (world.molly.earthCount !=0) {
                        world.molly.doEarth();
                        world.createEarth();
                        world.molly.earthCount = world.molly.earthCount - 1;
                    }else {
                        world.molly.doEarth();
                    }
                    ///ToDo Assets.playSound(Assets.jumpSound);
                }else if (touchDownY>event.y+200 && world.molly.state!=world.molly.MOLLY_STATE_JUMP&&startTouch!=0){
                    if (world.molly.airCount !=0) {
                        world.molly.doAir();
                        world.createAir();
                        world.molly.airCount = world.molly.airCount - 1;
                    }else {
                        world.molly.doAir();
                    }
                    ///ToDo Assets.playSound(Assets.jumpSound);
                }else if (touchDownX<event.x-100 && world.molly.state!=world.molly.MOLLY_STATE_JUMP&&startTouch!=0){
                    if (world.molly.fireCount !=0) {
                        world.molly.doFire();
                        world.createFire();
                        world.molly.fireCount = world.molly.fireCount - 1;
                    } else {
                        world.molly.doFire();
                    }

                    ///ToDo Assets.playSound(Assets.jumpSound);
                }else if (touchDownX>event.x+100 && world.molly.state!=world.molly.MOLLY_STATE_JUMP&&startTouch!=0){
                    if (world.molly.waterCount !=0) {
                        world.molly.doWater();
                        world.createWater();
                        world.molly.waterCount = world.molly.waterCount - 1;
                    }else {
                        world.molly.doWater();
                    }
                    ///ToDo Assets.playSound(Assets.jumpSound);
                }

                else if (world.molly.velocity.y==0&&startTouch!=0){
                    world.molly.jump();
                    Assets.playSound(Assets.jumpSound);
                }

                ///world.createBall();
                world.update(deltaTime, game.getInput().getAccelX()); ///uppdaterar med accelerator!!!
                startTouch=1;
                return;
            }
        }
        
        world.update(deltaTime, game.getInput().getAccelX());
        /*
        if(world.score != lastScore) {
            lastScore = world.score;
            scoreString = "" + lastScore;
        }
        */
        /*
        if(world.state == World.WORLD_STATE_NEXT_LEVEL) {
            state = GAME_LEVEL_END;        
        }
        */
        if(world.state == World.WORLD_STATE_GAME_OVER) {
            state = GAME_OVER;
            /*
            if(lastScore >= Settings.highscores[4]) 
                scoreString = "new highscore: " + lastScore;
            else
                scoreString = "score: " + lastScore;
            Settings.addScore(lastScore);
            Settings.save(game.getFileIO());
            */
        }
    }

    private void updatePaused() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            /*
            if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                return;
            }
            */
            if(OverlapTester.pointInRectangle(quitBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
               game.setScreen(new MainMenuScreen(game,mContext));
                return;
            
            }
        }
    }
/*
    private void updateLevelEnd() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {                   
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            world = new World(worldListener, mContext);
            renderer = new WorldRenderer(glGraphics, batcher, world, mContext);
            world.score = lastScore;
            state = GAME_READY;
        }
    }
*/
    private void updateGameOver() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {                   
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            try {
                ///ToDo Ändra xp efter motstånd
                int xpToGain = 100;
                world.molly.xp = world.molly.xp+xpToGain;
                mContext.sendResultBeforeExit(world.molly.earthCount, world.molly.fireCount, world.molly.airCount, world.molly.waterCount, world.molly.hp, world.molly.xp);
            }catch(Exception e){
                Toast.makeText(mContext, R.string.not_saved,
                        Toast.LENGTH_SHORT).show();
            }
            ///ToDo Skapa artefact om enemy är en boss!
            mContext.finish();


           ///SuperJumper tmpJumper = new SuperJumper();
            ///tmpJumper.onBackPressed();
           /// Context context= tmpJumper.getContext();

           /// Intent intent = new Intent(context, MapsActivity.class);
           /// context.startActivity(intent);

            ///Detta ger runtimeexception: Can't create handler inside thread that has not called Looper.prepare()

           /// game.setScreen(new MainMenuScreen(game));
        }
    }


    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        renderer.render();
        
        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.items);
        switch(state) {
        case GAME_READY:
            presentReady();
            break;
        case GAME_RUNNING:
            presentRunning();
            break;
        case GAME_PAUSED:
            presentPaused();
            break;
        /*
        case GAME_LEVEL_END:
            presentLevelEnd();
            break;
            */
        case GAME_OVER:
            presentGameOver();
            break;
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void presentReady() {
        String tap = mContext.getResources().getString(R.string.tap_and_swipe);
        Assets.font.drawText(batcher, tap, 96, 400);
        Assets.font.drawText(batcher, "20", 304, 850);
        ///batcher.drawSprite(160, 240, 192, 32, Assets.ready);
    }

    private void presentRunning() {

       /// batcher.drawSprite(320 - 32, 480 - 32, 64, 64, Assets.pause);
        if (world.timer.position.x>=0 && world.timer.position.x<2){Assets.font.drawText(batcher, "20", 304, 850);}
        if (world.timer.position.x>=2 && world.timer.position.x<4){Assets.font.drawText(batcher, "19", 304, 850);}
        if (world.timer.position.x>=4 && world.timer.position.x<6){Assets.font.drawText(batcher, "18", 304, 850);}
        if (world.timer.position.x>=6 && world.timer.position.x<8){Assets.font.drawText(batcher, "17", 304, 850);}
        if (world.timer.position.x>=8 && world.timer.position.x<10){Assets.font.drawText(batcher, "16", 304, 850);}
        if (world.timer.position.x>=10 && world.timer.position.x<12){Assets.font.drawText(batcher, "15", 304, 850);}
        if (world.timer.position.x>=12 && world.timer.position.x<14){Assets.font.drawText(batcher, "14", 304, 850);}
        if (world.timer.position.x>=14 && world.timer.position.x<16){Assets.font.drawText(batcher, "13", 304, 850);}
        if (world.timer.position.x>=16 && world.timer.position.x<18){Assets.font.drawText(batcher, "12", 304, 850);}
        if (world.timer.position.x>=18 && world.timer.position.x<20){Assets.font.drawText(batcher, "11", 304, 850);}
        if (world.timer.position.x>=20 && world.timer.position.x<22){Assets.font.drawText(batcher, "10", 304, 850);}
        if (world.timer.position.x>=22 && world.timer.position.x<24){Assets.font.drawText(batcher, "09", 304, 850);}
        if (world.timer.position.x>=24 && world.timer.position.x<26){Assets.font.drawText(batcher, "08", 304, 850);}
        if (world.timer.position.x>=26 && world.timer.position.x<28){Assets.font.drawText(batcher, "07", 304, 850);}
        if (world.timer.position.x>=28 && world.timer.position.x<30){Assets.font.drawText(batcher, "06", 304, 850);}
        if (world.timer.position.x>=30 && world.timer.position.x<32){Assets.font.drawText(batcher, "05", 304, 850);}
        if (world.timer.position.x>=32 && world.timer.position.x<34){Assets.font.drawText(batcher, "04", 304, 850);}
        if (world.timer.position.x>=34 && world.timer.position.x<36){Assets.font.drawText(batcher, "03", 304, 850);}
        if (world.timer.position.x>=36 && world.timer.position.x<38){Assets.font.drawText(batcher, "02", 304, 850);}
        if (world.timer.position.x>=38 && world.timer.position.x<40){Assets.font.drawText(batcher, "01", 304, 850);}
        if (world.timer.position.x>=40 && world.timer.position.x<42){Assets.font.drawText(batcher, "00", 304, 850);}
        ///Assets.font.drawText(batcher, scoreString, 32, 960-40); ///16, 480-20)
        ///Assets.font.drawText(batcher, "HELLO", 500, 800);
        ///Assets.font.drawText(batcher, "HELLO", 500, 800);
    }

    private void presentPaused() {        
       /// batcher.drawSprite(160, 240, 192, 96, Assets.pauseMenu);
       /// Assets.font.drawText(batcher, scoreString, 32, 960-40); ///16, 480-20)
    }

    private void presentLevelEnd() {
        String topText = "the princess is ...";
        String bottomText = "in another castle!";
        float topWidth = Assets.font.glyphWidth * topText.length();
        float bottomWidth = Assets.font.glyphWidth * bottomText.length();
        Assets.font.drawText(batcher, topText, 160 - topWidth / 2, 480 - 40);
        Assets.font.drawText(batcher, bottomText, 160 - bottomWidth / 2, 40);
    }

    private void presentGameOver() {
        if (world.molly.hp<1) {
            Assets.font.drawText(batcher, "game over", 190, 450);
        } else {
            Assets.font.drawText(batcher, "bravissimo", 188, 450);
        }
        ///batcher.drawSprite(160, 240, 160, 96, Assets.gameOver);
        float scoreWidth = Assets.font.glyphWidth * scoreString.length();
        Assets.font.drawText(batcher, scoreString, 160 - scoreWidth / 2, 480-20);
    }

    @Override
    public void pause() {
        if(state == GAME_RUNNING)
            state = GAME_PAUSED;
    }

    @Override
    public void resume() {        
    }

    @Override
    public void dispose() {       
    }
}
