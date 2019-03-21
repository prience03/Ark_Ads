package com.ark.adkit.polymers.ydt.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;

@SuppressLint("MissingPermission")
public class LocationHelper {

    private static String lon = "0", lat = "0";
    private SharedPreferences sharedPreferences;

    public void init(Context mContext) {
        sharedPreferences = mContext
                .getSharedPreferences(LocationHelper.class.getSimpleName(), Context.MODE_PRIVATE);
        lon = sharedPreferences.getString("key_lon", "0");
        lat = sharedPreferences.getString("key_lat", "0");
        LocationUtils.register(mContext, 500, 100, new LocationUtils.OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {
                sharedPreferences.edit()
                        .putString("key_lon", lon = String.valueOf(location.getLongitude()))
                        .putString("key_lat", lat = String.valueOf(location.getLatitude())).apply();
            }

            @Override
            public void onLocationChanged(Location location) {
                sharedPreferences.edit()
                        .putString("key_lon", lon = String.valueOf(location.getLongitude()))
                        .putString("key_lat", lat = String.valueOf(location.getLatitude())).apply();
                LocationUtils.unregister();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        });
    }

    public static String[] getLocation(Context mContext) {
        if (TextUtils.equals("0", lon) || TextUtils.equals("0", lat)) {
            SharedPreferences sharedPreferences = mContext
                    .getSharedPreferences(LocationHelper.class.getSimpleName(), Context.MODE_PRIVATE);
            lon = sharedPreferences.getString("key_lon", "0");
            lat = sharedPreferences.getString("key_lat", "0");
        }
        return new String[]{lon, lat};
    }

    public void release() {
        LocationUtils.unregister();
    }
}
