package com.example.contact_list.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contact_list.model.Contact;

import java.util.List;

@Dao
public interface ContactDateBaseDAO {
    @Query("SELECT * FROM `contactTable`")
    List<Contact> getContacts();

    @Query("DELETE FROM `contactTable`")
    void deleteAll();

    @Query("SELECT * FROM `contactTable` WHERE Id =:contactId")
    Contact getContact(String contactId);

    @Insert
    void insert(Contact contact);


}
