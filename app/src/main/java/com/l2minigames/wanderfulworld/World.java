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
    }

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;    
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0, -30); ///Tidigare 0,-12

    public final Bob bob;
    public final Molly molly;
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
    public Castle castle;    
    public final WorldListener listener;
    public final Random rand;
    
    public float heightSoFar;
    public int score;    
    public int state;
    public int mollyHp;
    SuperJumper mContext;

    public World(WorldListener listener, SuperJumper context) {
        this.bob = new Bob(5, 1);
        this.mContext = context;
        mollyHp = mContext.hp;
        Log.i("GAME", "Molly HP"+mollyHp);
        this.molly = new Molly(3, 19, mContext.hp, mContext.max_hp, mContext.earth_power, mContext.fire_power, mContext.air_power, mContext.water_power);
        this.ground = new Ground(3,17);
        this.platforms = new ArrayList<Platform>();
        this.hedgehogs = new ArrayList<Hedgehog>();
        this.wolves = new ArrayList<Wolf>();
        this.bulldogs = new ArrayList<Bulldog>();
        this.captains = new ArrayList<Captain>();
        this.birds = new ArrayList<Bird>();
        this.dragons = new ArrayList<Dragon>();
        this.snakes = new ArrayList<Snake>();
        this.stones = new ArrayList<Stone>();
        this.ghosts = new ArrayList<Ghost>();
        this.earths = new ArrayList<Earth>();
        this.fires = new ArrayList<Fire>();
        this.airs = new ArrayList<Air>();
        this.waters = new ArrayList<Water>();
        this.springs = new ArrayList<Spring>();
        this.squirrels = new ArrayList<Squirrel>();
        this.coins = new ArrayList<Coin>();        
        this.listener = listener;
        rand = new Random();
        generateLevel();
        createHedgehog(14,17,-4);
        createWolf(24,17.8f,-8);
        createBulldog(25,17.5f,-6);
        createCaptain(25,18.5f,-4);
        createBird(27,26f,-4);
        createDragon(34,26f,-4);
        createSnake(40,17.5f,-3);
        createStone(50,18.2f,-7);
        createGhost(53,22.2f,-4);
        
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
        updatePlatforms(deltaTime);
        updateSquirrels(deltaTime);
        updateCoins(deltaTime);
        updateEarths(deltaTime);
        updateFires(deltaTime);
        updateAirs(deltaTime);
        updateWaters(deltaTime);
        updateEnemies(deltaTime);
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

    }
    public void createEarth(){

        Earth earth = new Earth(molly.position.x,32); ///Tidigare 3
        earths.add(earth);
    }
    public void createFire(){

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
    public void createBird(float positionx, float positiony, float velocity){

        Bird bird = new Bird(positionx,positiony);
        bird.velocity.x=velocity;
        birds.add(bird);

    }
    public void createDragon(float positionx, float positiony, float velocity){

        Dragon dragon = new Dragon(positionx,positiony);
        dragon.velocity.x=velocity;
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
    public void createAir(){

        Air air = new Air(molly.position.x-3,molly.position.y+5);
        air.velocity.x=8;
        air.velocity.y=-1.2f;
        airs.add(air);

    }
    public void createWater(){

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
        checkPlatformCollisions();
        checkSquirrelCollisions();
        checkItemCollisions();
        checkCastleCollisions();
        checkEarthGroundCollisions();
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
               earth.velocity.x = 2;
                earth.position.y =17.8f;
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
        if (heightSoFar - 7.5f > bob.position.y) {
            state = WORLD_STATE_GAME_OVER;
        }
    }
}
