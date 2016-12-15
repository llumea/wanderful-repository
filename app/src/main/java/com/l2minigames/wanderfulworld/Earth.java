package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-14.
 */
public class Earth extends DynamicGameObject {
    public static final int EARTH_STATE_NORMAL = 0;
    public static final int EARTH_STATE_FALL = 1;
    public static final int EARTH_STATE_PULVERIZING = 2;
    public static final float EARTH_PULVERIZE_TIME = 0.2f * 4;
    public static final float EARTH_JUMP_VELOCITY = 11;
    public static final float EARTH_MOVE_VELOCITY = 20;
    public static final float EARTH_WIDTH = 1f;
    public static final float EARTH_HEIGHT = 1f;

    int state;
    float stateTime;

    public Earth(float x, float y) {
        super(x, y, EARTH_WIDTH, EARTH_HEIGHT);
        state = EARTH_STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        stateTime += deltaTime;
    }

}
