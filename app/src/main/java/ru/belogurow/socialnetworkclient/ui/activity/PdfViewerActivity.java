package ru.belogurow.socialnetworkclient.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.viewModel.FileViewModel;
import ru.belogurow.socialnetworkclient.common.extra.Extras;

public class PdfViewerActivity extends AppCompatActivity {

    private static final String TAG = PdfViewerActivity.class.getSimpleName();

    private static final String PDF_DATA = "PDF_DATA";
    private static final String CURRENT_PAGE = "CURRENT_PAGE";

    private Toolbar mToolbar;
    private PDFView mPDFView;

    private FileViewModel mFileViewModel;

    private FileEntityDto currentFile;
    private byte[] fileData;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        initFields();

        if (savedInstanceState != null) {
            currentFile = (FileEntityDto) getIntent().getSerializableExtra(Extras.EXTRA_FILE_ENTITY_DTO);
            fileData = savedInstanceState.getByteArray(PDF_DATA);
            currentPage = savedInstanceState.getInt(CURRENT_PAGE);
            showPdf(currentFile, fileData);
        } else {

            if (getIntent().hasExtra(Extras.EXTRA_FILE_ENTITY_DTO)) {
                currentFile = (FileEntityDto) getIntent().getSerializableExtra(Extras.EXTRA_FILE_ENTITY_DTO);

                setToolbar(currentFile);

                // Show pdf file in web view
                Call<ResponseBody> call = App.sFileWebService.getFileById(currentFile.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                fileData = response.body().bytes();
                                showPdf(currentFile, fileData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(PdfViewerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Receive null data from previous activity, retry.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initFields() {
        mPDFView = findViewById(R.id.act_pdf_viewer_pdfview);
        mToolbar = findViewById(R.id.act_pdf_viewer_toolbar);
        setSupportActionBar(mToolbar);
    }

    private void setToolbar(FileEntityDto fileEntityDto) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(fileEntityDto.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showPdf(FileEntityDto fileEntityDto, byte[] data) {
        mPDFView.setBackgroundColor(getResources().getColor(R.color.md_black_1000));

        mPDFView.fromBytes(data)
                .defaultPage(currentPage)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .spacing(2)
                .onPageChange((page, pageCount) -> {
                    currentPage = page;
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(fileEntityDto.getTitle() + " " + (currentPage + 1) + "/" + pageCount);
                    }
                })
                .load();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putByteArray(PDF_DATA, fileData);
        outState.putInt(CURRENT_PAGE, currentPage);
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
