package ru.belogurow.socialnetworkclient.common.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.ResponseBody;

import static ru.belogurow.socialnetworkclient.common.web.NetworkStatus.ERROR;
import static ru.belogurow.socialnetworkclient.common.web.NetworkStatus.SUCCESS;


public class Resource<T> {
    @NonNull public final NetworkStatus status;
    @Nullable public final T data;
    @Nullable public final String message;

    private Resource(@NonNull NetworkStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(@NonNull ResponseBody msg) {
        try {
            return new Resource<>(ERROR, null, msg.string());
        } catch (IOException e) {
            e.printStackTrace();
            return new Resource<>(ERROR, null, "Server error. See logs.");
        }
    }

}
