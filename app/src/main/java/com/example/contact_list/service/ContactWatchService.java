package com.example.contact_list.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.ContactsContract;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.contact_list.repository.ContactRepository;
import com.example.contact_list.utils.ContactContentObserver;

public class ContactWatchService extends Service {
    private ContactRepository mContactRepository;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                startContactObserver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void startContactObserver(){
        try{
            Toast.makeText(getApplicationContext(),"ContactWatchService Started",Toast.LENGTH_SHORT).show();

            getApplication().
                    getContentResolver().
                    registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                            true,
                            new ContactContentObserver(new Handler(),getApplicationContext()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate() {

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }
}
