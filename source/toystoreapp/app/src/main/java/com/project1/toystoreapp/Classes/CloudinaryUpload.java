package com.project1.toystoreapp.Classes;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryUpload {
    private final String CLOUD_NAME = "CLOUD_NAME";
    private final String API_KEY = "API_KEY";
    private final String API_SECRET = "API_SECRET";

    private final Context context;
    private UploadCallback callback;

    public CloudinaryUpload(Context context) {
        this.context = context;
    }

    public void uploadImage(Uri imageUri, UploadCallback callback) {
        this.callback = callback;
        new UploadTask().execute(imageUri);
    }

    private class UploadTask extends AsyncTask<Uri, Void, Map<String, String>> {
        private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        ));
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
                Log.e("doInBackground: ", e.getMessage());
                return null;
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (callback != null) {
                if (result != null) {
                    callback.onUploadSuccess(result.get("url"));

                } else {
                    callback.onUploadFailed();
                }
            }
        }
    }
    public interface UploadCallback {
        void onUploadSuccess(String imageUrl);
        void onUploadFailed();
    }
}
