package com.project1.toystoreapp.Activities;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.project1.toystoreapp.API_end_points.UserEndpoint;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.databinding.ActivityLoginBinding;
import com.project1.toystoreapp.model.ErrorRespond;
import com.project1.toystoreapp.model.User;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    SharedPreferences sharedPreferences ;
    private String accountKey="account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(accountKey, Context.MODE_PRIVATE);
        String savedaccount = sharedPreferences.getString(accountKey,null);
        if(savedaccount!=null){
            Gson gson = new Gson();
            User user =gson.fromJson(savedaccount,User.class);
            if(user.getRole()==1){
                Intent intent = new Intent(Login.this,Admin_screen.class);
                intent.putExtra(accountKey,user);
                startActivity(intent);
                finish();
            }else if(user.getRole()==0){
                Intent intent = new Intent(Login.this,User_main.class);
                intent.putExtra(accountKey,user);
                startActivity(intent);
                finish();
            }
            return;
        }
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        binding.progress.setVisibility(View.GONE);
        setContentView(binding.getRoot());
        binding.btnForgotPW.setOnClickListener(v -> {
            Intent i = new Intent(Login.this,ForgotPW.class);
            startActivity(i);
        });
        binding.btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });
        ///sample----------------------------------------------------------------------------------------
        binding.taikhoan.setText("083764312");
        binding.matkhau.setText("vuong123");
        ///sample----------------------------------------------------------------------------------------
        binding.btnlogin.setOnClickListener(v -> {
            if(binding.taikhoan.getText().toString().trim().equals("")||binding.matkhau.getText().toString().trim().equals("")){
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.progress.setVisibility(View.VISIBLE);
            UserEndpoint userEndpoint = new UserEndpoint();
            User user=  new User(binding.taikhoan.getText().toString(), binding.matkhau.getText().toString());
            userEndpoint.validate(user, new Callback<>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()&&response.body() != null){
                        User user = response.body();
                        if(binding.rememberme.isChecked()){
                            Gson gson = new Gson();
                            String accountinfo=gson.toJson(user);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(accountKey,accountinfo);
                            editor.apply();
                        }
                        if(user.getRole()==1){
                            Intent intent = new Intent(Login.this,Admin_screen.class);
                            intent.putExtra(accountKey,user);
                            startActivity(intent);
                            finish();
                        } else if (user.getRole()==0) {
                            Intent intent = new Intent(Login.this,User_main.class);
                            intent.putExtra(accountKey,user);
                            startActivity(intent);
                            finish();
                        }
                        binding.progress.setVisibility(View.GONE);
                    }else {
                        Log.e("API Response Error", "Code: " + response.code());
                        Log.e("API Response Error", "Message: " + response.message());
                        String jsonerror="";
                        Log.e(TAG, "onResponse:"+response.body());
                        try {
                            jsonerror=response.errorBody().string();
                            Gson gson = new Gson();
                            ErrorRespond errorRespond= gson.fromJson(jsonerror, ErrorRespond.class);
                            Toast.makeText(Login.this, errorRespond.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (IOException e){

                        }
                        binding.progress.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(Login.this, "Không thể kết nối với máy chủ", Toast.LENGTH_SHORT).show();
                    binding.progress.setVisibility(View.GONE);
                }
            });
        });
    }
}