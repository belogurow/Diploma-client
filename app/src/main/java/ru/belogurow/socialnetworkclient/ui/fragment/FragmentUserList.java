package ru.belogurow.socialnetworkclient.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.ui.adapter.UsersListAdapter;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class FragmentUserList extends Fragment {

    private static final String TAG = FragmentUserList.class.getSimpleName();

    private UserViewModel mUserViewModel;

    private RecyclerView mUsersRecyclerView;
    private UsersListAdapter mUsersAdapter;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        initFields(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mUserViewModel.getAllUsers().observe(this, listResource -> {
            mProgressBar.setVisibility(VISIBLE);

            if (listResource == null) {
                Toast.makeText(getActivity(), "Received null data", Toast.LENGTH_LONG).show();
                return;
            }

            switch (listResource.getStatus()) {
                case SUCCESS:
                    mUsersAdapter.setUserList(listResource.getData());
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), listResource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "Unknown status", Toast.LENGTH_LONG).show();
            }

            mProgressBar.setVisibility(GONE);
        });
    }

    private void initFields(View view) {
        mUsersRecyclerView = view.findViewById(R.id.recycler_frag_user_list);
        mProgressBar = view.findViewById(R.id.progress_frag_user_list);
        mUsersAdapter = new UsersListAdapter(view.getContext());

        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUsersRecyclerView.setAdapter(mUsersAdapter);
        mUsersRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
            DividerItemDecoration.VERTICAL));

        mProgressBar.setVisibility(VISIBLE);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }
}
