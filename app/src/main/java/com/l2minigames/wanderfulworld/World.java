package com.l2minigames.wanderfulworld;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.l2minigames.framework.math.OverlapTester;
import com.l2minigames.framework.math.Vector2;

public class World {
    public interface WorldListener {
        public void jump();
        public void highJump();
        public void hit();
        public void coin();
        public void victory();
        public void defeat();
        public void earthSound();
        public void fireSound();
        public void airSound();
        public void waterSound();
    }

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;    
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0, -30); ///Tidigare 0,-12

    public final Bob bob;
    public final Timer timer;
    public final Molly molly;
    public final Wave wave;
    public final Ground ground;
    public final List<Platform> platforms;
    public final List<Earth> earths;
    public final List<Fire> fires;
    public final List<Air> airs;
    public final List<Water> waters;
    public final List<Spring> springs;
    public final List<Squirrel> squirrels;
    public final List<Coin> coins;
    public final List<Hedgehog> hedgehogs;
    public final List<Wolf> wolves;
    public final List<Bulldog> bulldogs;
    public final List<Captain> captains;
    public final List<Bird> birds;
    public final List<Dragon> dragons;
    public final List<Snake> snakes;
    public final List<Stone> stones;
    public final List<Ghost> ghosts;
    public final List<Star> stars;
    public final List<Hit> hits;
    public final List<Spear> spears;
    public Castle castle;    
    public final WorldListener listener;
    public final Random rand;
    
    public float heightSoFar;
    public int score;    
    public int state;
    public int mollyHp;
    public int enemyHp;
    public int enemyMaxHp;
    SuperJumper mContext;

    public World(WorldListener listener, SuperJumper context) {
        this.bob = new Bob(5, 1);
        this.timer = new Timer(0,0);
        this.mContext = context;
        mollyHp = mContext.hp;
        Log.i("GAME", "Molly HP"+mollyHp);
        this.molly = new Molly(3, 19, mContext.hp, mContext.max_hp, mContext.earth_power, mContext.fire_power, mContext.air_power, mContext.water_power, mContext.xp);
        this.ground = new Ground(3,17);
        this.wave = new Wave(6,17);
        this.platforms = new ArrayList<Platform>();
        this.hits = new ArrayList<Hit>();
        this.hedgehogs = new ArrayList<Hedgehog>();
        this.wolves = new ArrayList<Wolf>();
        this.bulldogs = new ArrayList<Bulldog>();
        this.captains = new ArrayList<Captain>();
        this.birds = new ArrayList<Bird>();
        this.dragons = new ArrayList<Dragon>();
        this.snakes = new ArrayList<Snake>();
        this.stones = new ArrayList<Stone>();
        this.ghosts = new ArrayList<Ghost>();
        this.stars = new ArrayList<Star>();
        this.earths = new ArrayList<Earth>();
        this.fires = new ArrayList<Fire>();
        this.airs = new ArrayList<Air>();
        this.spears = new ArrayList<Spear>();
        this.waters = new ArrayList<Water>();
        this.springs = new ArrayList<Spring>();
        this.squirrels = new ArrayList<Squirrel>();
        this.coins = new ArrayList<Coin>();        
        this.listener = listener;
        rand = new Random();
        setupEnemies();
        generateLevel();
        ///Skapa en "dummy" utanför skärmen så att något alltid ritas ut från enemyatlas
        createHedgehog(14,50,0);
        //Rita ut enemies

        ///createHedgehog(14,17,-4);
        ///createWolf(24,17.8f,-8);
        ///createBulldog(25,17.5f,-6);
       /// createCaptain(25,18.5f,-4);
       ///createBird(27,26f,-4, -0.7f);
       ////createDragon(34,26f,-4, -0.7f);
       ///createSnake(40,17.5f,-3);
        ///createStone(50,18.2f,-7);
       /// createGhost(53,22.2f,-4);
        
        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
    }

    private void generateLevel() {
        float y = Platform.PLATFORM_HEIGHT / 2;
        float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY
                / (2 * -gravity.y);
        while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
            int type = rand.nextFloat() > 0.8f ? Platform.PLATFORM_TYPE_MOVING
                    : Platform.PLATFORM_TYPE_STATIC;
            float x = rand.nextFloat()
                    * (WORLD_WIDTH - Platform.PLATFORM_WIDTH)
                    + Platform.PLATFORM_WIDTH / 2;

            Platform platform = new Platform(type, x, y);
            platforms.add(platform);

            if (rand.nextFloat() > 0.9f
                    && type != Platform.PLATFORM_TYPE_MOVING) {
                Spring spring = new Spring(platform.position.x,
                        platform.position.y + Platform.PLATFORM_HEIGHT / 2
                                + Spring.SPRING_HEIGHT / 2);
                springs.add(spring);
            }

            if (y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8f) {
                Squirrel squirrel = new Squirrel(platform.position.x
                        + rand.nextFloat(), platform.position.y
                        + Squirrel.SQUIRREL_HEIGHT + rand.nextFloat() * 2);
                squirrels.add(squirrel);
            }

            if (rand.nextFloat() > 0.6f) {
                Coin coin = new Coin(platform.position.x + rand.nextFloat(),
                        platform.position.y + Coin.COIN_HEIGHT
                                + rand.nextFloat() * 3);
                coins.add(coin);
            }

            y += (maxJumpHeight - 0.5f);
            y -= rand.nextFloat() * (maxJumpHeight / 3);
        }

        castle = new Castle(WORLD_WIDTH / 2, y);
    }

    public void update(float deltaTime, float accelX) {
       /// updateBob(deltaTime, accelX);
        updateMolly(deltaTime);
        updateWave(deltaTime);
        updateHits(deltaTime);
        updateTimer(deltaTime);
        updatePlatforms(deltaTime);
        updateSquirrels(deltaTime);
        updateCoins(deltaTime);
        updateEarths(deltaTime);
        updateFires(deltaTime);
        updateAirs(deltaTime);
        updateWaters(deltaTime);
        updateEnemies(deltaTime);
        updateStars(deltaTime);
        removeUnusedObjects(deltaTime);

        checkGround();

        if (bob.state != Bob.BOB_STATE_HIT)
            checkCollisions();
        checkGameOver();
    }

    private void updateBob(float deltaTime, float accelX) {
        if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f)
            bob.hitPlatform();
        if (bob.state != Bob.BOB_STATE_HIT)
            bob.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
        bob.update(deltaTime);
        heightSoFar = Math.max(bob.position.y, heightSoFar);
    }
    private void updateMolly(float deltaTime) {
        molly.update(deltaTime);
    }
    private void updateWave(float deltaTime) {
        wave.update(deltaTime);
    }
    private void updateHits(float deltaTime) {

        int len = hits.size();
        for (int i = 0; i < len; i++) {
            Hit hit = hits.get(i);
            hit.update(deltaTime);
            if (hit.state == Hit.HIT_STATE_PULVERIZING
                    && hit.stateTime > Hit.HIT_PULVERIZE_TIME) {
                hits.remove(hit);
                len = hits.size();
            }
        }
    }
    private void updateTimer(float deltaTime) {
        timer.update(deltaTime);
    }
    private void removeUnusedObjects(float deltaTime) {

        int len = earths.size();
        for (int i = 0; i < len; i++) {
            Earth earth = earths.get(i);
            if (earth.position.x>22||earth.position.y<-4){
                earths.remove(i);
                len=earths.size();
            }
        }
        int len2 = fires.size();
        for (int i = 0; i < len2; i++) {
            Fire fire = fires.get(i);
            if (fire.position.x>22||fire.position.y<-4){
                fires.remove(i);
                len2=fires.size();
            }
        }
        int len3 = airs.size();
        for (int i = 0; i < len3; i++) {
            Air air = airs.get(i);
            if (air.position.x>22||air.position.y<-4){
                airs.remove(i);
                len3=airs.size();
            }
        }
        int len4 = waters.size();
        for (int i = 0; i < len4; i++) {
            Water water = waters.get(i);
            if (water.position.x>22||water.position.y<-4){
                waters.remove(i);
                len4=waters.size();
            }
        }

 ///ENEMIES PLOCKAS BORT NÄR DE PASSERAT PLAYER

        int len5 = hedgehogs.size();
        for (int i = 0; i < len5; i++) {
            Hedgehog hedgehog = hedgehogs.get(i);
            if (hedgehog.position.x<-4){
                hedgehogs.remove(i);
                len5=hedgehogs.size();
            }
        }
        int len6 = wolves.size();
        for (int i = 0; i < len6; i++) {
            Wolf wolf = wolves.get(i);
            if (wolf.position.x<-4){
                wolves.remove(i);
                len6=wolves.size();
            }
        }
        int len7 = bulldogs.size();
        for (int i = 0; i < len7; i++) {
            Bulldog bulldog = bulldogs.get(i);
            if (bulldog.position.x<-4){
                bulldogs.remove(i);
                len7=bulldogs.size();
            }
        }
        int len8 = captains.size();
        for (int i = 0; i < len8; i++) {
            Captain captain = captains.get(i);
            if (captain.position.x<-4){
                captains.remove(i);
                len8=captains.size();
            }
        }
        int len9 = birds.size();
        for (int i = 0; i < len9; i++) {
            Bird bird = birds.get(i);
            if (bird.position.x<-4){
                birds.remove(i);
                len9=birds.size();
            }
        }
        int len10 = dragons.size();
        for (int i = 0; i < len10; i++) {
            Dragon dragon = dragons.get(i);
            if (dragon.position.x<-4){
                dragons.remove(i);
                len10=dragons.size();
            }
        }
        int len11 = snakes.size();
        for (int i = 0; i < len11; i++) {
            Snake snake = snakes.get(i);
            if (snake.position.x<-4){
                snakes.remove(i);
                len11=snakes.size();
            }
        }
        int len12 = stones.size();
        for (int i = 0; i < len12; i++) {
            Stone stone = stones.get(i);
            if (stone.position.x<-4){
                stones.remove(i);
                len12=stones.size();
            }
        }
        int len13 = ghosts.size();
        for (int i = 0; i < len13; i++) {
            Ghost ghost = ghosts.get(i);
            if (ghost.position.x<-4){
                ghosts.remove(i);
                len13=ghosts.size();
            }
        }
        int len14 = stars.size();
        for (int i = 0; i < len14; i++) {
            Star star = stars.get(i);
            if (star.position.x<-4){
                stars.remove(i);
                len14=stars.size();
            }
            else if (star.position.x>22){
                stars.remove(i);
                len14=stars.size();
            }
            else if (star.position.y>30){
                stars.remove(i);
                len14=stars.size();
            }else if (star.position.y<0){
                stars.remove(i);
                len14=stars.size();
            }
        }
        int len15 = spears.size();
        for (int i = 0; i < len15; i++) {
            Spear spear = spears.get(i);
            if (spear.position.x<-4){
                spears.remove(i);
                len15=spears.size();
            }
        }

    }
    public void updateStars(float deltaTime){
        int len = stars.size();
        for (int i = 0; i < len; i++) {
            Star star = stars.get(i);
            star.update(deltaTime);
            if (star.position.x<0){
                stars.remove(i);
                len=stars.size();
            }
        }
    }
    public void updateEnemies(float deltaTime){

        int len = hedgehogs.size();
        for (int i = 0; i < len; i++) {
            Hedgehog hedgehog = hedgehogs.get(i);
            hedgehog.update(deltaTime);
        }

        int len2 = wolves.size();
        for (int i = 0; i < len2; i++) {
            Wolf wolf = wolves.get(i);
            wolf.update(deltaTime);
        }
        int len3 = bulldogs.size();
        for (int i = 0; i < len3; i++) {
            Bulldog bulldog = bulldogs.get(i);
            bulldog.update(deltaTime);
        }
        int len4 = captains.size();
        for (int i = 0; i < len4; i++) {
            Captain captain = captains.get(i);
            captain.update(deltaTime);
        }
        int len5 = birds.size();
        for (int i = 0; i < len5; i++) {
            Bird bird = birds.get(i);
            bird.update(deltaTime);
        }
        int len6 = dragons.size();
        for (int i = 0; i < len6; i++) {
            Dragon dragon = dragons.get(i);
            dragon.update(deltaTime);
        }
        int len7 = snakes.size();
        for (int i = 0; i < len7; i++) {
            Snake snake = snakes.get(i);
            snake.update(deltaTime);
        }
        int len8 = stones.size();
        for (int i = 0; i < len8; i++) {
            Stone stone = stones.get(i);
            stone.update(deltaTime);
        }
        int len9 = ghosts.size();
        for (int i = 0; i < len9; i++) {
            Ghost ghost = ghosts.get(i);
            ghost.update(deltaTime);
        }
        int len10 = spears.size();
        for (int i = 0; i < len10; i++) {
            Spear spear = spears.get(i);
            spear.update(deltaTime);
        }

    }
    public void createEarth(){
        listener.earthSound();
        Earth earth = new Earth(molly.position.x,32); ///Tidigare 3
        earths.add(earth);
    }
    public void createHit(){

        Hit hit = new Hit(molly.position.x,molly.position.y); ///Tidigare 3
        hits.add(hit);
    }
    public void createFire(){
        listener.fireSound();
        Fire fire = new Fire(molly.position.x+1f,molly.position.y+0.5f);
        fire.velocity.x=12;
        fires.add(fire);

    }
    public void createHedgehog(float positionx, float positiony, float velocity){

        Hedgehog hedgehog = new Hedgehog(positionx,positiony);
        hedgehog.velocity.x=velocity;
        hedgehogs.add(hedgehog);

    }
    public void createWolf(float positionx, float positiony, float velocity){

        Wolf wolf = new Wolf(positionx,positiony);
        wolf.velocity.x=velocity;
        wolves.add(wolf);

    }
    public void createBulldog(float positionx, float positiony, float velocity){

        Bulldog bulldog = new Bulldog(positionx,positiony);
        bulldog.velocity.x=velocity;
        bulldogs.add(bulldog);

    }
    public void createCaptain(float positionx, float positiony, float velocity){

        Captain captain = new Captain(positionx,positiony);
        captain.velocity.x=velocity;
        captains.add(captain);

    }
    public void createBird(float positionx, float positiony, float velocityx, float velocityy){

        Bird bird = new Bird(positionx,positiony);
        bird.velocity.x=velocityx;
        bird.velocity.y=velocityy;
        birds.add(bird);

    }
    public void createDragon(float positionx, float positiony, float velocityx, float velocityy){

        Dragon dragon = new Dragon(positionx,positiony);
        dragon.velocity.x=velocityx;
        dragon.velocity.y=velocityy;
        dragons.add(dragon);

    }
    public void createSnake(float positionx, float positiony, float velocity){

        Snake snake = new Snake(positionx,positiony);
        snake.velocity.x=velocity;
        snakes.add(snake);

    }
    public void createStone(float positionx, float positiony, float velocity){

        Stone stone = new Stone(positionx,positiony);
        stone.velocity.x=velocity;
        stones.add(stone);

    }
    public void createGhost(float positionx, float positiony, float velocity){

        Ghost ghost = new Ghost(positionx,positiony);
        ghost.velocity.x=velocity;
        ghosts.add(ghost);

    }
    public void createSpear(float positionx, float positiony, float velocity){

        Spear spear = new Spear(positionx,positiony);
        spear.velocity.x=velocity;
        spears.add(spear);

    }
    public void createAir(){
        listener.airSound();
        Air air = new Air(molly.position.x-4,molly.position.y);
        air.velocity.x=10;
        air.velocity.y=5f;
        airs.add(air);

    }
    public void createStars(float posx, float posy, String color){

        Star star = new Star(posx,posy);
        star.velocity.x=20;
        star.velocity.y=0;
        star.color = color;
        stars.add(star);

        Star star2 = new Star(posx,posy);
        star2.velocity.x=-20;
        star2.velocity.y=0;
        star2.color = color;
        stars.add(star2);

        Star star3 = new Star(posx,posy);
        star3.velocity.x=0;
        star3.velocity.y=20;
        star3.color = color;
        stars.add(star3);

        Star star4 = new Star(posx,posy);
        star4.velocity.x=0;
        star4.velocity.y=-20;
        star4.color = color;
        stars.add(star4);

        Star star5 = new Star(posx,posy);
        star5.velocity.x=15;
        star5.velocity.y=15;
        star5.color = color;
        stars.add(star5);

        Star star6 = new Star(posx,posy);
        star6.velocity.x=-15;
        star6.velocity.y=15;
        star6.color = color;
        stars.add(star6);

        Star star7 = new Star(posx,posy);
        star7.velocity.x=15;
        star7.velocity.y=-15;
        star7.color = color;
        stars.add(star7);

        Star star8 = new Star(posx,posy);
        star8.velocity.x=-15;
        star8.velocity.y=-15;
        star8.color = color;
        stars.add(star8);

    }
    public void createWater(){
        listener.waterSound();
        Water water = new Water(molly.position.x-1,molly.position.y-1);
        water.velocity.x=10;
        water.velocity.y=15;
        waters.add(water);
        Water water2 = new Water(molly.position.x,molly.position.y-1);
        water2.velocity.x=10;
        water2.velocity.y=19;
        waters.add(water2);
        Water water3 = new Water(molly.position.x+1,molly.position.y-1);
        water3.velocity.x=10;
        water3.velocity.y=23;
        waters.add(water3);

    }


    private void updatePlatforms(float deltaTime) {
        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            platform.update(deltaTime);
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING
                    && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
                platforms.remove(platform);
                len = platforms.size();
            }
        }
    }
    private void updateEarths(float deltaTime) {
        int len = earths.size();
        for (int i = 0; i < len; i++) {
            Earth earth = earths.get(i);
            earth.update(deltaTime);
            if (earth.state == Earth.EARTH_STATE_PULVERIZING
                    && earth.stateTime > Earth.EARTH_PULVERIZE_TIME) {
                earths.remove(earth);
                len = earths.size();
            }
        }
    }

    private void setupEnemies(){

        ///ToDo Slumpa var och vad som placeras ut. Jämför med mollys maxhp.
        if (molly.maxhp<13) {
            if (mContext.enemy.equals("birdman")) {
                enemyMaxHp = 8;
                enemyHp = 8;
                createBird(27,26f,-4, -0.7f);
                createBird(47,21f,-4, 0f);
                createBird(67,26f,-4, -0.5f);
                createBird(87,21f,-4, 0f);
                createBird(107,26f,-4, -0.3f);
                createStone(24,18.2f,-7);
                createStone(34,18.2f,-7);
                createStone(64,18.2f,-7);
                createStone(84,18.2f,-7);
                createStone(104,18.2f,-7);
                createStone(134,18.2f,-7);
                createStone(144,18.2f,-7);
                createStone(164,18.2f,-7);
                createStone(174,18.2f,-7);
                createStone(184,18.2f,-7);
                createStone(194,18.2f,-7);

            } else if (mContext.enemy.equals("speargirl")) {
                enemyMaxHp = 8;
                enemyHp = 8;
                createSpear(24,19.2f,-9);
                createSpear(42,18.2f,-9);
                createSpear(54,17.2f,-9);
                createSpear(74,19.2f,-9);
                createSpear(82,17.2f,-9);
                createSpear(94,18.2f,-9);
                createSpear(114,17.2f,-9);
                createSpear(122,18.2f,-9);
                createSpear(74,19.2f,-9);
                createSpear(82,17.2f,-9);
                createSpear(102,19.2f,-9);
                createSpear(114,17.2f,-9);
                createSpear(134,19.2f,-9);
                createSpear(158,17.2f,-9);
                createSpear(184,17.2f,-9);
                createSpear(198,18.2f,-9);
                createSpear(224,17.2f,-9);
                createSpear(250,17.2f,-9);
                createSpear(262,18.2f,-9);
            } else if (mContext.enemy.equals("wizboy")) {
                enemyMaxHp = 11;
                enemyHp = 11;
            } else if (mContext.enemy.equals("wizgirl")) {
                enemyMaxHp = 11;
                enemyHp = 11;
            } else if (mContext.enemy.equals("captain")) {
                enemyMaxHp = 14;
                enemyHp = 14;
            } else if (mContext.enemy.equals("darkwiz")) {
                enemyMaxHp = 16;
                enemyHp = 16;
            } else if (mContext.enemy.equals("hunchback")) {
                enemyMaxHp = 20;
                enemyHp = 20;
            } else if (mContext.enemy.equals("gent")) {
                enemyMaxHp = 22;
                enemyHp = 22;
            } else if (mContext.enemy.equals("bull")) {
                enemyMaxHp = 24;
                enemyHp = 24;
            }

        } else if (molly.maxhp>12 && molly.maxhp<18){
            if (mContext.enemy.equals("birdman")) {
                enemyMaxHp = 10;
                enemyHp = 10;
                createBird(27,26f,-4, -0.7f);
                createBird(54,21f,-4, 0f);
                createBird(67,26f,-4, -0.5f);
                createBird(87,21f,-4, 0f);
                createBird(94,21f,-4, 0f);
                createBird(107,26f,-4, -0.3f);
                createStone(24,18.2f,-7);
                createStone(34,18.2f,-7);
                createStone(64,18.2f,-7);
                createStone(84,18.2f,-7);
                createStone(104,18.2f,-7);
                createStone(134,18.2f,-7);
                createStone(144,18.2f,-7);
                createStone(164,18.2f,-7);
                createStone(174,18.2f,-7);
                createStone(184,18.2f,-7);
                createStone(194,18.2f,-7);
            } else if (mContext.enemy.equals("speargirl")) {
                enemyMaxHp = 10;
                enemyHp = 10;
                createSpear(24,19.2f,-9);
                createSpear(34,19.2f,-9);
                createSpear(42,18.2f,-9);
                createSpear(54,17.2f,-9);
                createSpear(62,18.2f,-9);
                createSpear(82,17.2f,-9);
                createSpear(94,18.2f,-9);
                createSpear(102,17.2f,-9);
                createSpear(114,17.2f,-9);
                createSpear(122,18.2f,-9);
                createSpear(74,19.2f,-9);
                createSpear(114,17.2f,-9);
                createSpear(122,18.2f,-9);
                createSpear(146,17.2f,-9);
                createSpear(158,17.2f,-9);
                createSpear(172,19.2f,-9);
                createSpear(184,17.2f,-9);
                createSpear(198,18.2f,-9);
                createSpear(212,17.2f,-9);
                createSpear(224,17.2f,-9);
                createSpear(236,19.2f,-9);
                createSpear(250,17.2f,-9);
                createSpear(262,18.2f,-9);
            } else if (mContext.enemy.equals("wizboy")) {
                enemyMaxHp = 13;
                enemyHp = 13;
            } else if (mContext.enemy.equals("wizgirl")) {
                enemyMaxHp = 13;
                enemyHp = 13;
            } else if (mContext.enemy.equals("captain")) {
                enemyMaxHp = 16;
                enemyHp = 16;
            } else if (mContext.enemy.equals("darkwiz")) {
                enemyMaxHp = 18;
                enemyHp = 18;
            } else if (mContext.enemy.equals("hunchback")) {
                enemyMaxHp = 20;
                enemyHp = 20;
            } else if (mContext.enemy.equals("gent")) {
                enemyMaxHp = 22;
                enemyHp = 22;
            } else if (mContext.enemy.equals("bull")) {
                enemyMaxHp = 24;
                enemyHp = 24;
            }
        } else if (molly.maxhp>17){
            if (mContext.enemy.equals("birdman")) {
                enemyMaxHp = 12;
                enemyHp = 12;
                createBird(27,26f,-4, -0.7f);
                createBird(47,21f,-4, 0f);
                createBird(54,21f,-4, 0f);
                createBird(67,26f,-4, -0.5f);
                createBird(87,21f,-4, 0f);
                createBird(94,21f,-4, 0f);
                createBird(107,26f,-4, -0.3f);
                createStone(24,18.2f,-7);
                createStone(34,18.2f,-7);
                createStone(44,18.2f,-7);
                createStone(64,18.2f,-7);
                createStone(84,18.2f,-7);
                createStone(104,18.2f,-7);
                createStone(124,18.2f,-7);
                createStone(134,18.2f,-7);
                createStone(144,18.2f,-7);
                createStone(154,18.2f,-7);
                createStone(164,18.2f,-7);
                createStone(174,18.2f,-7);
                createStone(184,18.2f,-7);
                createStone(194,18.2f,-7);
            } else if (mContext.enemy.equals("speargirl")) {
                enemyMaxHp = 12;
                enemyHp = 12;
                ///17,2, 18,2, 19,2)
                createSpear(24,19.2f,-9);
                createSpear(34,19.2f,-9);
                createSpear(42,18.2f,-9);
                createSpear(54,17.2f,-9);
                createSpear(62,18.2f,-9);
                createSpear(74,19.2f,-9);
                createSpear(82,17.2f,-9);
                createSpear(94,18.2f,-9);
                createSpear(102,17.2f,-9);
                createSpear(114,17.2f,-9);
                createSpear(122,18.2f,-9);
                createSpear(74,19.2f,-9);
                createSpear(82,17.2f,-9);
                createSpear(94,17.2f,-9);
                createSpear(102,19.2f,-9);
                createSpear(114,17.2f,-9);
                createSpear(122,18.2f,-9);
                createSpear(134,19.2f,-9);
                createSpear(146,17.2f,-9);
                createSpear(158,17.2f,-9);
                createSpear(172,19.2f,-9);
                createSpear(184,17.2f,-9);
                createSpear(198,18.2f,-9);
                createSpear(212,17.2f,-9);
                createSpear(224,17.2f,-9);
                createSpear(236,19.2f,-9);
                createSpear(250,17.2f,-9);
                createSpear(262,18.2f,-9);

            } else if (mContext.enemy.equals("wizboy")) {
                enemyMaxHp = 15;
                enemyHp = 15;
            } else if (mContext.enemy.equals("wizgirl")) {
                enemyMaxHp = 15;
                enemyHp = 15;
            } else if (mContext.enemy.equals("captain")) {
                enemyMaxHp = 18;
                enemyHp = 18;
            } else if (mContext.enemy.equals("darkwiz")) {
                enemyMaxHp = 20;
                enemyHp = 20;
            } else if (mContext.enemy.equals("hunchback")) {
                enemyMaxHp = 20;
                enemyHp = 20;
            } else if (mContext.enemy.equals("gent")) {
                enemyMaxHp = 22;
                enemyHp = 22;
            } else if (mContext.enemy.equals("bull")) {
                enemyMaxHp = 24;
                enemyHp = 24;
            }
        }
    }
    private void updateFires(float deltaTime) {
        int len = fires.size();
        for (int i = 0; i < len; i++) {
            Fire fire = fires.get(i);
            fire.update(deltaTime);
            if (fire.state == Fire.FIRE_STATE_PULVERIZING
                    && fire.stateTime > Fire.FIRE_PULVERIZE_TIME) {
                fires.remove(fire);
                len = fires.size();
            }
        }
    }
    private void updateAirs(float deltaTime) {
        int len = airs.size();
        for (int i = 0; i < len; i++) {
            Air air = airs.get(i);
            air.update(deltaTime);
            if (air.state == Air.AIR_STATE_PULVERIZING
                    && air.stateTime > Air.AIR_PULVERIZE_TIME) {
                airs.remove(air);
                len = airs.size();
            }
        }
    }

    private void updateWaters(float deltaTime) {
        int len = waters.size();
        for (int i = 0; i < len; i++) {
            Water water = waters.get(i);
            water.update(deltaTime);
            if (water.state == Water.WATER_STATE_PULVERIZING
                    && water.stateTime > Water.WATER_PULVERIZE_TIME) {
                waters.remove(water);
                len = waters.size();
            }
        }
    }

    private void updateSquirrels(float deltaTime) {
        int len = squirrels.size();
        for (int i = 0; i < len; i++) {
            Squirrel squirrel = squirrels.get(i);
            squirrel.update(deltaTime);
        }
    }

    private void updateCoins(float deltaTime) {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = coins.get(i);
            coin.update(deltaTime);
        }
    }

    private void checkCollisions() {

        ///SuperJumper-metoder
        checkPlatformCollisions();
       /// checkSquirrelCollisions();
        checkItemCollisions();
        ///checkCastleCollisions();
        ///Mina metoder
        checkEarthGroundCollisions();
        checkEarthEnemyCollisions();
        checkWaterEnemyCollisions();
        checkAirEnemyCollisions();
        checkFireEnemyCollisions();
        checkEnemyMollyCollisions();
    }

    private void checkPlatformCollisions() {
        if (bob.velocity.y > 0)
            return;

        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            if (bob.position.y > platform.position.y) {
                if (OverlapTester
                        .overlapRectangles(bob.bounds, platform.bounds)) {
                    bob.hitPlatform();
                    listener.jump();
                    if (rand.nextFloat() > 0.5f) {
                        platform.pulverize();
                    }
                    break;
                }
            }
        }
    }

    private void checkSquirrelCollisions() {
        int len = squirrels.size();
        for (int i = 0; i < len; i++) {
            Squirrel squirrel = squirrels.get(i);
            if (OverlapTester.overlapRectangles(squirrel.bounds, bob.bounds)) {
                bob.hitSquirrel();
                listener.hit();
            }
        }
    }
    private void checkEarthGroundCollisions() {
        int len = earths.size();
        for (int i = 0; i < len; i++) {
            Earth earth = earths.get(i);
            if (OverlapTester.overlapRectangles(earth.bounds, ground.bounds)) {
               earth.velocity.y = 0;
               earth.velocity.x = 3;
                earth.position.y =17.8f;
            }
        }
    }

    private void checkEnemyMollyCollisions(){

        /* public final List<Hedgehog> hedgehogs;
        public final List<Wolf> wolves;
        public final List<Bulldog> bulldogs;
        public final List<Captain> captains;
        public final List<Bird> birds;
        public final List<Dragon> dragons;
        public final List<Snake> snakes;
        public final List<Stone> stones;
        public final List<Ghost> ghosts;
        */
        int len = hedgehogs.size();
        int len2 = wolves.size();
        int len3 = bulldogs.size();
        int len4 = captains.size();
        int len5 = birds.size();
        int len6 = dragons.size();
        int len7 = snakes.size();
        int len8 = stones.size();
        int len9 = ghosts.size();
        int len10 = spears.size();

        for (int i = 0; i < len; i++) {

            Hedgehog hedgehog = hedgehogs.get(i);
            if (OverlapTester.overlapRectangles(hedgehog.bounds, molly.bounds)) {
                molly.hp = molly.hp-1;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                hedgehogs.remove(hedgehog);
                len = hedgehogs.size();
            }
        }
        for (int i = 0; i < len2; i++) {

            Wolf wolf = wolves.get(i);
            if (OverlapTester.overlapRectangles(wolf.bounds, molly.bounds)) {
                molly.hp = molly.hp-3;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                wolves.remove(wolf);
                len2 = wolves.size();
            }
        }
        for (int i = 0; i < len3; i++) {

            Bulldog bulldog = bulldogs.get(i);
            if (OverlapTester.overlapRectangles(bulldog.bounds, molly.bounds)) {
                molly.hp = molly.hp-2;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                bulldogs.remove(bulldog);
                len3 = bulldogs.size();
            }
        }
        for (int i = 0; i < len4; i++) {

            Captain captain = captains.get(i);
            if (OverlapTester.overlapRectangles(captain.bounds, molly.bounds)) {
                molly.hp = molly.hp-5;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                captains.remove(captain);
                len4 = captains.size();
            }
        }
        for (int i = 0; i < len5; i++) {

            Bird bird = birds.get(i);
            if (OverlapTester.overlapRectangles(bird.bounds, molly.bounds)) {
                molly.hp = molly.hp-2;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                birds.remove(bird);
                len5 = birds.size();
            }
        }
        for (int i = 0; i < len6; i++) {

            Dragon dragon = dragons.get(i);
            if (OverlapTester.overlapRectangles(dragon.bounds, molly.bounds)) {
                molly.hp = molly.hp-3;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                dragons.remove(dragon);
                len6 = dragons.size();
            }
        }
        for (int i = 0; i < len7; i++) {

            Snake snake = snakes.get(i);
            if (OverlapTester.overlapRectangles(snake.bounds, molly.bounds)) {
                molly.hp = molly.hp-2;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                snakes.remove(snake);
                len7 = snakes.size();
            }
        }
        for (int i = 0; i < len8; i++) {

            Stone stone = stones.get(i);
            if (OverlapTester.overlapRectangles(stone.bounds, molly.bounds)) {
                molly.hp = molly.hp-3;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                stones.remove(stone);
                len8 = stones.size();
            }
        }
        for (int i = 0; i < len9; i++) {

            Ghost ghost = ghosts.get(i);
            if (OverlapTester.overlapRectangles(ghost.bounds, molly.bounds)) {
                molly.hp = molly.hp-4;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                ghosts.remove(ghost);
                len9 = ghosts.size();
            }
        }
        for (int i = 0; i < len10; i++) {

            Spear spear = spears.get(i);
            if (OverlapTester.overlapRectangles(spear.bounds, molly.bounds)) {
                molly.hp = molly.hp-2;
                listener.hit();
                createHit();
                ///ToDo Smash up Molly
                spears.remove(spear);
                len10 = spears.size();
            }
        }

    }

    private void checkFireEnemyCollisions() {
        int len = fires.size();
        int len2 = captains.size();
        int len3 = wolves.size();
        int len4 = birds.size();
        int len5 = spears.size();
        for (int i = 0; i < len; i++) {
            Fire fire = fires.get(i);

            for (int j = 0; j<len2;j++){
                Captain captain = captains.get(j);
                if (OverlapTester.overlapRectangles(fire.bounds, captain.bounds)) {
                    createStars(fire.position.x, fire.position.y, "red");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    fires.remove(fire);
                    captains.remove(captain);
                    len2 = captains.size();
                    len =fires.size();

                }
            }

            for (int j = 0; j<len3;j++){
                Wolf wolf = wolves.get(j);
                if (OverlapTester.overlapRectangles(fire.bounds, wolf.bounds)) {
                    createStars(fire.position.x, fire.position.y, "red");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    fires.remove(fire);
                    wolves.remove(wolf);
                    len3 = wolves.size();
                    len =fires.size();
                }
            }
            for (int j = 0; j<len4;j++){
                Bird bird = birds.get(j);
                if (OverlapTester.overlapRectangles(fire.bounds, bird.bounds)) {
                    createStars(fire.position.x, fire.position.y, "red");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    fires.remove(fire);
                    birds.remove(bird);
                    len4 = birds.size();
                    len =fires.size();
                }
            }
            for (int j = 0; j<len5;j++){
                Spear spear = spears.get(j);
                if (OverlapTester.overlapRectangles(fire.bounds, spear.bounds)) {
                    createStars(fire.position.x, fire.position.y, "red");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    fires.remove(fire);
                    spears.remove(spear);
                    len5 = spears.size();
                    len =fires.size();
                }
            }
        }
    }
    private void checkEarthEnemyCollisions() {
        int len = earths.size();
        int len2 = snakes.size();
        int len3 = hedgehogs.size();
        int len4 = birds.size();
        int len5 = stones.size();
        int len6 = spears.size();

        for (int i = 0; i < len; i++) {
            Earth earth = earths.get(i);

            for (int j = 0; j<len2;j++){
                Snake snake = snakes.get(j);
                if (OverlapTester.overlapRectangles(earth.bounds, snake.bounds)) {
                    createStars(earth.position.x, earth.position.y, "green");

                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    earths.remove(earth);
                    snakes.remove(snake);
                    len2 = snakes.size();
                    len =earths.size();

                }
            }

            for (int j = 0; j<len3;j++){
                Hedgehog hedgehog = hedgehogs.get(j);
                if (OverlapTester.overlapRectangles(earth.bounds, hedgehog.bounds)) {
                    createStars(earth.position.x, earth.position.y, "green");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    earths.remove(earth);
                    hedgehogs.remove(hedgehog);
                    len3 = hedgehogs.size();
                    len =earths.size();
                }
            }
            for (int j = 0; j<len4;j++){
                Bird bird = birds.get(j);
                if (OverlapTester.overlapRectangles(earth.bounds, bird.bounds)) {
                    createStars(earth.position.x, earth.position.y, "green");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    earths.remove(earth);
                    birds.remove(bird);
                    len4 = birds.size();
                    len =earths.size();
                }
            }
            for (int j = 0; j<len5;j++){
                Stone stone = stones.get(j);
                if (OverlapTester.overlapRectangles(earth.bounds, stone.bounds)) {
                    createStars(earth.position.x, earth.position.y, "green");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    earths.remove(earth);
                    stones.remove(stone);
                    len5 = stones.size();
                    len =earths.size();
                }
            }
            for (int j = 0; j<len6;j++){
                Spear spear = spears.get(j);
                if (OverlapTester.overlapRectangles(earth.bounds, spear.bounds)) {
                    createStars(earth.position.x, earth.position.y, "green");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    earths.remove(earth);
                    spears.remove(spear);
                    len6 = spears.size();
                    len =earths.size();
                }
            }
        }
    }
    private void checkWaterEnemyCollisions() {
        int len = waters.size();
        int len2 = bulldogs.size();
        int len3 = ghosts.size();

        for (int i = 0; i < len; i++) {
            Water water = waters.get(i);

            for (int j = 0; j<len2;j++){
                Bulldog bulldog = bulldogs.get(j);
                if (OverlapTester.overlapRectangles(water.bounds, bulldog.bounds)) {
                    createStars(water.position.x, water.position.y, "blue");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    waters.remove(water);
                    bulldogs.remove(bulldog);
                    len2 = bulldogs.size();
                    len =waters.size();

                }
            }

            for (int j = 0; j<len3;j++){
                Ghost ghost = ghosts.get(j);
                if (OverlapTester.overlapRectangles(water.bounds, ghost.bounds)) {
                    createStars(water.position.x, water.position.y, "blue");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    waters.remove(water);
                    ghosts.remove(ghost);
                    len3 = ghosts.size();
                    len =waters.size();
                }
            }
        }
    }
    private void checkAirEnemyCollisions() {
        int len = airs.size();
        int len2 = birds.size();
        int len3 = dragons.size();
        int len4 = spears.size();
        for (int i = 0; i < len; i++) {
            Air air = airs.get(i);

            for (int j = 0; j<len2;j++){
                Bird bird = birds.get(j);
                if (OverlapTester.overlapRectangles(air.bounds, bird.bounds)) {
                    createStars(air.position.x, air.position.y, "yellow");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    airs.remove(air);
                    birds.remove(bird);
                    len2 = birds.size();
                    len =airs.size();

                }
            }

            for (int j = 0; j<len3;j++){
                Dragon dragon = dragons.get(j);
                if (OverlapTester.overlapRectangles(air.bounds, dragon.bounds)) {
                    createStars(air.position.x, air.position.y, "yellow");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    airs.remove(air);
                    dragons.remove(dragon);
                    len3 = dragons.size();
                    len =airs.size();
                }
            }
            for (int j = 0; j<len3;j++){
                Spear spear = spears.get(j);
                if (OverlapTester.overlapRectangles(air.bounds, spear.bounds)) {
                    createStars(air.position.x, air.position.y, "yellow");
                    ///ToDo Olika skada beroende på level?
                    enemyHp = enemyHp-1;
                    airs.remove(air);
                    spears.remove(spear);
                    len3 = spears.size();
                    len =airs.size();
                }
            }
        }
    }

    private void checkItemCollisions() {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = coins.get(i);
            if (OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {
                coins.remove(coin);
                len = coins.size();
                listener.coin();
                score += Coin.COIN_SCORE;
            }

        }

        if (bob.velocity.y > 0)
            return;

        len = springs.size();
        for (int i = 0; i < len; i++) {
            Spring spring = springs.get(i);
            if (bob.position.y > spring.position.y) {
                if (OverlapTester.overlapRectangles(bob.bounds, spring.bounds)) {
                    bob.hitSpring();
                    listener.highJump();
                }
            }
        }
    }

    private void checkCastleCollisions() {
        if (OverlapTester.overlapRectangles(castle.bounds, bob.bounds)) {
            state = WORLD_STATE_NEXT_LEVEL;
        }
    }
    private void checkGround() {
        if (OverlapTester.overlapRectangles(ground.bounds, molly.bounds)) {
            molly.state = molly.MOLLY_STATE_NORMAL;
            molly.position.y=19;
            molly.velocity.y=0;
        }
    }

    private void checkGameOver() {

        ///ToDo Fixa i GameScreen-klassen beroende på hur spelet slutade

        if (timer.position.x>=40 || molly.hp<1 || enemyHp<1){
            state = WORLD_STATE_GAME_OVER;
            listener.defeat();
        }

    }
}
