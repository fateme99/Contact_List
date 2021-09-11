package com.example.contact_list.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.contact_list.database.ContactDBHelper;
import com.example.contact_list.database.DataBaseSchema;
import com.example.contact_list.model.Contact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ContactRepository {

    private static ContactRepository sInstance;
    private SQLiteDatabase mDatabase;
    private Context mContext;
    private ContactRepository(Context context) {
        mContext=context.getApplicationContext();
        ContactDBHelper contactDBHelper=new ContactDBHelper(mContext);
        mDatabase=contactDBHelper.getWritableDatabase();
    }

    public static ContactRepository getInstance(Context context) {
        if (sInstance==null)
            sInstance=new ContactRepository(context);
        return sInstance;
    }


    public List<Contact> getContacts(){
        List<Contact>contacts=new ArrayList<>();
        ContactCursorWrapper contactCursorWrapper = getContactCursorWrapper(null, null);
        if (contactCursorWrapper==null || contactCursorWrapper.getCount()==0)
            return contacts;
        try{
            contactCursorWrapper.moveToFirst();
            while (!contactCursorWrapper.isAfterLast()){
                Contact contact=contactCursorWrapper.getContact();

                contacts.add(contact);
                contactCursorWrapper.moveToNext();
            }
        }finally {
            contactCursorWrapper.close();
        }
        return contacts;
    }





    public Contact getContact(String contact_id){
        String selection= DataBaseSchema.ContactTable.ContactCols.contact_id+" =? ";
        String[] selectionArgs=new String[]{contact_id};
        ContactCursorWrapper contactCursorWrapper= getContactCursorWrapper(selection,selectionArgs);
        if (contactCursorWrapper==null     ||  contactCursorWrapper.getCount()==0)
            return null;
        try {
            contactCursorWrapper.moveToFirst();
            return contactCursorWrapper.getContact();
        }finally {
            contactCursorWrapper.close();;
        }
    }

    public void updateContact(Contact contact) {
        String whereClause = DataBaseSchema.ContactTable.ContactCols.contact_id + " =? ";
        String[] whereArgs = new String[]{contact.getID()};
        mDatabase.update(DataBaseSchema.ContactTable.Name, getContentValues(contact), whereClause, whereArgs);
    }


    public void deleteContact(String id){
        String whereClause= DataBaseSchema.ContactTable.ContactCols.contact_id+" =? ";
        String[] whereArgs=new String[]{id};
        mDatabase.delete(DataBaseSchema.ContactTable.Name,whereClause,whereArgs);
    }


    public void insert (Contact contact){
        ContentValues values = getContentValues(contact);

        mDatabase.insert(DataBaseSchema.ContactTable.Name, null, values);

    }


    private ContentValues getContentValues(Contact contact) {
        ContentValues values=new ContentValues();
        values.put(DataBaseSchema.ContactTable.ContactCols.contact_id,contact.getID());
        values.put(DataBaseSchema.ContactTable.ContactCols.display_name,contact.getName_Display());
        values.put(DataBaseSchema.ContactTable.ContactCols.phone_NO,contact.getPhone_NO());

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

    /*public File getPhotoFile(Contact task){
        File fileDir=mContext.getFilesDir();
        File file=new File(fileDir,task.getPhotoFileName());
        return file;
    }*/

}
