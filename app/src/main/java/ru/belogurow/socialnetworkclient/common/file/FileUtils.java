package ru.belogurow.socialnetworkclient.common.file;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static File openPath(Context context, Uri uri) {
        Log.d(TAG, "openPath: " + uri.toString());

        File result = new File(context.getFilesDir() + File.separator + "temp");
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            result.createNewFile();
            org.apache.commons.io.FileUtils.copyInputStreamToFile(is, result);
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
}
