package ru.belogurow.socialnetworkclient.web;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.belogurow.socialnetworkclient.model.User;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public interface WebUserService {
    @POST("/users")
    Call<User> login(@Body User user);

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") UUID id);

    @GET("users")
    Call<List<User>> getUsers();
}
