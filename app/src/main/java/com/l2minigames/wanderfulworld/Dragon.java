package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Dragon extends DynamicGameObject {
    public static final float DRAGON_WIDTH = 1;
    public static final float DRAGON_HEIGHT = 1f;
    public static final float DRAGON_VELOCITY = 1f;

    float stateTime = 0;

    public Dragon(float x, float y) {
        super(x, y, DRAGON_WIDTH, DRAGON_HEIGHT);
        velocity.set(DRAGON_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(DRAGON_WIDTH / 2, DRAGON_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
