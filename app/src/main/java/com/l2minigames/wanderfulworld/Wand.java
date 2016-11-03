package com.l2minigames.wanderfulworld;

/**
 * Created by umyhlarsle on 2016-11-03.
 */
public class Wand {
    int earth;
    int fire;
    int wind;
    int water;

    public Wand() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Wand(int earth, int fire, int wind, int water) {
        this.earth = earth;
        this.fire = fire;
        this.wind = wind;
        this.water = water;


    }
}
