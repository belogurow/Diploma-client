package ru.belogurow.socialnetworkclient.users.repository;

import android.util.Log;

import java.util.UUID;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.users.dto.UserProfileDto;
import ru.belogurow.socialnetworkclient.users.model.UserProfile;

@Singleton
public class RemoteUserProfileRepository {
    private static final String TAG = RemoteUserProfileRepository.class.getSimpleName();

    public Flowable<UserProfileDto> addUserProfile(UUID userId, UserProfile userProfile) {
        Log.d(TAG, "addUserProfile: " + userId + " " + userProfile);
        return App.sProfileWebService.addUserProfile(userId, userProfile);
    }
}
