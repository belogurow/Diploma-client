package ru.belogurow.socialnetworkclient.users.service;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.belogurow.socialnetworkclient.users.model.User;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public interface WebUserService {
    @POST("/login")
    Call<User> login(@Body User user);

    @POST("/registration")
    Call<User> registration(@Body User user);

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") UUID id);

    @GET("users")
    Call<List<User>> getUsers();
}
