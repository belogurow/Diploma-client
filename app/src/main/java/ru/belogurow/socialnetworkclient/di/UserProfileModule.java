package ru.belogurow.socialnetworkclient.di;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.belogurow.socialnetworkclient.users.repository.RemoteUserProfileRepository;

@Module
public class UserProfileModule {

    @Provides
    @NonNull
    @Singleton
    public RemoteUserProfileRepository provideRemoteUserProfileRepository() {
        return new RemoteUserProfileRepository();
    }

}
