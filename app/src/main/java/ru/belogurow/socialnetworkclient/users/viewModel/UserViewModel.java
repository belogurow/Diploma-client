package ru.belogurow.socialnetworkclient.users.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.repository.UserRepository;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class UserViewModel extends ViewModel {

    private UserRepository mUserRepository;
    private LiveData<Resource<User>> liveDataUser;

    public UserViewModel() {
        mUserRepository = new UserRepository();
    }

    public LiveData<Resource<User>> login(User user) {
//        if (liveDataUser == null) {
          return mUserRepository.login(user);
//        }

//        return liveDataUser.m;
    }

    public LiveData<List<User>> getUsers() {
        return mUserRepository.getUsers();
    }
}
