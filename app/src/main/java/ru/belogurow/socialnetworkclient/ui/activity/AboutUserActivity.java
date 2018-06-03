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
import android.widget.ToggleButton;

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
import ru.belogurow.socialnetworkclient.users.dto.UserProfileDto;
import ru.belogurow.socialnetworkclient.users.model.FavoriteUsers;
import ru.belogurow.socialnetworkclient.users.viewModel.FavoriteUsersViewModel;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class AboutUserActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    private FavoriteUsersViewModel mFavoriteUsersViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mUserAvatarImageView;
    private TextView mFullnameTextView;
    private TextView mUsernameTextView;
    private Button mAddFriendButton;
    private Button mStartDialogButton;
    private ToggleButton mFavUserToggleButton;
    private Toolbar mToolbar;

    private TextView mRoleTextView;
    private TextView mProfessionTextView;
    private TextView mDescriptionTextView;

    private Drawable defaultUserIcon;
    private Drawable starIcon;
    private Drawable emptyStarIcon;

    private UserDto currentUser;

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
            loadIsFavUser(userId);
            hideProgressBar();
        });

        mFavUserToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mFavUserToggleButton.setBackgroundDrawable(starIcon);
                addUserToFavorite(userId);
            } else {

                mFavUserToggleButton.setBackgroundDrawable(emptyStarIcon);
                deleteUserFromFavorite(userId);
            }
        });

//        mFavUserRatingBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFavUserRatingBar.getRating() == 0) {
//                    mFavUserRatingBar.setRating(1);
//                } else if (mFavUserRatingBar.getRating() == 1) {
//                    mFavUserRatingBar.setRating(0);
//                }
//            }
//        });
//
//        mFavUserRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                if (fromUser) {
//                    if (rating == 1) {
//                        addUserToFavorite(userId);
//                    } else if (rating == 0) {
//                        deleteUserFromFavorite(userId);
//                    }
//                }
//            }
//        });
    }

    private void loadUserFromDB(UUID userId) {
        mUserViewModel.userFromDB().observe(this, userDtoResource -> {
            if (userDtoResource != null) {
                currentUser = userDtoResource.getData();
                loadIsFavUser(userId);

                if (!userDtoResource.getData().getId().equals(userId)) {
                    mAddFriendButton.setEnabled(true);
                    mStartDialogButton.setEnabled(true);
                    mFavUserToggleButton.setVisibility(View.VISIBLE);
                }
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
                    UserDto showedUser = userResource.getData();

                    mFullnameTextView.setText(showedUser.getName());
                    mUsernameTextView.setText(showedUser.getUsername());

                    UserProfileDto showedUserProfile = showedUser.getUserProfile();

                    if (showedUserProfile != null) {
                        mRoleTextView.setText(showedUserProfile.getRole().toString());

                        if (showedUserProfile.getDescription() == null) {
                            mDescriptionTextView.setVisibility(View.GONE);
                        } else {
                            mDescriptionTextView.setVisibility(View.VISIBLE);
                            mDescriptionTextView.setText(showedUserProfile.getDescription());
                        }

                        if (showedUserProfile.getProfession() == null) {
                            mProfessionTextView.setVisibility(View.GONE);
                        } else {
                            mDescriptionTextView.setVisibility(View.VISIBLE);
                            mDescriptionTextView.setText(showedUserProfile.getProfession());
                        }

                        if (showedUserProfile.getAvatarFile() != null) {
                            // Set avatar image
                            setImageWithGlide(showedUser.getUserProfile().getAvatarFile());
                        }
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

    private void loadIsFavUser(UUID userId) {
        mFavoriteUsersViewModel.isFromUserIdHasFavToUserId(currentUser.getId(), userId).observe(this, result -> {
            if (result != null) {
                switch (result.getStatus()) {
                    case SUCCESS:
                        if (result.getData()) {
                            mFavUserToggleButton.setChecked(true);
                            mFavUserToggleButton.setBackgroundDrawable(starIcon);
                        } else {
                            mFavUserToggleButton.setChecked(false);
                            mFavUserToggleButton.setBackgroundDrawable(emptyStarIcon);
                        }
                        break;
                    case ERROR:
                        Toast.makeText(AboutUserActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(AboutUserActivity.this, R.string.received_null_data, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Cannot receive information about favorite user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserToFavorite(UUID userId) {
        FavoriteUsers favoriteUsers = new FavoriteUsers(currentUser.getId(), userId);
        mFavoriteUsersViewModel.addToFavoriteUsers(favoriteUsers).observe(this, favoriteUsersDtoResource -> {
            if (favoriteUsersDtoResource == null) {
                Toast.makeText(this, R.string.received_null_data, Toast.LENGTH_SHORT).show();
                return;
            }

            switch (favoriteUsersDtoResource.getStatus()) {
                case SUCCESS:
//                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(AboutUserActivity.this, favoriteUsersDtoResource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(AboutUserActivity.this, R.string.received_null_data, Toast.LENGTH_LONG).show();
            }

        });
    }

    private void deleteUserFromFavorite(UUID userId) {
        mFavoriteUsersViewModel.deleteFromUserIdFavForToUserId(currentUser.getId(), userId).observe(this, booleanResource -> {
            if (booleanResource == null) {
                Toast.makeText(this, R.string.received_null_data, Toast.LENGTH_SHORT).show();
                return;
            }

            switch (booleanResource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(AboutUserActivity.this, booleanResource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(AboutUserActivity.this, R.string.received_null_data, Toast.LENGTH_LONG).show();
            }

        });
    }

    private void initFields() {
        View itemUserView = findViewById(R.id.include_about_user);

        defaultUserIcon = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_user_circle2)
                .color(getResources().getColor(R.color.md_grey_500))
                .sizeDp(48);

        starIcon = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_star2)
                .color(getResources().getColor(R.color.colorSecondaryLight))
                .sizeDp(48);

        emptyStarIcon = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_star)
                .color(getResources().getColor(R.color.colorSecondaryLight))
                .sizeDp(48);

        mUserAvatarImageView = itemUserView.findViewById(R.id.item_user_avatar_imageView);
        mUserAvatarImageView.setImageDrawable(defaultUserIcon);

        mSwipeRefreshLayout = findViewById(R.id.act_about_user_swipelayout);
        mFullnameTextView = itemUserView.findViewById(R.id.item_user_fullname_textView);
        mUsernameTextView = itemUserView.findViewById(R.id.item_user_username_textView);
        mFavUserToggleButton = itemUserView.findViewById(R.id.item_user_toggle_button);
        mFavUserToggleButton.setVisibility(View.GONE);
        mAddFriendButton = findViewById(R.id.button_add_friend_act_about_user);
        mAddFriendButton.setEnabled(false);
        mStartDialogButton = findViewById(R.id.button_start_dialog_act_about_user);
        mStartDialogButton.setEnabled(false);

        View itemUserProfileView = findViewById(R.id.act_about_user_include_layout);
        mRoleTextView = itemUserProfileView.findViewById(R.id.item_user_profile_role_text);
        mDescriptionTextView = itemUserProfileView.findViewById(R.id.item_user_profile_description_text);
        mProfessionTextView = itemUserProfileView.findViewById(R.id.item_user_profile_profession_text);

        mToolbar = findViewById(R.id.toolbar_act_about_user);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_about_user);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mFavoriteUsersViewModel = ViewModelProviders.of(this).get(FavoriteUsersViewModel.class);
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
