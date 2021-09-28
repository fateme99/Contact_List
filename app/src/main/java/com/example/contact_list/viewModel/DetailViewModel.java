package com.example.contact_list.viewModel;

import com.example.contact_list.model.Contact;
import com.example.contact_list.view.detail.DetailFragment;

public class DetailViewModel {
    private Contact mContact;
    private DetailFragment mDetailFragment;
    public DetailViewModel(Contact contact,DetailFragment detailFragment) {
        mContact = contact;
        mDetailFragment=detailFragment;
    }

    public Contact getContact() {
        return mContact;
    }

    public void setContact(Contact contact) {
        mContact = contact;
    }

    public void disMissDialog() {
        mDetailFragment.dismiss();
    }
}
