package ru.belogurow.socialnetworkclient.users.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.model.User;

/**
 * Created by alexbelogurow on 26.03.2018.
 */
@Singleton
public class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();

    public LiveData<Resource<User>> registration(User user) {
        Log.d(TAG, "registration: " + user.toString());

        // TODO: 04.04.2018 SAVE TO DB IF SUCCESS
        final MutableLiveData<Resource<User>> userData = new MutableLiveData<>();
        App.sWebUserService.registration(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body());
                    userData.postValue(Resource.success(response.body()));

                }  else if (response.errorBody() != null) {
                Log.d(TAG, "onResponse: " + response.errorBody());
                userData.postValue(Resource.error(response.errorBody()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                userData.postValue(Resource.error(t.getLocalizedMessage()));
            }
        });

        return userData;
    }

    public LiveData<Resource<User>> login(User user) {
        Log.d(TAG, "login: " + user.toString());

        // TODO: 30.03.2018 GET FROM DB, IF IT IS EMPTY -> call to server
        final MutableLiveData<Resource<User>> userData = new MutableLiveData<>();

        App.sWebUserService.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body());
                    userData.postValue(Resource.success(response.body()));

                } else if (response.errorBody() != null) {
                    Log.d(TAG, "onResponse: " + response.errorBody());
                    userData.postValue(Resource.error(response.errorBody()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                userData.postValue(Resource.error(t.getLocalizedMessage()));
            }
        });

        return userData;
    }

    public LiveData<Resource<List<User>>> getUsers() {
        final MutableLiveData<Resource<List<User>>> usersData = new MutableLiveData<>();

        App.sWebUserService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                Log.d(UserRepository.class.getSimpleName(), call.request().toString());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body());
                    usersData.postValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.d(UserRepository.class.getSimpleName(), t.getLocalizedMessage());
            }
        });
        return usersData;
    }
}
