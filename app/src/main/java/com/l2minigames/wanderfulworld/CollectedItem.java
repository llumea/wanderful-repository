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
    public String imageRef;
    public long timestamp;
    public String uid;



    public CollectedItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CollectedItem(String itemName, String itemType, String imageRef, long timestamp, String uid) {


        this.itemName = itemName;
        this.itemType = itemType;
        this.imageRef = imageRef;
        this.timestamp = timestamp;
        this.uid = uid;

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