package ru.belogurow.socialnetworkclient.chat.service;

import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileEntity;


public interface FileWebService {

    @Multipart
    @POST("/file/{userId}/avatar")
    Flowable<FileEntityDto> uploadAvatar(@Path("userId") UUID userId,
                                         @Part("fileEntity") FileEntity fileEntity,
                                         @Part("file\"; filename=\"file_temp.jpg\" ") RequestBody file);

    @Multipart
    @POST("/file")
    Flowable<FileEntityDto> uploadFile(@Part("fileEntity") FileEntity fileEntity,
                                       @Part("file\"; filename=\"temp\" ") RequestBody file,
                                       @Query("isAvatar") boolean isAvatar);

    @GET("/files")
    Flowable<List<FileEntityDto>> getAllFilesByUserId(@Query("userId") UUID userId);

}
