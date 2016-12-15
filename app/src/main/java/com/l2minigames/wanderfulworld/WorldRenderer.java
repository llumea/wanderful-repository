package com.l2minigames.wanderfulworld;

import javax.microedition.khronos.opengles.GL10;

import com.l2minigames.framework.gl.Animation;
import com.l2minigames.framework.gl.Camera2D;
import com.l2minigames.framework.gl.SpriteBatcher;
import com.l2minigames.framework.gl.TextureRegion;
import com.l2minigames.framework.impl.GLGraphics;

public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 20; ///Tidigare 10
    static final float FRUSTUM_HEIGHT = 30; ///Tidigare 15
    GLGraphics glGraphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;
    SuperJumper mContext;
    
    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world, SuperJumper context) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;
        this.mContext = context;
    }
    
    public void render() {
        if(world.bob.position.y > cam.position.y )
            cam.position.y = world.bob.position.y;
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();        
    }

    public void renderBackground() {

        batcher.beginBatch(Assets.background_vektor);
        if (mContext.world.equals("wanderful world")) {
            batcher.drawSprite(cam.position.x, cam.position.y+8f, FRUSTUM_WIDTH, FRUSTUM_WIDTH-5.8f, Assets.backgroundRegionVektorWorld); ///tidigare 160, 240, 320, 480
            batcher.drawSprite(cam.position.x, cam.position.y-7, FRUSTUM_WIDTH, FRUSTUM_WIDTH-2f, Assets.backgroundRegionVektorWorld2); ///tidigare 160, 240, 320, 480
        }else if (mContext.world.equals("paris")) {
            batcher.drawSprite(cam.position.x, cam.position.y+8f, FRUSTUM_WIDTH, FRUSTUM_WIDTH-5.8f, Assets.backgroundRegionVektorParis); ///tidigare 160, 240, 320, 480
            batcher.drawSprite(cam.position.x, cam.position.y-7, FRUSTUM_WIDTH, FRUSTUM_WIDTH-2f, Assets.backgroundRegionVektorParis2); ///tidigare 160, 240, 320, 480
        }else if (mContext.world.equals("london")) {
            batcher.drawSprite(cam.position.x, cam.position.y+8f, FRUSTUM_WIDTH, FRUSTUM_WIDTH-5.8f, Assets.backgroundRegionVektorLondon); ///tidigare 160, 240, 320, 480
            batcher.drawSprite(cam.position.x, cam.position.y-7, FRUSTUM_WIDTH, FRUSTUM_WIDTH-2f, Assets.backgroundRegionVektorLondon2); ///tidigare 160, 240, 320, 480
        }else if (mContext.world.equals("india")) {
            batcher.drawSprite(cam.position.x, cam.position.y+8f, FRUSTUM_WIDTH, FRUSTUM_WIDTH-5.8f, Assets.backgroundRegionVektorIndia); ///tidigare 160, 240, 320, 480
            batcher.drawSprite(cam.position.x, cam.position.y-7, FRUSTUM_WIDTH, FRUSTUM_WIDTH-2f, Assets.backgroundRegionVektorIndia2); ///tidigare 160, 240, 320, 480
        }

        batcher.endBatch();

        ///batcher.beginBatch(Assets.background);
        ///batcher.drawSprite(cam.position.x, cam.position.y,
         ///                  FRUSTUM_WIDTH, FRUSTUM_HEIGHT,
          ///                 Assets.backgroundRegion);
       /// batcher.endBatch();
    }

    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);

       /// renderBob();
       /// renderPlatforms();
       /// renderItems();
       /// renderSquirrels();
       /// renderCastle();
        renderGround();
        renderProgress();
        batcher.endBatch();
        batcher.beginBatch(Assets.playeratlas);
        renderMolly();
        renderEarths();
        renderFires();
        renderAirs();
        renderWaters();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderBob() {
        TextureRegion keyFrame;
        switch(world.bob.state) {
        case Bob.BOB_STATE_FALL:
            keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
            break;
        case Bob.BOB_STATE_JUMP:
            keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
            break;
        case Bob.BOB_STATE_HIT:
        default:
            keyFrame = Assets.bobHit;                       
        }
        
        float side = world.bob.velocity.x < 0? -1: 1;        
        batcher.drawSprite(world.bob.position.x, world.bob.position.y, side * 1, 1, keyFrame);        
    }
    private void renderMolly() {

        TextureRegion keyFrame;
        switch(world.molly.state) {
            case Molly.MOLLY_STATE_JUMP:
                keyFrame = Assets.mollyJump.getKeyFrame(world.molly.stateTime, Animation.ANIMATION_LOOPING);
                break;
            case Molly.MOLLY_STATE_BEFORE_JUMP:
                keyFrame = Assets.mollyJump.getKeyFrame(world.molly.stateTime, Animation.ANIMATION_LOOPING);
                break;
            case Molly.MOLLY_STATE_EARTH:
                keyFrame = Assets.mollyEarth.getKeyFrame(world.molly.stateTime, Animation.ANIMATION_NONLOOPING);
                break;
            case Molly.MOLLY_STATE_FIRE:
                keyFrame = Assets.mollyFire.getKeyFrame(world.molly.stateTime, Animation.ANIMATION_NONLOOPING);
                break;
            case Molly.MOLLY_STATE_AIR:
                keyFrame = Assets.mollyAir.getKeyFrame(world.molly.stateTime, Animation.ANIMATION_NONLOOPING);
                break;
            case Molly.MOLLY_STATE_WATER:
                keyFrame = Assets.mollyWater.getKeyFrame(world.molly.stateTime, Animation.ANIMATION_NONLOOPING);
                break;
            case Molly.MOLLY_STATE_HIT:
            default:
                keyFrame = Assets.playerNormal;
        }


        batcher.drawSprite(world.molly.position.x, world.molly.position.y, 6, 6, keyFrame);
    }
    private void renderProgress() {
        ///För spelaren
        float fullsize=5f;
        float actualsize = 4.5f;
        float marginLeft = 4f;
        float difference = (fullsize-actualsize)/2;
        batcher.drawSprite(marginLeft, 26.6f, fullsize, 0.5f, Assets.itemsHPBarBlack);
        batcher.drawSprite(marginLeft-difference, 26.6f, actualsize, 0.5f, Assets.itemsHPBarGreen);
        ///För AI
        float fullsizeAI=5f;
        float actualsizeAI = 3f;
        float marginLeftAI = 16f;
        float differenceAI = (fullsizeAI-actualsizeAI)/2;
        batcher.drawSprite(marginLeftAI, 26.6f, fullsizeAI, 0.5f, Assets.itemsHPBarBlack);
        batcher.drawSprite(marginLeftAI-differenceAI, 26.6f, actualsizeAI, 0.5f, Assets.itemsHPBarGreen);



        ///Elementindicators

        ///Earth
        batcher.drawSprite(3.4f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(3.3f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(3.5f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(3.2f, 27.8f, 4f, 4f, 75f, Assets.itemsElementIndicatorEarth);
        batcher.drawSprite(3.2f, 27.8f, 4f, 4f, 50f, Assets.itemsElementIndicatorEarth);
        batcher.drawSprite(3.2f, 27.8f, 4f, 4f, 25f, Assets.itemsElementIndicatorEarth);
        batcher.drawSprite(3.2f, 27.8f, 4f, 4f, Assets.itemsElementIndicatorEarth);
        batcher.drawSprite(3.2f, 27.8f, 4f, 4f, -25f, Assets.itemsElementIndicatorEarth);
        batcher.drawSprite(3.2f, 27.8f, 4f, 4f, -50f, Assets.itemsElementIndicatorEarth);
        batcher.drawSprite(3.2f, 27.8f, 4f, 4f, -75f, Assets.itemsElementIndicatorEarth);
        ///Fire
        batcher.drawSprite(7.9f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(8f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(7.8f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(7.7f, 27.8f, 4f, 4f, 75f, Assets.itemsElementIndicatorFire);
        batcher.drawSprite(7.7f, 27.8f, 4f, 4f, 50f, Assets.itemsElementIndicatorFire);
        batcher.drawSprite(7.7f, 27.8f, 4f, 4f, 25f, Assets.itemsElementIndicatorFire);
        batcher.drawSprite(7.7f, 27.8f, 4f, 4f, Assets.itemsElementIndicatorFire);
        batcher.drawSprite(7.7f, 27.8f, 4f, 4f, -25f, Assets.itemsElementIndicatorFire);
        batcher.drawSprite(7.7f, 27.8f, 4f, 4f, -50f, Assets.itemsElementIndicatorFire);
        batcher.drawSprite(7.7f, 27.8f, 4f, 4f, -75f, Assets.itemsElementIndicatorFire);
        ///Air
        batcher.drawSprite(12.4f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(12.3f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(12.5f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(12.2f, 27.8f, 4f, 4f, 75f, Assets.itemsElementIndicatorAir);
        batcher.drawSprite(12.2f, 27.8f, 4f, 4f, 50f, Assets.itemsElementIndicatorAir);
        batcher.drawSprite(12.2f, 27.8f, 4f, 4f, 25f, Assets.itemsElementIndicatorAir);
        batcher.drawSprite(12.2f, 27.8f, 4f, 4f, Assets.itemsElementIndicatorAir);
        batcher.drawSprite(12.2f, 27.8f, 4f, 4f, -25f, Assets.itemsElementIndicatorAir);
        batcher.drawSprite(12.2f, 27.8f, 4f, 4f, -50f, Assets.itemsElementIndicatorAir);
        batcher.drawSprite(12.2f, 27.8f, 4f, 4f, -75f, Assets.itemsElementIndicatorAir);
        ///Water
        batcher.drawSprite(16.9f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(16.8f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(17f, 28.8f, 4.4f, 2f, Assets.itemsElementIndicator);
        batcher.drawSprite(16.7f, 27.8f, 4f, 4f, 75f, Assets.itemsElementIndicatorWater);
        batcher.drawSprite(16.7f, 27.8f, 4f, 4f, 50f, Assets.itemsElementIndicatorWater);
        batcher.drawSprite(16.7f, 27.8f, 4f, 4f, 25f, Assets.itemsElementIndicatorWater);
        batcher.drawSprite(16.7f, 27.8f, 4f, 4f, Assets.itemsElementIndicatorWater);
        batcher.drawSprite(16.7f, 27.8f, 4f, 4f, -25f, Assets.itemsElementIndicatorWater);
        batcher.drawSprite(16.7f, 27.8f, 4f, 4f, -50f, Assets.itemsElementIndicatorWater);
        batcher.drawSprite(16.7f, 27.8f, 4f, 4f, -75f, Assets.itemsElementIndicatorWater);

        ///Elementpowers

        batcher.drawSprite(3.2f, 28.1f, 1.8f, 1.5f, Assets.itemsEarth);
        batcher.drawSprite(7.8f, 28.1f, 1.8f, 1.5f, Assets.itemsFire);
        batcher.drawSprite(12.3f, 28.1f, 1.8f, 1.5f, Assets.itemsAir);
        batcher.drawSprite(16.8f, 28.1f, 1.8f, 1.5f, Assets.itemsWater);



    }
    private void renderGround() {

        ///Kollar mot world.ground

        if (world.ground.type.equals("normal")){batcher.drawSprite(2, 16, 4, 1, Assets.itemsLog);}

        if (mContext.world.equals("wanderful world")) {
           /// batcher.drawSprite(2, 16, 4, 1, Assets.itemsLog);
            batcher.drawSprite(6, 16, 4, 1, Assets.itemsLog);
            batcher.drawSprite(10, 16, 4, 1, Assets.itemsLog);
            batcher.drawSprite(14, 16, 4, 1, Assets.itemsLog);
            batcher.drawSprite(18, 16, 4, 1, Assets.itemsLog);
        }

    }

    private void renderPlatforms() {
        int len = world.platforms.size();
        for(int i = 0; i < len; i++) {
            Platform platform = world.platforms.get(i);
            TextureRegion keyFrame = Assets.platform;
            if(platform.state == Platform.PLATFORM_STATE_PULVERIZING) {                
                keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
            }            
                                   
            batcher.drawSprite(platform.position.x, platform.position.y, 
                               2, 0.5f, keyFrame);            
        }
    }

    private void renderItems() {
        int len = world.springs.size();
        for(int i = 0; i < len; i++) {
            Spring spring = world.springs.get(i);            
            batcher.drawSprite(spring.position.x, spring.position.y, 1, 1, Assets.spring);
        }
        
        len = world.coins.size();
        for(int i = 0; i < len; i++) {
            Coin coin = world.coins.get(i);
            TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(coin.position.x, coin.position.y, 1, 1, keyFrame);
        }
    }
    private void renderEarths() {

        int len = world.earths.size();
        for(int i = 0; i < len; i++) {
            Earth earth = world.earths.get(i);
            TextureRegion keyFrame = Assets.earthAnimation.getKeyFrame(earth.stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(earth.position.x, earth.position.y, 4, 4, keyFrame);
        }
    }
    private void renderFires() {

        int len = world.fires.size();
        for(int i = 0; i < len; i++) {
            Fire fire = world.fires.get(i);
            TextureRegion keyFrame = Assets.fireAnimation.getKeyFrame(fire.stateTime, Animation.ANIMATION_NONLOOPING);
            batcher.drawSprite(fire.position.x, fire.position.y, 4, 4, keyFrame);
        }
    }
    private void renderAirs() {

        int len = world.airs.size();
        for(int i = 0; i < len; i++) {
            Air air = world.airs.get(i);
            TextureRegion keyFrame = Assets.airAnimation.getKeyFrame(air.stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(air.position.x, air.position.y, 6, 6, keyFrame);
        }
    }
    private void renderWaters() {

        int len = world.waters.size();
        for(int i = 0; i < len; i++) {
            Water water = world.waters.get(i);
            TextureRegion keyFrame = Assets.waterAnimation.getKeyFrame(water.stateTime, Animation.ANIMATION_NONLOOPING);
            batcher.drawSprite(water.position.x, water.position.y, 2, 2, keyFrame);
        }
    }

    private void renderSquirrels() {
        int len = world.squirrels.size();
        for(int i = 0; i < len; i++) {
            Squirrel squirrel = world.squirrels.get(i);
            TextureRegion keyFrame = Assets.squirrelFly.getKeyFrame(squirrel.stateTime, Animation.ANIMATION_LOOPING);
            float side = squirrel.velocity.x < 0?-1:1;
            batcher.drawSprite(squirrel.position.x, squirrel.position.y, side * 1, 1, keyFrame);
        }
    }

    private void renderCastle() {
        Castle castle = world.castle;
        batcher.drawSprite(castle.position.x, castle.position.y, 2, 2, Assets.castle);
    }
}

