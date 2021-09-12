package com.example.contact_list.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;

public class ContactContentObserver extends ContentObserver {
    private Context mContext;
    private ContactRepository mContactRepository;
    public ContactContentObserver(Handler handler) {
        super(handler);
    }
    public ContactContentObserver(Handler handler, Context context) {
        super(handler);
        mContext = context;
        mContactRepository=ContactRepository.getInstance(mContext);
    }


    @Override
    public void onChange(boolean selfChange, @Nullable Uri uri) {
        super.onChange(selfChange, uri);
        if (!selfChange){
            updateRepository();
        }
    }
    private void updateRepository(){
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.
                query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

        mContactRepository.deleteAll();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String disPlay_name = cursor.
                        getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                int has_phoneNumber = Integer.parseInt(cursor.
                        getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                List<String> phone_NOs = new ArrayList<>();
                if (has_phoneNumber > 0) {
                    Cursor cursor2 = contentResolver
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                    new String[]{contact_id},
                                    null);

                    cursor2.moveToFirst();
                    while (cursor2.moveToNext()) {
                        String phone_No = cursor2.
                                getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phone_NOs.add(phone_No);
                    }
                    cursor2.close();


                }

                Contact contact = new Contact(contact_id, disPlay_name, "123");
                if (!mContactRepository.is_exist(contact_id)){
                    mContactRepository.insert(contact);
                }


            }
            cursor.close();


        }
        Toast.makeText(mContext, "repository updated...", Toast.LENGTH_SHORT).show();
    }
}
