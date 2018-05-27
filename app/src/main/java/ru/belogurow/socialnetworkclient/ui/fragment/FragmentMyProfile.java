package ru.belogurow.socialnetworkclient.ui.fragment;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileEntity;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.chat.viewModel.FileViewModel;
import ru.belogurow.socialnetworkclient.common.file.FileUtils;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.activity.LoginActivity;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class FragmentMyProfile extends Fragment {

    private final int PICK_PHOTO_FOR_AVATAR = 1;

    private UserViewModel mUserViewModel;
    private FileViewModel mFileViewModel;

    private ImageView mUserAvatarImageView;
    private TextView mFullnameTextView;
    private TextView mUsernameTextView;
    private ProgressBar mProgressBar;
    private Button mLogOutButton;

    private UserDto currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initFields(view);
        subscribeProfile();

        mUserAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.pick_photo_for_avatar));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_PHOTO_FOR_AVATAR);
            }
        });

        return view;
    }

    private void initFields(View view) {
        mUserAvatarImageView = view.findViewById(R.id.frag_user_profile_avatar_imageView);
        mFullnameTextView = view.findViewById(R.id.frag_user_profile_fullname_textView);
        mUsernameTextView = view.findViewById(R.id.frag_user_profile_username_textView);
        mLogOutButton = view.findViewById(R.id.frag_user_profile_logout_button);
        mProgressBar = view.findViewById(R.id.frag_user_profile_progress);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mFileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);

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
        mUserViewModel.userFromDB().observe(this, userBDResource -> {
            mProgressBar.setVisibility(View.VISIBLE);

            if (userBDResource != null && userBDResource.getStatus() == NetworkStatus.SUCCESS) {
                mUserViewModel.getUserById(userBDResource.getData().getId()).observe(this, userDtoResource -> {

                    if (userDtoResource == null) {
                        Toast.makeText(getActivity(), R.string.received_null_data, Toast.LENGTH_LONG).show();
                    }

                    switch (userDtoResource.getStatus()) {
                        case SUCCESS:
                            currentUser = userDtoResource.getData();

                            mFullnameTextView.setText(currentUser.getName());
                            mUsernameTextView.setText(currentUser.getUsername());

                            // Set avatar image
                            if (currentUser.getUserProfile() != null && currentUser.getUserProfile().getAvatarFile() != null) {
                                setImageWithGlide(currentUser.getUserProfile().getAvatarFile());
                            }
                            break;
                        case ERROR:
                            Toast.makeText(getActivity(), userDtoResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), R.string.unknown_status, Toast.LENGTH_LONG).show();
                    }


                });
            }

            mProgressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(), "Cannot pick photo", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgressBar.setVisibility(View.VISIBLE);
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileType(FileType.IMAGE);
            fileEntity.setTitle("Avatar");
            fileEntity.setAuthorId(currentUser.getId());

            File avatarFile = FileUtils.openPath(getContext(), data.getData());
            if (currentUser != null) {
                mFileViewModel.uploadAvatar(currentUser.getId(), fileEntity, avatarFile).observe(this, avatarResource -> {
                    if (avatarResource == null) {
                        Toast.makeText(getActivity(), R.string.received_null_data, Toast.LENGTH_LONG).show();
                        return;
                    }

                    switch (avatarResource.getStatus()) {
                        case SUCCESS:
                            Toast.makeText(getActivity(), "Ok", Toast.LENGTH_SHORT).show();
                            setImageWithGlide(avatarResource.getData());
                            break;
                        case ERROR:
                            Toast.makeText(getActivity(), avatarResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), R.string.unknown_status, Toast.LENGTH_LONG).show();
                    }



                });

            }
            mProgressBar.setVisibility(View.GONE);

        }
    }

    private void setImageWithGlide(FileEntityDto avatarFile) {
        GlideApp.with(this)
                .load(App.BASE_URL + avatarFile.getDataUrl())
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .signature(new ObjectKey(avatarFile.getDataUrl()))
                .into(mUserAvatarImageView);
    }
}
