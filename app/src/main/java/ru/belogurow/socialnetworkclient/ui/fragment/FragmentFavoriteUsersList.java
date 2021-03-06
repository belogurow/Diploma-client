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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.adapter.UsersListAdapter;
import ru.belogurow.socialnetworkclient.users.dto.FavoriteUsersDto;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.FavoriteUsersViewModel;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class FragmentFavoriteUsersList extends Fragment {

    private static final String TAG = FragmentUserList.class.getSimpleName();

    private FavoriteUsersViewModel mFavoriteUsersViewModel;
    private UserViewModel mUserViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mUsersRecyclerView;
    private UsersListAdapter mUsersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favorite_users_list, container, false);

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
        mUserViewModel.userFromDB().observe(this, userDtoResource -> {
            showProgressBar();

            if (userDtoResource != null && userDtoResource.getStatus() == NetworkStatus.SUCCESS) {
                mFavoriteUsersViewModel.getAllFavoriteUsersByUserId(userDtoResource.getData().getId())
                        .observe(this, listResource -> {
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
                                        List<UserDto> users = new ArrayList<>();
                                        for (FavoriteUsersDto favUsersDto : listResource.getData()) {
                                            users.add(favUsersDto.getToUserId());
                                        }
                                        mUsersAdapter.setUserList(users);
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
            } else {
                hideProgressBar();
            }
        });
    }

    private void initFields(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.frag_fav_users_list_swipelayout);

        mUsersRecyclerView = view.findViewById(R.id.frag_fav_users_list_recycler);
        mUsersAdapter = new UsersListAdapter(view.getContext());

        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUsersRecyclerView.setAdapter(mUsersAdapter);
        mUsersRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mUsersRecyclerView.setHasFixedSize(true);

        mFavoriteUsersViewModel = ViewModelProviders.of(this).get(FavoriteUsersViewModel.class);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void hideProgressBar() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showProgressBar() {
        mSwipeRefreshLayout.setRefreshing(true);
    }
}
