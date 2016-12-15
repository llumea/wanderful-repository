package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.Music;
import com.l2minigames.framework.Sound;
import com.l2minigames.framework.gl.Animation;
import com.l2minigames.framework.gl.Font;
import com.l2minigames.framework.gl.Texture;
import com.l2minigames.framework.gl.TextureRegion;
import com.l2minigames.framework.impl.GLGame;

public class Assets {
    public static Texture background;
    public static Texture background_vektor;
    public static TextureRegion backgroundRegion;
    public static TextureRegion backgroundRegionVektorWorld;
    public static TextureRegion backgroundRegionVektorWorld2;
    public static TextureRegion backgroundRegionVektorParis;
    public static TextureRegion backgroundRegionVektorParis2;
    public static TextureRegion backgroundRegionVektorLondon;
    public static TextureRegion backgroundRegionVektorLondon2;
    public static TextureRegion backgroundRegionVektorIndia;
    public static TextureRegion backgroundRegionVektorIndia2;
    
    public static Texture items;
    public static TextureRegion itemsHPBarBlack;
    public static TextureRegion itemsHPBarGreen;
    public static TextureRegion itemsLog;
    public static TextureRegion itemsEarth;
    public static TextureRegion itemsFire;
    public static TextureRegion itemsAir;
    public static TextureRegion itemsWater;
    public static TextureRegion itemsElementIndicator;
    public static TextureRegion itemsElementIndicatorEarth;
    public static TextureRegion itemsElementIndicatorFire;
    public static TextureRegion itemsElementIndicatorAir;
    public static TextureRegion itemsElementIndicatorWater;
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static TextureRegion highScoresRegion;
    public static TextureRegion logo;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pause;    
    public static TextureRegion spring;
    public static TextureRegion castle;
    public static Animation coinAnim;
    public static Animation bobJump;
    public static Animation mollyJump;
    public static Animation mollyEarth;
    public static Animation mollyFire;
    public static Animation mollyAir;
    public static Animation mollyWater;
    public static Animation earthAnimation;
    public static Animation fireAnimation;
    public static Animation airAnimation;
    public static Animation waterAnimation;
    public static Animation bobFall;
    public static TextureRegion bobHit;
    public static Animation squirrelFly;
    public static TextureRegion platform;
    public static Animation brakingPlatform;    
    public static Font font;

    public static Texture playeratlas;
    public static TextureRegion playerNormal;

    
    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;

    public static void load(GLGame game) {
        background = new Texture(game, "background.png");
        playeratlas = new Texture(game, "playeratlas.png");
        background_vektor = new Texture(game, "bakgrund_vektor.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
        backgroundRegionVektorWorld = new TextureRegion(background_vektor, 0, 0, 512, 448);
        backgroundRegionVektorWorld2 = new TextureRegion(background_vektor, 0, 400, 512, 48);
        backgroundRegionVektorParis = new TextureRegion(background_vektor, 512, 0, 512, 448);
        backgroundRegionVektorParis2 = new TextureRegion(background_vektor, 512, 400, 512, 48);
        backgroundRegionVektorLondon = new TextureRegion(background_vektor, 0, 448, 512, 448);
        backgroundRegionVektorLondon2 = new TextureRegion(background_vektor, 0, 830, 512, 48);
        backgroundRegionVektorIndia = new TextureRegion(background_vektor, 512, 448, 512, 448);
        backgroundRegionVektorIndia2 = new TextureRegion(background_vektor, 512, 830, 512, 48);
        items = new Texture(game, "items.png");
        itemsHPBarBlack = new TextureRegion(items, 620, 700, 200, 20);
        itemsHPBarGreen = new TextureRegion(items, 620, 720, 200, 20);
        itemsLog = new TextureRegion(items, 0, 680, 260, 60);
        itemsEarth = new TextureRegion(items, 860, 240, 64, 64);
        itemsFire = new TextureRegion(items, 860, 320, 64, 64);
        itemsAir = new TextureRegion(items, 940, 240, 64, 64);
        itemsWater = new TextureRegion(items, 940, 320, 64, 64);
        itemsElementIndicator = new TextureRegion(items, 400, 680, 220, 100);
        itemsElementIndicatorEarth = new TextureRegion(items, 260, 800, 200, 200);
        itemsElementIndicatorFire = new TextureRegion(items, 380, 800, 200, 200);
        itemsElementIndicatorAir = new TextureRegion(items, 500, 800, 200, 200);
        itemsElementIndicatorWater = new TextureRegion(items, 620, 800, 200, 200);

        playerNormal = new TextureRegion(playeratlas, 0, 0, 128, 128);
        mainMenu = new TextureRegion(items, 0, 224, 300, 110);
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(items, 352, 256, 160, 96);
        highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
        logo = new TextureRegion(items, 0, 352, 274, 142);
        soundOff = new TextureRegion(items, 0, 0, 64, 64);
        soundOn = new TextureRegion(items, 64, 0, 64, 64);
        arrow = new TextureRegion(items, 0, 64, 64, 64);
        pause = new TextureRegion(items, 64, 64, 64, 64);
        
        spring = new TextureRegion(items, 128, 0, 32, 32);
        castle = new TextureRegion(items, 128, 64, 64, 64);
        coinAnim = new Animation(0.2f,                                 
                                 new TextureRegion(items, 128, 32, 32, 32),
                                 new TextureRegion(items, 160, 32, 32, 32),
                                 new TextureRegion(items, 192, 32, 32, 32),
                                 new TextureRegion(items, 160, 32, 32, 32));
        bobJump = new Animation(0.2f,
                                new TextureRegion(items, 0, 128, 32, 32),
                                new TextureRegion(items, 32, 128, 32, 32));
        earthAnimation = new Animation(0.2f,
                new TextureRegion(playeratlas, 256, 384, 128, 128),
                new TextureRegion(playeratlas, 384, 384, 128, 128),
                new TextureRegion(playeratlas, 512, 384, 128, 128),
                new TextureRegion(playeratlas, 640, 384, 128, 128));
        fireAnimation = new Animation(0.04f,
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 256, 128, 128, 128),
                new TextureRegion(playeratlas, 384, 128, 128, 128),
                new TextureRegion(playeratlas, 512, 128, 128, 128),
                new TextureRegion(playeratlas, 640, 128, 128, 128),
                new TextureRegion(playeratlas, 768, 128, 128, 128));
        airAnimation = new Animation(0.1f,
                new TextureRegion(playeratlas, 0, 640, 128, 128),
                new TextureRegion(playeratlas, 128, 640, 128, 128),
                new TextureRegion(playeratlas, 256, 640, 128, 128),
                new TextureRegion(playeratlas, 384, 640, 128, 128),
                new TextureRegion(playeratlas, 512, 640, 128, 128),
                new TextureRegion(playeratlas, 640, 640, 128, 128));
        waterAnimation = new Animation(0.05f,
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 896, 128, 128, 128),
                new TextureRegion(playeratlas, 256, 512, 128, 128),
                new TextureRegion(playeratlas, 384, 512, 128, 128),
                new TextureRegion(playeratlas, 512, 512, 128, 128),
                new TextureRegion(playeratlas, 640, 512, 128, 128),
                new TextureRegion(playeratlas, 768, 512, 128, 128));
        mollyJump = new Animation(0.12f,
                new TextureRegion(playeratlas, 128, 0, 128, 128),
                new TextureRegion(playeratlas, 256, 0, 128, 128),
                new TextureRegion(playeratlas, 384, 0, 128, 128),
                new TextureRegion(playeratlas, 384, 0, 128, 128),
                new TextureRegion(playeratlas, 384, 0, 128, 128),
                new TextureRegion(playeratlas, 384, 0, 128, 128),
                new TextureRegion(playeratlas, 384, 0, 128, 128),
                new TextureRegion(playeratlas, 512, 0, 128, 128),
                new TextureRegion(playeratlas, 640, 0, 128, 128));
        mollyEarth = new Animation(0.12f,
                new TextureRegion(playeratlas, 0, 384, 128, 128),
                new TextureRegion(playeratlas, 128, 384, 128, 128),
                new TextureRegion(playeratlas, 0, 384, 128, 128),
                new TextureRegion(playeratlas, 128, 384, 128, 128),
                new TextureRegion(playeratlas, 0, 384, 128, 128),
                new TextureRegion(playeratlas, 128, 384, 128, 128),
                new TextureRegion(playeratlas, 128, 384, 128, 128),
                new TextureRegion(playeratlas, 128, 384, 128, 128),
                new TextureRegion(playeratlas, 0, 0, 128, 128));

