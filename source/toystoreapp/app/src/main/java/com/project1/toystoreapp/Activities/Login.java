package com.project1.toystoreapp.Activities;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        binding.progress.setVisibility(View.GONE);
        setContentView(binding.getRoot());

        binding.btnlogin.setOnClickListener(v -> {
            binding.progress.setVisibility(View.VISIBLE);
            UserEndpoint userEndpoint = new UserEndpoint();
            User user=  new User(binding.taikhoan.getText().toString(), binding.matkhau.getText().toString());
            userEndpoint.validate(user, new Callback<>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()&&response.body() != null){
                        User user = response.body();
                        Toast.makeText(Login.this, "Đăng nhập thành công với tư cách"+(user.getRole()==1?"Admin":"người dùng"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder= new AlertDialog.Builder(Login.this);
                        builder.setTitle("Đăng nhập thành công")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();

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
                    Toast.makeText(Login.this, "Vui lòng kiểm tra kết nối", Toast.LENGTH_SHORT).show();
                    binding.progress.setVisibility(View.GONE);
                }
            });
        });
    }
}