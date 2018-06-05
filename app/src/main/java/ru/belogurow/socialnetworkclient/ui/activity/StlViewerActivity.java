package ru.belogurow.socialnetworkclient.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.common.extra.Extras;

public class StlViewerActivity extends AppCompatActivity {

    private static final String TAG = StlViewerActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;

    private FileEntityDto fileEntityDto;
    private File stlFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stl_viewer);

        if (getIntent().hasExtra(Extras.EXTRA_FILE_ENTITY_DTO)) {
            fileEntityDto = (FileEntityDto) getIntent().getSerializableExtra(Extras.EXTRA_FILE_ENTITY_DTO);

            initFields(fileEntityDto);
            askPermissions();

            loadStl();

        } else {
            Toast.makeText(this, "Receive null data from previous activity, retry.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFields(FileEntityDto fileEntityDto) {
        mWebView = findViewById(R.id.act_stl_viewer_web);
        mProgressBar = findViewById(R.id.act_stl_viewer_progress);
        showProgressBar();

        mToolbar = findViewById(R.id.act_stl_viewer_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(fileEntityDto.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showWebView(String stl) {
        mWebView.loadUrl("file:///android_asset/StlViewer.html");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                hideProgressBar();
                Log.d(TAG, "onPageFinished: " + stl);

                mWebView.loadUrl("javascript:loadStl('" + stl + "')"); //if passing in an object. Mapping may need to take place
            }
        });
    }

    private void askPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void loadStl() {
        // Show stl model in web view
        Call<ResponseBody> call = App.sFileWebService.getFileById(fileEntityDto.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        File path = Environment.getExternalStorageDirectory();
                        stlFile = new File(path, fileEntityDto.getTitle() + fileEntityDto.getFileType().toString().toLowerCase());
                        FileOutputStream fileOutputStream = new FileOutputStream(stlFile);
                        IOUtils.write(response.body().bytes(), fileOutputStream);

                        showWebView(stlFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        hideProgressBar();
                    }
                } else {
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(StlViewerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadStl();
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (stlFile != null && stlFile.exists()) {
            stlFile.delete();
        }
    }
}
