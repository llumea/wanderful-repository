package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Wolf extends DynamicGameObject {
    public static final float WOLF_WIDTH = 1;
    public static final float WOLF_HEIGHT = 1f;
    public static final float WOLF_VELOCITY = 1f;

    float stateTime = 0;

    public Wolf(float x, float y) {
        super(x, y, WOLF_WIDTH, WOLF_HEIGHT);
        velocity.set(WOLF_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(WOLF_WIDTH / 2, WOLF_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
