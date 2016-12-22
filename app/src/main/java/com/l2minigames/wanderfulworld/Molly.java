package com.l2minigames.wanderfulworld;

/**
 * Created by umyhlarsle on 2016-12-11.
 */
import com.l2minigames.framework.DynamicGameObject;

public class Molly extends DynamicGameObject{
    public static final int MOLLY_STATE_NORMAL = 0;
    public static final int MOLLY_STATE_BEFORE_JUMP = 1;
    public static final int MOLLY_STATE_JUMP = 2;
    public static final int MOLLY_STATE_EARTH = 3;
    public static final int MOLLY_STATE_FIRE = 4;
    public static final int MOLLY_STATE_AIR = 5;
    public static final int MOLLY_STATE_WATER = 6;
    public static final int MOLLY_STATE_HIT = 7;
    public static final int MOLLY_STATE_DEAD = 8;
    public static final int MOLLY_STATE_PULVERIZE = 9;
    public static final float MOLLY_JUMP_VELOCITY = 18;
    public static final float MOLLY_MOVE_VELOCITY = 20;
    public static final float MOLLY_WIDTH = 1f;
    public static final float MOLLY_HEIGHT = 1f;

    int hp;
    int maxhp;
    int earthCount;
    int fireCount;
    int waterCount;
    int airCount;
    int xp;
    int state;
    float stateTime;

    public static float MOLLY_PULVERIZE_TIME = 0;

    public Molly(float x, float y, int hp, int maxhp, int earthCount, int fireCount, int airCount, int waterCount, int xp) {
        super(x, y, MOLLY_WIDTH, MOLLY_HEIGHT);
        state = MOLLY_STATE_NORMAL;
        this.hp = hp;
        this.maxhp = maxhp;
        this.earthCount = earthCount;
        this.fireCount = fireCount;
        this.airCount = airCount;
        this.waterCount = waterCount;
        this.xp = xp;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        if (state==MOLLY_STATE_JUMP){velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);}
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2+1f);
///Sätt state = normal om höjdled = 0 och inget annat state är aktivt
        if(velocity.y == 0 && state != MOLLY_STATE_BEFORE_JUMP && state !=MOLLY_STATE_EARTH && state !=MOLLY_STATE_FIRE && state !=MOLLY_STATE_AIR && state !=MOLLY_STATE_WATER) {
            if(state != MOLLY_STATE_NORMAL) {
                state = MOLLY_STATE_NORMAL;
                stateTime = 0;
            }
        }



        stateTime += deltaTime;
    }

    public void hitMolly() {
        velocity.set(0,0);
        state = MOLLY_STATE_HIT;
        stateTime = 0;
    }

    public void killMolly() {

        state = MOLLY_STATE_DEAD;
        stateTime = 0;
    }

    public void doEarth() {

        state = MOLLY_STATE_EARTH;
        stateTime = 0;
    }
    public void doFire() {

        state = MOLLY_STATE_FIRE;
        stateTime = 0;
    }
    public void doAir() {

        state = MOLLY_STATE_AIR;
        stateTime = 0;
    }
    public void doWater() {

        state = MOLLY_STATE_WATER;
        stateTime = 0;
    }
    public void jump() {
        velocity.y = MOLLY_JUMP_VELOCITY;
        state = MOLLY_STATE_JUMP;
        stateTime = 0;
    }

}