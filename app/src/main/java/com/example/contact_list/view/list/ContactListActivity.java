package com.example.contact_list.view.list;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.example.contact_list.view.SingleFragmentActivity;

public class ContactListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ContactListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return ContactListFragment.newInstance();
    }
}