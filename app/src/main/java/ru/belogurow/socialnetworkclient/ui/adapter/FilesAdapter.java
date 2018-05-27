package ru.belogurow.socialnetworkclient.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;

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
        private TextView mFileTypeTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mFilePreviewImageView = itemView.findViewById(R.id.item_file_preview_image);
            mTitleTextView = itemView.findViewById(R.id.item_file_title_text);
            mDateTextView = itemView.findViewById(R.id.item_file_date_text);
            mFileTypeTextView = itemView.findViewById(R.id.item_file_type_text);
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
        holder.mFileTypeTextView.setText(fileEntityDto.getFileType().toString());

        if (fileEntityDto.getFileType().equals(FileType.JPG)) {
            setImageWithGlide(holder, fileEntityDto);
        }
    }

    private void setImageWithGlide(ViewHolder viewHolder, FileEntityDto avatarFile) {
        GlideApp.with(viewHolder.itemView.getContext())
                .load(App.BASE_URL + avatarFile.getDataUrl())
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .signature(new ObjectKey(avatarFile.getDataUrl()))
                .into(viewHolder.mFilePreviewImageView);
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
