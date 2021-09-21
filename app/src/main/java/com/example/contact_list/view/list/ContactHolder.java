package com.example.contact_list.view.list;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_list.R;
import com.example.contact_list.view.detail.DetailFragment;
import com.example.contact_list.model.Contact;

public class ContactHolder extends RecyclerView.ViewHolder {
    private static final String TAG_FRAGMENT_DETAIL = "detailContact";
    private TextView mTextViewDisplayName;
    private Contact mContact;

    public ContactHolder(View itemView, FragmentManager fragmentManager) {
        super(itemView);
        mTextViewDisplayName = itemView.findViewById(R.id.display_name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFragment detailFragment = DetailFragment.newInstance(mContact);
                detailFragment.show(fragmentManager,TAG_FRAGMENT_DETAIL);
            }
        });
    }

    public void bindContact(Contact contact) {
        mContact = contact;
        mTextViewDisplayName.setText(mContact.getNameDisplay());
    }
}
