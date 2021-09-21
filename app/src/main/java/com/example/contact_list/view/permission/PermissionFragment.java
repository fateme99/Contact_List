package com.example.contact_list.view.permission;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.contact_list.R;
import com.example.contact_list.utils.PermissionHelper;

public class PermissionFragment extends Fragment {
    private Button mButton_permission;

    public PermissionFragment() {

    }

    public static PermissionFragment newInstance() {
        PermissionFragment fragment = new PermissionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        findViews(view);
        setListeners();
        return view;
    }

    private void findViews(View view) {
        mButton_permission = view.findViewById(R.id.permission_btn);
    }

    private void setListeners() {
        mButton_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionHelper.getContactPermission(getActivity(), Manifest.permission.READ_CONTACTS);
            }
        });
    }
}