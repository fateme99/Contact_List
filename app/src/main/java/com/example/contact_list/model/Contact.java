package com.example.contact_list.model;

import java.util.List;

public class Contact {
    private String mID;
    private String mName_Display;
    private List<String> mPhone_NOs;
    // TODO: 9/10/2021 complete this model


    public Contact(String ID, String name_Display, List<String> phone_NOs) {
        mID = ID;
        mName_Display = name_Display;
        mPhone_NOs = phone_NOs;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public List<String> getPhone_NOs() {
        return mPhone_NOs;
    }

    public void setPhone_NOs(List<String> phone_NOs) {
        mPhone_NOs = phone_NOs;
    }

    public String getName_Display() {
        return mName_Display;
    }

    public void setName_Display(String name_Display) {
        mName_Display = name_Display;
    }
}
