package ru.belogurow.socialnetworkclient.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private List<FileEntityDto> mFileEntityDtos;

    public void setFileEntityDtos(List<FileEntityDto> fileEntityDtos) {
        mFileEntityDtos = fileEntityDtos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mFilePreviewImageView;
        private TextView mTitleTextView;
        private TextView mDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mFilePreviewImageView = itemView.findViewById(R.id.item_file_preview_image);
            mTitleTextView = itemView.findViewById(R.id.item_file_title_text);
            mDateTextView = itemView.findViewById(R.id.item_file_date_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileEntityDto fileEntityDto = mFileEntityDtos.get(position);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

        holder.mTitleTextView.setText(fileEntityDto.getTitle());
        holder.mDateTextView.setText(dateFormat.format(fileEntityDto.getUpdateTime()));
    }

    @Override
    public int getItemCount() {
        if (mFileEntityDtos == null) {
            return 0;
        } else {
            return mFileEntityDtos.size();
        }
    }
}
