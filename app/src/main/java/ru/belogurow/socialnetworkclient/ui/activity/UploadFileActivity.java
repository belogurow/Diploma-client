package ru.belogurow.socialnetworkclient.ui.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileEntity;
import ru.belogurow.socialnetworkclient.chat.model.FileType;
import ru.belogurow.socialnetworkclient.chat.viewModel.FileViewModel;
import ru.belogurow.socialnetworkclient.common.extra.Extras;
import ru.belogurow.socialnetworkclient.common.file.FileUtils;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class UploadFileActivity extends AppCompatActivity {

    private final int PICK_FILE = 1;

    private UserViewModel mUserViewModel;
    private FileViewModel mFileViewModel;

    private Button mPickFileButton;
    private Button mUploadButton;
    private Spinner mFileTypeSpinner;
    private TextInputLayout mTitleTextInput;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;

    private File choosenFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        initFields();
        clearFields();


        mPickFileButton.setOnClickListener(v -> {
            Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
            chooserIntent.setType("*/*");
            chooserIntent = Intent.createChooser(chooserIntent, getString(R.string.pick_file));

            startActivityForResult(chooserIntent, PICK_FILE);
        });

        mUploadButton.setOnClickListener(v -> {
            if (validateFields()) {
                uploadFile();
            }
        });
    }

    private void initFields() {
        mPickFileButton = findViewById(R.id.act_upload_file_pick_file_button);
        mTitleTextInput = findViewById(R.id.act_upload_file_title_input);

        mProgressBar = findViewById(R.id.act_upload_file_progress);
        hideProgressBar();

        mFileTypeSpinner = findViewById(R.id.act_upload_file_type_spinner);
        ArrayAdapter<String> fileTypesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, FileType.getTypes());
        mFileTypeSpinner.setAdapter(fileTypesAdapter);

        mUploadButton = findViewById(R.id.act_upload_file_upload_button);

        mToolbar = findViewById(R.id.act_upload_file_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.upload_file);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mFileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
    }

    private void uploadFile() {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileType(FileType.valueOf(mFileTypeSpinner.getSelectedItem().toString()));
        fileEntity.setTitle(mTitleTextInput.getEditText().getText().toString());

        mUserViewModel.userFromDB().observe(this, userBDResource -> {
            showProgressBar();

            if (userBDResource != null && userBDResource.getStatus() == NetworkStatus.SUCCESS) {
                fileEntity.setAuthorId(userBDResource.getData().getId());
                mFileViewModel.uploadFile(fileEntity, choosenFile, false).observe(this, fileEntityDtoResource -> {
                    if (fileEntityDtoResource == null) {
                        Toast.makeText(this, R.string.received_null_data, Toast.LENGTH_LONG).show();
                        hideProgressBar();
                        return;
                    }

                    switch (fileEntityDtoResource.getStatus()) {
                        case SUCCESS:
                            Toast.makeText(this, R.string.successful_upload, Toast.LENGTH_SHORT).show();
                            getResultOfFile(fileEntityDtoResource.getData());
                            break;
                        case ERROR:
                            Toast.makeText(this, fileEntityDtoResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(this, R.string.unknown_status, Toast.LENGTH_LONG).show();
                    }
                    hideProgressBar();
                });
            } else {
                hideProgressBar();
            }
        });
    }

    private void getResultOfFile(FileEntityDto fileEntityDto) {
        if (getIntent().hasExtra(Extras.EXTRA_FROM_CHAT)) {
            boolean sendResultToChat = getIntent().getBooleanExtra(Extras.EXTRA_FROM_CHAT, false);

            if (sendResultToChat) {
                Intent result = new Intent();
                result.putExtra(Extras.EXTRA_FILE_ENTITY_DTO, fileEntityDto);
                setResult(ChatRoomActivity.REQUEST_CODE_PICK_FROM_STORAGE, result);
                finish();
            } else {
                showSuccessfulAlertDialog();
            }
        } else {
            showSuccessfulAlertDialog();
        }
    }

    private void showSuccessfulAlertDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_file_upload_message)
                .setTitle(R.string.dialog_file_upload_title)
                .setPositiveButton(R.string.yes, (dialog, which) -> clearFields())
                .setNegativeButton(R.string.no, (dialog, which) -> finish())
                .show();
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void clearFields() {
        choosenFile = null;
        mUploadButton.setEnabled(false);
        mTitleTextInput.getEditText().setText("");
    }

    private boolean validateFields() {
        EditText mTitleEditText = mTitleTextInput.getEditText();

        String emptyField = getString(R.string.empty_field);

        if (mTitleEditText != null) {
            if (mTitleEditText.getText().length() == 0) {
                mTitleTextInput.setError(emptyField);
                return false;
            } else {
                mTitleTextInput.setErrorEnabled(false);
            }
        }

        if (choosenFile != null) {
            if (!choosenFile.exists()) {
                Toast.makeText(this, "File doesn't exists", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Pick a file", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == Activity.RESULT_OK) {
            if (data == null ) {
                Toast.makeText(this, R.string.cannot_pick_file, Toast.LENGTH_SHORT).show();
                return;
            }

            choosenFile = FileUtils.openPath(this, data.getData());
            mUploadButton.setEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Finish activity when press back button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
