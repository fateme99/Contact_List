package com.example.contact_list.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {
    public static final int REQUEST_CODE_CONTACT_PERMISSION = 1;
    public static boolean hasContactPermission(Context context,String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int has_permission = ContextCompat.
                    checkSelfPermission(context.getApplicationContext(), permission);
            if (has_permission == PackageManager.PERMISSION_GRANTED)
                result = true;
        } else {
            result = true;
        }
        return result;
    }

    public static void getContactPermission(Context context, String permission) {
        String[] permissions = {permission};
        ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_CODE_CONTACT_PERMISSION);
    }
}
