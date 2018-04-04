package ru.belogurow.socialnetworkclient.common.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

import static ru.belogurow.socialnetworkclient.common.web.NetworkStatus.ERROR;
import static ru.belogurow.socialnetworkclient.common.web.NetworkStatus.SUCCESS;


public class Resource<T> {
    @NonNull
    public final NetworkStatus status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private Resource(@NonNull NetworkStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(@NonNull ResponseBody errorBody) {
        try {
            JSONObject object = new JSONObject(errorBody.string());
            return new Resource<>(ERROR, null, object.getString("message"));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return Resource.error();
        }
    }

    public static <T> Resource<T> error(@NonNull String message) {
        return new Resource<>(ERROR, null, message);
    }

    private static <T> Resource<T> error() {
        return new Resource<>(ERROR, null, "Api error. See logs.");
    }

}
