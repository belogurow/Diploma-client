package ru.belogurow.socialnetworkclient.di;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.belogurow.socialnetworkclient.chat.repository.RemoteChatRepository;

@Module
public class ChatModule {

    @Provides
    @NonNull
    @Singleton
    public RemoteChatRepository provideRemoteChatRepository() {
        return new RemoteChatRepository();
    }
}
