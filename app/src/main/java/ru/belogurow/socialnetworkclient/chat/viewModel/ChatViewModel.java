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

    public LiveData<Resource<ChatRoom>> getChatRoom(ChatRoom chatRoom) {
        Log.d(TAG, "getChatRoom: " + chatRoom);

        MutableLiveData<Resource<ChatRoom>> chatRoomResult = new MutableLiveData<>();

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

    public LiveData<Resource<List<ChatRoom>>> getAllChatsByUserId(UUID userId) {
        Log.d(TAG, "getAllChatsByUserId: " + userId);

        MutableLiveData<Resource<List<ChatRoom>>> chatRoomListResult = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteChatRepository.getAllChatsByUserId(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                chatResult -> {
                                    Log.d(TAG, "getAllChatsByUserId: " + userId);
                                    chatRoomListResult.postValue(Resource.success(chatResult));
                                },
                                error -> {
                                    Log.d(TAG, "getAllChatsByUserId: " + Arrays.toString(error.getStackTrace()));

                                    if (error instanceof HttpException)
                                        chatRoomListResult.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        chatRoomListResult.postValue(Resource.error(error.getMessage()));
                                    }
                                })

        );

        return chatRoomListResult;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
