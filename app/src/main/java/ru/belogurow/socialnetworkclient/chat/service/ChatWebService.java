package ru.belogurow.socialnetworkclient.chat.service;

import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.belogurow.socialnetworkclient.chat.dto.ChatMessageDto;
import ru.belogurow.socialnetworkclient.chat.dto.ChatRoomDto;
import ru.belogurow.socialnetworkclient.chat.model.ChatRoom;

public interface ChatWebService {
    @POST("/chat")
    Flowable<ChatRoomDto> getChatRoom(@Body ChatRoom chatRoom);

    @GET("/chat/{userId}")
    Flowable<List<ChatRoomDto>> getAllChatsByUserId(@Path("userId") UUID userId);

    @GET("/chat/{chatId}/messages")
    Flowable<List<ChatMessageDto>> getAllMessagesByChatId(@Path("chatId") UUID chatId);
}
