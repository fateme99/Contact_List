package com.example.contact_list.model;

import java.util.List;

public class Contact {
    private String mID;
    private String mName_Display;
    private String mPhone_NO;
    // TODO: 9/10/2021 complete this model


    public Contact(String ID, String name_Display, String phone_NO) {
        mID = ID;
        mName_Display = name_Display;
        mPhone_NO = phone_NO;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getPhone_NO() {
        return mPhone_NO;
    }

    public void setPhone_NO(String phone_NO) {
        mPhone_NO = phone_NO;
    }

    public String getName_Display() {
        return mName_Display;
    }

    public void setName_Display(String name_Display) {
        mName_Display = name_Display;
    }
}
