package com.example.contact_list.model;

import java.io.Serializable;

public class Contact implements Serializable {
    private String mId;
    private String mNameDisplay;
    private String mPhoneNO;

    public Contact(String ID, String nameDisplay, String phoneNO) {
        mId = ID;
        mNameDisplay = nameDisplay;
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
