package com.l2minigames.wanderfulworld;

import android.util.Log;

/**
 * Created by umyhlarsle on 2016-11-09.
 */
public class PauseUtility {
    public static void pause (int ms){

        try
        {
            Thread.sleep(ms);
        } catch (InterruptedException e)
        {
            Log.d("TAG","thread interrupted");
        }
    }
}