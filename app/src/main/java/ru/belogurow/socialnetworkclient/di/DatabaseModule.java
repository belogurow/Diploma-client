package ru.belogurow.socialnetworkclient.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.belogurow.socialnetworkclient.AppDatabase;
import ru.belogurow.socialnetworkclient.users.dao.UserDao;

@Module
public class DatabaseModule {

    private AppDatabase mAppDatabase;

    public DatabaseModule(Context context) {
        mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "database").build();
    }

    @NonNull
    @Provides
    @Singleton
    public AppDatabase provideAppDatabase() {
        return mAppDatabase;
    }

    @NonNull
    @Provides
    @Singleton
    public UserDao provideUserDao() {
        return mAppDatabase.mUserDao();
    }
}
