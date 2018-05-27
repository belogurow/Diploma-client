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
import ru.belogurow.socialnetworkclient.chat.viewModel.ChatViewModel;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.adapter.DialogsAdapter;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FragmentDialogs extends Fragment {

    private static final String TAG = FragmentDialogs.class.getSimpleName();

    private UserViewModel mUserViewModel;
    private ChatViewModel mChatViewModel;

    private RecyclerView mRecyclerView;
    private DialogsAdapter mDialogsAdapter;
    private ProgressBar mProgressBar;

    private UserDto currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        initFields(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        mUserViewModel.userFromDB().observe(this, userResource -> {
            mProgressBar.setVisibility(VISIBLE);

            if (userResource != null && userResource.getStatus() == NetworkStatus.SUCCESS) {
                currentUser = userResource.getData();

                mChatViewModel.getAllChatsByUserId(currentUser.getId()).observe(this, chatResource -> {
                    if (chatResource == null) {
                        Toast.makeText(getContext(), R.string.received_null_data, Toast.LENGTH_LONG).show();
                        return;
                    }

                    switch (chatResource.getStatus()) {
                        case SUCCESS:
                            if (chatResource.getData().isEmpty()) {
                                Toast.makeText(getContext(), R.string.list_is_empty, Toast.LENGTH_SHORT).show();
                            } else {
                                mDialogsAdapter.setChatList(chatResource.getData());
                                mDialogsAdapter.setCurrentUser(currentUser);
                            }
                            break;
                        case ERROR:
                            Toast.makeText(getContext(), chatResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getContext(), R.string.unknown_status, Toast.LENGTH_LONG).show();
                    }

                });
            }
            mProgressBar.setVisibility(GONE);
        });

    }

    private void initFields(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_frag_user_list);
        mProgressBar = view.findViewById(R.id.progress_frag_user_list);
        mDialogsAdapter = new DialogsAdapter(view.getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(mDialogsAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL));

        mChatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }
}
