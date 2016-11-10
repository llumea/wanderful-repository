package com.l2minigames.wanderfulworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by umyhlarsle on 2016-11-01.
 */
public class UserObject {

    public String username;
    public String email;
    public long timer;
    public int XP;
    public int level;
    double latitude;
    double longitude;
    Wand myWand = new Wand();
    ArrayList<MyMarker> markerList = new ArrayList<>();
    Map<String, CollectedItem> collectedItems = new HashMap<>();


    public UserObject() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserObject(String username, String email, long timer, int XP, int level, double latitude, double longitude, Wand myWand, ArrayList<MyMarker> markerList, HashMap<String, CollectedItem> collectedItems) {
        this.username = username;
        this.email = email;
        this.timer = timer;
        this.XP = XP;
        this.level = level;
        this.latitude = latitude;
        this.longitude = longitude;
        this.myWand = myWand;
        this.markerList = markerList;
        this.collectedItems = collectedItems;

    }

}