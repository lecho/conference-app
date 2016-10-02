package com.github.lecho.mobilization.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by Leszek on 02.10.2016.
 */

public class FileUtils {

    public static final String TAG = FileUtils.class.getSimpleName();

    public static boolean saveStreamToFile(InputStream inputStream, File destinationFile) {
        OutputStream outputStream = null;

        try {
            int read;
            byte[] buffer = new byte[4096];
            outputStream = new FileOutputStream(destinationFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Could not save file " + destinationFile.getName(), e);
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Could not close stream", e);
            }
        }
    }
}
