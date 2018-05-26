package ru.belogurow.socialnetworkclient.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.UUID;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class AboutUserActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    private ImageView mUserAvatarImageView;
    private TextView mFullnameTextView;
    private TextView mUsernameTextView;
    private Button mAddFriendButton;
    private Button mStartDialogButton;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_user);

        initFields();

        Intent intent = getIntent();
        UUID userId = (UUID) intent.getSerializableExtra(Extras.EXTRA_USER_ID);

        Toast.makeText(this, userId.toString(), Toast.LENGTH_SHORT).show();


        mUserViewModel.getUserById(userId).observe(this, userResource -> {
            if (userResource == null) {
                Toast.makeText(AboutUserActivity.this, "Received null data", Toast.LENGTH_LONG).show();
                return;
            }

            switch (userResource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(AboutUserActivity.this, userResource.getData().toString(), Toast.LENGTH_LONG).show();
                    UserDto currentUser = userResource.getData();

                    mFullnameTextView.setText(currentUser.getName());
                    mUsernameTextView.setText(currentUser.getUsername());


                    // Set avatar image
                    if (currentUser.getUserProfile() != null && currentUser.getUserProfile().getAvatarFile() != null) {
                        setImageWithGlide(currentUser.getUserProfile().getAvatarFile());
                    }

                    break;
                case ERROR:
                    Toast.makeText(AboutUserActivity.this, userResource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(AboutUserActivity.this, "Unknown status", Toast.LENGTH_LONG).show();
            }

//                mProgressBarLogin.setVisibility(View.GONE);
        });

        mStartDialogButton.setOnClickListener(v -> {
            Intent chatRoom = new Intent(v.getContext(), ChatRoomActivity.class);
            chatRoom.putExtra(Extras.EXTRA_USER_ID, userId);
            startActivity(chatRoom);
        });
    }

    private void initFields() {
        View itemUserView = findViewById(R.id.include_about_user);

        mUserAvatarImageView = itemUserView.findViewById(R.id.item_user_avatar_imageView);
        mFullnameTextView = itemUserView.findViewById(R.id.item_user_fullname_textView);
        mUsernameTextView = itemUserView.findViewById(R.id.item_user_username_textView);
        mAddFriendButton = findViewById(R.id.button_add_friend_act_about_user);
        mStartDialogButton = findViewById(R.id.button_start_dialog_act_about_user);

        mToolbar = findViewById(R.id.toolbar_act_about_user);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_about_user);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Finish activity when press back button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
