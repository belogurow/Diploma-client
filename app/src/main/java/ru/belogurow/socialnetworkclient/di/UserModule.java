package ru.belogurow.socialnetworkclient.di;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.belogurow.socialnetworkclient.users.repository.LocalUserRepository;
import ru.belogurow.socialnetworkclient.users.repository.RemoteUserRepository;

@Module
public class UserModule {

    @Provides
    @NonNull
    @Singleton
    public RemoteUserRepository provideRemoteUserRepository() {
        return new RemoteUserRepository();
    }

    @Provides
    @NonNull
    @Singleton
    public LocalUserRepository provieLocalUserRepository() {
        return new LocalUserRepository();
    }
}
