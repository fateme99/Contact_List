package com.example.contact_list.controller.splash;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contact_list.R;
import com.example.contact_list.controller.list.ContactListActivity;
import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;
import com.example.contact_list.utils.ContactContentObserver;
import com.example.contact_list.utils.MyLog;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SplashFragment extends Fragment {
    private ContactRepository mContactRepository;

    public SplashFragment() {
        // Required empty public constructor
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
        mContactRepository = ContactRepository.getInstance(getActivity());
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
                getAllContacts();
                notifyToUI();
            }
        };
        executor.execute(runnable);
    }

    private void getAllContacts() {

        ContentResolver contentResolver = getActivity().getContentResolver();
        try {
            Cursor cursorContact = contentResolver.
                    query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);
            if (cursorContact != null && cursorContact.getCount() > 0) {
                cursorContact.moveToFirst();
                while (cursorContact.moveToNext()) {
                    String contactNumber = "";
                    String contactId = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
                    String disPlayName = cursorContact.
                            getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cursorContact.getString
                            (cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor cursorPhone = contentResolver.
                                query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{contactId},
                                        null);
                        if (cursorPhone != null) {
                            while (cursorPhone.moveToNext()) {
                                contactNumber = cursorPhone.getString(cursorPhone.
                                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if (contactNumber != null && contactNumber.length() > 0) {
                                    contactNumber = contactNumber.replace(" ", "");
                                }
                            }
                            cursorPhone.close();
                        }
                    }
                    Contact contact = new Contact(contactId, disPlayName, contactNumber);
                    if (!mContactRepository.is_exist(contactId)) {
                        mContactRepository.insert(contact);
                    }
                }
                cursorContact.close();
            }
        } catch (Exception e) {
            MyLog.e(e);
        }

    }

    private void setContactObserver() {
        try {

            getActivity().getApplication().
                    getContentResolver().
                    registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                            true,
                            new ContactContentObserver(new Handler(), getActivity()));
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