package com.example.contact_list.controller.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.contact_list.R;
import com.example.contact_list.utils.ContactContentObserver;
import com.example.contact_list.utils.MyLog;
import com.example.contact_list.utils.Permission;

import static com.example.contact_list.utils.Permission.REQUEST_CODE_CONTACT_PERMISSION;

public class ContactListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        if (!Permission.hasContactPermission(this, Manifest.permission.READ_CONTACTS)) {
            Permission.getContactPermission(this, Manifest.permission.READ_CONTACTS);
        } else {
            startFragment();
            setContactObserver();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CONTACT_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startFragment();
            setContactObserver();

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
                            new ContactContentObserver(new Handler(),this));
        } catch (Exception e) {
            MyLog.e(e);
        }
    }

}