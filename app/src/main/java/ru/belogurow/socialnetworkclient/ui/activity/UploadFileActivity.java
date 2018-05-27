package ru.belogurow.socialnetworkclient.ui.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.model.FileEntity;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.chat.viewModel.FileViewModel;
import ru.belogurow.socialnetworkclient.common.file.FileUtils;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class UploadFileActivity extends AppCompatActivity {

    private final int PICK_FILE = 1;

    private UserViewModel mUserViewModel;
    private FileViewModel mFileViewModel;

    private Button mPickFileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        initFields();


        mPickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
                chooserIntent.setType("*/*");
                chooserIntent = Intent.createChooser(chooserIntent, getString(R.string.pick_file));

                startActivityForResult(chooserIntent, PICK_FILE);
            }
        });
    }

    private void initFields() {
        mPickFileButton = findViewById(R.id.act_upload_file_pick_file_button);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mFileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, R.string.cannot_pick_file, Toast.LENGTH_SHORT).show();
                return;
            }


            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileType(FileType.PDF);
            fileEntity.setTitle("TODO get title");

            File pickedFile = FileUtils.openPath(this, data.getData());

            mUserViewModel.userFromDB().observe(this, userBDResource -> {
                // TODO: 27.05.2018 INIT PROGRESS BAR
//                mProgressBar.setVisibility(View.VISIBLE);

                if (userBDResource != null && userBDResource.getStatus() == NetworkStatus.SUCCESS) {
                    fileEntity.setAuthorId(userBDResource.getData().getId());
                    mFileViewModel.uploadFile(fileEntity, pickedFile, false).observe(this, fileEntityDtoResource -> {
                        if (fileEntityDtoResource == null) {
                            Toast.makeText(this, R.string.received_null_data, Toast.LENGTH_LONG).show();
                            return;
                        }

                        switch (fileEntityDtoResource.getStatus()) {
                            case SUCCESS:
                                Toast.makeText(this, R.string.successful_upload, Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR:
                                Toast.makeText(this, fileEntityDtoResource.getMessage(), Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(this, R.string.unknown_status, Toast.LENGTH_LONG).show();
                        }
                    });

                }
                // TODO: 27.05.2018 CLOSE PROGRESS BAR
            });
        }
    }
}
