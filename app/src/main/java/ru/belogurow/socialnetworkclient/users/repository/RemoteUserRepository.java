package ru.belogurow.socialnetworkclient.users.repository;

import android.util.Log;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.users.model.User;

/**
 * Created by alexbelogurow on 26.03.2018.
 */
@Singleton
public class RemoteUserRepository {
    private static final String TAG = RemoteUserRepository.class.getSimpleName();

    @Inject protected LocalUserRepository mLocalUserRepository;

    public RemoteUserRepository() {
        App.getComponent().inject(this);
    }

    public Flowable<User> registration(User user) {
        Log.d(TAG, "registration: " + user.toString());
        return App.sWebUserService.registration(user);
    }

    public Flowable<User> login(User user) {
        Log.d(TAG, "login: " + user.toString());
        return App.sWebUserService.login(user);
    }


    public Flowable<List<User>> getUsers() {
        Log.d(TAG, "getUsers: ");
        return App.sWebUserService.getUsers();
    }

    public Flowable<User> getUserById(UUID id) {
        Log.d(TAG, "getUserById: " + id);
        return App.sWebUserService.getUserById(id);
    }
}
