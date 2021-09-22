package com.example.contact_list.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.contact_list.model.Contact;

@Database(entities = Contact.class, version = 1)
public abstract class ContactDataBase extends RoomDatabase {
    public abstract ContactDateBaseDAO getContactDataBaseDAO();
}
