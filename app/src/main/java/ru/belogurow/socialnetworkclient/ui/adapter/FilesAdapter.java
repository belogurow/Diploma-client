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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;
import ru.belogurow.socialnetworkclient.ui.activity.ChatRoomActivity;
import ru.belogurow.socialnetworkclient.ui.activity.ImageViewerActivity;
import ru.belogurow.socialnetworkclient.ui.activity.PdfViewerActivity;
import ru.belogurow.socialnetworkclient.ui.activity.StlViewerActivity;

//import ru.belogurow.socialnetworkclient.ui.activity.ImageViewerActivity;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private List<FileEntityDto> mFileEntityDtos;
    private Drawable defaultFileIcon;
    private Activity mActivity;
    private boolean isFilePickable;

    public FilesAdapter(Activity activity, boolean isFilePickable) {
        mFileEntityDtos = new ArrayList<>();
        mActivity = activity;
        this.isFilePickable = isFilePickable;

        defaultFileIcon = new IconicsDrawable(activity)
                .icon(FontAwesome.Icon.faw_file)
                .color(activity.getResources().getColor(R.color.md_grey_500))
                .sizeDp(48);
    }

    public void setFileEntityDtos(List<FileEntityDto> fileEntityDtos) {
        mFileEntityDtos.clear();
        mFileEntityDtos.addAll(fileEntityDtos);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            if (isFilePickable) {
                Intent result = new Intent();
                result.putExtra(Extras.EXTRA_FILE_ENTITY_DTO, mFileEntityDtos.get(position));
                mActivity.setResult(ChatRoomActivity.REQUEST_CODE_PICK_FROM_FILES, result);
                mActivity.finish();
            } else {
                switch (mFileEntityDtos.get(position).getFileType()) {
                    case STL:
                        Intent stlViewerActivity = new Intent(v.getContext(), StlViewerActivity.class);
                        stlViewerActivity.putExtra(Extras.EXTRA_FILE_ENTITY_DTO, mFileEntityDtos.get(position));
                        v.getContext().startActivity(stlViewerActivity);
                        break;
                    case PDF:
                        Intent pdfViewerIntent = new Intent(v.getContext(), PdfViewerActivity.class);
                        pdfViewerIntent.putExtra(Extras.EXTRA_FILE_ENTITY_DTO, mFileEntityDtos.get(position));
                        v.getContext().startActivity(pdfViewerIntent);
                        break;
                    case JPG:
                        Intent imageViewerIntent = new Intent(v.getContext(), ImageViewerActivity.class);
                        imageViewerIntent.putExtra(Extras.EXTRA_FILE_ENTITY_DTO, mFileEntityDtos.get(position));
                        mActivity.startActivity(imageViewerIntent);
                        break;
                    default:
                        break;
                }
            }

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

        holder.mFilePreviewImageView.setImageDrawable(defaultFileIcon);
        holder.mTitleTextView.setText(fileEntityDto.getTitle());
        holder.mDateTextView.setText(dateFormat.format(fileEntityDto.getUpdateTime()));
        holder.mFileTypeTextView.setText(fileEntityDto.getFileType().toString());

        setImageWithGlide(holder, fileEntityDto);
    }

    private void setImageWithGlide(ViewHolder viewHolder, FileEntityDto file) {
//        viewHolder.mFilePreviewImageView.setImageDrawable(defaultFileIcon);

        if (file.getFileType().equals(FileType.JPG)) {
            GlideApp.with(viewHolder.itemView.getContext())
                    .load(App.BASE_URL + file.getDataUrl())
//                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                    .signature(new ObjectKey(file.getDataUrl()))
                    .error(defaultFileIcon)
                    .into(viewHolder.mFilePreviewImageView);
        }
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
