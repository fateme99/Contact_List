package com.example.contact_list.utils;

import android.app.Application;
import android.content.Context;

public class ApplicationLoader extends Application {
    public static Context sContextApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sContextApplication = getApplicationContext();

    }


}
