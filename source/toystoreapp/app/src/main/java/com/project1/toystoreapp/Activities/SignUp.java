package com.project1.toystoreapp.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project1.toystoreapp.API_end_points.UserEndpoint;
import com.project1.toystoreapp.Classes.PasswordHasher;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.databinding.ActivityLoginBinding;
import com.project1.toystoreapp.databinding.ActivitySignUpBinding;
import com.project1.toystoreapp.model.User;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    UserEndpoint userEndpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userEndpoint = new UserEndpoint();
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSigin.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        addTextWatcherToField(binding.xacthucmatkhau,binding.xacthucmatkhaucontainer);
        addTextWatcherToField(binding.sdt,binding.sdtcontainer);
        addTextWatcherToField(binding.email,binding.emailcontainer);
        addTextWatcherToField(binding.hoten,binding.hotenwrapper);
        addTextWatcherToField(binding.matkhau,binding.matkhaucontainer);
        binding.btnSignup.setOnClickListener(v -> {
            boolean isValid = true;
            if (Objects.requireNonNull(binding.xacthucmatkhau.getText()).toString().trim().isEmpty()) {
                binding.xacthucmatkhaucontainer.setError("Vui lòng xác thực mật khẩu");
                binding.xacthucmatkhau.requestFocus();
                isValid=false;
            }
            if (Objects.requireNonNull(binding.matkhau.getText()).toString().trim().isEmpty()) {
                binding.matkhaucontainer.setError("Vui lòng nhập mật khẩu");
                binding.matkhau.requestFocus();
                isValid=false;
            }
            if (Objects.requireNonNull(binding.email.getText()).toString().trim().isEmpty()) {
                binding.email.setError("Vui lòng nhập email");
                binding.email.requestFocus();
                isValid=false;
            }
            if (Objects.requireNonNull(binding.sdt.getText()).toString().trim().isEmpty()) {
                binding.sdt.setError("Vui lòng nhập số điện thoại");
                binding.sdt.requestFocus();
                isValid=false;
            }
            if (Objects.requireNonNull(binding.hoten.getText()).toString().trim().isEmpty()) {
                binding.hoten.setError("Vui lòng nhập họ tên");
                binding.hoten.requestFocus();
                isValid=false;
            }

            if(!isValid) return;
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(binding.email.getText().toString());
            if(!matcher.matches()){
                binding.emailcontainer.setError("Email không đúng định dạng");
                return;
            }
            if (!binding.xacthucmatkhau.getText().toString().equals(binding.matkhau.getText().toString())){
                binding.xacthucmatkhaucontainer.setError("Xác thực sai mật khẩu");
                return;
            }
            if(binding.matkhau.length()<6){
                binding.matkhaucontainer.setError("Mật khẩu phải dài ít nhất 6 ký tự");
                return;
            }
            User user = new User(binding.hoten.getText().toString(),
                            binding.matkhau.getText().toString(),
                            binding.sdt.getText().toString(),
                            binding.email.getText().toString(),0);
            userEndpoint.vaildteInfo(User.cloneUser(user), new UserEndpoint.VaildteInfolistener() {
                @Override
                public void onSucsess() {
                    Intent intent = new Intent(SignUp.this,ValidateCode.class);
                    intent.putExtra("account",user);
                    startActivity(intent);
                }

                @Override
                public void onFailure() {
                    MotionToast.Companion.createToast(SignUp.this,
                            "Thất bại!",
                            "Thông tin về số điện thoại hoặc email đã tồn tại!",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(SignUp.this, www.sanju.motiontoast.R.font.helvetica_regular));
                }

                @Override
                public void onErrorr() {
                    MotionToast.Companion.createToast(SignUp.this,
                            "Thất bại!",
                            "Không thể kết nối với máy chủ!",
                            MotionToastStyle.NO_INTERNET,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(SignUp.this, www.sanju.motiontoast.R.font.helvetica_regular));
                }
            });
        });
    }

    private void addTextWatcherToField(TextInputEditText editText, TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    textInputLayout.setError(null);
                }
            }
        });
    }
}