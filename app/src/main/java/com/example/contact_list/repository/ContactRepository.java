package com.example.contact_list.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.contact_list.database.ContactDBHelper;
import com.example.contact_list.database.DataBaseSchema;
import com.example.contact_list.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {

    private static ContactRepository sInstance;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private ContactRepository(Context context) {
        mContext = context.getApplicationContext();
        ContactDBHelper contactDBHelper = new ContactDBHelper(mContext);
        mDatabase = contactDBHelper.getWritableDatabase();
    }

    public static ContactRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new ContactRepository(context);
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

    public Contact getContact(String contact_id) {
        String selection = DataBaseSchema.ContactTable.ContactCols.contactId + " =? ";
        String[] selectionArgs = new String[]{contact_id};
        ContactCursorWrapper contactCursorWrapper = getContactCursorWrapper(selection, selectionArgs);
        if (contactCursorWrapper == null || contactCursorWrapper.getCount() == 0)
            return null;
        try {
            contactCursorWrapper.moveToFirst();
            return contactCursorWrapper.getContact();
        } finally {
            contactCursorWrapper.close();
            ;
        }
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

    public void updateContact(Contact contact) {
        String whereClause = DataBaseSchema.ContactTable.ContactCols.contactId + " =? ";
        String[] whereArgs = new String[]{contact.getId()};
        mDatabase.update(DataBaseSchema.ContactTable.Name, getContentValues(contact), whereClause, whereArgs);
    }

    public void deleteContact(String id) {
        String whereClause = DataBaseSchema.ContactTable.ContactCols.contactId + " =? ";
        String[] whereArgs = new String[]{id};
        mDatabase.delete(DataBaseSchema.ContactTable.Name, whereClause, whereArgs);
    }

    public void insert(Contact contact) {
        ContentValues values = getContentValues(contact);
        mDatabase.insert(DataBaseSchema.ContactTable.Name, null, values);
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
