package com.example.contact_list.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;

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
            ContactUpdate contactUpdate=new ContactUpdate();
            contactUpdate.doInBackground();
        }
    }
    private class ContactUpdate extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            updateAllContacts();
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }
    private void updateAllContacts(){
        ContentResolver contentResolver = mContext.getContentResolver();
        mContactRepository.deleteAll();
        try {
            Cursor cursor = contentResolver.
                    query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    String contact_NO="";
                    String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String disPlay_name = cursor.
                            getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cursor.getString
                            (cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                        Cursor cursor1=contentResolver.
                                query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{contact_id},
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
                    if (!mContactRepository.is_exist(contact_id)) {
                        mContactRepository.insert(contact);
                    }
                }
                cursor.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
