package com.l2minigames.wanderfulworld;

/**
 * Created by umyhlarsle on 2016-11-01.
 */
public class UserObject {

    public String username;
    public String email;

    public UserObject() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserObject(String username, String email) {
        this.username = username;
        this.email = email;
    }

}