package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Timer extends DynamicGameObject {
    public static final float TIMER_WIDTH = 1;
    public static final float TIMER_HEIGHT = 0.6f;
    public static final float TIMER_VELOCITY = 1.5f;

    float stateTime = 0;

    public Timer(float x, float y) {
        super(x, y, TIMER_WIDTH, TIMER_HEIGHT);
        velocity.set(TIMER_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(TIMER_WIDTH / 2, TIMER_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
