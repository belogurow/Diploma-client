package ru.belogurow.socialnetworkclient.users.service;

import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.model.User;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public interface UserWebService {
    @POST("/login")
    Flowable<UserDto> login(@Body User user);

    @POST("/registration")
    Flowable<UserDto> registration(@Body User user);

    @GET("/users/{id}")
    Flowable<UserDto> getUserById(@Path("id") UUID id);

    @GET("users")
    Flowable<List<UserDto>> getUsers();
}
