package ru.belogurow.socialnetworkclient.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.ChatMessageDto;
import ru.belogurow.socialnetworkclient.chat.dto.ChatRoomDto;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;
import ru.belogurow.socialnetworkclient.ui.activity.StlViewerActivity;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final Integer MY_MESSAGE = 1;
    private static final Integer ANOTHER_MESSAGE = 2;

    private List<ChatMessageDto> mMessages;
    private ChatRoomDto mChatRoomDto;
    private UserDto currentUser;
    private Activity mActivity;
    private Drawable defaultFileIcon;

    public ChatRoomAdapter(Activity activity) {
        mActivity = activity;
        mMessages = new LinkedList<>();

        defaultFileIcon = new IconicsDrawable(activity)
                .icon(FontAwesome.Icon.faw_file)
                .color(activity.getResources().getColor(R.color.md_grey_500))
                .sizeDp(48);
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

    class ViewHolderMyMessage extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageFileImageView;

        private View mFileView;
        private ImageView mFileIconImageView;
        private TextView mFileTitleTextView;
        private TextView mFileTypeTextView;
        private TextView mFileDateTextView;

        private TextView mMessageTextView;
        private TextView mTimeTextView;

        ViewHolderMyMessage(View itemView) {
            super(itemView);

            mFileView = itemView.findViewById(R.id.item_message_right_include_item_file);
            mFileIconImageView = mFileView.findViewById(R.id.item_file_preview_image);
            mFileTitleTextView = mFileView.findViewById(R.id.item_file_title_text);
            mFileTypeTextView = mFileView.findViewById(R.id.item_file_type_text);
            mFileDateTextView = mFileView.findViewById(R.id.item_file_date_text);

            mImageFileImageView = itemView.findViewById(R.id.item_message_right_image);

            mMessageTextView = itemView.findViewById(R.id.item_message_right_text);
            mTimeTextView = itemView.findViewById(R.id.item_message_right_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openFile(getLayoutPosition());
        }
    }

    class ViewHolderAnotherMessage extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageFileImageView;

        private View mFileView;
        private ImageView mFileIconImageView;
        private TextView mFileTitleTextView;
        private TextView mFileTypeTextView;
        private TextView mFileDateTextView;

        private TextView mMessageTextView;
        private TextView mTimeTextView;

        ViewHolderAnotherMessage(View itemView) {
            super(itemView);

            mFileView = itemView.findViewById(R.id.item_message_left_include_item_file);
            mFileIconImageView = mFileView.findViewById(R.id.item_file_preview_image);
            mFileTitleTextView = mFileView.findViewById(R.id.item_file_title_text);
            mFileTypeTextView = mFileView.findViewById(R.id.item_file_type_text);
            mFileDateTextView = mFileView.findViewById(R.id.item_file_date_text);

            mImageFileImageView = itemView.findViewById(R.id.item_message_left_image);

            mMessageTextView = itemView.findViewById(R.id.item_message_left_text);
            mTimeTextView = itemView.findViewById(R.id.item_message_left_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openFile(getLayoutPosition());
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
            // my message
            ViewHolderMyMessage holderMyMessage = (ViewHolderMyMessage) holder;
            holderMyMessage.mTimeTextView.setText(dateFormat.format(message.getDate()));

            if (message.getFileEntity() == null) {
                holderMyMessage.mMessageTextView.setText(message.getText());
                holderMyMessage.mMessageTextView.setVisibility(View.VISIBLE);
            } else {
                FileEntityDto fileEntityDto = message.getFileEntity();
                if (fileEntityDto.getFileType().equals(FileType.JPG)) {
                    setImageWithGlideRight(holderMyMessage, fileEntityDto);
                    holderMyMessage.mImageFileImageView.setVisibility(View.VISIBLE);
                } else {
                    holderMyMessage.mFileIconImageView.setImageDrawable(defaultFileIcon);
                    holderMyMessage.mFileTitleTextView.setText(fileEntityDto.getTitle());
                    holderMyMessage.mFileDateTextView.setText(fileEntityDto.getFileType().toString().toUpperCase());
                    holderMyMessage.mFileTypeTextView.setVisibility(View.GONE);
                    holderMyMessage.mFileView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            // another message
            ViewHolderAnotherMessage holderAnotherMessage = (ViewHolderAnotherMessage) holder;
            holderAnotherMessage.mTimeTextView.setText(dateFormat.format(message.getDate()));

            if (message.getFileEntity() == null) {
                holderAnotherMessage.mMessageTextView.setText(message.getText());
                holderAnotherMessage.mMessageTextView.setVisibility(View.VISIBLE);
            } else {
                FileEntityDto fileEntityDto = message.getFileEntity();
                if (fileEntityDto.getFileType().equals(FileType.JPG)) {
                    setImageWithGlideLeft(holderAnotherMessage, fileEntityDto);
                    holderAnotherMessage.mImageFileImageView.setVisibility(View.VISIBLE);
                } else {
                    holderAnotherMessage.mFileIconImageView.setImageDrawable(defaultFileIcon);
                    holderAnotherMessage.mFileTitleTextView.setText(fileEntityDto.getTitle());
                    holderAnotherMessage.mFileDateTextView.setText(fileEntityDto.getFileType().toString().toUpperCase());
                    holderAnotherMessage.mFileTypeTextView.setVisibility(View.GONE);
                    holderAnotherMessage.mFileView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setImageWithGlideLeft(ViewHolderAnotherMessage holder, FileEntityDto file) {
        GlideApp.with(holder.itemView.getContext())
                .load(App.BASE_URL + file.getDataUrl())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .signature(new ObjectKey(file.getDataUrl()))
                .error(defaultFileIcon)
                .into(holder.mImageFileImageView);
    }

    private void setImageWithGlideRight(ViewHolderMyMessage holder, FileEntityDto file) {
        GlideApp.with(holder.itemView.getContext())
                .load(App.BASE_URL + file.getDataUrl())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .signature(new ObjectKey(file.getDataUrl()))
                .error(defaultFileIcon)
                .into(holder.mImageFileImageView);
    }

    private void openFile(int position) {
        ChatMessageDto chatMessageDto = mMessages.get(position);

        if (chatMessageDto.getFileEntity() != null) {
            switch (chatMessageDto.getFileEntity().getFileType()) {
                case STL:
                    Intent stlViewer = new Intent(mActivity, StlViewerActivity.class);
                    stlViewer.putExtra(Extras.EXTRA_FILE_ENTITY_DTO, chatMessageDto.getFileEntity());
                    mActivity.startActivity(stlViewer);
                    break;
                default:
                    break;
            }
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
