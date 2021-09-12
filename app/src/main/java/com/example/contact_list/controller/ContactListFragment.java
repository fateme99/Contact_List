package com.example.contact_list.controller;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact_list.R;
import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;
import com.example.contact_list.service.ContactWatchService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment {
    private static final String TAG_FRAGMENT_DETAIL = "detailContact";
    public static final int REQUEST_CODE_CONTACT_PERMISSION = 1;
    private RecyclerView mRecyclerView_contact;
    private ContactAdapter mContactAdapter;
    private List<Contact> mContacts;
    private ContactRepository mContactRepository;
    private FrameLayout mFrameLayout_recycler;
    private LinearLayout mLayout_empty;
    public ContactListFragment() {
        // Required empty public constructor
    }


    public static ContactListFragment newInstance() {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContacts = new ArrayList<>();
        mContactRepository = ContactRepository.getInstance(getActivity());
        if (getArguments() != null) {

        }
        if (!hasContactPermission(Manifest.permission.READ_CONTACTS)) {
            getContactPermission(Manifest.permission.READ_CONTACTS);

        } else {
            getAllContacts();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        findViews(view);
        initView();
        return view;
    }

    private void findViews(View view) {
        mRecyclerView_contact = view.findViewById(R.id.contact_list_recycler_view);
        mLayout_empty =view.findViewById(R.id.empty_layout);
        mFrameLayout_recycler=view.findViewById(R.id.recyclerLayout);
    }

    private void initView() {
        mRecyclerView_contact.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateView();

    }

    private void updateView() {
        mContactRepository = ContactRepository.getInstance(getActivity());
        mContacts = mContactRepository.getContacts();
        if (mContacts.size()==0){
            mLayout_empty.setVisibility(View.VISIBLE);
            mFrameLayout_recycler.setVisibility(View.GONE);
        }
        else {
            mLayout_empty.setVisibility(View.GONE);
            mFrameLayout_recycler.setVisibility(View.VISIBLE);

            if (mContactAdapter == null) {
                mContactAdapter = new ContactAdapter(mContacts);
                mRecyclerView_contact.setAdapter(mContactAdapter);
            }
            mContactAdapter.setContacts(mContacts);
            mContactAdapter.notifyDataSetChanged();
        }

    }

    private boolean hasContactPermission(String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int has_permission = ContextCompat.
                    checkSelfPermission(getActivity().getApplicationContext(), permission);
            if (has_permission == PackageManager.PERMISSION_GRANTED)
                result = true;
        } else {
            result = true;
        }
        return result;
    }

    private void getContactPermission(String permission) {
        String[] permissions = {permission};
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_CONTACT_PERMISSION);
    }

    // TODO: 9/10/2021 check deny request...
    private void getAllContacts() {
        Intent intent = new Intent(getActivity(), ContactWatchService.class);
        getActivity().startService(intent);

        ContentResolver contentResolver = getActivity().getContentResolver();
        try {
            Cursor cursor = contentResolver.
                    query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    String contact_NO="";
                    String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String disPlay_name = cursor.
                            getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    if (Integer.parseInt(cursor.getString
                            (cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                        Cursor cursor1=contentResolver.
                                query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{contact_id},
                                        null);
                        if (cursor1 != null) {
                            while (cursor1.moveToNext()) {
                                contact_NO = cursor1.getString(cursor1.
                                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if (contact_NO != null && contact_NO.length() > 0) {
                                    contact_NO = contact_NO.replace(" ", "");
                                }


                            }
                            cursor1.close();
                        }
                    }
                /*int has_phoneNumber = Integer.parseInt(cursor.
                        getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                List<String> phone_NOs = new ArrayList<>();
                if (has_phoneNumber > 0) {
                    Cursor cursor2 = contentResolver
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                    new String[]{contact_id},
                                    null);

                    cursor2.moveToFirst();
                    while (cursor2.moveToNext()) {
                        String phone_No = cursor2.
                                getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phone_NOs.add(phone_No);
                    }
                    cursor2.close();


                }*/

                    Contact contact = new Contact(contact_id, disPlay_name, contact_NO);
                    if (!mContactRepository.is_exist(contact_id)) {
                        mContactRepository.insert(contact);
                    }


                }
                cursor.close();


            }
        }
            catch (Exception e) {
                e.printStackTrace();
            }
        }




    private class ContactHolder extends RecyclerView.ViewHolder {
        private TextView mTextView_display_name;
        private Contact mContact;

        public ContactHolder(View itemView) {
            super(itemView);
            mTextView_display_name = itemView.findViewById(R.id.display_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailFragment detailFragment = DetailFragment.newInstance(mContact);
                    detailFragment.show(getActivity().getSupportFragmentManager(), TAG_FRAGMENT_DETAIL);
                }
            });
        }

        public void bindContact(Contact contact) {
            mContact = contact;
            mTextView_display_name.setText(mContact.getName_Display());
        }
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
        private List<Contact> mContacts;

        public ContactAdapter(List<Contact> contacts) {
            mContacts = contacts;
        }

        public List<Contact> getContacts() {
            return mContacts;
        }

        public void setContacts(List<Contact> contacts) {
            mContacts = contacts;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.contact_item, parent, false);
            ContactHolder contactHolder = new ContactHolder(view);
            return contactHolder;
        }

        @Override
        public void onBindViewHolder(ContactListFragment.ContactHolder holder, int position) {
            holder.bindContact(mContacts.get(position));
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_CONTACT_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "allowed...", Toast.LENGTH_SHORT).show();
            getAllContacts();
            updateView();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        updateView();
        Toast.makeText(getActivity(), "onresumed update..", Toast.LENGTH_SHORT).show();
    }
}