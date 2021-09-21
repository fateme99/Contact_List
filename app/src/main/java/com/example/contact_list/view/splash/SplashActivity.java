package com.example.contact_list.view.splash;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.example.contact_list.view.SingleFragmentActivity;

public class SplashActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SplashFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }
}