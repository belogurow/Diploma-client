package ru.belogurow.socialnetworkclient.users.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import ru.belogurow.socialnetworkclient.users.model.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User")
    Flowable<List<User>> getAllRx();

    @Query("DELETE FROM User")
    void deleteAll();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);


}
