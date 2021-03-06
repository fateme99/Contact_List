package com.example.contact_list.view.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_list.R;
import com.example.contact_list.databinding.ContactItemBinding;
import com.example.contact_list.model.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
    private List<Contact> mContacts;
    private FragmentManager mFragmentManager;

    public ContactAdapter(List<Contact> contacts, FragmentManager fragmentManager) {
        mContacts = contacts;
        mFragmentManager = fragmentManager;
    }

    public void setContacts(List<Contact> contacts) {
        mContacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ContactItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.contact_item,
                parent,
                false);
        ContactHolder contactHolder = new ContactHolder(binding, mFragmentManager);
        return contactHolder;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        holder.bindContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}
