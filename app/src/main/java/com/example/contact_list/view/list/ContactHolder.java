package com.example.contact_list.view.list;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_list.R;
import com.example.contact_list.databinding.ContactItemBinding;
import com.example.contact_list.view.detail.DetailFragment;
import com.example.contact_list.model.Contact;

public class ContactHolder extends RecyclerView.ViewHolder {
    private ContactItemBinding mBinding;
    private static final String TAG_FRAGMENT_DETAIL = "detailContact";
    FragmentManager mFragmentManager;

    public ContactHolder(ContactItemBinding contactItemBinding, FragmentManager fragmentManager) {
        super(contactItemBinding.getRoot());
        mBinding = contactItemBinding;
        mBinding.setContactHolder(this);
        mFragmentManager = fragmentManager;
    }

    public void startDetailFragment() {
        DetailFragment detailFragment = DetailFragment.newInstance(mBinding.getContact());
        detailFragment.show(mFragmentManager, TAG_FRAGMENT_DETAIL);
    }

    public void bindContact(Contact contact) {
        mBinding.setContact(contact);
    }
}
