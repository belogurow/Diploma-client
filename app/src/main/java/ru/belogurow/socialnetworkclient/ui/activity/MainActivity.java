package ru.belogurow.socialnetworkclient.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentChat;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentDialogs;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentMyProfile;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentUserList;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentUserProfile;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Fragment fragmentUserList;
    private Fragment fragmentUserProfile;
    private Fragment fragmentMyProfile;
    private Fragment fragmentDialogs;
    private Fragment fragmentWebSocketTest;

    private int navigationSelectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 11.04.2018 ROTATE CHANGE CURRENT FRAGMENT CONFIGURATION

        initFragments();
        initDrawer();
    }

    private void initFragments() {
        fragmentUserList = new FragmentUserList();
        fragmentUserProfile = new FragmentUserProfile();
        fragmentMyProfile = new FragmentMyProfile();
        fragmentDialogs = new FragmentDialogs();

        fragmentWebSocketTest = new FragmentChat();

        mToolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initDrawer() {
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        PrimaryDrawerItem myProfile = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.my_profile)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mToolbar.setTitle(R.string.my_profile);
                    navigationSelectedItem = 0;
                    openFragment();

                    return false;
                });

        PrimaryDrawerItem users = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.users)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mToolbar.setTitle(R.string.users);
                    navigationSelectedItem = 1;
                    openFragment();

                    return false;
                });

        PrimaryDrawerItem dialogs = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.dialogs)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mToolbar.setTitle(R.string.dialogs);
                    navigationSelectedItem = 2;
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
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        navigationSelectedItem = 4;
                        openFragment();

                        return false;
                    }
                });

        final Drawer drawerResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(header)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        myProfile,
                        users,
                        dialogs,
                        new DividerDrawerItem(),
                        websocketItem
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
            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_acitivity_container, fragmentWebSocketTest)
                        .addToBackStack("WEB_SOCKET")
                        .commit();
                break;
            default:
                Toast.makeText(this,
                        "Fragment with number " + navigationSelectedItem + " does not exists!",
                        Toast.LENGTH_SHORT).show();
        }
    }
}
