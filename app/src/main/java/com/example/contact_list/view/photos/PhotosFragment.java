package com.example.contact_list.view.photos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.contact_list.R;
import com.example.contact_list.databinding.FragmentPermissionBinding;
import com.example.contact_list.databinding.FragmentPhotosBinding;

public class PhotosFragment extends DialogFragment {
    private FragmentPhotosBinding mBinding;

    public PhotosFragment() {

    }

    public static PhotosFragment newInstance() {
        PhotosFragment fragment = new PhotosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mBinding= DataBindingUtil.
                inflate(LayoutInflater.from(getContext()),
                        R.layout.fragment_photos,
                        null,
                        false);
        setValues();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).
                setTitle(R.string.photos_title).setView(mBinding.getRoot());
        return builder.create();
    }

    private void setValues() {
        String image1Url = getString(R.string.image1_url);
        Glide.with(getActivity())
                .load(image1Url)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_internet)
                .error(R.drawable.ic_internet_error)
                .into(mBinding.image1);
        String image2Url = getString(R.string.image2_url);
        Glide.with(getActivity())
                .load(image2Url)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_internet)
                .error(R.drawable.ic_internet_error)
                .into(mBinding.image2);
        String image3Url = getString(R.string.image3_url);
        Glide.with(getActivity())
                .load(image3Url)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_internet)
                .error(R.drawable.ic_internet_error)
                .into(mBinding.image3);


    }
}