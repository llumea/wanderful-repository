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
    public int earthpower;
    public int firepower;
    public int airpower;
    public int waterpower;
    public int hp;
    public int maxhp;
    public int cp;
    public int maxcp;
    public long timer;
    public int XP;
    public int level;
    public int travelMode;
    double latitude;
    double longitude;
    ArrayList<MyMarker> markerList = new ArrayList<>();
    Map<String, CollectedItem> collectedItems = new HashMap<>();


    public UserObject() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserObject(String username, String email, int earthpower, int firepower, int airpower, int waterpower, int hp, int maxhp, int cp, int maxcp, long timer, int XP, int level, int travelMode,double latitude, double longitude, ArrayList<MyMarker> markerList, HashMap<String, CollectedItem> collectedItems) {
        this.username = username;
        this.email = email;
        this.earthpower = earthpower;
        this.firepower = firepower;
        this.airpower = airpower;
        this.waterpower = waterpower;
        this.hp = hp;
        this.maxhp =maxhp;
        this.cp = cp;
        this.maxcp = maxcp;
        this.timer = timer;
        this.XP = XP;
        this.level = level;
        this.travelMode = travelMode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerList = markerList;
        this.collectedItems = collectedItems;

    }

}