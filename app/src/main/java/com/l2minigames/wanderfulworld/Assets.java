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
    public static Texture enemyatlas;
    public static TextureRegion backgroundRegion;
    public static TextureRegion backgroundRegionVektorWorld;
    public static TextureRegion backgroundRegionVektorWorld2;
    public static TextureRegion backgroundRegionVektorParis;
    public static TextureRegion backgroundRegionVektorParis2;
    public static TextureRegion backgroundRegionVektorLondon;
    public static TextureRegion backgroundRegionVektorLondon2;
    public static TextureRegion backgroundRegionVektorIndia;
    public static TextureRegion backgroundRegionVektorIndia2;

    public static TextureRegion enemyatlasHedgehog;
    
    public static Texture items;
    public static TextureRegion itemsHPBarBlack;
    public static TextureRegion itemsHPBarGreen;
    public static TextureRegion itemsLog;
    public static TextureRegion itemsEarth;
    public static TextureRegion itemsFire;
    public static TextureRegion itemsAir;
    public static TextureRegion itemsWater;
    public static TextureRegion itemsHelp;
    public static TextureRegion itemsClose;
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
    public static TextureRegion starGreen;
    public static TextureRegion starRed;
    public static TextureRegion starYellow;
    public static TextureRegion starBlue;
    public static TextureRegion birdman1;
    public static TextureRegion birdman2;
    public static TextureRegion wizboy1;
    public static TextureRegion wizboy2;
    public static TextureRegion wizgirl1;
    public static TextureRegion wizgirl2;
    public static TextureRegion speargirl1;
    public static TextureRegion speargirl2;
    public static TextureRegion captain1;
    public static TextureRegion captain2;
    public static TextureRegion darkwiz1;
    public static TextureRegion darkwiz2;
    public static TextureRegion hunchback1;
    public static TextureRegion hunchback2;
    public static TextureRegion gent1;
    public static TextureRegion gent2;
    public static TextureRegion bull1;
    public static TextureRegion bull2;
    public static TextureRegion spear;
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
    public static Animation hedgehogAnimation;
    public static Animation wolfAnimation;
    public static Animation bulldogAnimation;
    public static Animation captainAnimation;
    public static Animation birdAnimation;
    public static Animation dragonAnimation;
    public static Animation snakeAnimation;
    public static Animation stoneAnimation;
    public static Animation ghostAnimation;
    public static Animation hitAnimation;
    public static Animation waveAnimation;
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
    public static Sound victorySound;
    public static Sound defeatSound;
    public static Sound earthSound;
    public static Sound fireSound;
    public static Sound airSound;
    public static Sound waterSound;

    public static void load(GLGame game) {
        background = new Texture(game, "background.png");
        playeratlas = new Texture(game, "playeratlas.png");
        background_vektor = new Texture(game, "bakgrund_vektor.png");
        enemyatlas = new Texture(game, "enemyatlas.png");
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
        itemsClose = new TextureRegion(items, 320, 448, 128, 128);
        itemsElementIndicator = new TextureRegion(items, 400, 680, 220, 100);
        itemsElementIndicatorEarth = new TextureRegion(items, 260, 800, 200, 200);
        itemsElementIndicatorFire = new TextureRegion(items, 380, 800, 200, 200);
        itemsElementIndicatorAir = new TextureRegion(items, 500, 800, 200, 200);
        itemsElementIndicatorWater = new TextureRegion(items, 620, 800, 200, 200);

        playerNormal = new TextureRegion(playeratlas, 0, 0, 128, 128);
        mainMenu = new TextureRegion(items, 0, 224, 300, 110);
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        itemsHelp = new TextureRegion(items, 320, 320, 128,128);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(items, 352, 256, 160, 96);
        highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
        logo = new TextureRegion(items, 0, 256, 256, 256);
        soundOff = new TextureRegion(items, 0, 0, 128, 128);
        soundOn = new TextureRegion(items, 128, 0, 128, 128);
        arrow = new TextureRegion(items, 0, 64, 64, 64);
        pause = new TextureRegion(items, 64, 64, 64, 64);
        
        spring = new TextureRegion(items, 128, 0, 32, 32);
        castle = new TextureRegion(items, 128, 64, 64, 64);
        starGreen = new TextureRegion(playeratlas, 896, 0, 64, 64);
        starRed = new TextureRegion(playeratlas, 960, 0, 64, 64);
        starYellow = new TextureRegion(playeratlas, 896, 64, 64, 64);
        starBlue = new TextureRegion(playeratlas, 960, 64, 64, 64);

        birdman1 = new TextureRegion(enemyatlas, 0, 704, 128, 128);
        birdman2 = new TextureRegion(enemyatlas, 0, 832, 128, 128);
        speargirl1 = new TextureRegion(enemyatlas, 128, 704, 128, 128);
        speargirl2 = new TextureRegion(enemyatlas, 128, 832, 128, 128);
        wizboy1 = new TextureRegion(enemyatlas, 256, 704, 128, 128);
        wizboy2 = new TextureRegion(enemyatlas, 256, 832, 128, 128);
        wizgirl1 = new TextureRegion(enemyatlas, 384, 704, 128, 128);
        wizgirl2 = new TextureRegion(enemyatlas, 384, 832, 128, 128);
        captain1 = new TextureRegion(enemyatlas, 512, 704, 128, 128);
        captain2 = new TextureRegion(enemyatlas, 512, 832, 128, 128);
        darkwiz1 = new TextureRegion(enemyatlas, 640, 704, 128, 128);
        darkwiz2 = new TextureRegion(enemyatlas, 640, 832, 128, 128);
        hunchback1 = new TextureRegion(enemyatlas, 768, 704, 128, 128);
        hunchback2 = new TextureRegion(enemyatlas, 768, 832, 128, 128);
        gent1 = new TextureRegion(enemyatlas, 896, 704, 128, 128);
        gent2 = new TextureRegion(enemyatlas, 896, 832, 128, 128);
        bull1 = new TextureRegion(enemyatlas, 384, 0, 128, 128);
        bull2 = new TextureRegion(enemyatlas, 896, 576, 128, 128);
        spear = new TextureRegion(enemyatlas, 768, 256, 128, 32);




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

        hedgehogAnimation = new Animation(0.2f,
                new TextureRegion(enemyatlas, 0, 0, 128, 128),
                new TextureRegion(enemyatlas, 128, 0, 128, 128),
                new TextureRegion(enemyatlas, 256, 0, 128, 128));
        birdAnimation = new Animation(0.2f,
                new TextureRegion(enemyatlas, 0, 448, 128, 128),
                new TextureRegion(enemyatlas, 128, 448, 128, 128),
                new TextureRegion(enemyatlas, 256, 448, 128, 128));
        snakeAnimation = new Animation(0.2f,
                new TextureRegion(enemyatlas, 768, 448, 128, 128),
                new TextureRegion(enemyatlas, 896, 448, 128, 128));
        dragonAnimation = new Animation(0.15f,
                new TextureRegion(enemyatlas, 384, 448, 128, 128),
                new TextureRegion(enemyatlas, 512, 448, 128, 128),
                new TextureRegion(enemyatlas, 640, 448, 128, 128),
                new TextureRegion(enemyatlas, 512, 448, 128, 128));
        wolfAnimation = new Animation(0.1f,
                new TextureRegion(enemyatlas, 512, 0, 256, 128),
                new TextureRegion(enemyatlas, 768, 0, 256, 128),
                new TextureRegion(enemyatlas, 0, 128, 256, 128),
                new TextureRegion(enemyatlas, 256, 128, 256, 128),
                new TextureRegion(enemyatlas, 512, 128, 256, 128),
                new TextureRegion(enemyatlas, 768, 128, 256, 128));
        stoneAnimation = new Animation(0.1f,
                new TextureRegion(enemyatlas, 0, 576, 64, 64),
                new TextureRegion(enemyatlas, 64, 576, 64, 64),
                new TextureRegion(enemyatlas, 128, 576, 64, 64),
                new TextureRegion(enemyatlas, 192, 576, 64, 64),
                new TextureRegion(enemyatlas, 256, 576, 64, 64),
                new TextureRegion(enemyatlas, 320, 576, 64, 64));
        bulldogAnimation = new Animation(0.1f,
                new TextureRegion(enemyatlas, 0, 256, 128, 64),
                new TextureRegion(enemyatlas, 128, 256, 128, 64),
                new TextureRegion(enemyatlas, 256, 256, 128, 64),
                new TextureRegion(enemyatlas, 384, 256, 128, 64),
                new TextureRegion(enemyatlas, 512, 256, 128, 64),
                new TextureRegion(enemyatlas, 640, 256, 128, 64));
        captainAnimation = new Animation(0.1f,
                new TextureRegion(enemyatlas, 0, 320, 128, 128),
                new TextureRegion(enemyatlas, 128, 320, 128, 128),
                new TextureRegion(enemyatlas, 256, 320, 128, 128),
                new TextureRegion(enemyatlas, 384, 320, 128, 128),
                new TextureRegion(enemyatlas, 512, 320, 128, 128),
                new TextureRegion(enemyatlas, 640, 320, 128, 128),
                new TextureRegion(enemyatlas, 768, 320, 128, 128),
                new TextureRegion(enemyatlas, 896, 320, 128, 128));
        ghostAnimation = new Animation(0.15f,
                new TextureRegion(enemyatlas, 384, 576, 128, 128),
                new TextureRegion(enemyatlas, 512, 576, 128, 128),
                new TextureRegion(enemyatlas, 640, 576, 128, 128),
                new TextureRegion(enemyatlas, 768, 576, 128, 128));
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
        hitAnimation = new Animation(0.05f,
                new TextureRegion(playeratlas, 0, 768, 128, 128),
                new TextureRegion(playeratlas, 128, 768, 128, 128),
                new TextureRegion(playeratlas, 256, 768, 128, 128),
                new TextureRegion(playeratlas, 384, 768, 128, 128),
                new TextureRegion(playeratlas, 512, 768, 128, 128),
                new TextureRegion(playeratlas, 640, 768, 128, 128));

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


        waveAnimation = new Animation(0.15f,
                new TextureRegion(items, 576, 240, 260, 64),
                new TextureRegion(items, 576, 304, 260, 64),
                new TextureRegion(items, 576, 368, 260, 64),
                new TextureRegion(items, 576, 432, 260, 64),
                new TextureRegion(items, 576, 496, 260, 64),
                new TextureRegion(items, 576, 560, 260, 64));

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
        highJumpSound = game.getAudio().newSound("jump.ogg");
        hitSound = game.getAudio().newSound("hit.ogg");
        coinSound = game.getAudio().newSound("coin.ogg");
        clickSound = game.getAudio().newSound("click.ogg");
        victorySound = game.getAudio().newSound("victory.ogg");
        defeatSound = game.getAudio().newSound("gameover.ogg");
        earthSound = game.getAudio().newSound("earthSound.ogg");
        fireSound = game.getAudio().newSound("fireSound.ogg");
        airSound = game.getAudio().newSound("airSound.ogg");
        waterSound = game.getAudio().newSound("waterSound.ogg");
    }       

    public static void reload() {
        background.reload();
        background_vektor.reload();
        playeratlas.reload();
        items.reload();
        enemyatlas.reload();
        if(Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}
