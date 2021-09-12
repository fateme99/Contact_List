package com.example.contact_list.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.contact_list.R;

public class ContactListActivity extends AppCompatActivity {

    public  static Intent newIntent(Context context){
        Intent intent=new Intent(context,ContactListActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.container_fragment);
        if (fragment==null){
            fragmentManager.
                    beginTransaction().
                    add(R.id.container_fragment, ContactListFragment.newInstance()).
                    commit();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "on request permission", Toast.LENGTH_SHORT).show();
    }
}