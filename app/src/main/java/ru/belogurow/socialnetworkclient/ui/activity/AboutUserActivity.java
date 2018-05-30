package ru.belogurow.socialnetworkclient.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.UUID;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class AboutUserActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mUserAvatarImageView;
    private TextView mFullnameTextView;
    private TextView mUsernameTextView;
    private Button mAddFriendButton;
    private Button mStartDialogButton;
    private Toolbar mToolbar;

    private Drawable defaultUserIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_user);

        initFields();
        showProgressBar();

        Intent intent = getIntent();
        UUID userId = (UUID) intent.getSerializableExtra(Extras.EXTRA_USER_ID);

        if (userId != null) {
            loadUserInfo(userId);
            loadUserFromDB(userId);
        }

        mStartDialogButton.setOnClickListener(v -> {
            Intent chatRoom = new Intent(v.getContext(), ChatRoomActivity.class);
            chatRoom.putExtra(Extras.EXTRA_USER_ID, userId);
            startActivity(chatRoom);
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadUserInfo(userId);
            hideProgressBar();
        });
    }

    private void loadUserFromDB(UUID userId) {
        mUserViewModel.userFromDB().observe(this, userDtoResource -> {
            if (userDtoResource != null && !userDtoResource.getData().getId().equals(userId)) {
                mAddFriendButton.setEnabled(true);
                mStartDialogButton.setEnabled(true);
            }
        });
    }

    private void loadUserInfo(UUID userId) {
        mUserViewModel.getUserById(userId).observe(this, userResource -> {
            showProgressBar();

            if (userResource == null) {
                Toast.makeText(AboutUserActivity.this, R.string.received_null_data, Toast.LENGTH_LONG).show();
                hideProgressBar();
                return;
            }

            switch (userResource.getStatus()) {
                case SUCCESS:
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
                    Toast.makeText(AboutUserActivity.this, R.string.received_null_data, Toast.LENGTH_LONG).show();
            }

            hideProgressBar();
        });
    }

    private void initFields() {
        View itemUserView = findViewById(R.id.include_about_user);

        defaultUserIcon = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_user_circle2)
                .color(getResources().getColor(R.color.md_grey_500))
                .sizeDp(48);

        mUserAvatarImageView = itemUserView.findViewById(R.id.item_user_avatar_imageView);
        mUserAvatarImageView.setImageDrawable(defaultUserIcon);

        mSwipeRefreshLayout = findViewById(R.id.act_about_user_swipelayout);
        mFullnameTextView = itemUserView.findViewById(R.id.item_user_fullname_textView);
        mUsernameTextView = itemUserView.findViewById(R.id.item_user_username_textView);
        mAddFriendButton = findViewById(R.id.button_add_friend_act_about_user);
        mAddFriendButton.setEnabled(false);
        mStartDialogButton = findViewById(R.id.button_start_dialog_act_about_user);
        mStartDialogButton.setEnabled(false);

        mToolbar = findViewById(R.id.toolbar_act_about_user);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_about_user);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void setImageWithGlide(FileEntityDto avatarFile) {
        if (avatarFile.getFileType().equals(FileType.JPG)) {
            GlideApp.with(this)
                    .load(App.BASE_URL + avatarFile.getDataUrl())
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .signature(new ObjectKey(avatarFile.getDataUrl()))
                    .error(defaultUserIcon)
                    .into(mUserAvatarImageView);
        }
    }

    private void hideProgressBar() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showProgressBar() {
        mSwipeRefreshLayout.setRefreshing(true);
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
