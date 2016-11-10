package com.l2minigames.wanderfulworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by umyhlarsle on 2016-11-09.
 */
public class CollectedItem {

    public String itemName;
    public String itemType;
    public String elementType;
    public String imageRef;
    public long timestamp;
    public int level;
    public int cp;
    public int hp;


    public CollectedItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CollectedItem(String itemName, String itemType, String elementType, String imageRef, long timestamp, int level, int cp, int hp) {

        this.itemName = itemName;
        this.itemType = itemType;
        this.elementType = elementType;
        this.imageRef = imageRef;
        this.timestamp = timestamp;
        this.level = level;
        this.cp = cp;
        this.hp = hp;

    }
   /* public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("itemName", itemName);
        result.put("itemType", itemType);
        result.put("elementType", elementType);
        result.put("imageRef", imageRef);
        result.put("level", level);
        result.put("cp", cp);
        result.put("hp", hp);

        return result;


    }
*/
}