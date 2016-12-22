package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Bird extends DynamicGameObject {
    public static final float BIRD_WIDTH = 4f;
    public static final float BIRD_HEIGHT = 4f;
    public static final float BIRD_VELOCITY = 1f;

    float stateTime = 0;

    public Bird(float x, float y) {
        super(x, y, BIRD_WIDTH, BIRD_HEIGHT);
        velocity.set(BIRD_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(BIRD_WIDTH / 2, BIRD_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
