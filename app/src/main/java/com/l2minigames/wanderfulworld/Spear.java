package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2017-01-02.
 */
public class Spear extends DynamicGameObject {
    public static final float SPEAR_WIDTH = 4;
    public static final float SPEAR_HEIGHT = 2f;
    public static final float SPEAR_VELOCITY = 1f;

    float stateTime = 0;

    public Spear(float x, float y) {
        super(x, y, SPEAR_WIDTH, SPEAR_HEIGHT);
        velocity.set(SPEAR_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(SPEAR_WIDTH / 2, SPEAR_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
