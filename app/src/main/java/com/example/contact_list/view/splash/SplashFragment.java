package com.example.contact_list.view.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contact_list.R;
import com.example.contact_list.repository.ContactRepository;
import com.example.contact_list.view.list.ContactListActivity;
import com.example.contact_list.utils.ContactContentObserver;
import com.example.contact_list.utils.MyLog;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SplashFragment extends Fragment {
    public SplashFragment() {
    }

    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        contactGetter();
        setContactObserver();
        return view;
    }

    private void contactGetter() {
        Executor executor = Executors.newCachedThreadPool();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ContactRepository.getAllContacts();
                notifyToUI();
            }
        };
        executor.execute(runnable);
    }

    private void setContactObserver() {
        try {

            getActivity().getApplication().
                    getContentResolver().
                    registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                            true,
                            new ContactContentObserver(new Handler()));
        } catch (Exception e) {
            MyLog.e(e);
        }
    }

    private void notifyToUI() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            startListFragment();
        });

    }

    private void startListFragment() {
        Intent intent = ContactListActivity.newIntent(getActivity());
        startActivity(intent);
        getActivity().finish();
    }
}