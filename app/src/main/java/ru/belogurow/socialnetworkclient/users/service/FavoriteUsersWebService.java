package ru.belogurow.socialnetworkclient.users.service;

import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.belogurow.socialnetworkclient.users.dto.FavoriteUsersDto;
import ru.belogurow.socialnetworkclient.users.model.FavoriteUsers;

public interface FavoriteUsersWebService {
    @POST("/favorite-users")
    Flowable<FavoriteUsersDto> addToFavoriteUsers(@Body FavoriteUsers favoriteUsers);

    @GET("/favorite-users")
    Flowable<List<FavoriteUsersDto>> getAllFavoriteUsersByUserId(@Query("fromUserId") UUID fromUserId);

    @GET("/favorite-users/is-favorite")
    Flowable<Boolean> isFromUserIdHasFavToUserId(@Query("fromUserId") UUID fromUserId,
                                                 @Query("toUserId") UUID toUserId);

    @DELETE("/favorite-users")
    Flowable<Boolean>  deleteFromUserIdFavForToUserId(@Query("fromUserId") UUID fromUserId,
                                                      @Query("toUserId") UUID toUserId);
}
