package ru.belogurow.socialnetworkclient.users.service;

import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.belogurow.socialnetworkclient.users.model.User;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public interface UserWebService {
    @POST("/login")
    Flowable<User> login(@Body User user);

    @POST("/registration")
    Flowable<User> registration(@Body User user);

    @GET("/users/{id}")
    Flowable<User> getUserById(@Path("id") UUID id);

    @GET("users")
    Flowable<List<User>> getUsers();
}
