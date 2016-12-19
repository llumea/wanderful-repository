package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Captain extends DynamicGameObject {
    public static final float CAPTAIN_WIDTH = 1;
    public static final float CAPTAIN_HEIGHT = 1f;
    public static final float CAPTAIN_VELOCITY = 1f;

    float stateTime = 0;

    public Captain(float x, float y) {
        super(x, y, CAPTAIN_WIDTH, CAPTAIN_HEIGHT);
        velocity.set(CAPTAIN_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(CAPTAIN_WIDTH / 2, CAPTAIN_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
