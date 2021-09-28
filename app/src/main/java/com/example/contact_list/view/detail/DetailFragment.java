package com.example.contact_list.view.detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.contact_list.R;
import com.example.contact_list.databinding.FragmentDetailBinding;
import com.example.contact_list.model.Contact;
import com.example.contact_list.viewModel.DetailViewModel;

public class DetailFragment extends DialogFragment {
    private FragmentDetailBinding mBinding;
    private static final String ARGS_CONTACT = "contact";
    Contact mContact;

    public DetailFragment() {

    }

    public static DetailFragment newInstance(Contact contact) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContact = (Contact) getArguments().getSerializable(ARGS_CONTACT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, null, false);
        mBinding.setDetailViewModel(new DetailViewModel(mContact, this));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.contact_detail)
                .setView(mBinding.getRoot());
        AlertDialog dialog = builder.create();
        return dialog;
    }

}