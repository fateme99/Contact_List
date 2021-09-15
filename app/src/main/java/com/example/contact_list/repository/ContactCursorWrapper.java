package com.example.contact_list.repository;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.contact_list.database.DataBaseSchema;
import com.example.contact_list.model.Contact;

public class ContactCursorWrapper extends CursorWrapper {
    public ContactCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Contact getContact() {
        String id = getString(getColumnIndex(DataBaseSchema.ContactTable.ContactCols.contactId));
        String displayName = getString(getColumnIndex(DataBaseSchema.ContactTable.ContactCols.displayName));
        String phoneNO = getString(getColumnIndex(DataBaseSchema.ContactTable.ContactCols.phoneNO));
        Contact contact = new Contact(id, displayName, phoneNO);
        return contact;
    }
}
