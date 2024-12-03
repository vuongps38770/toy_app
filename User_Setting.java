package com.project1.toystoreapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.utils.StringUtils;
import com.project1.toystoreapp.API_end_points.DiachiEndpoints;
import com.project1.toystoreapp.API_end_points.LocationEndpoint;
import com.project1.toystoreapp.Classes.DiaChiDialog;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.RecyclerAdapters.CustomArrayAdapter;
import com.project1.toystoreapp.databinding.ActivityUserSettingBinding;
import com.project1.toystoreapp.model.DiaChi;
import com.project1.toystoreapp.model.location.District;
import com.project1.toystoreapp.model.location.Province;
import com.project1.toystoreapp.model.location.Ward;

import java.util.ArrayList;
import java.util.List;

public class User_Setting extends AppCompatActivity {
    ActivityUserSettingBinding binding;
    LocationEndpoint locationEndpoint;
    List<Province> provinces;
    List<District> districts;
    List<Ward> wards;
    String userID="673b166ef5c50bce0cd7311f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityUserSettingBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        locationEndpoint = new LocationEndpoint();

    }


}