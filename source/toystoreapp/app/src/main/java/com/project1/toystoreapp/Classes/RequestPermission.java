package com.project1.toystoreapp.Classes;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RequestPermission {
    public static boolean request(Activity activity, String ManifestPermission, int requestCode){

        if(ContextCompat.checkSelfPermission(activity,ManifestPermission)== PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            try {
                ActivityCompat.requestPermissions(activity,new String[] {ManifestPermission},requestCode);
            }catch (Exception e){
                Log.e(TAG, e.getMessage());
            }
            return false;
        }
    }
}
