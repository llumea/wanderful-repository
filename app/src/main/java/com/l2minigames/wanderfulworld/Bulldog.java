package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Bulldog extends DynamicGameObject {
    public static final float BULLDOG_WIDTH = 1;
    public static final float BULLDOG_HEIGHT = 1f;
    public static final float BULLDOG_VELOCITY = 1f;

    float stateTime = 0;

    public Bulldog(float x, float y) {
        super(x, y, BULLDOG_WIDTH, BULLDOG_HEIGHT);
        velocity.set(BULLDOG_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(BULLDOG_WIDTH / 2, BULLDOG_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
