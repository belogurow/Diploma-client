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
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;
import ru.belogurow.socialnetworkclient.ui.activity.ImageViewerActivity;
import ru.belogurow.socialnetworkclient.ui.activity.PdfViewerActivity;
import ru.belogurow.socialnetworkclient.ui.activity.StlViewerActivity;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;

//import ru.belogurow.socialnetworkclient.ui.activity.ImageViewerActivity;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final Integer MY_MESSAGE_TEXT = 10;
    private static final Integer MY_MESSAGE_IMAGE = 11;
    private static final Integer MY_MESSAGE_FILE = 12;

    private static final Integer ANOTHER_MESSAGE_TEXT = 20;
    private static final Integer ANOTHER_MESSAGE_IMAGE = 21;
    private static final Integer ANOTHER_MESSAGE_FILE = 22;

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
        private TextView mMessageTextView;
        private TextView mTimeTextView;

        ViewHolderMyMessage(View itemView) {
            super(itemView);

            mMessageTextView = itemView.findViewById(R.id.item_message_right_text);
            mTimeTextView = itemView.findViewById(R.id.item_message_right_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openFile(getLayoutPosition());
        }
    }

    class ViewHolderMyMessageImage extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageFileImageView;
        private TextView mTimeTextView;

        ViewHolderMyMessageImage(View itemView) {
            super(itemView);

            mImageFileImageView = itemView.findViewById(R.id.item_message_image_right_image);
            mTimeTextView = itemView.findViewById(R.id.item_message_image_right_time);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            openFile(getLayoutPosition());
        }
    }

    class ViewHolderMyMessageFile extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mFileView;
        private ImageView mFileIconImageView;
        private TextView mFileTitleTextView;
        private TextView mFileTypeTextView;
        private TextView mFileDateTextView;

        private TextView mTimeTextView;

        ViewHolderMyMessageFile(View itemView) {
            super(itemView);

            mFileView = itemView.findViewById(R.id.item_message_file_right_include_item_file);
            mFileIconImageView = mFileView.findViewById(R.id.item_file_preview_image);
            mFileTitleTextView = mFileView.findViewById(R.id.item_file_title_text);
            mFileTypeTextView = mFileView.findViewById(R.id.item_file_type_text);
            mFileDateTextView = mFileView.findViewById(R.id.item_file_date_text);

            mTimeTextView = itemView.findViewById(R.id.item_message_file_right_time);

            mFileTypeTextView.setVisibility(View.GONE);
            mFileIconImageView.setImageDrawable(defaultFileIcon);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openFile(getLayoutPosition());
        }
    }

    class ViewHolderAnotherMessage extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMessageTextView;
        private TextView mTimeTextView;

        ViewHolderAnotherMessage(View itemView) {
            super(itemView);

            mMessageTextView = itemView.findViewById(R.id.item_message_left_text);
            mTimeTextView = itemView.findViewById(R.id.item_message_left_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openFile(getLayoutPosition());
        }
    }

    class ViewHolderAnotherMessageImage extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageFileImageView;
        private TextView mTimeTextView;

        public ViewHolderAnotherMessageImage(View itemView) {
            super(itemView);

            mImageFileImageView = itemView.findViewById(R.id.item_message_image_left_image);
            mTimeTextView = itemView.findViewById(R.id.item_message_image_left_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openFile(getLayoutPosition());
        }
    }

    class ViewHolderAnotherMessageFile extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mFileView;
        private ImageView mFileIconImageView;
        private TextView mFileTitleTextView;
        private TextView mFileTypeTextView;
        private TextView mFileDateTextView;

        private TextView mTimeTextView;

        public ViewHolderAnotherMessageFile(View itemView) {
            super(itemView);

            mFileView = itemView.findViewById(R.id.item_message_file_left_include_item_file);
            mFileIconImageView = mFileView.findViewById(R.id.item_file_preview_image);
            mFileTitleTextView = mFileView.findViewById(R.id.item_file_title_text);
            mFileTypeTextView = mFileView.findViewById(R.id.item_file_type_text);
            mFileDateTextView = mFileView.findViewById(R.id.item_file_date_text);

            mTimeTextView = itemView.findViewById(R.id.item_message_file_left_time);

            mFileTypeTextView.setVisibility(View.GONE);
            mFileIconImageView.setImageDrawable(defaultFileIcon);


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
                if (message.getFileEntity() == null) {
                    return MY_MESSAGE_TEXT;
                } else {
                    FileEntityDto fileEntityDto = message.getFileEntity();

                    switch (fileEntityDto.getFileType()) {
                        case JPG:
                            return MY_MESSAGE_IMAGE;
                        default:
                            return MY_MESSAGE_FILE;
                    }
                }
            } else {
                if (message.getFileEntity() == null) {
                    return ANOTHER_MESSAGE_TEXT;
                } else {
                    FileEntityDto fileEntityDto = message.getFileEntity();

                    switch (fileEntityDto.getFileType()) {
                        case JPG:
                            return ANOTHER_MESSAGE_IMAGE;
                        default:
                            return ANOTHER_MESSAGE_FILE;
                    }
                }
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MY_MESSAGE_TEXT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new ViewHolderMyMessage(view);
        } else if (viewType == ANOTHER_MESSAGE_TEXT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new ViewHolderAnotherMessage(view);
        } else if (viewType == MY_MESSAGE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_image_right, parent, false);
            return new ViewHolderMyMessageImage(view);
        } else if (viewType == ANOTHER_MESSAGE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_image_left, parent, false);
            return new ViewHolderAnotherMessageImage(view);
        } else if (viewType == MY_MESSAGE_FILE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_file_right, parent, false);
            return new ViewHolderMyMessageFile(view);
        } else if (viewType == ANOTHER_MESSAGE_FILE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_file_left, parent, false);
            return new ViewHolderAnotherMessageFile(view);
        } else {
            throw new RuntimeException("The type has to be MY_MESSAGE_TEXT or ANOTHER_MESSAGE_TEXT");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageDto message = mMessages.get(position);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

        if (holder.getItemViewType() == MY_MESSAGE_TEXT) {
            // my message
            ViewHolderMyMessage holderMyMessage = (ViewHolderMyMessage) holder;

            holderMyMessage.mTimeTextView.setText(dateFormat.format(message.getDate()));
            holderMyMessage.mMessageTextView.setText(message.getText());

        } else if (holder.getItemViewType() == MY_MESSAGE_IMAGE) {
            // my message image
            ViewHolderMyMessageImage holderMyMessageImage = (ViewHolderMyMessageImage) holder;

            holderMyMessageImage.mTimeTextView.setText(dateFormat.format(message.getDate()));
            setImageWithGlideRight(holderMyMessageImage, message.getFileEntity());

        } else if (holder.getItemViewType() == MY_MESSAGE_FILE) {
            // my message file
            ViewHolderMyMessageFile viewHolderMyMessageFile = (ViewHolderMyMessageFile) holder;
            FileEntityDto fileEntityDto = message.getFileEntity();

            viewHolderMyMessageFile.mFileTitleTextView.setText(fileEntityDto.getTitle());
            viewHolderMyMessageFile.mFileDateTextView.setText(fileEntityDto.getFileType().toString().toUpperCase());
            viewHolderMyMessageFile.mTimeTextView.setText(dateFormat.format(message.getDate()));

        } else if (holder.getItemViewType() == ANOTHER_MESSAGE_TEXT) {
            // another message
            ViewHolderAnotherMessage viewHolderAnotherMessage = (ViewHolderAnotherMessage) holder;

            viewHolderAnotherMessage.mTimeTextView.setText(dateFormat.format(message.getDate()));
            viewHolderAnotherMessage.mMessageTextView.setText(message.getText());

        } else if (holder.getItemViewType() == ANOTHER_MESSAGE_IMAGE) {
            // another message image
            ViewHolderAnotherMessageImage holderAnotherMessageImage = (ViewHolderAnotherMessageImage) holder;

            holderAnotherMessageImage.mTimeTextView.setText(dateFormat.format(message.getDate()));
            setImageWithGlideLeft(holderAnotherMessageImage, message.getFileEntity());

        } else if (holder.getItemViewType() == ANOTHER_MESSAGE_FILE) {
            // another message file
            ViewHolderAnotherMessageFile holderAnotherMessageFile = (ViewHolderAnotherMessageFile) holder;
            FileEntityDto fileEntityDto = message.getFileEntity();

            holderAnotherMessageFile.mFileTitleTextView.setText(fileEntityDto.getTitle());
            holderAnotherMessageFile.mFileDateTextView.setText(fileEntityDto.getFileType().toString().toUpperCase());
            holderAnotherMessageFile.mTimeTextView.setText(dateFormat.format(message.getDate()));
        }
    }

    private void setImageWithGlideLeft(ViewHolderAnotherMessageImage holder, FileEntityDto file) {
        GlideApp.with(holder.itemView.getContext())
                .load(App.BASE_URL + file.getDataUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .signature(new ObjectKey(file.getDataUrl()))
                .error(defaultFileIcon)
                .into(holder.mImageFileImageView);
    }

    private void setImageWithGlideRight(ViewHolderMyMessageImage holder, FileEntityDto file) {
        GlideApp.with(holder.itemView.getContext())
                .load(App.BASE_URL + file.getDataUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .signature(new ObjectKey(file.getDataUrl()))
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
                case PDF:
                    Intent pdfViewerIntent = new Intent(mActivity, PdfViewerActivity.class);
                    pdfViewerIntent.putExtra(Extras.EXTRA_FILE_ENTITY_DTO, chatMessageDto.getFileEntity());
                    mActivity.startActivity(pdfViewerIntent);
                    break;
                case JPG:
                    Intent imageViewerIntent = new Intent(mActivity, ImageViewerActivity.class);
                    imageViewerIntent.putExtra(Extras.EXTRA_FILE_ENTITY_DTO, chatMessageDto.getFileEntity());
                    mActivity.startActivity(imageViewerIntent);
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
