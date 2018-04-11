package ru.belogurow.socialnetworkclient.users.repository;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.users.dao.UserDao;
import ru.belogurow.socialnetworkclient.users.model.User;

public class LocalUserRepository {
    private static final String TAG = LocalUserRepository.class.getSimpleName();

    @Inject
    protected UserDao mUserDao;

    @Inject
    protected Executor mExecutor;

    public LocalUserRepository() {
        App.getComponent().inject(this);
    }

    public void insert(User user) {
        mExecutor.execute(() -> mUserDao.insert(user));
    }

    public void deleteAll() {
        mExecutor.execute(() -> mUserDao.deleteAll());
    }

    public Flowable<User> getOneUser() {
        return mUserDao.getAllRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(users -> !users.isEmpty())
                .flatMap(users -> Flowable.just(users.get(0)));
    }
}
