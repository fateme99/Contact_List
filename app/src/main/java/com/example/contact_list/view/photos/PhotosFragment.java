package com.example.contact_list.view.photos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.contact_list.R;

public class PhotosFragment extends DialogFragment {
    private ImageView mImageView_first, mImageView_second, mImageView_third;

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
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_photos, null);
        findViews(view);
        setValues();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).
                setTitle(R.string.photos_title).setView(view);
        return builder.create();
    }

    private void findViews(View view) {
        mImageView_first = view.findViewById(R.id.image1);
        mImageView_second = view.findViewById(R.id.image2);
        mImageView_third = view.findViewById(R.id.image3);
    }

    private void setValues() {
        String image1Url = getString(R.string.image1_url);
        Glide.with(getActivity())
                .load(image1Url)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_internet)
                .error(R.drawable.ic_internet_error)
                .into(mImageView_first);
        String image2Url = getString(R.string.image2_url);
        Glide.with(getActivity())
                .load(image2Url)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_internet)
                .error(R.drawable.ic_internet_error)
                .into(mImageView_second);
        String image3Url = getString(R.string.image3_url);
        Glide.with(getActivity())
                .load(image3Url)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_internet)
                .error(R.drawable.ic_internet_error)
                .into(mImageView_third);


    }
}