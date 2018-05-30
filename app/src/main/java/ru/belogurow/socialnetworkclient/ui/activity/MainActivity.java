package ru.belogurow.socialnetworkclient.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.concurrent.ExecutionException;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentChat;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentDialogs;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentMyFiles;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentMyProfile;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentUserList;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentUserProfile;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private UserViewModel mUserViewModel;

    private Fragment fragmentUserList;
    private Fragment fragmentUserProfile;
    private Fragment fragmentMyProfile;
    private Fragment fragmentDialogs;
    private Fragment fragmentMyFiles;
    private Fragment fragmentWebSocketTest;

    private ProfileDrawerItem mProfileDrawerItem;
    private AccountHeader mAccountHeader;
    private Bitmap mBitmap;

    private UserDto currentUser;

    private int navigationSelectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 11.04.2018 ROTATE CHANGE CURRENT FRAGMENT CONFIGURATION

        initFragments();
        initDrawer();
        subscribeProfile();

//        startActivity(new Intent(this, StlViewerActivity.class));
    }

    private void initFragments() {
        fragmentUserList = new FragmentUserList();
        fragmentUserProfile = new FragmentUserProfile();
        fragmentMyProfile = new FragmentMyProfile();
        fragmentDialogs = new FragmentDialogs();
        fragmentMyFiles = new FragmentMyFiles();

        fragmentWebSocketTest = new FragmentChat();

        mToolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void subscribeProfile() {
        mUserViewModel.userFromDB().observe(this, userBDResource -> {

            if (userBDResource != null && userBDResource.getStatus() == NetworkStatus.SUCCESS) {
                currentUser = userBDResource.getData();
                mProfileDrawerItem.withName(currentUser.getName());
                mProfileDrawerItem.withEmail(currentUser.getUsername());
                updateProfileData();

                mUserViewModel.getUserById(userBDResource.getData().getId()).observe(this, userDtoResource -> {
                    if (userDtoResource == null) {
                        return;
                    }

                    switch (userDtoResource.getStatus()) {
                        case SUCCESS:
                            currentUser = userDtoResource.getData();

                            if (currentUser.getUserProfile() != null && currentUser.getUserProfile().getAvatarFile() != null) {
                                loadProfilePicture(this, currentUser.getUserProfile().getAvatarFile());
                            }
                            break;
                    }
                });
            }
        });
    }


    private void initDrawer() {
        mProfileDrawerItem = new ProfileDrawerItem();

        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.navigation_drawer_background2)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(mProfileDrawerItem)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        mToolbar.setTitle(R.string.my_profile);
                        navigationSelectedItem = 0;
                        openFragment();

                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem myProfile = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.my_profile)
                .withIcon(FontAwesome.Icon.faw_user2)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mToolbar.setTitle(R.string.my_profile);
                    navigationSelectedItem = 0;
                    openFragment();

                    return false;
                });

        PrimaryDrawerItem users = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.users)
                .withIcon(FontAwesome.Icon.faw_users)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mToolbar.setTitle(R.string.users);
                    navigationSelectedItem = 1;
                    openFragment();

                    return false;
                });

        PrimaryDrawerItem dialogs = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.dialogs)
                .withIcon(FontAwesome.Icon.faw_comment2)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mToolbar.setTitle(R.string.dialogs);
                    navigationSelectedItem = 2;
                    openFragment();

                    return false;
                });

        PrimaryDrawerItem files = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.my_files)
                .withIcon(FontAwesome.Icon.faw_file2)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mToolbar.setTitle(R.string.my_files);
                    navigationSelectedItem = 3;
                    openFragment();

                    return false;
                });

        SecondaryDrawerItem elseItem = new SecondaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.else_field)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mToolbar.setTitle(R.string.else_field);
                    navigationSelectedItem = 3;
                    openFragment();

                    return false;
                });

        SecondaryDrawerItem websocketItem = new SecondaryDrawerItem()
                .withIdentifier(4)
                .withName("WebSocketTest")
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    navigationSelectedItem = 4;
                    openFragment();

                    return false;
                });

        final Drawer drawerResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(mAccountHeader)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        myProfile,
                        users,
                        dialogs,
                        files,
                        new DividerDrawerItem()
//                        websocketItem
                )
                .withSelectedItem(navigationSelectedItem)
                .build();
    }

    /**
     * Open fragment by number of navigationSelectedItem
     */
    private void openFragment() {
        switch (navigationSelectedItem) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_acitivity_container, fragmentMyProfile)
                        .addToBackStack("MY_PROFILE")
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_acitivity_container, fragmentUserList)
                        .addToBackStack("USER_LIST")
                        .commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_acitivity_container, fragmentDialogs)
                        .addToBackStack("DIALOGS")
                        .commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_acitivity_container, fragmentMyFiles)
                        .addToBackStack("MY_FILES")
                        .commit();
                break;
            default:
                Toast.makeText(this,
                        "Fragment with number " + navigationSelectedItem + " does not exists!",
                        Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfilePicture(Context context, FileEntityDto avatarImage) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    mBitmap = Glide.with(context)
                            .asBitmap()
                            .load(App.BASE_URL + avatarImage.getDataUrl())
                            .submit(100, 100)
                            .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                updateProfileData();
            }
        }.execute();
    }

    private void updateProfileData() {
        mAccountHeader.removeProfile(0);
        mProfileDrawerItem.withIcon(mBitmap);
        mAccountHeader.addProfile(mProfileDrawerItem, 0);
        mAccountHeader.setActiveProfile(mProfileDrawerItem);
    }
}