        mollyFire = new Animation(0.2f,
                new TextureRegion(playeratlas, 0, 128, 128, 128),
                new TextureRegion(playeratlas, 128, 128, 128, 128),
                new TextureRegion(playeratlas, 128, 128, 128, 128),
                new TextureRegion(playeratlas, 128, 128, 128, 128),
                new TextureRegion(playeratlas, 0, 0, 128, 128));

        mollyWater = new Animation(0.4f,
                new TextureRegion(playeratlas, 0, 512, 128, 128),
                new TextureRegion(playeratlas, 128, 512, 128, 128),
                new TextureRegion(playeratlas, 0, 0, 128, 128));

        mollyAir = new Animation(0.12f,
                new TextureRegion(playeratlas, 0, 256, 128, 128),
                new TextureRegion(playeratlas, 128, 256, 128, 128),
                new TextureRegion(playeratlas, 256, 256, 128, 128),
                new TextureRegion(playeratlas, 384, 256, 128, 128),
                new TextureRegion(playeratlas, 0, 256, 128, 128),
                new TextureRegion(playeratlas, 128, 256, 128, 128),
                new TextureRegion(playeratlas, 256, 256, 128, 128),
                new TextureRegion(playeratlas, 384, 256, 128, 128),
                new TextureRegion(playeratlas, 0, 0, 128, 128));

        bobFall = new Animation(0.2f,
                                new TextureRegion(items, 64, 128, 32, 32),
                                new TextureRegion(items, 96, 128, 32, 32));
        bobHit = new TextureRegion(items, 128, 128, 32, 32);
        squirrelFly = new Animation(0.2f, 
                                    new TextureRegion(items, 0, 160, 32, 32),
                                    new TextureRegion(items, 32, 160, 32, 32));
        platform = new TextureRegion(items, 128, 320, 128, 32); ///Tidigare 64,160,64,16
        brakingPlatform = new Animation(0.2f,
                                     new TextureRegion(items, 64, 160, 64, 16),
                                     new TextureRegion(items, 64, 176, 64, 16),
                                     new TextureRegion(items, 64, 192, 64, 16),
                                     new TextureRegion(items, 64, 208, 64, 16));
        
        font = new Font(items, 448, 0, 16, 32, 40); ///Tidigare 224,0,16,16,20
        
        music = game.getAudio().newMusic("music.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        if(Settings.soundEnabled)
            music.play();
        jumpSound = game.getAudio().newSound("jump.ogg");
        highJumpSound = game.getAudio().newSound("highjump.ogg");
        hitSound = game.getAudio().newSound("hit.ogg");
        coinSound = game.getAudio().newSound("coin.ogg");
        clickSound = game.getAudio().newSound("click.ogg");       
    }       

    public static void reload() {
        background.reload();
        background_vektor.reload();
        playeratlas.reload();
        items.reload();
        if(Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}
