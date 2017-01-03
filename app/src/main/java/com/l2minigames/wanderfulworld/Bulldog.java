package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.DynamicGameObject;

/**
 * Created by umyhlarsle on 2016-12-19.
 */
public class Bulldog extends DynamicGameObject {
    public static final float BULLDOG_WIDTH = 4;
    public static final float BULLDOG_HEIGHT = 4f;
    public static final float BULLDOG_VELOCITY = 1f;
    public static final int BULLDOG_STATE_NORMAL = 0;
    public static final int BULLDOG_STATE_JUMP = 1;
    public static final int BULLDOG_STATE_JUMP_HIGH = 2;
    int jumperType =0;
    int state;
    float stateTime = 0;

    public Bulldog(float x, float y, int jumperType) {
        super(x, y, BULLDOG_WIDTH, BULLDOG_HEIGHT);
        velocity.set(BULLDOG_VELOCITY, 0);
        state = BULLDOG_STATE_NORMAL;
        this.jumperType = jumperType;
    }

    public void update(float deltaTime) {
        if (state==BULLDOG_STATE_JUMP || state==BULLDOG_STATE_JUMP_HIGH){velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);}

        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(BULLDOG_WIDTH / 2, BULLDOG_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
