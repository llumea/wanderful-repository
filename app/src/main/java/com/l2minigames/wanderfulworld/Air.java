package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-14.
 */
public class Air extends DynamicGameObject {
    public static final int AIR_STATE_NORMAL = 0;
    public static final int AIR_STATE_FALL = 1;
    public static final int AIR_STATE_PULVERIZING = 2;
    public static final float AIR_PULVERIZE_TIME = 0.2f * 4;
    public static final float AIR_JUMP_VELOCITY = 11;
    public static final float AIR_MOVE_VELOCITY = 20;
    public static final float AIR_WIDTH = 1f;
    public static final float AIR_HEIGHT = 1f;

    int state;
    float stateTime;

    public Air(float x, float y) {
        super(x, y, AIR_WIDTH, AIR_HEIGHT);
        state = AIR_STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float deltaTime) {
       /// velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        stateTime += deltaTime;
    }

}