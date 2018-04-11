package ru.belogurow.socialnetworkclient.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentUserList;
import ru.belogurow.socialnetworkclient.ui.fragment.FragmentUserProfile;

public class MainActivity extends FragmentActivity {

    private Fragment fragmentUserList;
    private Fragment fragmentUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();

//        mFragmentTransaction = getSupportFragmentManager().beginTransaction().remove(fragmentUserProfile);
//                mFragmentTransaction.commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_acitivity_container, fragmentUserList)
                .addToBackStack(null)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_acitivity_container, fragmentUserProfile)
                .addToBackStack(null)
                .commit();


        String test = "";
//        startActivity(new Intent(this, UsersList.class));
    }

    private void initFragments() {
        fragmentUserList = new FragmentUserList();
        fragmentUserProfile = new FragmentUserProfile();

    }
}
