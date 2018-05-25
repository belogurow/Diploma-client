package ru.belogurow.socialnetworkclient.chat.repository;

import android.util.Log;

import java.io.File;
import java.util.UUID;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileEntity;

@Singleton
public class RemoteFileRepository {
    private static final String TAG = RemoteFileRepository.class.getSimpleName();

    public Flowable<FileEntityDto> uploadAvatar(UUID userId, FileEntity fileEntity, File file) {
        Log.d(TAG, "uploadAvatar: " + userId);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        return App.sFileWebService.uploadAvatar(userId, fileEntity, requestFile);
    }

}
