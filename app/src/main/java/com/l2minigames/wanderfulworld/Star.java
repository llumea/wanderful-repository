package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-20.
 */
public class Star extends DynamicGameObject {
    public static final float STAR_WIDTH = 1;
    public static final float STAR_HEIGHT = 1f;
    public static final float STAR_VELOCITY = 1f;
    String color ="green";

    float stateTime = 0;

    public Star(float x, float y) {
        super(x, y, STAR_WIDTH, STAR_HEIGHT);
        velocity.set(STAR_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(STAR_WIDTH / 2, STAR_HEIGHT / 2);
        stateTime += deltaTime;
    }
}