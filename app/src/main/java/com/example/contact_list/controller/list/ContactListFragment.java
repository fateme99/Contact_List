package com.example.contact_list.controller.list;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.example.contact_list.controller.detail.DetailFragment;
import com.example.contact_list.controller.photos.PhotosFragment;
import com.example.contact_list.model.Contact;
import com.example.contact_list.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {
    private static final String TAG_FRAGMENT_DETAIL = "detailContact";
    private static final String TAG_FRAGMENT_PHOTOS = "photosDetail";
    private RecyclerView mRecyclerViewContact;
    private ContactAdapter mContactAdapter;
    private List<Contact> mContacts;
    private ContactRepository mContactRepository;
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
        mContactRepository = ContactRepository.getInstance(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        View view = setViewDynamically();
        //findViews(view);
        initView();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu,
                                    @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.photos_fragment_contact_list,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_photos:
                PhotosFragment photosFragment=PhotosFragment.newInstance();
                photosFragment.show(getActivity().getSupportFragmentManager(),TAG_FRAGMENT_PHOTOS);

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

    private void findViews(View view) {
        mRecyclerViewContact = view.findViewById(R.id.contact_list_recycler_view);
        mLayoutEmpty = view.findViewById(R.id.empty_layout);
        mFrameLayoutRecycler = view.findViewById(R.id.recyclerLayout);

    }

    private void initView() {
        mRecyclerViewContact.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateView();

    }

    private void updateView() {
        mContactRepository = ContactRepository.getInstance(getActivity());
        mContacts = mContactRepository.getContacts();
        if (mContacts.size() == 0) {
            mLayoutEmpty.setVisibility(View.VISIBLE);
            mFrameLayoutRecycler.setVisibility(View.GONE);
        } else {
            mLayoutEmpty.setVisibility(View.GONE);
            mFrameLayoutRecycler.setVisibility(View.VISIBLE);

            if (mContactAdapter == null) {
                mContactAdapter = new ContactAdapter(mContacts);
                mRecyclerViewContact.setAdapter(mContactAdapter);
            }
            mContactAdapter.setContacts(mContacts);
            mContactAdapter.notifyDataSetChanged();
        }
    }

    private class ContactHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewDisplayName;
        private Contact mContact;

        public ContactHolder(View itemView) {
            super(itemView);
            mTextViewDisplayName = itemView.findViewById(R.id.display_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailFragment detailFragment = DetailFragment.newInstance(mContact);
                    detailFragment.show(getActivity().getSupportFragmentManager(), TAG_FRAGMENT_DETAIL);
                }
            });
        }

        public void bindContact(Contact contact) {
            mContact = contact;
            mTextViewDisplayName.setText(mContact.getNameDisplay());
        }
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
        private List<Contact> mContacts;

        public ContactAdapter(List<Contact> contacts) {
            mContacts = contacts;
        }

        public void setContacts(List<Contact> contacts) {
            mContacts = contacts;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.contact_item, parent, false);
            ContactHolder contactHolder = new ContactHolder(view);
            return contactHolder;
        }

        @Override
        public void onBindViewHolder(ContactListFragment.ContactHolder holder, int position) {
            holder.bindContact(mContacts.get(position));
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        /*mProgressBar.setVisibility(View.VISIBLE);
        mFrameLayoutRecycler.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }


}