package com.example.contact_list.controller.detail;

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
    private TextView mTextViewId, mTextViewDisplayName, mTextViewPhone;
    private Button mButtonOk;
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
                .setTitle(R.string.contact_detail)
                .setView(view);
        AlertDialog dialog=builder.create();
        return dialog;
    }
    private void findViews(View view){
        mTextViewId =view.findViewById(R.id.contact_id_txt);
        mTextViewDisplayName =view.findViewById(R.id.contact_display_name_txt);
        mTextViewPhone =view.findViewById(R.id.contact_phone_number);
        mButtonOk =view.findViewById(R.id.ok_btn);
    }
    private void setValues(){
        mTextViewId.setText(mContact.getId());
        mTextViewDisplayName.setText(mContact.getNameDisplay());
        mTextViewPhone.setText(mContact.getPhoneNO());
    }
    private void setListeners(){
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }
}