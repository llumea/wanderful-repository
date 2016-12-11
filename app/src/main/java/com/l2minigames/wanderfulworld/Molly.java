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
    public static final float MOLLY_JUMP_VELOCITY = 11;
    public static final float MOLLY_MOVE_VELOCITY = 20;
    public static final float MOLLY_WIDTH = 0.8f;
    public static final float MOLLY_HEIGHT = 0.8f;

    int state;
    float stateTime;

    public Molly(float x, float y) {
        super(x, y, MOLLY_WIDTH, MOLLY_HEIGHT);
        state = MOLLY_STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
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

    public void doEARTH() {

        state = MOLLY_STATE_EARTH;
        stateTime = 0;
    }
    public void doFIRE() {

        state = MOLLY_STATE_FIRE;
        stateTime = 0;
    }
    public void doAIR() {

        state = MOLLY_STATE_AIR;
        stateTime = 0;
    }
    public void doWATER() {

        state = MOLLY_STATE_WATER;
        stateTime = 0;
    }
}