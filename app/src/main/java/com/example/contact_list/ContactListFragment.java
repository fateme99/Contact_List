package com.example.contact_list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contact_list.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment {
    private RecyclerView mRecyclerView_contact;
    private ContactAdapter mContactAdapter;
    private List<Contact>mContacts;

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
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_list, container, false);
        findViews(view);
        initView();
        return view;
    }
    private void findViews(View view){
        mRecyclerView_contact=view.findViewById(R.id.contact_list_recycler_view);
    }
    private void initView(){
        mRecyclerView_contact.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateView();

    }
    private void updateView(){
        mContacts=new ArrayList<>();
        mContacts.add(new Contact("fateme"));
        mContacts.add(new Contact("zahra"));
        mContacts.add(new Contact("mina"));

        if (mContactAdapter==null){
            mContactAdapter=new ContactAdapter(mContacts);
            mRecyclerView_contact.setAdapter(mContactAdapter);
        }
        else {
            mContactAdapter.notifyDataSetChanged();
        }

    }


    private class ContactHolder extends RecyclerView.ViewHolder{
        private TextView mTextView_display_name;
        private Contact mContact;
        public ContactHolder( View itemView) {
            super(itemView);
            mTextView_display_name=itemView.findViewById(R.id.display_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 9/10/2021 show details
                }
            });
        }
        public void bindContact(Contact contact){
            mContact=contact;
            mTextView_display_name.setText(mContact.getName_Display());
        }
    }
    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder>{
        private List<Contact>mContacts;

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
        public ContactHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity()).
                    inflate(R.layout.contact_item,parent,false);
            ContactHolder contactHolder=new ContactHolder(view);
            return contactHolder;
        }

        @Override
        public void onBindViewHolder( ContactListFragment.ContactHolder holder, int position) {
            holder.bindContact(mContacts.get(position));
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }


}