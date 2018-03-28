package ru.belogurow.socialnetworkclient.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ru.belogurow.socialnetworkclient.model.User;
import ru.belogurow.socialnetworkclient.model.UserMessage;
import ru.belogurow.socialnetworkclient.repository.UserRepository;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class UserViewModel extends ViewModel {

    private UserRepository mUserRepository;
    private LiveData<User> user;

    public UserViewModel() {
        mUserRepository = new UserRepository();
    }

    public LiveData<UserMessage> login(User user) {
//        user.setPassword(PasswordEncoder.);
        return mUserRepository.login(user);
    }

    public LiveData<List<User>> getUsers() {
        return mUserRepository.getUsers();
    }
}
