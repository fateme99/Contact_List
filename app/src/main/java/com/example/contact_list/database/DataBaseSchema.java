package com.example.contact_list.database;

public class DataBaseSchema {
    public static final String Name="contactmanager.db";
    public static int version=1;

    public class ContactTable{
        public static final String Name="contactTable";
        public class ContactCols{
            public static final String contact_id="contactID";
            public static final String display_name="displayName";
            public static final String phone_NO="phoneNO";

        }
    }
    /*public class PhoneTable{
        public static final String Name="phoneTable";
        public class PhoneNOCols{
            public static final String phone_NO="phoneNumber";
            public static final String contact_id="contactID";
        }
    }*/
}
