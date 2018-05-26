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
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;
import ru.belogurow.socialnetworkclient.ui.activity.AboutUserActivity;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder>{

    private List<UserDto> mUserList;
    private Context mContext;

    public void setUserList(List<UserDto> userList) {
        mUserList = userList;
        notifyDataSetChanged();
    }

    public UsersListAdapter(Context context) {
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mUserAvatarImageView;
        private TextView mFullnameTextView;
        private TextView mUsernameTextView;

        ViewHolder(View itemView) {
            super(itemView);

            mUserAvatarImageView = itemView.findViewById(R.id.item_user_avatar_imageView);
            mFullnameTextView = itemView.findViewById(R.id.item_user_fullname_textView);
            mUsernameTextView = itemView.findViewById(R.id.item_user_username_textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            Intent aboutUserActivity = new Intent(v.getContext(), AboutUserActivity.class);
            aboutUserActivity.putExtra(Extras.EXTRA_USER_ID, mUserList.get(position).getId());
            v.getContext().startActivity(aboutUserActivity);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserDto currentUser = mUserList.get(position);

        // Set data
        holder.mFullnameTextView.setText(currentUser.getName());
        holder.mUsernameTextView.setText(currentUser.getUsername());

        // Set avatar image
        if (currentUser.getUserProfile() != null && currentUser.getUserProfile().getAvatarFile() != null) {
            setImageWithGlide(currentUser.getUserProfile().getAvatarFile(), holder);
        }
    }

    private void setImageWithGlide(FileEntityDto avatarFile, ViewHolder viewHolder) {
        GlideApp.with(mContext)
                .load(App.BASE_URL + avatarFile.getDataUrl())
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .signature(new ObjectKey(avatarFile.getDataUrl()))
                .into(viewHolder.mUserAvatarImageView);
    }

    @Override
    public int getItemCount() {
        if (mUserList == null) {
            return 0;
        } else {
            return mUserList.size();
        }
    }
}
