package com.l2minigames.wanderfulworld;

/**
 * Created by umyhlarsle on 2016-11-03.
 */
public class MyMarker {
    double markerLatitude;
    double markerLongitude;
    String markerType;

    public MyMarker(){

    }

    public MyMarker(double markerLatitude, double markerLongitude, String markerType){

        this.markerLatitude = markerLatitude;
        this.markerLongitude = markerLongitude;
        this.markerType = markerType;
    }
}
