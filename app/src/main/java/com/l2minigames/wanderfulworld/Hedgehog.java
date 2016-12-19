package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Hedgehog extends DynamicGameObject {
    public static final float HEDGEHOG_WIDTH = 1;
    public static final float HEDGEHOG_HEIGHT = 1f;
    public static final float HEDGEHOG_VELOCITY = 1f;

    float stateTime = 0;

    public Hedgehog(float x, float y) {
        super(x, y, HEDGEHOG_WIDTH, HEDGEHOG_HEIGHT);
        velocity.set(HEDGEHOG_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(HEDGEHOG_WIDTH / 2, HEDGEHOG_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
