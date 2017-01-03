package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Wolf extends DynamicGameObject {
    public static final float WOLF_WIDTH = 1;
    public static final float WOLF_HEIGHT = 1f;
    public static final float WOLF_VELOCITY = 1f;
    public static final int WOLF_STATE_NORMAL = 0;
    public static final int WOLF_STATE_JUMP = 1;
    public static final int WOLF_STATE_JUMP_HIGH = 2;
    int jumperType = 0;
    int state;
    float stateTime = 0;



    public Wolf(float x, float y, int jumperType) {
        super(x, y, WOLF_WIDTH, WOLF_HEIGHT);
        velocity.set(WOLF_VELOCITY, 0);
        this.jumperType = jumperType;
    }

    public void update(float deltaTime) {
        if (state==WOLF_STATE_JUMP || state==WOLF_STATE_JUMP_HIGH){velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);}
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(WOLF_WIDTH / 2, WOLF_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
