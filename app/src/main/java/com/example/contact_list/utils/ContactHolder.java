package com.example.contact_list.utils;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_list.databinding.ContactItemBinding;
import com.example.contact_list.model.Contact;
import com.example.contact_list.viewModel.ContactItemViewModel;

public class ContactHolder extends RecyclerView.ViewHolder {
    private ContactItemBinding mBinding;
    FragmentManager mFragmentManager;

    public ContactHolder(ContactItemBinding contactItemBinding, FragmentManager fragmentManager) {
        super(contactItemBinding.getRoot());
        mBinding = contactItemBinding;
        mFragmentManager = fragmentManager;
    }

    public void bindContact(Contact contact) {
        ContactItemViewModel contactItemViewModel = new ContactItemViewModel(contact,mFragmentManager);
        mBinding.setContactItemViewModel(contactItemViewModel);
    }
}
