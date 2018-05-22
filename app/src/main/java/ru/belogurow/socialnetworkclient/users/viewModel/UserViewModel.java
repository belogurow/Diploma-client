package ru.belogurow.socialnetworkclient.users.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.repository.LocalUserRepository;
import ru.belogurow.socialnetworkclient.users.repository.RemoteUserRepository;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class UserViewModel extends ViewModel {

    private static final String TAG = UserViewModel.class.getSimpleName();

    private MutableLiveData<Resource<List<User>>> allUsers;
    private MutableLiveData<Resource<User>> currentUser;

    @Inject
    protected RemoteUserRepository mRemoteUserRepository;

    @Inject
    protected LocalUserRepository mLocalUserRepository;

    private CompositeDisposable mCompositeDisposable;

    public UserViewModel() {
        App.getComponent().inject(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void deleteAllDB() {
        Log.d(TAG, "deleteAllDB: ");
        mLocalUserRepository.deleteAll();
    }

    public LiveData<Resource<User>> userFromDB() {
        Log.d(TAG, "userFromDB: ");

        if (currentUser == null) {
            currentUser = new MutableLiveData<>();

            mCompositeDisposable.add(
                    mLocalUserRepository.getOneUser()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userResult -> {
                                        Log.d(TAG, "userFromDB-result: " + userResult.toString());
                                        currentUser.postValue(Resource.success(userResult));
                                    },
                                    error -> {
                                        Log.d(TAG, "userFromDB-error:" + Arrays.toString(error.getStackTrace()));

                                        if (error instanceof HttpException)
                                            currentUser.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                        else {
                                            currentUser.postValue(Resource.error(error.getMessage()));
                                        }
                                    })
            );
        }

        return currentUser;
    }

    public LiveData<Resource<User>> login(User user) {
        Log.d(TAG, "login: " + user.toString());

        final MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        mCompositeDisposable.add(
                mRemoteUserRepository.login(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            userResult -> {
                                Log.d(TAG, "loginRx-result: " + userResult.toString());
                                liveData.postValue(Resource.success(userResult));

                                mLocalUserRepository.insert(userResult);
                            },
                            error -> {
                                Log.d(TAG, "login-error:" + error.getMessage());

                                if (error instanceof HttpException)
                                    liveData.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                else {
                                    liveData.postValue(Resource.error(error.getMessage()));
                                }
                            })
        );

        return liveData;
    }

    public LiveData<Resource<User>> registration(User user) {
        Log.d(TAG, "registration: " + user.toString());

        final MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        mCompositeDisposable.add(
                mRemoteUserRepository.registration(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userResult -> {
                                    Log.d(TAG, "registrationRx-result: " + userResult.toString());
                                    liveData.postValue(Resource.success(userResult));

                                    mLocalUserRepository.insert(userResult);
                                },
                                error -> {
                                    Log.d(TAG, "registration-error:" + error.getMessage());

                                    if (error instanceof HttpException)
                                        liveData.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        liveData.postValue(Resource.error(error.getMessage()));
                                    }
                                })
        );

        return liveData;
    }

    public LiveData<Resource<List<User>>> getAllUsers() {
        Log.d(TAG, "getAllUsers: ");

        if (allUsers == null) {
            allUsers = new MutableLiveData<>();

            mCompositeDisposable.add(
                    mRemoteUserRepository.getUsers()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    usersResult -> {
                                        Log.d(TAG, "getAllUsers-result: " + usersResult);
                                        allUsers.postValue(Resource.success(usersResult));
                                    },
                                    error -> {
                                        Log.d(TAG, "getAllUsers-error:" + error.getMessage());

                                        if (error instanceof HttpException)
                                            allUsers.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                        else {
                                            allUsers.postValue(Resource.error(error.getMessage()));
                                        }
                                    })
            );
        }

        return allUsers;
    }

    public LiveData<Resource<User>> getUserById(String id) {
        Log.d(TAG, "getUserById: " + id);

        final MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        mCompositeDisposable.add(
                mRemoteUserRepository.getUserById(UUID.fromString(id))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userResult -> {
                                    Log.d(TAG, "getUserById-result: " + userResult.toString());
                                    liveData.postValue(Resource.success(userResult));
                                },
                                error -> {
                                    Log.d(TAG, "getUserById-error:" + error.getMessage());

                                    if (error instanceof HttpException)
                                        liveData.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        liveData.postValue(Resource.error(error.getMessage()));
                                    }
                                })
        );

        return liveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
