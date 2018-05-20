package ru.belogurow.socialnetworkclient.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.model.Message;
import ru.belogurow.socialnetworkclient.ui.adapter.ChatAdapter;
import ru.belogurow.socialnetworkclient.web.SelfSigningClientBuilder;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class FragmentChat extends Fragment {

    private static final String TAG = FragmentChat.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private EditText mMessageEditText;
    private Button mSendMessageButton;
    private ChatAdapter mChatAdapter;
    private StompClient mStompClient;
    private Gson mGson = new GsonBuilder().create();

    private List<Message> mMessages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_chat, container, false);

        initFields(view);

        OkHttpClient client = SelfSigningClientBuilder.createClient(getActivity());
//        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://10.0.2.2:8080/chat", null, client);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://192.168.1.64:8080/chat", null, client);

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
        mStompClient.topic("/topic/messages")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    Log.d(TAG, "Received " + message);
                    mMessages.add(mGson.fromJson(message.getPayload(), Message.class));
                    mChatAdapter.setUserList(mMessages);
                });

        mStompClient.topic("/topic/chat")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    Log.d(TAG, "sended: " + stompMessage.toString());
                });

        mStompClient.connect();

        mSendMessageButton.setOnClickListener(v -> {
            mStompClient.send("/topic/chat", mMessageEditText.getText().toString())
                    .compose(applySchedulers())
                    .subscribe(() -> {
                        Log.d(TAG, "STOMP echo send successfully");
                    }, throwable -> {
                        Log.e(TAG, "Error send STOMP echo", throwable);
                    });

            mMessageEditText.getText().clear();
        });

        return view;
    }

    private void initFields(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_frag_chat);
        mMessageEditText = view.findViewById(R.id.frag_chat_message_input);
        mSendMessageButton = view.findViewById(R.id.frag_chat_button_send);

        mChatAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // TODO: 20.05.2018 GET MESSAGES FROM SERVER
        mMessages = new LinkedList<>();
        mChatAdapter.setUserList(mMessages);
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onDestroy() {
        mStompClient.disconnect();
        super.onDestroy();
    }


}
