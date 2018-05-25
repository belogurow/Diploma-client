package ru.belogurow.socialnetworkclient.ui.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.model.FileEntity;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.chat.viewModel.FileViewModel;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.activity.LoginActivity;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class FragmentMyProfile extends Fragment {

    private final int PICK_PHOTO_FOR_AVATAR = 1;

    private UserViewModel mUserViewModel;
    private FileViewModel mFileViewModel;

    private ImageView mUserAvatarImageView;
    private TextView mFullnameTextView;
    private TextView mUsernameTextView;
    private Button mLogOutButton;

    private User currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mFileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);

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
                currentUser = userResource.getData();

                mFullnameTextView.setText(currentUser.getName());
                mUsernameTextView.setText(currentUser.getUsername());
            }

            // TODO: 24.05.2018 CANCEL PROGRESS BAR
//            mProgressBar.setVisibility(GONE);
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

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileType(FileType.IMAGE);

            File avatarFile = openPath(data.getData());
            if (currentUser != null) {
                mFileViewModel.uploadAvatar(currentUser.getIdAsUUID(), fileEntity, avatarFile).observe(this, avatarResource -> {
                    // TODO: 25.05.2018 INIT PROGRESS BAR
                    if (avatarResource == null) {
                        Toast.makeText(getActivity(), "Received null data", Toast.LENGTH_LONG).show();
                        return;
                    }

                    switch (avatarResource.getStatus()) {
                        case SUCCESS:
                            // TODO: 25.05.2018 UPDATE AVATAR IMAGE
                            Toast.makeText(getActivity(), "Ok", Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR:
                            Toast.makeText(getActivity(), avatarResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "Unknown status", Toast.LENGTH_LONG).show();
                    }


                    // TODO: 25.05.2018 CANCEL PROGRESS BAR
                });
            }

        }
    }

    public File openPath(Uri uri){
        File result = new File(getActivity().getFilesDir() + File.separator + "temp");
        try {

            InputStream is = getActivity().getContentResolver().openInputStream(uri);
            //Convert your stream to data here

            result.createNewFile();
            FileUtils.copyInputStreamToFile(is, result);
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
}
