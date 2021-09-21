package com.example.contact_list.controller.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.contact_list.R;
import com.example.contact_list.controller.SingleFragmentActivity;
import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;
import com.example.contact_list.utils.ContactContentObserver;
import com.example.contact_list.utils.MyLog;
import com.example.contact_list.utils.Permission;
import com.example.contact_list.view.ProgressBarView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.contact_list.utils.Permission.REQUEST_CODE_CONTACT_PERMISSION;

public class ContactListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,ContactListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return ContactListFragment.newInstance();
    }
}