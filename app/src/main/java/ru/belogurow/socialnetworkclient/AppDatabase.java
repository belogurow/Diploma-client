package ru.belogurow.socialnetworkclient;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.belogurow.socialnetworkclient.users.dao.UserDao;
import ru.belogurow.socialnetworkclient.users.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao mUserDao();
}
