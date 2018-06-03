package ru.belogurow.socialnetworkclient.users.service;

import java.util.UUID;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.belogurow.socialnetworkclient.users.dto.UserProfileDto;
import ru.belogurow.socialnetworkclient.users.model.UserProfile;

public interface UserProfileWebService {

    @POST("/user-profile")
    Flowable<UserProfileDto> addUserProfile(@Query("userId") UUID userId,
                                            @Body UserProfile userProfile);
}
