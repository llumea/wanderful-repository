package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-29.
 */
public class Hit extends DynamicGameObject {
    public static final float HIT_WIDTH = 4f;
    public static final float HIT_HEIGHT = 4f;
    public static final float HIT_VELOCITY = 1f;
    public static final float HIT_PULVERIZE_TIME = 0.05f * 6;
    public static final int HIT_STATE_PULVERIZING = 0;

    int state;
    float stateTime = 0;

    public Hit(float x, float y) {
        super(x, y, HIT_WIDTH, HIT_HEIGHT);
        velocity.set(0, 0);
        state = HIT_STATE_PULVERIZING;
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(HIT_WIDTH / 2, HIT_HEIGHT / 2);
        stateTime += deltaTime;

    }
}
