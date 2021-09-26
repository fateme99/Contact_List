package com.example.contact_list.view.permission;

import android.Manifest;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.contact_list.R;
import com.example.contact_list.databinding.FragmentPermissionBinding;
import com.example.contact_list.utils.PermissionHelper;

public class PermissionFragment extends Fragment {
    private FragmentPermissionBinding mBinding;

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
        mBinding = DataBindingUtil.
                inflate(inflater, R.layout.fragment_permission, container, false);
        setListeners();
        return mBinding.getRoot();
    }

    private void setListeners() {
        mBinding.permissionBtn.setOnClickListener(view -> PermissionHelper.getContactPermission(getActivity(), Manifest.permission.READ_CONTACTS));
    }
}