package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Snake extends DynamicGameObject {
    public static final float SNAKE_WIDTH = 1;
    public static final float SNAKE_HEIGHT = 1f;
    public static final float SNAKE_VELOCITY = 1f;

    float stateTime = 0;

    public Snake(float x, float y) {
        super(x, y, SNAKE_WIDTH, SNAKE_HEIGHT);
        velocity.set(SNAKE_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(SNAKE_WIDTH / 2, SNAKE_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
