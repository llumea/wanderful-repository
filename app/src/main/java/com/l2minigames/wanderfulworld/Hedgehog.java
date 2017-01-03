package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Hedgehog extends DynamicGameObject {
    public static final float HEDGEHOG_WIDTH = 1;
    public static final float HEDGEHOG_HEIGHT = 2f;
    public static final float HEDGEHOG_VELOCITY = 1f;
    public static final int HEDGEHOG_STATE_NORMAL = 0;
    public static final int HEDGEHOG_STATE_JUMP = 1;
    public static final int HEDGEHOG_STATE_JUMP_HIGH = 2;
    int jumperType = 0;
    int state;
    float stateTime = 0;

    public Hedgehog(float x, float y, int jumperType) {
        super(x, y, HEDGEHOG_WIDTH, HEDGEHOG_HEIGHT);
        velocity.set(HEDGEHOG_VELOCITY, 0);
        state = HEDGEHOG_STATE_NORMAL;
        this.jumperType = jumperType;
    }

    public void update(float deltaTime) {
        if (state==HEDGEHOG_STATE_JUMP || state==HEDGEHOG_STATE_JUMP_HIGH){velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);}
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(HEDGEHOG_WIDTH / 2, HEDGEHOG_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
