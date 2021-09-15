package com.example.contact_list.controller.list;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.contact_list.utils.MyLog;
import com.example.contact_list.R;
import com.example.contact_list.controller.detail.DetailFragment;
import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ContactListFragment extends Fragment {
    private static final String TAG_FRAGMENT_DETAIL = "detailContact";
    private RecyclerView mRecyclerViewContact;
    private ProgressBar mProgressBar;
    private ContactAdapter mContactAdapter;
    private List<Contact> mContacts;
    private ContactRepository mContactRepository;
    private FrameLayout mFrameLayoutRecycler;
    private LinearLayout mLayoutEmpty;

    public ContactListFragment() {

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
        contactGetter();
        /*ContactGetter contactGetter = new ContactGetter();
        contactGetter.execute();*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        findViews(view);
        initView();
        return view;
    }

    private void findViews(View view) {
        mRecyclerViewContact = view.findViewById(R.id.contact_list_recycler_view);
        mLayoutEmpty = view.findViewById(R.id.empty_layout);
        mFrameLayoutRecycler = view.findViewById(R.id.recyclerLayout);
        mProgressBar = view.findViewById(R.id.progressbar);
    }

    private void initView() {
        mRecyclerViewContact.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateView();

    }

    private void updateView() {
        mContactRepository = ContactRepository.getInstance(getActivity());
        mContacts = mContactRepository.getContacts();
        if (mContacts.size() == 0 && mProgressBar.getVisibility() == View.GONE) {
            mLayoutEmpty.setVisibility(View.VISIBLE);
            mFrameLayoutRecycler.setVisibility(View.GONE);
        } else if (mProgressBar.getVisibility() == View.GONE) {
            mLayoutEmpty.setVisibility(View.GONE);
            mFrameLayoutRecycler.setVisibility(View.VISIBLE);

            if (mContactAdapter == null) {
                mContactAdapter = new ContactAdapter(mContacts);
                mRecyclerViewContact.setAdapter(mContactAdapter);
            }
            mContactAdapter.setContacts(mContacts);
            mContactAdapter.notifyDataSetChanged();
        }
    }

    private void getAllContacts() {

        ContentResolver contentResolver = getActivity().getContentResolver();
        try {
            Cursor cursorContact = contentResolver.
                    query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);
            if (cursorContact != null && cursorContact.getCount() > 0) {
                cursorContact.moveToFirst();
                while (cursorContact.moveToNext()) {
                    String contactNumber = "";
                    String contactId = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
                    String disPlayName = cursorContact.
                            getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cursorContact.getString
                            (cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor cursorPhone = contentResolver.
                                query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{contactId},
                                        null);
                        if (cursorPhone != null) {
                            while (cursorPhone.moveToNext()) {
                                contactNumber = cursorPhone.getString(cursorPhone.
                                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if (contactNumber != null && contactNumber.length() > 0) {
                                    contactNumber = contactNumber.replace(" ", "");
                                }
                            }
                            cursorPhone.close();
                        }
                    }
                    Contact contact = new Contact(contactId, disPlayName, contactNumber);
                    if (!mContactRepository.is_exist(contactId)) {
                        mContactRepository.insert(contact);
                    }
                }
                cursorContact.close();
            }
        } catch (Exception e) {
            MyLog.e(e);
        }
    }

    private class ContactHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewDisplayName;
        private Contact mContact;

        public ContactHolder(View itemView) {
            super(itemView);
            mTextViewDisplayName = itemView.findViewById(R.id.display_name);
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
            mTextViewDisplayName.setText(mContact.getNameDisplay());
        }
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
        private List<Contact> mContacts;

        public ContactAdapter(List<Contact> contacts) {
            mContacts = contacts;
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
    public void onStop() {
        super.onStop();
        /*mProgressBar.setVisibility(View.VISIBLE);
        mFrameLayoutRecycler.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void contactGetter() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getAllContacts();
            }
        });
        handler.post(() -> {
            mProgressBar.setVisibility(View.GONE);
            updateView();
        });
    }


}