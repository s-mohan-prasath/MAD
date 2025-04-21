package com.legendmp.mad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class AppPermissions {
    public static final int PERMISSION_REQUEST_CODE = 100;

    public static boolean hasAllPermissions(Context context) {
        boolean fineLocationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean nearbyGranted = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            nearbyGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED;
        }

        return fineLocationGranted && coarseLocationGranted && nearbyGranted;
    }

    public static void requestAllPermissions(Activity activity) {
        List<String> permissionsToRequest = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.NEARBY_WIFI_DEVICES);
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionsToRequest.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    public static boolean allPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
