package com.example.contact_list.controller.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.contact_list.controller.SingleFragmentActivity;

public class SplashActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SplashFragment.newInstance();
    }
    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,SplashActivity.class);
        return intent;
    }
}