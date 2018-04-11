package ru.belogurow.socialnetworkclient.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.adapter.UsersListAdapter;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UsersList extends AppCompatActivity {
    private UserViewModel mUserViewModel;

    private RecyclerView mUsersRecyclerView;
    private UsersListAdapter mUsersAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        initFields();

        mUserViewModel.getAllUsers().observe(this, listResource -> {
            mProgressBar.setVisibility(VISIBLE);


            if (listResource != null && listResource.status == NetworkStatus.SUCCESS) {
                mUsersAdapter.setUserList(listResource.data);
            }

            mProgressBar.setVisibility(GONE);
        });
    }

    private void initFields() {
        mUsersRecyclerView = findViewById(R.id.users_list_recyclerView);
        mProgressBar = findViewById(R.id.users_list_progressBar);
        mUsersAdapter = new UsersListAdapter();

        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsersRecyclerView.setAdapter(mUsersAdapter);

        mProgressBar.setVisibility(VISIBLE);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }
}
