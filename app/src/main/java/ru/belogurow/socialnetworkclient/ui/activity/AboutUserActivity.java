package ru.belogurow.socialnetworkclient.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class AboutUserActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    private ImageView mUserAvatarImageView;
    private TextView mFullnameTextView;
    private TextView mUsernameTextView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_user);

        initFields();

        Intent intent = getIntent();
        String id = intent.getStringExtra(Extras.EXTRA_USER_ID);

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();


        mUserViewModel.getUserById(id).observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(@Nullable Resource<User> userResource) {
                if (userResource == null) {
                    Toast.makeText(AboutUserActivity.this, "Received null data", Toast.LENGTH_LONG).show();
                    return;
                }

                switch (userResource.getStatus()) {
                    case SUCCESS:
                        Toast.makeText(AboutUserActivity.this, userResource.getData().toString(), Toast.LENGTH_LONG).show();
                        User currentUser = userResource.getData();

                        mFullnameTextView.setText(currentUser.getName());
                        mUsernameTextView.setText(currentUser.getUsername());

                        break;
                    case ERROR:
                        Toast.makeText(AboutUserActivity.this, userResource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(AboutUserActivity.this, "Unknown status", Toast.LENGTH_LONG).show();
                }

//                mProgressBarLogin.setVisibility(View.GONE);
            };
        });
    }

    private void initFields() {
        View itemUserView = findViewById(R.id.include_about_user);

        mUserAvatarImageView = itemUserView.findViewById(R.id.item_user_avatar_imageView);
        mFullnameTextView = itemUserView.findViewById(R.id.item_user_fullname_textView);
        mUsernameTextView = itemUserView.findViewById(R.id.item_user_username_textView);

        mToolbar = findViewById(R.id.toolbar_act_about_user);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_about_user);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
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
