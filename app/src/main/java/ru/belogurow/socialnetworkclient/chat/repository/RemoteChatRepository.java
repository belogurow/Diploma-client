package ru.belogurow.socialnetworkclient.chat.repository;

import android.util.Log;

import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.chat.model.ChatMessage;
import ru.belogurow.socialnetworkclient.chat.model.ChatRoom;

@Singleton
public class RemoteChatRepository {
    private static final String TAG = RemoteChatRepository.class.getSimpleName();

    public Flowable<ChatRoom> getChatRoom(ChatRoom chatRoom) {
        Log.d(TAG, "getChatRoom: " + chatRoom);
        return App.sChatWebService.getChatRoom(chatRoom);
    }

    public Flowable<List<ChatRoom>> getAllChatsByUserId(UUID userId) {
        Log.d(TAG, "getAllChatsByUserId: " + userId);
        return App.sChatWebService.getAllChatsByUserId(userId);
    }

    public Flowable<List<ChatMessage>> getAllMessagesByChatId(UUID chatId) {
        Log.d(TAG, "getAllMessagesByChatId: " + chatId);
        return App.sChatWebService.getAllMessagesByChatId(chatId);
    }

}
