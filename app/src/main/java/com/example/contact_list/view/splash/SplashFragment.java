package com.example.contact_list.view.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contact_list.databinding.FragmentSplashBinding;
import com.example.contact_list.model.Contact;
import com.example.contact_list.view.list.ContactListActivity;
import com.example.contact_list.R;
import com.example.contact_list.repository.ContactRepository;
import com.example.contact_list.utils.ContactContentObserver;
import com.example.contact_list.utils.MyLog;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SplashFragment extends Fragment {
    private FragmentSplashBinding mBinding;

    public SplashFragment() {
    }

    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.
                inflate(inflater, R.layout.fragment_splash, container, false);
        contactGetter();
        setContactObserver();
        return mBinding.getRoot();
    }

    private void contactGetter() {


        Executor executor = Executors.newCachedThreadPool();
        Runnable runnable = () -> {
            ContactRepository.getAllContacts();
            notifyToUI();
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