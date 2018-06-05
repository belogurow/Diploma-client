package ru.belogurow.socialnetworkclient.users.repository;

import android.util.Log;

import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.users.dto.FavoriteUsersDto;
import ru.belogurow.socialnetworkclient.users.model.FavoriteUsers;

@Singleton
public class RemoteFavoriteUsersRepository {
    private static final String TAG = RemoteFavoriteUsersRepository.class.getSimpleName();

    public Flowable<FavoriteUsersDto> addToFavoriteUsers(FavoriteUsers favoriteUsers) {
        Log.d(TAG, "addToFavoriteUsers: " + favoriteUsers.toString());
        return App.sFavoriteUsersWebService.addToFavoriteUsers(favoriteUsers);
    }

    public Flowable<List<FavoriteUsersDto>> getAllFavoriteUsersByUserId(UUID fromUserId) {
        Log.d(TAG, "getAllFavoriteUsersByUserId: " + fromUserId);
        return App.sFavoriteUsersWebService.getAllFavoriteUsersByUserId(fromUserId);
    }

    public Flowable<Boolean> isFromUserIdHasFavToUserId(UUID fromUserId, UUID toUserId) {
        Log.d(TAG, "isFromUserIdHasFavToUserId: " + fromUserId + " " + toUserId);
        return App.sFavoriteUsersWebService.isFromUserIdHasFavToUserId(fromUserId, toUserId);
    }

    public Flowable<Boolean> deleteFromUserIdFavForToUserId(UUID fromUserId, UUID toUserId) {
        Log.d(TAG, "deleteFromUserIdFavForToUserId: " + fromUserId + " " + toUserId);
        return App.sFavoriteUsersWebService.deleteFromUserIdFavForToUserId(fromUserId, toUserId);
    }
}
