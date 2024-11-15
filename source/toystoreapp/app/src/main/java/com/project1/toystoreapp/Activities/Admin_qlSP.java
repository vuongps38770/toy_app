package com.project1.toystoreapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.databinding.ActivityAdminQlSpBinding;

public class Admin_qlSP extends AppCompatActivity {
    private String ID="";
    ActivityAdminQlSpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminQlSpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Glide.with(binding.load)
                .load(R.drawable.loading)
                .into(binding.load);

        Intent intent = getIntent();
        ID=intent.getStringExtra("IDSPCON");
        if(ID!=""){
            Toast.makeText(this, ID, Toast.LENGTH_SHORT).show();
            Log.e("ID", ID );
        }
    }
}