package com.example.contact_list.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ContactContentObserver extends ContentObserver {

    public ContactContentObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange, @Nullable Uri uri) {
        super.onChange(selfChange, uri);
        if (!selfChange) {
            contactGetter(ApplicationLoader.sContextApplication);
        }
    }

    private void contactGetter(Context context) {
        Runnable runnable = () -> {
            updateAllContacts(context);
            notifyToUI();
        };
        ExecutorHelper.doInOtherThread(runnable);
    }

    private void notifyToUI() {
        ExecutorHelper.getMainHandler().post(() -> {
            // TODO: 9/19/2021 send main thread to show the list
        });
    }

    private void updateAllContacts(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        ContactRepository contactRepository=ContactRepository.getInstance();
        contactRepository.deleteAll();
        try {
            Cursor cursor = contentResolver.
                    query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    String contact_NO = "";
                    String contact_id = String.valueOf(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String disPlay_name = cursor.
                            getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cursor.getString
                            (cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor cursor1 = contentResolver.
                                query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{String.valueOf(contact_id)},
                                        null);
                        if (cursor1 != null) {
                            while (cursor1.moveToNext()) {
                                contact_NO = cursor1.getString(cursor1.
                                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if (contact_NO != null && contact_NO.length() > 0) {
                                    contact_NO = contact_NO.replace(" ", "");
                                }
                            }
                            cursor1.close();
                        }
                    }
                    Contact contact = new Contact(contact_id, disPlay_name, contact_NO);
                    if (!contactRepository.is_exist(contact_id)) {
                        contactRepository.insert(contact);
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            MyLog.e(e);
        }
    }

}
