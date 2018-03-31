package ru.belogurow.socialnetworkclient.users.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.belogurow.socialnetworkclient.common.App;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.model.User;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();

    public LiveData<User> registration(User user) {
        return null;
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

//    public LiveData<User> getUser(UUID id) {
//        final MutableLiveData<User> userData = new MutableLiveData<>();
//
//        WebService.getWebUserService().getUser(id).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
//                if (response.isSuccessful()) {
//                    userData.postValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
//                String test = "";
//            }
//        });
//        return userData;
//    }
//
//    public LiveData<UserMessage> login(User user) {
//        final MutableLiveData<UserMessage> resultUser = new MutableLiveData<>();
//
//        final int OK = 200;
//        final int NOT_FOUND = 404;
//        final int CONFLICT = 409;
//        final int CREATED = 201;
//        final int UNPROCESSABLE_ENTITY = 422;
//
//        WebService.getWebUserService().login(user).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                switch (user.getUserStatus()) {
//                    case REGISTRATION:
//                        if (response.code() == CONFLICT) {
//                            resultUser.postValue(new UserMessage("Пользователь с таким логином уже существует!", null));
//                        } else if (response.code() == CREATED) {
//                            resultUser.postValue(new UserMessage("OK", response.body()));
//                        }
//                        break;
//                    case ONLINE:
//                        break;
//                    case OFFLINE:
//                        if (response.code() == OK) {
//                            resultUser.postValue(new UserMessage("OK", response.body()));
//                        } else if (response.code() == CONFLICT) {
//                            resultUser.postValue(new UserMessage("Такой пользователь уже в сети", null));
//                        } else if (response.code() == NOT_FOUND) {
//                            resultUser.postValue(new UserMessage("Неправильный логин или пароль", null));
//                        }
//                        break;
//                        default:
//                            resultUser.postValue(new UserMessage("Неопознанная ошибка c кодом " + response.code(), null));
//                            break;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                resultUser.postValue(new UserMessage("Неопознанная ошибка " + t.getLocalizedMessage(), null));
//            }
//        });
//        return resultUser;
//    }

    public LiveData<List<User>> getUsers() {
        final MutableLiveData<List<User>> usersData = new MutableLiveData<>();

        App.sWebUserService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                Log.d(UserRepository.class.getSimpleName(), call.request().toString());
                if (response.isSuccessful()) {
                    usersData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.d(UserRepository.class.getSimpleName(), call.request().toString());
            }
        });
        return usersData;
    }
}
