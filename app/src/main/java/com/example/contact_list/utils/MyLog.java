package com.example.contact_list.utils;

import android.util.Log;

import com.example.contact_list.BuildConfig;

public class MyLog {
    public static void e(Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }

}
