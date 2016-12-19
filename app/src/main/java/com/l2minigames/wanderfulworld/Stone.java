package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Stone extends DynamicGameObject {
    public static final float STONE_WIDTH = 1;
    public static final float STONE_HEIGHT = 1f;
    public static final float STONE_VELOCITY = 1f;

    float stateTime = 0;

    public Stone(float x, float y) {
        super(x, y, STONE_WIDTH, STONE_HEIGHT);
        velocity.set(STONE_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(STONE_WIDTH / 2, STONE_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
