package ru.belogurow.socialnetworkclient.chat.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.chat.dto.ChatRoomDto;
import ru.belogurow.socialnetworkclient.chat.model.ChatMessage;
import ru.belogurow.socialnetworkclient.chat.model.ChatRoom;
import ru.belogurow.socialnetworkclient.chat.repository.RemoteChatRepository;
import ru.belogurow.socialnetworkclient.common.web.Resource;

public class ChatViewModel extends ViewModel {

    private static final String TAG = ChatViewModel.class.getSimpleName();

    private CompositeDisposable mCompositeDisposable;

    @Inject
    protected RemoteChatRepository mRemoteChatRepository;

    public ChatViewModel() {
        App.getComponent().inject(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    public LiveData<Resource<ChatRoomDto>> getChatRoom(ChatRoom chatRoom) {
        Log.d(TAG, "getChatRoom: " + chatRoom);

        final MutableLiveData<Resource<ChatRoomDto>> chatRoomResult = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteChatRepository.getChatRoom(chatRoom)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                chatResult -> {
                                    Log.d(TAG, "getChatRoom-result: " + chatResult);
                                    chatRoomResult.postValue(Resource.success(chatResult));
                                },
                                error -> {
                                    Log.d(TAG, "getChatRoom-error: " + Arrays.toString(error.getStackTrace()));

                                    if (error instanceof HttpException)
                                        chatRoomResult.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        chatRoomResult.postValue(Resource.error(error.getMessage()));
                                    }
                                })

        );

        return chatRoomResult;
    }

    public LiveData<Resource<List<ChatRoomDto>>> getAllChatsByUserId(UUID userId) {
        Log.d(TAG, "getAllChatsByUserId: " + userId);

        final MutableLiveData<Resource<List<ChatRoomDto>>> chatRoomListResult = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteChatRepository.getAllChatsByUserId(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                chatResult -> {
                                    Log.d(TAG, "getAllChatsByUserId-result: " + chatResult);
                                    chatRoomListResult.postValue(Resource.success(chatResult));
                                },
                                error -> {
                                    Log.d(TAG, "getAllChatsByUserId-error: " + Arrays.toString(error.getStackTrace()));

                                    if (error instanceof HttpException)
                                        chatRoomListResult.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        chatRoomListResult.postValue(Resource.error(error.getMessage()));
                                    }
                                })

        );

        return chatRoomListResult;
    }

    public LiveData<Resource<List<ChatMessage>>> getAllMessagesByChatId(UUID chatId) {
        Log.d(TAG, "getAllMessagesByChatId: " + chatId);

        final MutableLiveData<Resource<List<ChatMessage>>> chatMessageListResult = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteChatRepository.getAllMessagesByChatId(chatId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                chatResult -> {
                                    Log.d(TAG, "getAllMessagesByChatId-result: " + chatResult);
                                    chatMessageListResult.postValue(Resource.success(chatResult));
                                },
                                error -> {
                                    Log.d(TAG, "getAllMessagesByChatId-error: " + Arrays.toString(error.getStackTrace()));

                                    if (error instanceof HttpException)
                                        chatMessageListResult.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        chatMessageListResult.postValue(Resource.error(error.getMessage()));
                                    }
                                })

        );

        return chatMessageListResult;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
