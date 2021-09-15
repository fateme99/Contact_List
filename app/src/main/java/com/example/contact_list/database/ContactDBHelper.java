package com.example.contact_list.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contact_list.utils.ApplicationLoader;
import com.example.contact_list.database.DataBaseSchema.*;

public class ContactDBHelper extends SQLiteOpenHelper {

    public ContactDBHelper(Context context) {
        super(context, DataBaseSchema.Name, null, DataBaseSchema.version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sbContact = new StringBuilder();
        sbContact.append("CREATE TABLE " + DataBaseSchema.ContactTable.Name + "( ");
        sbContact.append(ContactTable.ContactCols.contactId + " INTEGER PRIMARY KEY ,");
        sbContact.append(ContactTable.ContactCols.displayName + " TEXT ,");
        sbContact.append(ContactTable.ContactCols.phoneNO + " INTEGER ");
        sbContact.append(");");
        sqLiteDatabase.execSQL(sbContact.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
