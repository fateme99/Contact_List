package com.example.contact_list.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.contact_list.R;

import static com.example.contact_list.utils.Permission.REQUEST_CODE_CONTACT_PERMISSION;
import static com.example.contact_list.utils.Permission.getContactPermission;
import static com.example.contact_list.utils.Permission.hasContactPermission;

public class ContactListActivity extends AppCompatActivity {
    public  static Intent newIntent(Context context){
        Intent intent=new Intent(context,ContactListActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        if (!hasContactPermission(this,Manifest.permission.READ_CONTACTS)) {
            getContactPermission(this,Manifest.permission.READ_CONTACTS);

        } else {
            startFragment();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_CONTACT_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "allowed...", Toast.LENGTH_SHORT).show();
            startFragment();

        }else {
            Toast.makeText(this, "please give permission to show your contacts...", Toast.LENGTH_SHORT).show();
        }
    }
    private void startFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.container_fragment);
        if (fragment==null){
            fragmentManager.
                    beginTransaction().
                    add(R.id.container_fragment, ContactListFragment.newInstance()).
                    commit();
        }
    }
}