package com.project1.toystoreapp.Classes;

import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

public class ImagePicker {

    private final ActivityResultLauncher<String> imagePickerLauncher;
    private Uri selectedImageUri;
    private ImagePickerCallback callback;

    public ImagePicker(AppCompatActivity activity) {
        imagePickerLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> handleResult(uri)
        );
    }

    public ImagePicker(Fragment fragment) {
        imagePickerLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> handleResult(uri)
        );
    }

    public void pickImage(ImagePickerCallback callback) {
        this.callback = callback;
        imagePickerLauncher.launch("image/*");
    }

    private void handleResult(Uri uri) {
        if (uri != null) {
            selectedImageUri = uri;
            if (callback != null) {
                callback.onImagePicked(uri);
            }
        } else {
            if (callback != null) {
                callback.onPickCancelled();
            }
        }
    }

    public Uri getSelectedImageUri() {
        return selectedImageUri;
    }

    public interface ImagePickerCallback {
        void onImagePicked(Uri uri);
        void onPickCancelled();
    }
}
