package ru.belogurow.socialnetworkclient.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;

import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.web.GlideApp;

public class ImageViewerActivity extends AppCompatActivity {

    private FileEntityDto currentFile;
    private PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        initFields();

        if (getIntent().hasExtra(Extras.EXTRA_FILE_ENTITY_DTO)) {
            currentFile = (FileEntityDto) getIntent().getSerializableExtra(Extras.EXTRA_FILE_ENTITY_DTO);

            if (currentFile.getFileType().equals(FileType.JPG)) {
                GlideApp.with(this)
                        .load(App.BASE_URL + currentFile.getDataUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
//                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                        .error(defaultFileIcon)
                        .into(mPhotoView);
            } else {
                Toast.makeText(this, "File not must be image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initFields() {
        mPhotoView = findViewById(R.id.act_image_viewer_photoview);
    }
}
