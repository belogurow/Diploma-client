package ru.belogurow.socialnetworkclient.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.ChatRoomDto;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.ui.activity.ChatRoomActivity;
import ru.belogurow.socialnetworkclient.users.model.User;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder>{

    private List<ChatRoomDto> mChatRoomDtos;
    private User currentUser;

    public void setChatList(List<ChatRoomDto> chatRooms) {
        mChatRoomDtos = chatRooms;
        notifyDataSetChanged();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mFullnameTextView;
        private TextView mMessageTextView;

        ViewHolder(View itemView) {
            super(itemView);

            mMessageTextView = itemView.findViewById(R.id.item_user_last_message_text);
            mFullnameTextView = itemView.findViewById(R.id.item_user_last_message_fullname_text);
//            mTimeTextView = itemView.findViewById(R.id.item_message_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            Intent chatRoomActivity = new Intent(v.getContext(), ChatRoomActivity.class);
            chatRoomActivity.putExtra(Extras.EXTRA_CHAT_ROOM_DTO, mChatRoomDtos.get(position));
            v.getContext().startActivity(chatRoomActivity);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_last_message, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoomDto chatRoomDto = mChatRoomDtos.get(position);

        // Set data
        if (currentUser.getIdAsUUID().equals(chatRoomDto.getLastChatMessage().getAuthorId())) {
            holder.mMessageTextView.setText(holder.itemView.getContext().getResources().getString(R.string.you, chatRoomDto.getLastChatMessage().getText()));
        } else {
            holder.mMessageTextView.setText(chatRoomDto.getLastChatMessage().getText());
        }

        if (currentUser.equalsById(chatRoomDto.getFirstUser())) {
            holder.mFullnameTextView.setText(chatRoomDto.getSecondUser().getName());
        } else {
            holder.mFullnameTextView.setText(chatRoomDto.getFirstUser().getName());
        }
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    @Override
    public int getItemCount() {
        if (mChatRoomDtos == null) {
            return 0;
        } else {
            return mChatRoomDtos.size();
        }
    }
}
