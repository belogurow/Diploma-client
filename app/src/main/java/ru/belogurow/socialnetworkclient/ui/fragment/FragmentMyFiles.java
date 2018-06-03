package ru.belogurow.socialnetworkclient.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.viewModel.FileViewModel;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.activity.UploadFileActivity;
import ru.belogurow.socialnetworkclient.ui.adapter.FilesAdapter;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class FragmentMyFiles extends Fragment {

    private static final String TAG = FragmentMyFiles.class.getSimpleName();

    private UserViewModel mUserViewModel;
    private FileViewModel mFileViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private FloatingActionButton mAddFileFloatingActionButton;
    private FilesAdapter mFilesAdapter;

    private UserDto currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_files, container, false);

        initFields(view);

        mAddFileFloatingActionButton.setOnClickListener(v -> {
            Intent uploadFileActivity = new Intent(v.getContext(), UploadFileActivity.class);
            startActivity(uploadFileActivity);
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            updateFilesList();
            mSwipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void initFields(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.frag_my_files_swipelayout);
        mRecyclerView = view.findViewById(R.id.frag_my_files_recycler);
        mAddFileFloatingActionButton = view.findViewById(R.id.frag_my_files_floatting);
        mProgressBar = view.findViewById(R.id.frag_my_files_progress);

        mAddFileFloatingActionButton.setImageDrawable(new IconicsDrawable(view.getContext())
                .icon(FontAwesome.Icon.faw_plus)
                .color(Color.WHITE)
                .sizeDp(24));

        mFilesAdapter = new FilesAdapter(getActivity(), false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(mFilesAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);


        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mFileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
    }

    @Override
    public void onStart() {
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
                        Toast.makeText(getContext(), R.string.received_null_data, Toast.LENGTH_LONG).show();
                        hideProgressBar();
                        return;
                    }

                    switch (fileResource.getStatus()) {
                        case SUCCESS:
                            if (fileResource.getData().isEmpty()) {
                                mFilesAdapter.setFileEntityDtos(new ArrayList<>());
                                Toast.makeText(getContext(), R.string.list_is_empty, Toast.LENGTH_SHORT).show();
                            } else {
                                mFilesAdapter.setFileEntityDtos(fileResource.getData());
                            }
                            break;
                        case ERROR:
                            Toast.makeText(getContext(), fileResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getContext(), R.string.unknown_status, Toast.LENGTH_LONG).show();
                    }

                    hideProgressBar();
                });
            }
        });
    }

    private void showProgressBar() {
//        mProgressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgressBar() {
//        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
