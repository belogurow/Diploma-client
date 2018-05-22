package ru.belogurow.socialnetworkclient.chat.service;

import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.belogurow.socialnetworkclient.chat.model.ChatRoom;

public interface ChatWebService {
    @POST("/chat")
    Flowable<ChatRoom> getChatRoom(@Body ChatRoom chatRoom);

    @GET("/chat/{userId}")
    Flowable<List<ChatRoom>> getAllChatsByUserId(@Path("userId") UUID id);
}
