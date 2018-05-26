package ru.belogurow.socialnetworkclient.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.ChatRoomDto;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;
import ru.belogurow.socialnetworkclient.ui.activity.ChatRoomActivity;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder>{

    private List<ChatRoomDto> mChatRoomDtos;
    private UserDto currentUser;
    private Context mContext;

    public void setChatList(List<ChatRoomDto> chatRooms) {
        mChatRoomDtos = chatRooms;
        notifyDataSetChanged();
    }

    public DialogsAdapter(Context context) {
        mContext = context;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageViewAvatar;
        private TextView mFullnameTextView;
        private TextView mMessageTextView;

        ViewHolder(View itemView) {
            super(itemView);

            mMessageTextView = itemView.findViewById(R.id.item_user_last_message_text);
            mFullnameTextView = itemView.findViewById(R.id.item_user_last_message_fullname_text);
            mImageViewAvatar = itemView.findViewById(R.id.item_user_last_message_avatar_image);
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
        UserDto anotherUser = null;

        // Set message text
        if (currentUser.getId().equals(chatRoomDto.getLastChatMessage().getAuthorId())) {
            holder.mMessageTextView.setText(holder.itemView.getContext().getResources().getString(R.string.you, chatRoomDto.getLastChatMessage().getText()));
        } else {
            holder.mMessageTextView.setText(chatRoomDto.getLastChatMessage().getText());
        }

        // Set another user
        if (currentUser.equalsById(chatRoomDto.getFirstUser())) {
            anotherUser = chatRoomDto.getSecondUser();
        } else {
            anotherUser = chatRoomDto.getFirstUser();
        }

        // Set fullname
        holder.mFullnameTextView.setText(anotherUser.getName());

        // Set avatar image
        if (anotherUser.getUserProfile() != null && anotherUser.getUserProfile().getAvatarFile() != null) {
            setImageWithGlide(anotherUser.getUserProfile().getAvatarFile(), holder);
        }
    }

    private void setImageWithGlide(FileEntityDto avatarFile, ViewHolder viewHolder) {
        GlideApp.with(mContext)
                .load(App.BASE_URL + avatarFile.getDataUrl())
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .signature(new ObjectKey(avatarFile.getDataUrl()))
                .into(viewHolder.mImageViewAvatar);
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
