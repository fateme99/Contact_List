package com.example.contact_list.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorHelper {
    private static Executor mExecutor=Executors.newCachedThreadPool();
    private static Handler mHandlerMain=new Handler(Looper.getMainLooper());
    public static void doInOtherThread(Runnable task){
        mExecutor.execute(task);
    }
    public static Handler getMainHandler(){
        return mHandlerMain;
    }
}
