package com.example.contact_list.view.permission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.contact_list.R;
import com.example.contact_list.view.splash.SplashActivity;
import com.example.contact_list.utils.PermissionHelper;

import static com.example.contact_list.utils.PermissionHelper.REQUEST_CODE_CONTACT_PERMISSION;

public class PermissionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);
        if (PermissionHelper.hasContactPermission(this, Manifest.permission.READ_CONTACTS)) {
            startSplashActivity();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.container_fragment_activity);
            if (fragment == null) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.container_fragment_activity, PermissionFragment.newInstance())
                        .commit();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CONTACT_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startSplashActivity();
        }
    }

    private void startSplashActivity() {
        Intent intent = SplashActivity.newIntent(this);
        startActivity(intent);
        finish();
    }

}