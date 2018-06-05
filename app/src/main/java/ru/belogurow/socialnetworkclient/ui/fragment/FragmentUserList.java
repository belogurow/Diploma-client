package ru.belogurow.socialnetworkclient.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.ui.adapter.UsersListAdapter;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;


public class FragmentUserList extends Fragment {

    private static final String TAG = FragmentUserList.class.getSimpleName();

    private UserViewModel mUserViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
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

        mSwipeRefreshLayout.setOnRefreshListener(this::loadUsers);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadUsers();
    }

    private void loadUsers() {
        mUserViewModel.getAllUsers().observe(this, listResource -> {
            showProgressBar();

            if (listResource == null) {
                Toast.makeText(getActivity(), R.string.received_null_data, Toast.LENGTH_LONG).show();
                hideProgressBar();
                return;
            }

            switch (listResource.getStatus()) {
                case SUCCESS:
                    if (listResource.getData().isEmpty()) {
                        mUsersAdapter.setUserList(new ArrayList<>());
                        Toast.makeText(getContext(), R.string.list_is_empty, Toast.LENGTH_SHORT).show();
                    } else {
                        mUsersAdapter.setUserList(listResource.getData());
                    }
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), listResource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getActivity(), R.string.unknown_status, Toast.LENGTH_LONG).show();
            }

            hideProgressBar();
        });
    }

    private void initFields(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.frag_user_list_swipelayout);
        mUsersRecyclerView = view.findViewById(R.id.recycler_frag_user_list);
        mProgressBar = view.findViewById(R.id.progress_frag_user_list);
        mUsersAdapter = new UsersListAdapter(view.getContext());

        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUsersRecyclerView.setAdapter(mUsersAdapter);
        mUsersRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
            DividerItemDecoration.VERTICAL));
        mUsersRecyclerView.setHasFixedSize(true);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void hideProgressBar() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showProgressBar() {
        mSwipeRefreshLayout.setRefreshing(true);
    }
}
