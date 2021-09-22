package com.example.contact_list.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ContactTable")
public class Contact implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Id")
    private String mId;
    @ColumnInfo(name = "DisplayName")
    private String mNameDisplay;
    @ColumnInfo(name = "PhoneNumber")
    private String mPhoneNO;

    public Contact() {
    }

    public Contact(String ID, String nameDisplay, String phoneNO) {
        mId = ID;
        mNameDisplay = nameDisplay;
        mPhoneNO = phoneNO;
    }

    public void setNameDisplay(String nameDisplay) {
        mNameDisplay = nameDisplay;
    }

    public void setPhoneNO(String phoneNO) {
        mPhoneNO = phoneNO;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPhoneNO() {
        return mPhoneNO;
    }

    public String getNameDisplay() {
        return mNameDisplay;
    }

}
