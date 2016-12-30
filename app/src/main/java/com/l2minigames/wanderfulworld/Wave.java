package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-30.
 */
public class Wave extends DynamicGameObject {
    public static final int WAVE_STATE_NORMAL = 0;
    public static final float WAVE_WIDTH = 4f;
    public static final float WAVE_HEIGHT = 1f;

    int state;
    float stateTime;

    public Wave(float x, float y) {
        super(x, y, WAVE_WIDTH, WAVE_HEIGHT);
        state = WAVE_STATE_NORMAL;
        stateTime = 0;
    }

    public void update(float deltaTime) {

        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        stateTime += deltaTime;
    }

}
