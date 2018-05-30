package ru.belogurow.socialnetworkclient.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.UUID;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.ChatMessageDto;
import ru.belogurow.socialnetworkclient.chat.dto.ChatRoomDto;
import ru.belogurow.socialnetworkclient.chat.model.ChatRoom;
import ru.belogurow.socialnetworkclient.chat.viewModel.ChatViewModel;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.adapter.ChatRoomAdapter;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;
import ru.belogurow.socialnetworkclient.web.SelfSigningClientBuilder;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = ChatRoom.class.getSimpleName();

    private UserViewModel mUserViewModel;
    private ChatViewModel mChatViewModel;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private Button mSendMessageButton;
    private Toolbar mToolbar;
    private ChatRoomAdapter mChatRoomAdapter;
    private StompClient mStompClient;

    private List<ChatMessageDto> mMessages;
    private Gson mGson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initFields();

        Intent intent = getIntent();
        UUID anotherUserId = (UUID) intent.getSerializableExtra(Extras.EXTRA_USER_ID);

        loadMessages(intent, anotherUserId);
    }

    private void loadMessages(Intent intent, UUID anotherUserId) {
        if (anotherUserId != null) {
            mUserViewModel.userFromDB().observe(this, userResource -> {
                showProgressBar();

                if (userResource != null && userResource.getStatus() == NetworkStatus.SUCCESS) {
                    UserDto currentUser = userResource.getData();

                    mChatViewModel.getChatRoom(new ChatRoom(currentUser.getId(), anotherUserId))
                            .observe(this, chatRoomResource -> {
                                if (chatRoomResource == null) {
                                    Toast.makeText(this, R.string.received_null_data, Toast.LENGTH_LONG).show();
                                    hideProgressBar();
                                    return;
                                }

                                switch (chatRoomResource.getStatus()) {
                                    case SUCCESS:
                                        ChatRoomDto chatRoomDtoData = chatRoomResource.getData();
                                        initChatData(chatRoomDtoData, currentUser);
                                        break;
                                    case ERROR:
                                        Toast.makeText(this, userResource.getMessage(), Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(this, R.string.unknown_status, Toast.LENGTH_LONG).show();
                                }
                                hideProgressBar();
                            });
                } else {
                    hideProgressBar();
                }
            });
        } else {
            ChatRoomDto chatRoomDto = (ChatRoomDto) intent.getSerializableExtra(Extras.EXTRA_CHAT_ROOM_DTO);
            mUserViewModel.userFromDB().observe(this, userResource -> {
                showProgressBar();

                if (userResource != null && userResource.getStatus() == NetworkStatus.SUCCESS) {
                    UserDto currentUser = userResource.getData();
                    initChatData(chatRoomDto, currentUser);
                }

                hideProgressBar();
            });
        }
    }

    private void initChatData(ChatRoomDto chatRoomDto, UserDto currentUser) {
        if (currentUser.equalsById(chatRoomDto.getFirstUser())) {
            setChatRoomTitle(chatRoomDto.getSecondUser());
        } else {
            setChatRoomTitle(chatRoomDto.getFirstUser());
        }
        mChatRoomAdapter.setChatRoomDto(chatRoomDto);
        mChatRoomAdapter.setCurrentUser(currentUser);
        getAllMessages(chatRoomDto);
        initStomp(chatRoomDto, currentUser);
    }

    private void initFields() {
        mRecyclerView = findViewById(R.id.recycler_act_chat_room);
        mMessageEditText = findViewById(R.id.message_input_act_chat_room);
        mSendMessageButton = findViewById(R.id.button_send_act_chat_room);
        mProgressBar = findViewById(R.id.progress_act_chat_room);

        mToolbar = findViewById(R.id.toolbar_act_chat_room);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mChatRoomAdapter = new ChatRoomAdapter();
        mRecyclerView.setAdapter(mChatRoomAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        mChatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void setChatRoomTitle(UserDto anotherUser) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(anotherUser.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getAllMessages(ChatRoomDto chatRoomDto) {
        mChatViewModel.getAllMessagesByChatId(chatRoomDto.getId()).observe(this, chatMessageResource -> {
            showProgressBar();

            if (chatMessageResource == null) {
                Toast.makeText(this, "Received null data", Toast.LENGTH_LONG).show();
                hideProgressBar();
                return;
            }

            switch (chatMessageResource.getStatus()) {
                case SUCCESS:
                    mMessages = chatMessageResource.getData();
                    mChatRoomAdapter.setMessagesList(mMessages);
                    mRecyclerView.scrollToPosition(mMessages.size() - 1);
                    break;
                case ERROR:
                    Toast.makeText(this, chatMessageResource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, "Unknown status", Toast.LENGTH_LONG).show();
            }
            hideProgressBar();
        });
    }

    private void initStomp(ChatRoomDto chatRoomDto, UserDto currentUser) {
        OkHttpClient client = SelfSigningClientBuilder.createClient(this);

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, App.BASE_WEB_SOCKET_URL + "/chatRoom", null, client);

        mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(TAG, "OPENED: ");
                            break;
                        case ERROR:
                            Log.d(TAG, "Stomp connection error", lifecycleEvent.getException());
                            Toast.makeText(this, lifecycleEvent.getException().toString(), Toast.LENGTH_SHORT).show();
//                            toast("Stomp connection error");
                            break;
                        case CLOSED:
                            Log.d(TAG, "CLOSED: ");
//                            toast("Stomp connection closed");
                    }
                });

        // Receive greetings
        mStompClient.topic("/topic/chatRoom/" + chatRoomDto.getId().toString() + "/messages")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    Log.d(TAG, "Received " + message);
                    mChatRoomAdapter.addMessage(mGson.fromJson(message.getPayload(), ChatMessageDto.class));
//                    mChatRoomAdapter.setMessagesList(mMessages);
                    mRecyclerView.scrollToPosition(mMessages.size() - 1);
                });

        mStompClient.topic("/topic/chatRoom/" + chatRoomDto.getId() + "/" + currentUser.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    Log.d(TAG, "Sended: " + stompMessage.toString());
                });

        mStompClient.connect();

        mSendMessageButton.setOnClickListener(v -> {
            mStompClient.send("/topic/chatRoom/" + chatRoomDto.getId() + "/" + currentUser.getId(), mMessageEditText.getText().toString())
                    .compose(applySchedulers())
                    .subscribe(() -> {
                        Log.d(TAG, "STOMP echo send successfully");
                    }, throwable -> {
                        Log.e(TAG, "Error send STOMP echo", throwable);
                    });

            mMessageEditText.getText().clear();
        });
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Finish activity when press back button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        mStompClient.disconnect();
        super.onDestroy();
    }
}
