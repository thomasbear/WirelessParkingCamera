package com.wordpress.yujiaguo.parkingcamera4yp2;

import android.content.*;
import android.net.wifi.*;
import java.lang.reflect.*;

/*
1. retrieve wifi hotspot on/off status
2.  turn on hotspot (and turn off wifi if needed)
    turn off hotspot

 */

public class ApManager {

    //check whether wifi hotspot on or off
    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable ignored) {}
        return false;
    }

    // toggle wifi hotspot on or off
    public static boolean configApState(Context context) {
        //declare wifimanager
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        //variable to store wifi state
        boolean isWifiEnabled = wifimanager.isWifiEnabled();

        try {
            // if WiFi is on, turn it off
            if(isWifiEnabled) {
                wifimanager.setWifiEnabled(false);
            }
            //turn on/off wifi hotspot according to weather it is on or off
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
} // end of class