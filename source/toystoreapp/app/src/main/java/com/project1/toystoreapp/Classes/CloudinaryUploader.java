package com.project1.toystoreapp.Classes;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryUploader {
    private final String CLOUD_NAME = "dzuqdrb1e";
    private final String API_KEY = "444138869151349";
    private final String API_SECRET = "3v80auXRVnYpbLZmrVoJk1VA4eQ";
    private final Cloudinary cloudinary;
    private final UploadCallback callback;
    private final Context context;

    public CloudinaryUploader(Context context, UploadCallback callback) {
        this.context = context;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        ));
        this.callback = callback;
    }
    @SuppressWarnings("deprecation")
    public void uploadImage(Uri imageUri) {
        new UploadTask().execute(imageUri);
    }
    @SuppressWarnings("deprecation")
    private class UploadTask extends AsyncTask<Uri, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Uri... uris) {
            Uri imageUri = uris[0];
            File tempFile = null;
            try {
                tempFile = File.createTempFile("upload", ".jpg", context.getCacheDir());
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                inputStream.close();
                outputStream.close();

                return cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (result != null) {
                callback.onUploadSuccess(result.get("url"));
            } else {
                callback.onUploadFailed();
            }
        }
    }

    public interface UploadCallback {
        void onUploadSuccess(String imageUrl);
        void onUploadFailed();
    }
}
