package com.l2minigames.wanderfulworld;

/**
 * Created by umyhlarsle on 2016-11-03.
 */
public class MyMarker {
    long markerLatitude;
    long markerLongitude;
    String markerType;

    public MyMarker(){

    }

    public MyMarker(long markerLatitude, long markerLongitude, String markerType){

        this.markerLatitude = markerLatitude;
        this.markerLongitude = markerLongitude;
        this.markerType = markerType;
    }
}
