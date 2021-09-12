package com.example.contact_list;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment {
    private static final String TAG_FRAGMENT_DETAIL ="detailContact" ;
    public static final int REQUEST_CODE_CONTACT_PERMISSION = 1;
    private RecyclerView mRecyclerView_contact;
    private ContactAdapter mContactAdapter;
    private List<Contact> mContacts;
    private ContactRepository mContactRepository;
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
        mContactRepository=ContactRepository.getInstance(getActivity());
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
    }

    private void initView() {
        mRecyclerView_contact.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateView();

    }

    private void updateView() {


        if (mContactAdapter == null) {
            mContacts=mContactRepository.getContacts();
            mContactAdapter = new ContactAdapter(mContacts);
            mRecyclerView_contact.setAdapter(mContactAdapter);
        } else {
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

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.
                query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String disPlay_name = cursor.
                        getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                int has_phoneNumber = Integer.parseInt(cursor.
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


                }

                Contact contact = new Contact(contact_id, disPlay_name, "123");
                mContactRepository.insert(contact);

            }
            cursor.close();


        }
        // TODO: 9/10/2021 save in to db
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
                    DetailFragment detailFragment=DetailFragment.newInstance(mContact);
                    detailFragment.show(getActivity().getSupportFragmentManager(),TAG_FRAGMENT_DETAIL);
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


}