package com.l2minigames.wanderfulworld;

import com.l2minigames.framework.GameObject;

/**
 * Created by umyhlarsle on 2016-12-12.
 */
public class Ground extends GameObject {
    public static float GROUND_WIDTH = 0.3f;
    public static float GROUND_HEIGHT = 0.3f;
    String type = "normal";

    public Ground(float x, float y) {
        super(x, y, GROUND_WIDTH, GROUND_HEIGHT);
    }
}
