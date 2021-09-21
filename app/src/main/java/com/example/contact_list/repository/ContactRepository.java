package com.example.contact_list.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.contact_list.database.ContactDBHelper;
import com.example.contact_list.database.DataBaseSchema;
import com.example.contact_list.model.Contact;
import com.example.contact_list.utils.ApplicationLoader;
import com.example.contact_list.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    private static volatile ContactRepository sInstance;
    private SQLiteDatabase mDatabase;

    private ContactRepository(Context context) {
        ContactDBHelper contactDBHelper = new ContactDBHelper(context);
        mDatabase = contactDBHelper.getWritableDatabase();
    }

    public static ContactRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ContactRepository.class) {
                if (sInstance == null) {
                    sInstance = new ContactRepository(context);
                }
            }
        }
        return sInstance;
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        ContactCursorWrapper contactCursorWrapper = getContactCursorWrapper(null, null);
        if (contactCursorWrapper == null || contactCursorWrapper.getCount() == 0)
            return contacts;
        try {
            contactCursorWrapper.moveToFirst();
            while (!contactCursorWrapper.isAfterLast()) {
                Contact contact = contactCursorWrapper.getContact();
                contacts.add(contact);
                contactCursorWrapper.moveToNext();
            }
        } finally {
            contactCursorWrapper.close();
        }
        return contacts;
    }

    public void deleteAll() {
        mDatabase.delete(DataBaseSchema.ContactTable.Name, null, null);
    }

    public boolean is_exist(String contact_id) {
        String selection = DataBaseSchema.ContactTable.ContactCols.contactId + " =? ";
        String[] selectionArgs = new String[]{contact_id};
        ContactCursorWrapper contactCursorWrapper = getContactCursorWrapper(selection, selectionArgs);
        if (contactCursorWrapper == null || contactCursorWrapper.getCount() == 0)
            return false;
        try {
            contactCursorWrapper.moveToFirst();
            return true;
        } finally {
            contactCursorWrapper.close();
            ;
        }
    }

    public void insert(Contact contact) {
        ContentValues values = getContentValues(contact);
        mDatabase.insert(DataBaseSchema.ContactTable.Name, null, values);
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
                    if (!sInstance.is_exist(contactId)) {
                        sInstance.insert(contact);
                    }
                }
                cursorContact.close();
            }
        } catch (Exception e) {
            MyLog.e(e);
        }

    }

    private ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(DataBaseSchema.ContactTable.ContactCols.contactId, contact.getId());
        values.put(DataBaseSchema.ContactTable.ContactCols.displayName, contact.getNameDisplay());
        values.put(DataBaseSchema.ContactTable.ContactCols.phoneNO, contact.getPhoneNO());
        return values;
    }

    private ContactCursorWrapper getContactCursorWrapper(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(
                DataBaseSchema.ContactTable.Name,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return new ContactCursorWrapper(cursor);
    }
}
