package ru.belogurow.socialnetworkclient.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.ChatMessageDto;
import ru.belogurow.socialnetworkclient.chat.dto.ChatRoomDto;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final Integer MY_MESSAGE = 1;
    private static final Integer ANOTHER_MESSAGE = 2;

    private List<ChatMessageDto> mMessages;
    private ChatRoomDto mChatRoomDto;
    private UserDto currentUser;

    public ChatRoomAdapter() {
        mMessages = new LinkedList<>();
    }

    public void setMessagesList(List<ChatMessageDto> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessageDto messageDto) {
        mMessages.add(messageDto);
        notifyItemInserted(mMessages.size() - 1);
    }

    public void setChatRoomDto(ChatRoomDto chatRoomDto) {
        mChatRoomDto = chatRoomDto;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    class ViewHolderMyMessage extends RecyclerView.ViewHolder{
        private TextView mMessageTextView;
        private TextView mTimeTextView;

        ViewHolderMyMessage(View itemView) {
            super(itemView);

            mMessageTextView = itemView.findViewById(R.id.item_message_right_text);
            mTimeTextView = itemView.findViewById(R.id.item_message__right_time);
        }
    }

    class ViewHolderAnotherMessage extends RecyclerView.ViewHolder{
        private TextView mMessageTextView;
        private TextView mTimeTextView;

        ViewHolderAnotherMessage(View itemView) {
            super(itemView);

            mMessageTextView = itemView.findViewById(R.id.item_message_left_text);
            mTimeTextView = itemView.findViewById(R.id.item_message_left_time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessageDto message = mMessages.get(position);

        if (currentUser.equalsById(mChatRoomDto.getFirstUser()) || currentUser.equalsById(mChatRoomDto.getSecondUser())) {
            if (message.getAuthorId().equals(currentUser.getId())) {
                return MY_MESSAGE;
            } else {
                return ANOTHER_MESSAGE;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MY_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new ViewHolderMyMessage(view);
        } else if (viewType == ANOTHER_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new ViewHolderAnotherMessage(view);
        } else {
            throw new RuntimeException("The type has to be MY_MESSAGE or ANOTHER_MESSAGE");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageDto message = mMessages.get(position);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

        if (holder.getItemViewType() == MY_MESSAGE) {
            ((ViewHolderMyMessage) holder).mMessageTextView.setText(message.getText());
            ((ViewHolderMyMessage) holder).mTimeTextView.setText(dateFormat.format(message.getDate()));
        } else {
            ((ViewHolderAnotherMessage) holder).mMessageTextView.setText(message.getText());
            ((ViewHolderAnotherMessage) holder).mTimeTextView.setText(dateFormat.format(message.getDate()));
        }
    }

    @Override
    public int getItemCount() {
        if (mMessages == null) {
            return 0;
        } else {
            return mMessages.size();
        }
    }


}
