package com.l2minigames.wanderfulworld;

import java.util.ArrayList;

/**
 * Created by umyhlarsle on 2016-11-09.
 */
public class CollectedItem {

    public String itemName;
    public long timestamp;
    public String itemType;
    public int level;


    public CollectedItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CollectedItem(String itemName, long timestamp, String itemType, int level) {

        this.itemName = itemName;
        this.timestamp = timestamp;
        this.itemType = itemType;
        this.level = level;

    }

}