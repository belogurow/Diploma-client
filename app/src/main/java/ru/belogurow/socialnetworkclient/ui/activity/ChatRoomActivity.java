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
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.model.ChatMessage;
import ru.belogurow.socialnetworkclient.chat.model.ChatRoom;
import ru.belogurow.socialnetworkclient.chat.viewModel.ChatViewModel;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.ui.adapter.ChatRoomAdapter;
import ru.belogurow.socialnetworkclient.users.model.User;
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

    private List<ChatMessage> mMessages;
    private Gson mGson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initFields();

        Intent intent = getIntent();
        UUID secondUserId = UUID.fromString(intent.getStringExtra(Extras.EXTRA_USER_ID));

        mUserViewModel.userFromDB().observe(this, userResource -> {
            if (userResource != null && userResource.getStatus() == NetworkStatus.SUCCESS) {
                User currentUser = userResource.getData();

                mChatViewModel.getChatRoom(new ChatRoom(UUID.fromString(currentUser.getId()), secondUserId))
                        .observe(this, chatRoomResource -> {
                            if (chatRoomResource == null) {
                                Toast.makeText(this, "Received null data", Toast.LENGTH_LONG).show();
                                return;
                            }

                            switch (chatRoomResource.getStatus()) {
                                case SUCCESS:
                                    ChatRoom chatRoom = chatRoomResource.getData();
                                    mChatRoomAdapter.setChatRoom(chatRoom);
                                    mChatRoomAdapter.setUserId(UUID.fromString(currentUser.getId()));
                                    getAllMessages(chatRoom);
                                    initStomp(chatRoom, currentUser);
                                    break;
                                case ERROR:
                                    Toast.makeText(this, userResource.getMessage(), Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(this, "Unknown status", Toast.LENGTH_LONG).show();
                            }
                        });
            }

            mProgressBar.setVisibility(View.GONE);
        });
    }

    private void initFields() {
        mRecyclerView = findViewById(R.id.recycler_act_chat_room);
        mMessageEditText = findViewById(R.id.message_input_act_chat_room);
        mSendMessageButton = findViewById(R.id.button_send_act_chat_room);
        mProgressBar = findViewById(R.id.progress_act_chat_room);

        mToolbar = findViewById(R.id.toolbar_act_chat_room);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Чат // TODO");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mChatRoomAdapter = new ChatRoomAdapter();
        mRecyclerView.setAdapter(mChatRoomAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

//        mMessages = new LinkedList<>();
        mChatRoomAdapter.setMessagesList(mMessages);

        mProgressBar.setVisibility(View.VISIBLE);

        mChatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void getAllMessages(ChatRoom chatRoom) {
        mChatViewModel.getAllMessagesByChatId(chatRoom.getId()).observe(this, chatMessageResource -> {
            if (chatMessageResource == null) {
                Toast.makeText(this, "Received null data", Toast.LENGTH_LONG).show();
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
        });
    }

    private void initStomp(ChatRoom chatRoom, User currentUser) {
        OkHttpClient client = SelfSigningClientBuilder.createClient(this);

//        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://192.168.1.64:8080/chat", null, client);
//        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://10.0.2.2:8080/chat/" + chatRoom.getId().toString() + "/" + chatRoom.getFirstUserId().toString(), null, client);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://10.0.2.2:8080/chatRoom", null, client);

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
//                            toast("Stomp connection error");
                            break;
                        case CLOSED:
                            Log.d(TAG, "CLOSED: ");
//                            toast("Stomp connection closed");
                    }
                });

        // Receive greetings
        mStompClient.topic("/topic/chatRoom/" + chatRoom.getId().toString() + "/messages")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    Log.d(TAG, "Received " + message);
                    mMessages.add(mGson.fromJson(message.getPayload(), ChatMessage.class));
                    mChatRoomAdapter.setMessagesList(mMessages);
                    mRecyclerView.scrollToPosition(mMessages.size() - 1);
                });

        mStompClient.topic("/topic/chatRoom/" + chatRoom.getId() + "/" + currentUser.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    Log.d(TAG, "Sended: " + stompMessage.toString());
                });

        mStompClient.connect();

        mSendMessageButton.setOnClickListener(v -> {
            mStompClient.send("/topic/chatRoom/" + chatRoom.getId() + "/" + currentUser.getId(), mMessageEditText.getText().toString())
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
