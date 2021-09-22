package com.example.contact_list.view.list;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contact_list.R;
import com.example.contact_list.view.photos.PhotosFragment;
import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ContactListFragment extends Fragment {
    private static final String TAG_FRAGMENT_PHOTOS = "photosDetail";
    private RecyclerView mRecyclerViewContact;
    private ContactAdapter mContactAdapter;
    private List<Contact> mContacts;
    private FrameLayout mFrameLayoutRecycler;
    private LinearLayout mLayoutEmpty;

    public ContactListFragment() {
    }

    public static ContactListFragment newInstance() {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContacts = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = setViewDynamically();
        initView();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu,
                                    @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.photos_fragment_contact_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photos:
                PhotosFragment photosFragment = PhotosFragment.newInstance();
                photosFragment.show(getActivity().getSupportFragmentManager(), TAG_FRAGMENT_PHOTOS);

        }
        return super.onOptionsItemSelected(item);
    }

    private View setViewDynamically() {
        LinearLayout linearLayout = new LinearLayout(getActivity());

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        mFrameLayoutRecycler = new FrameLayout(getActivity());
        mFrameLayoutRecycler.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        mFrameLayoutRecycler.setVisibility(View.GONE);

        mRecyclerViewContact = new RecyclerView(getActivity());
        mRecyclerViewContact.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        mFrameLayoutRecycler.addView(mRecyclerViewContact);
        linearLayout.addView(mFrameLayoutRecycler);

        mLayoutEmpty = new LinearLayout(getActivity());
        mLayoutEmpty.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        mLayoutEmpty.setOrientation(LinearLayout.VERTICAL);
        mLayoutEmpty.setGravity(Gravity.CENTER);
        mLayoutEmpty.setVisibility(View.GONE);
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        imageView.setImageResource(R.drawable.ic_empty_icon);
        mLayoutEmpty.addView(imageView);

        TextView textView = new TextView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        textView.setText(R.string.empty_box);
        textView.setTextSize(24);
        textView.setLayoutParams(params);
        mLayoutEmpty.addView(textView);
        linearLayout.addView(mLayoutEmpty);
        return linearLayout;
    }

    private void initView() {
        mRecyclerViewContact.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateView();

    }

    private void updateView() {
        Executor executor = Executors.newCachedThreadPool();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ContactRepository contactRepository = ContactRepository.getInstance();
                mContacts = contactRepository.getContacts();
                notifyUI();
            }
        };
        executor.execute(runnable);
    }

    private void notifyUI() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            if (mContacts.size() == 0) {
                mLayoutEmpty.setVisibility(View.VISIBLE);
                mFrameLayoutRecycler.setVisibility(View.GONE);
            } else {
                mLayoutEmpty.setVisibility(View.GONE);
                mFrameLayoutRecycler.setVisibility(View.VISIBLE);

                if (mContactAdapter == null) {
                    mContactAdapter = new ContactAdapter(mContacts,
                            getActivity(), getActivity().getSupportFragmentManager());
                    mRecyclerViewContact.setAdapter(mContactAdapter);
                }
                mContactAdapter.setContacts(mContacts);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}