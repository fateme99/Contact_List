package com.example.contact_list.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.contact_list.R;
import com.example.contact_list.model.Contact;

public class DetailFragment extends DialogFragment {
    private static final String ARGS_CONTACT = "contact";
    private Contact mContact;
    private TextView mTextView_id,mTextView_display_name,mTextView_phone;
    private Button mButton_ok;
    public DetailFragment() {

    }
    public static DetailFragment newInstance(Contact contact) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CONTACT,contact);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContact= (Contact) getArguments().getSerializable(ARGS_CONTACT);
        }
    }
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View view=inflater.inflate(R.layout.fragment_detail,null);
        findViews(view);
        setValues();
        setListeners();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Contact Detail")
                .setView(view);
        AlertDialog dialog=builder.create();
        return dialog;
    }
    private void findViews(View view){
        mTextView_id=view.findViewById(R.id.contact_id_txt);
        mTextView_display_name=view.findViewById(R.id.contact_display_name_txt);
        mTextView_phone=view.findViewById(R.id.contact_phone_number);
        mButton_ok=view.findViewById(R.id.ok_btn);
    }
    private void setValues(){
        mTextView_id.setText(mContact.getID());
        mTextView_display_name.setText(mContact.getName_Display());
        mTextView_phone.setText(mContact.getPhone_NO());
    }
    private void setListeners(){
        mButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }
}