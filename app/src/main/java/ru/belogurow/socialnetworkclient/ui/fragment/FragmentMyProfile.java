package ru.belogurow.socialnetworkclient.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.activity.LoginActivity;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class FragmentMyProfile extends Fragment {

    private UserViewModel mUserViewModel;

    private ImageView mUserAvatarImageView;
    private TextView mFullnameTextView;
    private TextView mUsernameTextView;
    private Button mLogOutButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

        initFields(view);
        subscribeProfile();

        return view;
    }

    private void initFields(View view) {
        mUserAvatarImageView = view.findViewById(R.id.frag_user_profile_avatar_imageView);
        mFullnameTextView = view.findViewById(R.id.frag_user_profile_fullname_textView);
        mUsernameTextView = view.findViewById(R.id.frag_user_profile_username_textView);
        mLogOutButton = view.findViewById(R.id.frag_user_profile_logout_button);

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.deleteAllDB();

                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

    private void subscribeProfile() {
        mUserViewModel.userFromDB().observe(this, userResource -> {
            // TODO: 24.05.2018 INIT PROGRESS BAR

            if (userResource != null && userResource.getStatus() == NetworkStatus.SUCCESS) {
                User user = userResource.getData();

                mFullnameTextView.setText(user.getName());
                mUsernameTextView.setText(user.getUsername());
            }

            // TODO: 24.05.2018 CANCEL PROGRESS BAR
//            mProgressBar.setVisibility(GONE);
        });
    }
}
