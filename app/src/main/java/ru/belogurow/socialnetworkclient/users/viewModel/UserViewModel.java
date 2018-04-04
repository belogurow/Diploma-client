package ru.belogurow.socialnetworkclient.users.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.repository.UserRepository;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class UserViewModel extends ViewModel {

    @Inject
    protected UserRepository mUserRepository;
    private LiveData<Resource<User>> liveDataUser;

    public UserViewModel() {
        App.getComponent().inject(this);
    }

    public LiveData<Resource<User>> login(User user) {
          return mUserRepository.login(user);
    }

    public LiveData<Resource<User>> registration(User user) {
        return mUserRepository.registration(user);
    }

    public LiveData<List<User>> getUsers() {
        return mUserRepository.getUsers();
    }
}
