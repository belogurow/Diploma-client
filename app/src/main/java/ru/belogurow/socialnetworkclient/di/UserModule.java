package ru.belogurow.socialnetworkclient.di;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.belogurow.socialnetworkclient.users.repository.UserRepository;

@Module
public class UserModule {

    @Provides
    @NonNull
    @Singleton
    public UserRepository provideUserRepository() {
        return new UserRepository();
    }
}
