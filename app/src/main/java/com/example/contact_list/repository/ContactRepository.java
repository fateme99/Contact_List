package com.example.contact_list.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.room.Room;

import com.example.contact_list.database.ContactDataBase;
import com.example.contact_list.database.ContactDateBaseDAO;
import com.example.contact_list.model.Contact;
import com.example.contact_list.utils.ApplicationLoader;
import com.example.contact_list.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    private static volatile ContactRepository sInstance;
    private ContactDateBaseDAO mContactDAO;

    private ContactRepository() {
        ContactDataBase contactDataBase = Room.databaseBuilder(ApplicationLoader.sContextApplication,
                ContactDataBase.class,
                "ContactTable.db").
                build();
        mContactDAO = contactDataBase.getContactDataBaseDAO();
    }

    public static ContactRepository getInstance() {
        if (sInstance == null) {
            synchronized (ContactRepository.class) {
                if (sInstance == null) {
                    sInstance = new ContactRepository();
                }
            }
        }
        return sInstance;
    }

    public List<Contact> getContacts() {
        return mContactDAO.getContacts();
    }

    public void deleteAll() {
        mContactDAO.deleteAll();
    }

    public boolean is_exist(String contact_id) {
        if (mContactDAO.getContact(contact_id) == null)
            return false;
        return true;
    }

    public void insert(Contact contact) {
        mContactDAO.insert(contact);
    }

    public static void getAllContacts() {

        ContentResolver contentResolver = ApplicationLoader.sContextApplication.getContentResolver();
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
                                        new String[]{String.valueOf(contactId)},
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
                    if (!getInstance().is_exist(contactId)) {
                        getInstance().insert(contact);
                    }
                }
                cursorContact.close();
            }
        } catch (Exception e) {
            MyLog.e(e);
        }
    }
}
