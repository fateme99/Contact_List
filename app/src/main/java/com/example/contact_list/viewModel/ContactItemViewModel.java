package com.example.contact_list.viewModel;

import androidx.fragment.app.FragmentManager;

import com.example.contact_list.model.Contact;
import com.example.contact_list.view.detail.DetailFragment;

public class ContactItemViewModel {
    private Contact mContact;
    private static final String TAG_FRAGMENT_DETAIL = "detailContact";
    private FragmentManager mFragmentManager;

    public ContactItemViewModel(Contact contact,FragmentManager fragmentManager) {
        mContact = contact;
        mFragmentManager=fragmentManager;
    }

    public Contact getContact() {
        return mContact;
    }

    public void setContact(Contact contact) {
        mContact = contact;
    }

    public void startDetailFragment() {
        DetailFragment detailFragment = DetailFragment.newInstance(mContact);
        detailFragment.show(mFragmentManager, TAG_FRAGMENT_DETAIL);
    }
}
