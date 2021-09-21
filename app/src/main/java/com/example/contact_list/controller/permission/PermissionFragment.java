package com.example.contact_list.controller.permission;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.contact_list.R;
import com.example.contact_list.utils.Permission;

public class PermissionFragment extends Fragment {
    private LottieAnimationView mLottiePermission;
    private Button mButton_permission;

    public PermissionFragment() {
        // Required empty public constructor
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
        mLottiePermission = view.findViewById(R.id.permission_lottie);
        mButton_permission = view.findViewById(R.id.permission_btn);
    }

    private void setListeners() {
        mButton_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permission.getContactPermission(getActivity(), Manifest.permission.READ_CONTACTS);
            }
        });
    }


}