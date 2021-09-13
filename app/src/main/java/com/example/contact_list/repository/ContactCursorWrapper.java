package com.example.contact_list.repository;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.contact_list.database.DataBaseSchema;
import com.example.contact_list.model.Contact;

public class ContactCursorWrapper extends CursorWrapper {
    public ContactCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Contact getContact(){
        String id=getString(getColumnIndex(DataBaseSchema.ContactTable.ContactCols.contact_id));
        String display_name=getString(getColumnIndex(DataBaseSchema.ContactTable.ContactCols.display_name));
        String phone_NO=getString(getColumnIndex(DataBaseSchema.ContactTable.ContactCols.phone_NO));
        Contact contact=new Contact(id,display_name,phone_NO);
        return contact;
    }
}
