package ru.belogurow.socialnetworkclient.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.model.ChatRoom;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder>{

    private List<ChatRoom> mChatRooms;

    public void setChatList(List<ChatRoom> chatRooms) {
        mChatRooms = chatRooms;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
//        private TextView mMessageTextView;
//        private TextView mTimeTextView;

        ViewHolder(View itemView) {
            super(itemView);

//            mMessageTextView = itemView.findViewById(R.id.item_message_text);
//            mTimeTextView = itemView.findViewById(R.id.item_message_time);

//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            int position = getLayoutPosition();
//
//            Intent aboutUserActivity = new Intent(v.getContext(), AboutUserActivity.class);
//            aboutUserActivity.putExtra(Extras.EXTRA_USER_ID, mUserList.get(position).getId());
//            v.getContext().startActivity(aboutUserActivity);
//        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoom message = mChatRooms.get(position);

        // Set data
//        holder.mMessageTextView.setText(message.getText());

//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        holder.mTimeTextView.setText(dateFormat.format(message.getDate()));
    }

    @Override
    public int getItemCount() {
        if (mChatRooms == null) {
            return 0;
        } else {
            return mChatRooms.size();
        }
    }
}
