package com.example.contact_list.database;

public class DataBaseSchema {
    public static final String Name = "contactManager.db";
    public static int version = 1;

    public class ContactTable {
        public static final String Name = "contactTable";

        public class ContactCols {
            public static final String contactId = "contactID";
            public static final String displayName = "displayName";
            public static final String phoneNO = "phoneNO";
        }
    }

}
