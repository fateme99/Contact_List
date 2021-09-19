package com.example.contact_list.controller.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.contact_list.R;
import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;
import com.example.contact_list.utils.ContactContentObserver;
import com.example.contact_list.utils.MyLog;
import com.example.contact_list.utils.Permission;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.contact_list.utils.Permission.REQUEST_CODE_CONTACT_PERMISSION;

public class ContactListActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private ContactRepository mContactRepository;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        findViews();
        mContactRepository = ContactRepository.getInstance(this);
        if (!Permission.hasContactPermission(this, Manifest.permission.READ_CONTACTS)) {
            Permission.getContactPermission(this, Manifest.permission.READ_CONTACTS);
        } else {
            setContactObserver();
            contactGetter();
        }

    }

    private void findViews() {
        mProgressBar = findViewById(R.id.progressbar);
        mFrameLayout = findViewById(R.id.container_fragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CONTACT_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setContactObserver();
            contactGetter();

        } else {
            Toast.makeText(this, R.string.give_permission_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void startFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container_fragment);
        if (fragment == null) {
            fragmentManager.
                    beginTransaction().
                    add(R.id.container_fragment, ContactListFragment.newInstance()).
                    commit();
        }
    }

    private void setContactObserver() {
        try {

            getApplication().
                    getContentResolver().
                    registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                            true,
                            new ContactContentObserver(new Handler(), this));
        } catch (Exception e) {
            MyLog.e(e);
        }
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
    private void notifyToUI(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            mProgressBar.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
        });

    }


    private void getAllContacts() {

        ContentResolver contentResolver = getContentResolver();
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

        startFragment();

    }

}