package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Ghost extends DynamicGameObject {
    public static final float GHOST_WIDTH = 1;
    public static final float GHOST_HEIGHT = 1f;
    public static final float GHOST_VELOCITY = 1f;

    float stateTime = 0;

    public Ghost(float x, float y) {
        super(x, y, GHOST_WIDTH, GHOST_HEIGHT);
        velocity.set(GHOST_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(GHOST_WIDTH / 2, GHOST_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
