package ru.belogurow.socialnetworkclient.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.viewModel.FileViewModel;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.adapter.FilesAdapter;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class PickFileActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    private FileViewModel mFileViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FilesAdapter mFilesAdapter;
    private Toolbar mToolbar;

    private UserDto currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_file);

        initFields();
    }

    private void initFields() {
        mSwipeRefreshLayout = findViewById(R.id.act_pick_file_swipelayout);

        mToolbar = findViewById(R.id.act_pick_file_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.pick_file);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mFilesAdapter = new FilesAdapter(this, true);

        mRecyclerView = findViewById(R.id.act_pick_file_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFilesAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);


        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mFileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateFilesList();
    }

    private void updateFilesList() {
        mUserViewModel.userFromDB().observe(this, userDtoResource -> {
            showProgressBar();

            if (userDtoResource != null && userDtoResource.getStatus() == NetworkStatus.SUCCESS) {
                currentUser = userDtoResource.getData();

                mFileViewModel.getAllFilesByUserId(currentUser.getId()).observe(this, fileResource -> {
                    if (fileResource == null) {
                        Toast.makeText(this, R.string.received_null_data, Toast.LENGTH_LONG).show();
                        hideProgressBar();
                        return;
                    }

                    switch (fileResource.getStatus()) {
                        case SUCCESS:
                            if (fileResource.getData().isEmpty()) {
                                Toast.makeText(this, R.string.list_is_empty, Toast.LENGTH_SHORT).show();
                            } else {
                                mFilesAdapter.setFileEntityDtos(fileResource.getData());
                            }
                            break;
                        case ERROR:
                            Toast.makeText(this, fileResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(this, R.string.unknown_status, Toast.LENGTH_LONG).show();
                    }

                    hideProgressBar();
                });
            }
        });
    }

    private void showProgressBar() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgressBar() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
