package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-14.
 */
public class Water extends DynamicGameObject {
    public static final int WATER_STATE_NORMAL = 0;
    public static final int WATER_STATE_FALL = 1;
    public static final int WATER_STATE_PULVERIZING = 2;
    public static final float WATER_PULVERIZE_TIME = 0.2f * 4;
    public static final float WATER_JUMP_VELOCITY = 11;
    public static final float WATER_MOVE_VELOCITY = 20;
    public static final float WATER_WIDTH = 2f;
    public static final float WATER_HEIGHT = 2f;

    int state;
    float stateTime;

    public Water(float x, float y) {
        super(x, y, WATER_WIDTH, WATER_HEIGHT);
        state = WATER_STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width, bounds.height);
        stateTime += deltaTime;
    }

}
