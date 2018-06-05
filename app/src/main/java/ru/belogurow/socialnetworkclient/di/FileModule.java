package ru.belogurow.socialnetworkclient.di;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.belogurow.socialnetworkclient.chat.repository.RemoteFileRepository;

@Module
public class FileModule {

    @Provides
    @NonNull
    @Singleton
    public RemoteFileRepository provideRemoteFileRepository() {
        return new RemoteFileRepository();
    }
}
